package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.repository.TipoCargaRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoCargaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoCargaMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoCargaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoCargaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-cargas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tipo-cargas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoCargaRepository tipoCargaRepository;

    @Autowired
    private TipoCargaMapper tipoCargaMapper;

    @Autowired
    private TipoCargaSearchRepository tipoCargaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoCargaMockMvc;

    private TipoCarga tipoCarga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoCarga createEntity(EntityManager em) {
        TipoCarga tipoCarga = new TipoCarga().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return tipoCarga;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoCarga createUpdatedEntity(EntityManager em) {
        TipoCarga tipoCarga = new TipoCarga().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return tipoCarga;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        tipoCargaSearchRepository.deleteAll();
        assertThat(tipoCargaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        tipoCarga = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoCarga() throws Exception {
        int databaseSizeBeforeCreate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);
        restTipoCargaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TipoCarga testTipoCarga = tipoCargaList.get(tipoCargaList.size() - 1);
        assertThat(testTipoCarga.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoCarga.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createTipoCargaWithExistingId() throws Exception {
        // Create the TipoCarga with an existing ID
        tipoCarga.setId(1L);
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        int databaseSizeBeforeCreate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoCargaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        // set the field null
        tipoCarga.setNome(null);

        // Create the TipoCarga, which fails.
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        restTipoCargaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO)))
            .andExpect(status().isBadRequest());

        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTipoCargas() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList
        restTipoCargaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCarga.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipoCarga() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get the tipoCarga
        restTipoCargaMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoCarga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoCarga.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getTipoCargasByIdFiltering() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        Long id = tipoCarga.getId();

        defaultTipoCargaShouldBeFound("id.equals=" + id);
        defaultTipoCargaShouldNotBeFound("id.notEquals=" + id);

        defaultTipoCargaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoCargaShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoCargaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoCargaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoCargasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where nome equals to DEFAULT_NOME
        defaultTipoCargaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the tipoCargaList where nome equals to UPDATED_NOME
        defaultTipoCargaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoCargasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTipoCargaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the tipoCargaList where nome equals to UPDATED_NOME
        defaultTipoCargaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoCargasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where nome is not null
        defaultTipoCargaShouldBeFound("nome.specified=true");

        // Get all the tipoCargaList where nome is null
        defaultTipoCargaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoCargasByNomeContainsSomething() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where nome contains DEFAULT_NOME
        defaultTipoCargaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the tipoCargaList where nome contains UPDATED_NOME
        defaultTipoCargaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoCargasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where nome does not contain DEFAULT_NOME
        defaultTipoCargaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the tipoCargaList where nome does not contain UPDATED_NOME
        defaultTipoCargaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoCargasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where descricao equals to DEFAULT_DESCRICAO
        defaultTipoCargaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the tipoCargaList where descricao equals to UPDATED_DESCRICAO
        defaultTipoCargaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoCargasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultTipoCargaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the tipoCargaList where descricao equals to UPDATED_DESCRICAO
        defaultTipoCargaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoCargasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where descricao is not null
        defaultTipoCargaShouldBeFound("descricao.specified=true");

        // Get all the tipoCargaList where descricao is null
        defaultTipoCargaShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoCargasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where descricao contains DEFAULT_DESCRICAO
        defaultTipoCargaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the tipoCargaList where descricao contains UPDATED_DESCRICAO
        defaultTipoCargaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoCargasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        // Get all the tipoCargaList where descricao does not contain DEFAULT_DESCRICAO
        defaultTipoCargaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the tipoCargaList where descricao does not contain UPDATED_DESCRICAO
        defaultTipoCargaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoCargasByTabelaFreteIsEqualToSomething() throws Exception {
        TabelaFrete tabelaFrete;
        if (TestUtil.findAll(em, TabelaFrete.class).isEmpty()) {
            tipoCargaRepository.saveAndFlush(tipoCarga);
            tabelaFrete = TabelaFreteResourceIT.createEntity(em);
        } else {
            tabelaFrete = TestUtil.findAll(em, TabelaFrete.class).get(0);
        }
        em.persist(tabelaFrete);
        em.flush();
        tipoCarga.addTabelaFrete(tabelaFrete);
        tipoCargaRepository.saveAndFlush(tipoCarga);
        Long tabelaFreteId = tabelaFrete.getId();
        // Get all the tipoCargaList where tabelaFrete equals to tabelaFreteId
        defaultTipoCargaShouldBeFound("tabelaFreteId.equals=" + tabelaFreteId);

        // Get all the tipoCargaList where tabelaFrete equals to (tabelaFreteId + 1)
        defaultTipoCargaShouldNotBeFound("tabelaFreteId.equals=" + (tabelaFreteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoCargaShouldBeFound(String filter) throws Exception {
        restTipoCargaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCarga.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restTipoCargaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoCargaShouldNotBeFound(String filter) throws Exception {
        restTipoCargaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoCargaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoCarga() throws Exception {
        // Get the tipoCarga
        restTipoCargaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoCarga() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        tipoCargaSearchRepository.save(tipoCarga);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());

        // Update the tipoCarga
        TipoCarga updatedTipoCarga = tipoCargaRepository.findById(tipoCarga.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoCarga are not directly saved in db
        em.detach(updatedTipoCarga);
        updatedTipoCarga.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(updatedTipoCarga);

        restTipoCargaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoCargaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        TipoCarga testTipoCarga = tipoCargaList.get(tipoCargaList.size() - 1);
        assertThat(testTipoCarga.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoCarga.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TipoCarga> tipoCargaSearchList = IterableUtils.toList(tipoCargaSearchRepository.findAll());
                TipoCarga testTipoCargaSearch = tipoCargaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTipoCargaSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testTipoCargaSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTipoCarga() throws Exception {
        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        tipoCarga.setId(longCount.incrementAndGet());

        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoCargaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoCargaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoCarga() throws Exception {
        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        tipoCarga.setId(longCount.incrementAndGet());

        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCargaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoCarga() throws Exception {
        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        tipoCarga.setId(longCount.incrementAndGet());

        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCargaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTipoCargaWithPatch() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();

        // Update the tipoCarga using partial update
        TipoCarga partialUpdatedTipoCarga = new TipoCarga();
        partialUpdatedTipoCarga.setId(tipoCarga.getId());

        restTipoCargaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoCarga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoCarga))
            )
            .andExpect(status().isOk());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        TipoCarga testTipoCarga = tipoCargaList.get(tipoCargaList.size() - 1);
        assertThat(testTipoCarga.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoCarga.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoCargaWithPatch() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);

        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();

        // Update the tipoCarga using partial update
        TipoCarga partialUpdatedTipoCarga = new TipoCarga();
        partialUpdatedTipoCarga.setId(tipoCarga.getId());

        partialUpdatedTipoCarga.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restTipoCargaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoCarga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoCarga))
            )
            .andExpect(status().isOk());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        TipoCarga testTipoCarga = tipoCargaList.get(tipoCargaList.size() - 1);
        assertThat(testTipoCarga.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoCarga.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoCarga() throws Exception {
        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        tipoCarga.setId(longCount.incrementAndGet());

        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoCargaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoCargaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoCarga() throws Exception {
        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        tipoCarga.setId(longCount.incrementAndGet());

        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCargaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoCarga() throws Exception {
        int databaseSizeBeforeUpdate = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        tipoCarga.setId(longCount.incrementAndGet());

        // Create the TipoCarga
        TipoCargaDTO tipoCargaDTO = tipoCargaMapper.toDto(tipoCarga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoCargaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoCargaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoCarga in the database
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTipoCarga() throws Exception {
        // Initialize the database
        tipoCargaRepository.saveAndFlush(tipoCarga);
        tipoCargaRepository.save(tipoCarga);
        tipoCargaSearchRepository.save(tipoCarga);

        int databaseSizeBeforeDelete = tipoCargaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tipoCarga
        restTipoCargaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoCarga.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoCarga> tipoCargaList = tipoCargaRepository.findAll();
        assertThat(tipoCargaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoCargaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTipoCarga() throws Exception {
        // Initialize the database
        tipoCarga = tipoCargaRepository.saveAndFlush(tipoCarga);
        tipoCargaSearchRepository.save(tipoCarga);

        // Search the tipoCarga
        restTipoCargaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tipoCarga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoCarga.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
