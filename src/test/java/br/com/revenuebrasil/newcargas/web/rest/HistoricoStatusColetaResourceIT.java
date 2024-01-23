package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.repository.HistoricoStatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.HistoricoStatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.HistoricoStatusColetaMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link HistoricoStatusColetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoricoStatusColetaResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CRIACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historico-status-coletas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/historico-status-coletas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoricoStatusColetaRepository historicoStatusColetaRepository;

    @Autowired
    private HistoricoStatusColetaMapper historicoStatusColetaMapper;

    @Autowired
    private HistoricoStatusColetaSearchRepository historicoStatusColetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoricoStatusColetaMockMvc;

    private HistoricoStatusColeta historicoStatusColeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricoStatusColeta createEntity(EntityManager em) {
        HistoricoStatusColeta historicoStatusColeta = new HistoricoStatusColeta()
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .observacao(DEFAULT_OBSERVACAO);
        return historicoStatusColeta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricoStatusColeta createUpdatedEntity(EntityManager em) {
        HistoricoStatusColeta historicoStatusColeta = new HistoricoStatusColeta()
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .observacao(UPDATED_OBSERVACAO);
        return historicoStatusColeta;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        historicoStatusColetaSearchRepository.deleteAll();
        assertThat(historicoStatusColetaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        historicoStatusColeta = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeCreate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);
        restHistoricoStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void createHistoricoStatusColetaWithExistingId() throws Exception {
        // Create the HistoricoStatusColeta with an existing ID
        historicoStatusColeta.setId(1L);
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        int databaseSizeBeforeCreate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricoStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        // set the field null
        historicoStatusColeta.setDataCriacao(null);

        // Create the HistoricoStatusColeta, which fails.
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        restHistoricoStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetas() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoStatusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }

    @Test
    @Transactional
    void getHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get the historicoStatusColeta
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL_ID, historicoStatusColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historicoStatusColeta.getId().intValue()))
            .andExpect(jsonPath("$.dataCriacao").value(sameInstant(DEFAULT_DATA_CRIACAO)))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO));
    }

    @Test
    @Transactional
    void getHistoricoStatusColetasByIdFiltering() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        Long id = historicoStatusColeta.getId();

        defaultHistoricoStatusColetaShouldBeFound("id.equals=" + id);
        defaultHistoricoStatusColetaShouldNotBeFound("id.notEquals=" + id);

        defaultHistoricoStatusColetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoricoStatusColetaShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoricoStatusColetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoricoStatusColetaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao equals to DEFAULT_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.equals=" + DEFAULT_DATA_CRIACAO);

        // Get all the historicoStatusColetaList where dataCriacao equals to UPDATED_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.equals=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsInShouldWork() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao in DEFAULT_DATA_CRIACAO or UPDATED_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.in=" + DEFAULT_DATA_CRIACAO + "," + UPDATED_DATA_CRIACAO);

        // Get all the historicoStatusColetaList where dataCriacao equals to UPDATED_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.in=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao is not null
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.specified=true");

        // Get all the historicoStatusColetaList where dataCriacao is null
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao is greater than or equal to DEFAULT_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.greaterThanOrEqual=" + DEFAULT_DATA_CRIACAO);

        // Get all the historicoStatusColetaList where dataCriacao is greater than or equal to UPDATED_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.greaterThanOrEqual=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao is less than or equal to DEFAULT_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.lessThanOrEqual=" + DEFAULT_DATA_CRIACAO);

        // Get all the historicoStatusColetaList where dataCriacao is less than or equal to SMALLER_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.lessThanOrEqual=" + SMALLER_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao is less than DEFAULT_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.lessThan=" + DEFAULT_DATA_CRIACAO);

        // Get all the historicoStatusColetaList where dataCriacao is less than UPDATED_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.lessThan=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByDataCriacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where dataCriacao is greater than DEFAULT_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldNotBeFound("dataCriacao.greaterThan=" + DEFAULT_DATA_CRIACAO);

        // Get all the historicoStatusColetaList where dataCriacao is greater than SMALLER_DATA_CRIACAO
        defaultHistoricoStatusColetaShouldBeFound("dataCriacao.greaterThan=" + SMALLER_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where observacao equals to DEFAULT_OBSERVACAO
        defaultHistoricoStatusColetaShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the historicoStatusColetaList where observacao equals to UPDATED_OBSERVACAO
        defaultHistoricoStatusColetaShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultHistoricoStatusColetaShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the historicoStatusColetaList where observacao equals to UPDATED_OBSERVACAO
        defaultHistoricoStatusColetaShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where observacao is not null
        defaultHistoricoStatusColetaShouldBeFound("observacao.specified=true");

        // Get all the historicoStatusColetaList where observacao is null
        defaultHistoricoStatusColetaShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where observacao contains DEFAULT_OBSERVACAO
        defaultHistoricoStatusColetaShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the historicoStatusColetaList where observacao contains UPDATED_OBSERVACAO
        defaultHistoricoStatusColetaShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList where observacao does not contain DEFAULT_OBSERVACAO
        defaultHistoricoStatusColetaShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the historicoStatusColetaList where observacao does not contain UPDATED_OBSERVACAO
        defaultHistoricoStatusColetaShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasBySolicitacaoColetaIsEqualToSomething() throws Exception {
        SolicitacaoColeta solicitacaoColeta;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
            solicitacaoColeta = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solicitacaoColeta = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solicitacaoColeta);
        em.flush();
        historicoStatusColeta.setSolicitacaoColeta(solicitacaoColeta);
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        Long solicitacaoColetaId = solicitacaoColeta.getId();
        // Get all the historicoStatusColetaList where solicitacaoColeta equals to solicitacaoColetaId
        defaultHistoricoStatusColetaShouldBeFound("solicitacaoColetaId.equals=" + solicitacaoColetaId);

        // Get all the historicoStatusColetaList where solicitacaoColeta equals to (solicitacaoColetaId + 1)
        defaultHistoricoStatusColetaShouldNotBeFound("solicitacaoColetaId.equals=" + (solicitacaoColetaId + 1));
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByRoteirizacaoIsEqualToSomething() throws Exception {
        Roteirizacao roteirizacao;
        if (TestUtil.findAll(em, Roteirizacao.class).isEmpty()) {
            historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
            roteirizacao = RoteirizacaoResourceIT.createEntity(em);
        } else {
            roteirizacao = TestUtil.findAll(em, Roteirizacao.class).get(0);
        }
        em.persist(roteirizacao);
        em.flush();
        historicoStatusColeta.setRoteirizacao(roteirizacao);
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        Long roteirizacaoId = roteirizacao.getId();
        // Get all the historicoStatusColetaList where roteirizacao equals to roteirizacaoId
        defaultHistoricoStatusColetaShouldBeFound("roteirizacaoId.equals=" + roteirizacaoId);

        // Get all the historicoStatusColetaList where roteirizacao equals to (roteirizacaoId + 1)
        defaultHistoricoStatusColetaShouldNotBeFound("roteirizacaoId.equals=" + (roteirizacaoId + 1));
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByStatusColetaOrigemIsEqualToSomething() throws Exception {
        StatusColeta statusColetaOrigem;
        if (TestUtil.findAll(em, StatusColeta.class).isEmpty()) {
            historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
            statusColetaOrigem = StatusColetaResourceIT.createEntity(em);
        } else {
            statusColetaOrigem = TestUtil.findAll(em, StatusColeta.class).get(0);
        }
        em.persist(statusColetaOrigem);
        em.flush();
        historicoStatusColeta.setStatusColetaOrigem(statusColetaOrigem);
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        Long statusColetaOrigemId = statusColetaOrigem.getId();
        // Get all the historicoStatusColetaList where statusColetaOrigem equals to statusColetaOrigemId
        defaultHistoricoStatusColetaShouldBeFound("statusColetaOrigemId.equals=" + statusColetaOrigemId);

        // Get all the historicoStatusColetaList where statusColetaOrigem equals to (statusColetaOrigemId + 1)
        defaultHistoricoStatusColetaShouldNotBeFound("statusColetaOrigemId.equals=" + (statusColetaOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetasByStatusColetaDestinoIsEqualToSomething() throws Exception {
        StatusColeta statusColetaDestino;
        if (TestUtil.findAll(em, StatusColeta.class).isEmpty()) {
            historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
            statusColetaDestino = StatusColetaResourceIT.createEntity(em);
        } else {
            statusColetaDestino = TestUtil.findAll(em, StatusColeta.class).get(0);
        }
        em.persist(statusColetaDestino);
        em.flush();
        historicoStatusColeta.setStatusColetaDestino(statusColetaDestino);
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        Long statusColetaDestinoId = statusColetaDestino.getId();
        // Get all the historicoStatusColetaList where statusColetaDestino equals to statusColetaDestinoId
        defaultHistoricoStatusColetaShouldBeFound("statusColetaDestinoId.equals=" + statusColetaDestinoId);

        // Get all the historicoStatusColetaList where statusColetaDestino equals to (statusColetaDestinoId + 1)
        defaultHistoricoStatusColetaShouldNotBeFound("statusColetaDestinoId.equals=" + (statusColetaDestinoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoricoStatusColetaShouldBeFound(String filter) throws Exception {
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoStatusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));

        // Check, that the count call also returns 1
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoricoStatusColetaShouldNotBeFound(String filter) throws Exception {
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoricoStatusColeta() throws Exception {
        // Get the historicoStatusColeta
        restHistoricoStatusColetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        historicoStatusColetaSearchRepository.save(historicoStatusColeta);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());

        // Update the historicoStatusColeta
        HistoricoStatusColeta updatedHistoricoStatusColeta = historicoStatusColetaRepository
            .findById(historicoStatusColeta.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedHistoricoStatusColeta are not directly saved in db
        em.detach(updatedHistoricoStatusColeta);
        updatedHistoricoStatusColeta.dataCriacao(UPDATED_DATA_CRIACAO).observacao(UPDATED_OBSERVACAO);
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(updatedHistoricoStatusColeta);

        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historicoStatusColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HistoricoStatusColeta> historicoStatusColetaSearchList = IterableUtils.toList(
                    historicoStatusColetaSearchRepository.findAll()
                );
                HistoricoStatusColeta testHistoricoStatusColetaSearch = historicoStatusColetaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHistoricoStatusColetaSearch.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
                assertThat(testHistoricoStatusColetaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historicoStatusColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateHistoricoStatusColetaWithPatch() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();

        // Update the historicoStatusColeta using partial update
        HistoricoStatusColeta partialUpdatedHistoricoStatusColeta = new HistoricoStatusColeta();
        partialUpdatedHistoricoStatusColeta.setId(historicoStatusColeta.getId());

        partialUpdatedHistoricoStatusColeta.dataCriacao(UPDATED_DATA_CRIACAO).observacao(UPDATED_OBSERVACAO);

        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoricoStatusColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoricoStatusColeta))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void fullUpdateHistoricoStatusColetaWithPatch() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();

        // Update the historicoStatusColeta using partial update
        HistoricoStatusColeta partialUpdatedHistoricoStatusColeta = new HistoricoStatusColeta();
        partialUpdatedHistoricoStatusColeta.setId(historicoStatusColeta.getId());

        partialUpdatedHistoricoStatusColeta.dataCriacao(UPDATED_DATA_CRIACAO).observacao(UPDATED_OBSERVACAO);

        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoricoStatusColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoricoStatusColeta))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void patchNonExistingHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historicoStatusColetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        historicoStatusColetaRepository.save(historicoStatusColeta);
        historicoStatusColetaSearchRepository.save(historicoStatusColeta);

        int databaseSizeBeforeDelete = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the historicoStatusColeta
        restHistoricoStatusColetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, historicoStatusColeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColeta = historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        historicoStatusColetaSearchRepository.save(historicoStatusColeta);

        // Search the historicoStatusColeta
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + historicoStatusColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoStatusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }
}
