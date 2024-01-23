package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.repository.TipoVeiculoRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoVeiculoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoVeiculoMapper;
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
 * Integration tests for the {@link TipoVeiculoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoVeiculoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tipo-veiculos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoVeiculoRepository tipoVeiculoRepository;

    @Autowired
    private TipoVeiculoMapper tipoVeiculoMapper;

    @Autowired
    private TipoVeiculoSearchRepository tipoVeiculoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoVeiculoMockMvc;

    private TipoVeiculo tipoVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoVeiculo createEntity(EntityManager em) {
        TipoVeiculo tipoVeiculo = new TipoVeiculo().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return tipoVeiculo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoVeiculo createUpdatedEntity(EntityManager em) {
        TipoVeiculo tipoVeiculo = new TipoVeiculo().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return tipoVeiculo;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        tipoVeiculoSearchRepository.deleteAll();
        assertThat(tipoVeiculoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        tipoVeiculo = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoVeiculo() throws Exception {
        int databaseSizeBeforeCreate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);
        restTipoVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TipoVeiculo testTipoVeiculo = tipoVeiculoList.get(tipoVeiculoList.size() - 1);
        assertThat(testTipoVeiculo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoVeiculo.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createTipoVeiculoWithExistingId() throws Exception {
        // Create the TipoVeiculo with an existing ID
        tipoVeiculo.setId(1L);
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        int databaseSizeBeforeCreate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        // set the field null
        tipoVeiculo.setNome(null);

        // Create the TipoVeiculo, which fails.
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        restTipoVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTipoVeiculos() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipoVeiculo() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get the tipoVeiculo
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoVeiculo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getTipoVeiculosByIdFiltering() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        Long id = tipoVeiculo.getId();

        defaultTipoVeiculoShouldBeFound("id.equals=" + id);
        defaultTipoVeiculoShouldNotBeFound("id.notEquals=" + id);

        defaultTipoVeiculoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoVeiculoShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoVeiculoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoVeiculoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where nome equals to DEFAULT_NOME
        defaultTipoVeiculoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the tipoVeiculoList where nome equals to UPDATED_NOME
        defaultTipoVeiculoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTipoVeiculoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the tipoVeiculoList where nome equals to UPDATED_NOME
        defaultTipoVeiculoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where nome is not null
        defaultTipoVeiculoShouldBeFound("nome.specified=true");

        // Get all the tipoVeiculoList where nome is null
        defaultTipoVeiculoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByNomeContainsSomething() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where nome contains DEFAULT_NOME
        defaultTipoVeiculoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the tipoVeiculoList where nome contains UPDATED_NOME
        defaultTipoVeiculoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where nome does not contain DEFAULT_NOME
        defaultTipoVeiculoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the tipoVeiculoList where nome does not contain UPDATED_NOME
        defaultTipoVeiculoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where descricao equals to DEFAULT_DESCRICAO
        defaultTipoVeiculoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the tipoVeiculoList where descricao equals to UPDATED_DESCRICAO
        defaultTipoVeiculoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultTipoVeiculoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the tipoVeiculoList where descricao equals to UPDATED_DESCRICAO
        defaultTipoVeiculoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where descricao is not null
        defaultTipoVeiculoShouldBeFound("descricao.specified=true");

        // Get all the tipoVeiculoList where descricao is null
        defaultTipoVeiculoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where descricao contains DEFAULT_DESCRICAO
        defaultTipoVeiculoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the tipoVeiculoList where descricao contains UPDATED_DESCRICAO
        defaultTipoVeiculoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        // Get all the tipoVeiculoList where descricao does not contain DEFAULT_DESCRICAO
        defaultTipoVeiculoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the tipoVeiculoList where descricao does not contain UPDATED_DESCRICAO
        defaultTipoVeiculoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTipoVeiculosBySolitacaoColetaIsEqualToSomething() throws Exception {
        SolicitacaoColeta solitacaoColeta;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            tipoVeiculoRepository.saveAndFlush(tipoVeiculo);
            solitacaoColeta = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solitacaoColeta = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solitacaoColeta);
        em.flush();
        tipoVeiculo.addSolitacaoColeta(solitacaoColeta);
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);
        Long solitacaoColetaId = solitacaoColeta.getId();
        // Get all the tipoVeiculoList where solitacaoColeta equals to solitacaoColetaId
        defaultTipoVeiculoShouldBeFound("solitacaoColetaId.equals=" + solitacaoColetaId);

        // Get all the tipoVeiculoList where solitacaoColeta equals to (solitacaoColetaId + 1)
        defaultTipoVeiculoShouldNotBeFound("solitacaoColetaId.equals=" + (solitacaoColetaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoVeiculoShouldBeFound(String filter) throws Exception {
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoVeiculoShouldNotBeFound(String filter) throws Exception {
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoVeiculo() throws Exception {
        // Get the tipoVeiculo
        restTipoVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoVeiculo() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        tipoVeiculoSearchRepository.save(tipoVeiculo);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());

        // Update the tipoVeiculo
        TipoVeiculo updatedTipoVeiculo = tipoVeiculoRepository.findById(tipoVeiculo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoVeiculo are not directly saved in db
        em.detach(updatedTipoVeiculo);
        updatedTipoVeiculo.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(updatedTipoVeiculo);

        restTipoVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoVeiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        TipoVeiculo testTipoVeiculo = tipoVeiculoList.get(tipoVeiculoList.size() - 1);
        assertThat(testTipoVeiculo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoVeiculo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TipoVeiculo> tipoVeiculoSearchList = IterableUtils.toList(tipoVeiculoSearchRepository.findAll());
                TipoVeiculo testTipoVeiculoSearch = tipoVeiculoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTipoVeiculoSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testTipoVeiculoSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTipoVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        tipoVeiculo.setId(longCount.incrementAndGet());

        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoVeiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        tipoVeiculo.setId(longCount.incrementAndGet());

        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        tipoVeiculo.setId(longCount.incrementAndGet());

        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoVeiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTipoVeiculoWithPatch() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();

        // Update the tipoVeiculo using partial update
        TipoVeiculo partialUpdatedTipoVeiculo = new TipoVeiculo();
        partialUpdatedTipoVeiculo.setId(tipoVeiculo.getId());

        partialUpdatedTipoVeiculo.descricao(UPDATED_DESCRICAO);

        restTipoVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        TipoVeiculo testTipoVeiculo = tipoVeiculoList.get(tipoVeiculoList.size() - 1);
        assertThat(testTipoVeiculo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoVeiculo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoVeiculoWithPatch() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);

        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();

        // Update the tipoVeiculo using partial update
        TipoVeiculo partialUpdatedTipoVeiculo = new TipoVeiculo();
        partialUpdatedTipoVeiculo.setId(tipoVeiculo.getId());

        partialUpdatedTipoVeiculo.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restTipoVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        TipoVeiculo testTipoVeiculo = tipoVeiculoList.get(tipoVeiculoList.size() - 1);
        assertThat(testTipoVeiculo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoVeiculo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        tipoVeiculo.setId(longCount.incrementAndGet());

        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoVeiculoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        tipoVeiculo.setId(longCount.incrementAndGet());

        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        tipoVeiculo.setId(longCount.incrementAndGet());

        // Create the TipoVeiculo
        TipoVeiculoDTO tipoVeiculoDTO = tipoVeiculoMapper.toDto(tipoVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoVeiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoVeiculo in the database
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTipoVeiculo() throws Exception {
        // Initialize the database
        tipoVeiculoRepository.saveAndFlush(tipoVeiculo);
        tipoVeiculoRepository.save(tipoVeiculo);
        tipoVeiculoSearchRepository.save(tipoVeiculo);

        int databaseSizeBeforeDelete = tipoVeiculoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tipoVeiculo
        restTipoVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoVeiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoVeiculo> tipoVeiculoList = tipoVeiculoRepository.findAll();
        assertThat(tipoVeiculoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tipoVeiculoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTipoVeiculo() throws Exception {
        // Initialize the database
        tipoVeiculo = tipoVeiculoRepository.saveAndFlush(tipoVeiculo);
        tipoVeiculoSearchRepository.save(tipoVeiculo);

        // Search the tipoVeiculo
        restTipoVeiculoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tipoVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
