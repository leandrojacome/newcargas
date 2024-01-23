package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.repository.TipoFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoFreteMapper;
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
 * Integration tests for the {@link TipoFreteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoFreteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-fretes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tipo-fretes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoFreteRepository tipoFreteRepository;

    @Autowired
    private TipoFreteMapper tipoFreteMapper;

    @Autowired
    private TipoFreteSearchRepository tipoFreteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoFreteMockMvc;

    private TipoFrete tipoFrete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFrete createEntity(EntityManager em) {
        TipoFrete tipoFrete = new TipoFrete().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return tipoFrete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFrete createUpdatedEntity(EntityManager em) {
        TipoFrete tipoFrete = new TipoFrete().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return tipoFrete;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        tipoFreteSearchRepository.deleteAll();
        assertThat(tipoFreteSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        tipoFrete = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoFrete() throws Exception {
        int databaseSizeBeforeCreate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);
        restTipoFreteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TipoFrete testTipoFrete = tipoFreteList.get(tipoFreteList.size() - 1);
        assertThat(testTipoFrete.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoFrete.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createTipoFreteWithExistingId() throws Exception {
        // Create the TipoFrete with an existing ID
        tipoFrete.setId(1L);
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        int databaseSizeBeforeCreate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoFreteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        // set the field null
        tipoFrete.setNome(null);

        // Create the TipoFrete, which fails.
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        restTipoFreteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO)))
            .andExpect(status().isBadRequest());

        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTipoFretes() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList
        restTipoFreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoFrete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipoFrete() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get the tipoFrete
        restTipoFreteMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoFrete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoFrete.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getTipoFretesByIdFiltering() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        Long id = tipoFrete.getId();

        defaultTipoFreteShouldBeFound("id.equals=" + id);
        defaultTipoFreteShouldNotBeFound("id.notEquals=" + id);

        defaultTipoFreteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoFreteShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoFreteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoFreteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoFretesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where nome equals to DEFAULT_NOME
        defaultTipoFreteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the tipoFreteList where nome equals to UPDATED_NOME
        defaultTipoFreteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoFretesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTipoFreteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the tipoFreteList where nome equals to UPDATED_NOME
        defaultTipoFreteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoFretesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where nome is not null
        defaultTipoFreteShouldBeFound("nome.specified=true");

        // Get all the tipoFreteList where nome is null
        defaultTipoFreteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoFretesByNomeContainsSomething() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where nome contains DEFAULT_NOME
        defaultTipoFreteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the tipoFreteList where nome contains UPDATED_NOME
        defaultTipoFreteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoFretesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where nome does not contain DEFAULT_NOME
        defaultTipoFreteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the tipoFreteList where nome does not contain UPDATED_NOME
        defaultTipoFreteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoFretesByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where descricao equals to DEFAULT_DESCRICAO
        defaultTipoFreteShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the tipoFreteList where descricao equals to UPDATED_DESCRICAO
        defaultTipoFreteShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoFretesByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultTipoFreteShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the tipoFreteList where descricao equals to UPDATED_DESCRICAO
        defaultTipoFreteShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoFretesByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where descricao is not null
        defaultTipoFreteShouldBeFound("descricao.specified=true");

        // Get all the tipoFreteList where descricao is null
        defaultTipoFreteShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoFretesByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where descricao contains DEFAULT_DESCRICAO
        defaultTipoFreteShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the tipoFreteList where descricao contains UPDATED_DESCRICAO
        defaultTipoFreteShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoFretesByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        // Get all the tipoFreteList where descricao does not contain DEFAULT_DESCRICAO
        defaultTipoFreteShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the tipoFreteList where descricao does not contain UPDATED_DESCRICAO
        defaultTipoFreteShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoFretesByTabelaFreteIsEqualToSomething() throws Exception {
        TabelaFrete tabelaFrete;
        if (TestUtil.findAll(em, TabelaFrete.class).isEmpty()) {
            tipoFreteRepository.saveAndFlush(tipoFrete);
            tabelaFrete = TabelaFreteResourceIT.createEntity(em);
        } else {
            tabelaFrete = TestUtil.findAll(em, TabelaFrete.class).get(0);
        }
        em.persist(tabelaFrete);
        em.flush();
        tipoFrete.addTabelaFrete(tabelaFrete);
        tipoFreteRepository.saveAndFlush(tipoFrete);
        Long tabelaFreteId = tabelaFrete.getId();
        // Get all the tipoFreteList where tabelaFrete equals to tabelaFreteId
        defaultTipoFreteShouldBeFound("tabelaFreteId.equals=" + tabelaFreteId);

        // Get all the tipoFreteList where tabelaFrete equals to (tabelaFreteId + 1)
        defaultTipoFreteShouldNotBeFound("tabelaFreteId.equals=" + (tabelaFreteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoFreteShouldBeFound(String filter) throws Exception {
        restTipoFreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoFrete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restTipoFreteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoFreteShouldNotBeFound(String filter) throws Exception {
        restTipoFreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoFreteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoFrete() throws Exception {
        // Get the tipoFrete
        restTipoFreteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoFrete() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        tipoFreteSearchRepository.save(tipoFrete);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());

        // Update the tipoFrete
        TipoFrete updatedTipoFrete = tipoFreteRepository.findById(tipoFrete.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoFrete are not directly saved in db
        em.detach(updatedTipoFrete);
        updatedTipoFrete.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(updatedTipoFrete);

        restTipoFreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoFreteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        TipoFrete testTipoFrete = tipoFreteList.get(tipoFreteList.size() - 1);
        assertThat(testTipoFrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoFrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TipoFrete> tipoFreteSearchList = IterableUtils.toList(tipoFreteSearchRepository.findAll());
                TipoFrete testTipoFreteSearch = tipoFreteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTipoFreteSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testTipoFreteSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTipoFrete() throws Exception {
        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        tipoFrete.setId(longCount.incrementAndGet());

        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoFreteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoFrete() throws Exception {
        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        tipoFrete.setId(longCount.incrementAndGet());

        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoFrete() throws Exception {
        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        tipoFrete.setId(longCount.incrementAndGet());

        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFreteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTipoFreteWithPatch() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();

        // Update the tipoFrete using partial update
        TipoFrete partialUpdatedTipoFrete = new TipoFrete();
        partialUpdatedTipoFrete.setId(tipoFrete.getId());

        restTipoFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoFrete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoFrete))
            )
            .andExpect(status().isOk());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        TipoFrete testTipoFrete = tipoFreteList.get(tipoFreteList.size() - 1);
        assertThat(testTipoFrete.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoFrete.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoFreteWithPatch() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);

        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();

        // Update the tipoFrete using partial update
        TipoFrete partialUpdatedTipoFrete = new TipoFrete();
        partialUpdatedTipoFrete.setId(tipoFrete.getId());

        partialUpdatedTipoFrete.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restTipoFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoFrete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoFrete))
            )
            .andExpect(status().isOk());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        TipoFrete testTipoFrete = tipoFreteList.get(tipoFreteList.size() - 1);
        assertThat(testTipoFrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoFrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoFrete() throws Exception {
        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        tipoFrete.setId(longCount.incrementAndGet());

        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoFreteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoFrete() throws Exception {
        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        tipoFrete.setId(longCount.incrementAndGet());

        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoFrete() throws Exception {
        int databaseSizeBeforeUpdate = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        tipoFrete.setId(longCount.incrementAndGet());

        // Create the TipoFrete
        TipoFreteDTO tipoFreteDTO = tipoFreteMapper.toDto(tipoFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFreteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoFreteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoFrete in the database
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTipoFrete() throws Exception {
        // Initialize the database
        tipoFreteRepository.saveAndFlush(tipoFrete);
        tipoFreteRepository.save(tipoFrete);
        tipoFreteSearchRepository.save(tipoFrete);

        int databaseSizeBeforeDelete = tipoFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tipoFrete
        restTipoFreteMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoFrete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoFrete> tipoFreteList = tipoFreteRepository.findAll();
        assertThat(tipoFreteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTipoFrete() throws Exception {
        // Initialize the database
        tipoFrete = tipoFreteRepository.saveAndFlush(tipoFrete);
        tipoFreteSearchRepository.save(tipoFrete);

        // Search the tipoFrete
        restTipoFreteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tipoFrete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoFrete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
