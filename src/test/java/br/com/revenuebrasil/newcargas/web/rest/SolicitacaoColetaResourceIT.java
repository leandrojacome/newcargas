package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.repository.SolicitacaoColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.SolicitacaoColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.SolicitacaoColetaMapper;
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
 * Integration tests for the {@link SolicitacaoColetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SolicitacaoColetaResourceIT {

    private static final Boolean DEFAULT_COLETADO = false;
    private static final Boolean UPDATED_COLETADO = true;

    private static final ZonedDateTime DEFAULT_DATA_HORA_COLETA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_COLETA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_COLETA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ENTREGUE = false;
    private static final Boolean UPDATED_ENTREGUE = true;

    private static final ZonedDateTime DEFAULT_DATA_HORA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELADO = false;
    private static final Boolean UPDATED_CANCELADO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final String ENTITY_API_URL = "/api/solicitacao-coletas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/solicitacao-coletas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SolicitacaoColetaRepository solicitacaoColetaRepository;

    @Autowired
    private SolicitacaoColetaMapper solicitacaoColetaMapper;

    @Autowired
    private SolicitacaoColetaSearchRepository solicitacaoColetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSolicitacaoColetaMockMvc;

    private SolicitacaoColeta solicitacaoColeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolicitacaoColeta createEntity(EntityManager em) {
        SolicitacaoColeta solicitacaoColeta = new SolicitacaoColeta()
            .coletado(DEFAULT_COLETADO)
            .dataHoraColeta(DEFAULT_DATA_HORA_COLETA)
            .entregue(DEFAULT_ENTREGUE)
            .dataHoraEntrega(DEFAULT_DATA_HORA_ENTREGA)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .observacao(DEFAULT_OBSERVACAO)
            .cancelado(DEFAULT_CANCELADO)
            .removido(DEFAULT_REMOVIDO);
        return solicitacaoColeta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolicitacaoColeta createUpdatedEntity(EntityManager em) {
        SolicitacaoColeta solicitacaoColeta = new SolicitacaoColeta()
            .coletado(UPDATED_COLETADO)
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .entregue(UPDATED_ENTREGUE)
            .dataHoraEntrega(UPDATED_DATA_HORA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        return solicitacaoColeta;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        solicitacaoColetaSearchRepository.deleteAll();
        assertThat(solicitacaoColetaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        solicitacaoColeta = createEntity(em);
    }

    @Test
    @Transactional
    void createSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);
        restSolicitacaoColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SolicitacaoColeta testSolicitacaoColeta = solicitacaoColetaList.get(solicitacaoColetaList.size() - 1);
        assertThat(testSolicitacaoColeta.getColetado()).isEqualTo(DEFAULT_COLETADO);
        assertThat(testSolicitacaoColeta.getDataHoraColeta()).isEqualTo(DEFAULT_DATA_HORA_COLETA);
        assertThat(testSolicitacaoColeta.getEntregue()).isEqualTo(DEFAULT_ENTREGUE);
        assertThat(testSolicitacaoColeta.getDataHoraEntrega()).isEqualTo(DEFAULT_DATA_HORA_ENTREGA);
        assertThat(testSolicitacaoColeta.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testSolicitacaoColeta.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void createSolicitacaoColetaWithExistingId() throws Exception {
        // Create the SolicitacaoColeta with an existing ID
        solicitacaoColeta.setId(1L);
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        int databaseSizeBeforeCreate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitacaoColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkColetadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        // set the field null
        solicitacaoColeta.setColetado(null);

        // Create the SolicitacaoColeta, which fails.
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        restSolicitacaoColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataHoraColetaIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        // set the field null
        solicitacaoColeta.setDataHoraColeta(null);

        // Create the SolicitacaoColeta, which fails.
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        restSolicitacaoColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEntregueIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        // set the field null
        solicitacaoColeta.setEntregue(null);

        // Create the SolicitacaoColeta, which fails.
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        restSolicitacaoColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetas() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacaoColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].coletado").value(hasItem(DEFAULT_COLETADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHoraColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_COLETA))))
            .andExpect(jsonPath("$.[*].entregue").value(hasItem(DEFAULT_ENTREGUE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHoraEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENTREGA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }

    @Test
    @Transactional
    void getSolicitacaoColeta() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get the solicitacaoColeta
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_API_URL_ID, solicitacaoColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(solicitacaoColeta.getId().intValue()))
            .andExpect(jsonPath("$.coletado").value(DEFAULT_COLETADO.booleanValue()))
            .andExpect(jsonPath("$.dataHoraColeta").value(sameInstant(DEFAULT_DATA_HORA_COLETA)))
            .andExpect(jsonPath("$.entregue").value(DEFAULT_ENTREGUE.booleanValue()))
            .andExpect(jsonPath("$.dataHoraEntrega").value(sameInstant(DEFAULT_DATA_HORA_ENTREGA)))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getSolicitacaoColetasByIdFiltering() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        Long id = solicitacaoColeta.getId();

        defaultSolicitacaoColetaShouldBeFound("id.equals=" + id);
        defaultSolicitacaoColetaShouldNotBeFound("id.notEquals=" + id);

        defaultSolicitacaoColetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSolicitacaoColetaShouldNotBeFound("id.greaterThan=" + id);

        defaultSolicitacaoColetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSolicitacaoColetaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByColetadoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where coletado equals to DEFAULT_COLETADO
        defaultSolicitacaoColetaShouldBeFound("coletado.equals=" + DEFAULT_COLETADO);

        // Get all the solicitacaoColetaList where coletado equals to UPDATED_COLETADO
        defaultSolicitacaoColetaShouldNotBeFound("coletado.equals=" + UPDATED_COLETADO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByColetadoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where coletado in DEFAULT_COLETADO or UPDATED_COLETADO
        defaultSolicitacaoColetaShouldBeFound("coletado.in=" + DEFAULT_COLETADO + "," + UPDATED_COLETADO);

        // Get all the solicitacaoColetaList where coletado equals to UPDATED_COLETADO
        defaultSolicitacaoColetaShouldNotBeFound("coletado.in=" + UPDATED_COLETADO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByColetadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where coletado is not null
        defaultSolicitacaoColetaShouldBeFound("coletado.specified=true");

        // Get all the solicitacaoColetaList where coletado is null
        defaultSolicitacaoColetaShouldNotBeFound("coletado.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta equals to DEFAULT_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.equals=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the solicitacaoColetaList where dataHoraColeta equals to UPDATED_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.equals=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta in DEFAULT_DATA_HORA_COLETA or UPDATED_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.in=" + DEFAULT_DATA_HORA_COLETA + "," + UPDATED_DATA_HORA_COLETA);

        // Get all the solicitacaoColetaList where dataHoraColeta equals to UPDATED_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.in=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta is not null
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.specified=true");

        // Get all the solicitacaoColetaList where dataHoraColeta is null
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta is greater than or equal to DEFAULT_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.greaterThanOrEqual=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the solicitacaoColetaList where dataHoraColeta is greater than or equal to UPDATED_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.greaterThanOrEqual=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta is less than or equal to DEFAULT_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.lessThanOrEqual=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the solicitacaoColetaList where dataHoraColeta is less than or equal to SMALLER_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.lessThanOrEqual=" + SMALLER_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta is less than DEFAULT_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.lessThan=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the solicitacaoColetaList where dataHoraColeta is less than UPDATED_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.lessThan=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraColetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraColeta is greater than DEFAULT_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraColeta.greaterThan=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the solicitacaoColetaList where dataHoraColeta is greater than SMALLER_DATA_HORA_COLETA
        defaultSolicitacaoColetaShouldBeFound("dataHoraColeta.greaterThan=" + SMALLER_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByEntregueIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where entregue equals to DEFAULT_ENTREGUE
        defaultSolicitacaoColetaShouldBeFound("entregue.equals=" + DEFAULT_ENTREGUE);

        // Get all the solicitacaoColetaList where entregue equals to UPDATED_ENTREGUE
        defaultSolicitacaoColetaShouldNotBeFound("entregue.equals=" + UPDATED_ENTREGUE);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByEntregueIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where entregue in DEFAULT_ENTREGUE or UPDATED_ENTREGUE
        defaultSolicitacaoColetaShouldBeFound("entregue.in=" + DEFAULT_ENTREGUE + "," + UPDATED_ENTREGUE);

        // Get all the solicitacaoColetaList where entregue equals to UPDATED_ENTREGUE
        defaultSolicitacaoColetaShouldNotBeFound("entregue.in=" + UPDATED_ENTREGUE);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByEntregueIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where entregue is not null
        defaultSolicitacaoColetaShouldBeFound("entregue.specified=true");

        // Get all the solicitacaoColetaList where entregue is null
        defaultSolicitacaoColetaShouldNotBeFound("entregue.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega equals to DEFAULT_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.equals=" + DEFAULT_DATA_HORA_ENTREGA);

        // Get all the solicitacaoColetaList where dataHoraEntrega equals to UPDATED_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.equals=" + UPDATED_DATA_HORA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega in DEFAULT_DATA_HORA_ENTREGA or UPDATED_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.in=" + DEFAULT_DATA_HORA_ENTREGA + "," + UPDATED_DATA_HORA_ENTREGA);

        // Get all the solicitacaoColetaList where dataHoraEntrega equals to UPDATED_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.in=" + UPDATED_DATA_HORA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega is not null
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.specified=true");

        // Get all the solicitacaoColetaList where dataHoraEntrega is null
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega is greater than or equal to DEFAULT_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.greaterThanOrEqual=" + DEFAULT_DATA_HORA_ENTREGA);

        // Get all the solicitacaoColetaList where dataHoraEntrega is greater than or equal to UPDATED_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.greaterThanOrEqual=" + UPDATED_DATA_HORA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega is less than or equal to DEFAULT_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.lessThanOrEqual=" + DEFAULT_DATA_HORA_ENTREGA);

        // Get all the solicitacaoColetaList where dataHoraEntrega is less than or equal to SMALLER_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.lessThanOrEqual=" + SMALLER_DATA_HORA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega is less than DEFAULT_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.lessThan=" + DEFAULT_DATA_HORA_ENTREGA);

        // Get all the solicitacaoColetaList where dataHoraEntrega is less than UPDATED_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.lessThan=" + UPDATED_DATA_HORA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByDataHoraEntregaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where dataHoraEntrega is greater than DEFAULT_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldNotBeFound("dataHoraEntrega.greaterThan=" + DEFAULT_DATA_HORA_ENTREGA);

        // Get all the solicitacaoColetaList where dataHoraEntrega is greater than SMALLER_DATA_HORA_ENTREGA
        defaultSolicitacaoColetaShouldBeFound("dataHoraEntrega.greaterThan=" + SMALLER_DATA_HORA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultSolicitacaoColetaShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the solicitacaoColetaList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultSolicitacaoColetaShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the solicitacaoColetaList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal is not null
        defaultSolicitacaoColetaShouldBeFound("valorTotal.specified=true");

        // Get all the solicitacaoColetaList where valorTotal is null
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultSolicitacaoColetaShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the solicitacaoColetaList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultSolicitacaoColetaShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the solicitacaoColetaList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the solicitacaoColetaList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultSolicitacaoColetaShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultSolicitacaoColetaShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the solicitacaoColetaList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultSolicitacaoColetaShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where observacao equals to DEFAULT_OBSERVACAO
        defaultSolicitacaoColetaShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the solicitacaoColetaList where observacao equals to UPDATED_OBSERVACAO
        defaultSolicitacaoColetaShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultSolicitacaoColetaShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the solicitacaoColetaList where observacao equals to UPDATED_OBSERVACAO
        defaultSolicitacaoColetaShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where observacao is not null
        defaultSolicitacaoColetaShouldBeFound("observacao.specified=true");

        // Get all the solicitacaoColetaList where observacao is null
        defaultSolicitacaoColetaShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where observacao contains DEFAULT_OBSERVACAO
        defaultSolicitacaoColetaShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the solicitacaoColetaList where observacao contains UPDATED_OBSERVACAO
        defaultSolicitacaoColetaShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where observacao does not contain DEFAULT_OBSERVACAO
        defaultSolicitacaoColetaShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the solicitacaoColetaList where observacao does not contain UPDATED_OBSERVACAO
        defaultSolicitacaoColetaShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByCanceladoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where cancelado equals to DEFAULT_CANCELADO
        defaultSolicitacaoColetaShouldBeFound("cancelado.equals=" + DEFAULT_CANCELADO);

        // Get all the solicitacaoColetaList where cancelado equals to UPDATED_CANCELADO
        defaultSolicitacaoColetaShouldNotBeFound("cancelado.equals=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByCanceladoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where cancelado in DEFAULT_CANCELADO or UPDATED_CANCELADO
        defaultSolicitacaoColetaShouldBeFound("cancelado.in=" + DEFAULT_CANCELADO + "," + UPDATED_CANCELADO);

        // Get all the solicitacaoColetaList where cancelado equals to UPDATED_CANCELADO
        defaultSolicitacaoColetaShouldNotBeFound("cancelado.in=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByCanceladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where cancelado is not null
        defaultSolicitacaoColetaShouldBeFound("cancelado.specified=true");

        // Get all the solicitacaoColetaList where cancelado is null
        defaultSolicitacaoColetaShouldNotBeFound("cancelado.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where removido equals to DEFAULT_REMOVIDO
        defaultSolicitacaoColetaShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the solicitacaoColetaList where removido equals to UPDATED_REMOVIDO
        defaultSolicitacaoColetaShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultSolicitacaoColetaShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the solicitacaoColetaList where removido equals to UPDATED_REMOVIDO
        defaultSolicitacaoColetaShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        // Get all the solicitacaoColetaList where removido is not null
        defaultSolicitacaoColetaShouldBeFound("removido.specified=true");

        // Get all the solicitacaoColetaList where removido is null
        defaultSolicitacaoColetaShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByNotaFiscalColetaIsEqualToSomething() throws Exception {
        NotaFiscalColeta notaFiscalColeta;
        if (TestUtil.findAll(em, NotaFiscalColeta.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            notaFiscalColeta = NotaFiscalColetaResourceIT.createEntity(em);
        } else {
            notaFiscalColeta = TestUtil.findAll(em, NotaFiscalColeta.class).get(0);
        }
        em.persist(notaFiscalColeta);
        em.flush();
        solicitacaoColeta.addNotaFiscalColeta(notaFiscalColeta);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long notaFiscalColetaId = notaFiscalColeta.getId();
        // Get all the solicitacaoColetaList where notaFiscalColeta equals to notaFiscalColetaId
        defaultSolicitacaoColetaShouldBeFound("notaFiscalColetaId.equals=" + notaFiscalColetaId);

        // Get all the solicitacaoColetaList where notaFiscalColeta equals to (notaFiscalColetaId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("notaFiscalColetaId.equals=" + (notaFiscalColetaId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByEnderecoOrigemIsEqualToSomething() throws Exception {
        Endereco enderecoOrigem;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            enderecoOrigem = EnderecoResourceIT.createEntity(em);
        } else {
            enderecoOrigem = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(enderecoOrigem);
        em.flush();
        solicitacaoColeta.addEnderecoOrigem(enderecoOrigem);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long enderecoOrigemId = enderecoOrigem.getId();
        // Get all the solicitacaoColetaList where enderecoOrigem equals to enderecoOrigemId
        defaultSolicitacaoColetaShouldBeFound("enderecoOrigemId.equals=" + enderecoOrigemId);

        // Get all the solicitacaoColetaList where enderecoOrigem equals to (enderecoOrigemId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("enderecoOrigemId.equals=" + (enderecoOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByEnderecoDestinoIsEqualToSomething() throws Exception {
        Endereco enderecoDestino;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            enderecoDestino = EnderecoResourceIT.createEntity(em);
        } else {
            enderecoDestino = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(enderecoDestino);
        em.flush();
        solicitacaoColeta.addEnderecoDestino(enderecoDestino);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long enderecoDestinoId = enderecoDestino.getId();
        // Get all the solicitacaoColetaList where enderecoDestino equals to enderecoDestinoId
        defaultSolicitacaoColetaShouldBeFound("enderecoDestinoId.equals=" + enderecoDestinoId);

        // Get all the solicitacaoColetaList where enderecoDestino equals to (enderecoDestinoId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("enderecoDestinoId.equals=" + (enderecoDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByHistoricoStatusColetaIsEqualToSomething() throws Exception {
        HistoricoStatusColeta historicoStatusColeta;
        if (TestUtil.findAll(em, HistoricoStatusColeta.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            historicoStatusColeta = HistoricoStatusColetaResourceIT.createEntity(em);
        } else {
            historicoStatusColeta = TestUtil.findAll(em, HistoricoStatusColeta.class).get(0);
        }
        em.persist(historicoStatusColeta);
        em.flush();
        solicitacaoColeta.addHistoricoStatusColeta(historicoStatusColeta);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long historicoStatusColetaId = historicoStatusColeta.getId();
        // Get all the solicitacaoColetaList where historicoStatusColeta equals to historicoStatusColetaId
        defaultSolicitacaoColetaShouldBeFound("historicoStatusColetaId.equals=" + historicoStatusColetaId);

        // Get all the solicitacaoColetaList where historicoStatusColeta equals to (historicoStatusColetaId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("historicoStatusColetaId.equals=" + (historicoStatusColetaId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        solicitacaoColeta.setEmbarcador(embarcador);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long embarcadorId = embarcador.getId();
        // Get all the solicitacaoColetaList where embarcador equals to embarcadorId
        defaultSolicitacaoColetaShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the solicitacaoColetaList where embarcador equals to (embarcadorId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByStatusColetaIsEqualToSomething() throws Exception {
        StatusColeta statusColeta;
        if (TestUtil.findAll(em, StatusColeta.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            statusColeta = StatusColetaResourceIT.createEntity(em);
        } else {
            statusColeta = TestUtil.findAll(em, StatusColeta.class).get(0);
        }
        em.persist(statusColeta);
        em.flush();
        solicitacaoColeta.setStatusColeta(statusColeta);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long statusColetaId = statusColeta.getId();
        // Get all the solicitacaoColetaList where statusColeta equals to statusColetaId
        defaultSolicitacaoColetaShouldBeFound("statusColetaId.equals=" + statusColetaId);

        // Get all the solicitacaoColetaList where statusColeta equals to (statusColetaId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("statusColetaId.equals=" + (statusColetaId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByRoteirizacaoIsEqualToSomething() throws Exception {
        Roteirizacao roteirizacao;
        if (TestUtil.findAll(em, Roteirizacao.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            roteirizacao = RoteirizacaoResourceIT.createEntity(em);
        } else {
            roteirizacao = TestUtil.findAll(em, Roteirizacao.class).get(0);
        }
        em.persist(roteirizacao);
        em.flush();
        solicitacaoColeta.setRoteirizacao(roteirizacao);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long roteirizacaoId = roteirizacao.getId();
        // Get all the solicitacaoColetaList where roteirizacao equals to roteirizacaoId
        defaultSolicitacaoColetaShouldBeFound("roteirizacaoId.equals=" + roteirizacaoId);

        // Get all the solicitacaoColetaList where roteirizacao equals to (roteirizacaoId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("roteirizacaoId.equals=" + (roteirizacaoId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaoColetasByTipoVeiculoIsEqualToSomething() throws Exception {
        TipoVeiculo tipoVeiculo;
        if (TestUtil.findAll(em, TipoVeiculo.class).isEmpty()) {
            solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
            tipoVeiculo = TipoVeiculoResourceIT.createEntity(em);
        } else {
            tipoVeiculo = TestUtil.findAll(em, TipoVeiculo.class).get(0);
        }
        em.persist(tipoVeiculo);
        em.flush();
        solicitacaoColeta.setTipoVeiculo(tipoVeiculo);
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        Long tipoVeiculoId = tipoVeiculo.getId();
        // Get all the solicitacaoColetaList where tipoVeiculo equals to tipoVeiculoId
        defaultSolicitacaoColetaShouldBeFound("tipoVeiculoId.equals=" + tipoVeiculoId);

        // Get all the solicitacaoColetaList where tipoVeiculo equals to (tipoVeiculoId + 1)
        defaultSolicitacaoColetaShouldNotBeFound("tipoVeiculoId.equals=" + (tipoVeiculoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSolicitacaoColetaShouldBeFound(String filter) throws Exception {
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacaoColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].coletado").value(hasItem(DEFAULT_COLETADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHoraColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_COLETA))))
            .andExpect(jsonPath("$.[*].entregue").value(hasItem(DEFAULT_ENTREGUE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHoraEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENTREGA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSolicitacaoColetaShouldNotBeFound(String filter) throws Exception {
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSolicitacaoColeta() throws Exception {
        // Get the solicitacaoColeta
        restSolicitacaoColetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSolicitacaoColeta() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        solicitacaoColetaSearchRepository.save(solicitacaoColeta);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());

        // Update the solicitacaoColeta
        SolicitacaoColeta updatedSolicitacaoColeta = solicitacaoColetaRepository.findById(solicitacaoColeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSolicitacaoColeta are not directly saved in db
        em.detach(updatedSolicitacaoColeta);
        updatedSolicitacaoColeta
            .coletado(UPDATED_COLETADO)
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .entregue(UPDATED_ENTREGUE)
            .dataHoraEntrega(UPDATED_DATA_HORA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(updatedSolicitacaoColeta);

        restSolicitacaoColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitacaoColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        SolicitacaoColeta testSolicitacaoColeta = solicitacaoColetaList.get(solicitacaoColetaList.size() - 1);
        assertThat(testSolicitacaoColeta.getColetado()).isEqualTo(UPDATED_COLETADO);
        assertThat(testSolicitacaoColeta.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
        assertThat(testSolicitacaoColeta.getEntregue()).isEqualTo(UPDATED_ENTREGUE);
        assertThat(testSolicitacaoColeta.getDataHoraEntrega()).isEqualTo(UPDATED_DATA_HORA_ENTREGA);
        assertThat(testSolicitacaoColeta.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testSolicitacaoColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SolicitacaoColeta> solicitacaoColetaSearchList = IterableUtils.toList(solicitacaoColetaSearchRepository.findAll());
                SolicitacaoColeta testSolicitacaoColetaSearch = solicitacaoColetaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSolicitacaoColetaSearch.getColetado()).isEqualTo(UPDATED_COLETADO);
                assertThat(testSolicitacaoColetaSearch.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
                assertThat(testSolicitacaoColetaSearch.getEntregue()).isEqualTo(UPDATED_ENTREGUE);
                assertThat(testSolicitacaoColetaSearch.getDataHoraEntrega()).isEqualTo(UPDATED_DATA_HORA_ENTREGA);
                assertThat(testSolicitacaoColetaSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testSolicitacaoColetaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testSolicitacaoColetaSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testSolicitacaoColetaSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
            });
    }

    @Test
    @Transactional
    void putNonExistingSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        solicitacaoColeta.setId(longCount.incrementAndGet());

        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitacaoColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        solicitacaoColeta.setId(longCount.incrementAndGet());

        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        solicitacaoColeta.setId(longCount.incrementAndGet());

        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoColetaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSolicitacaoColetaWithPatch() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();

        // Update the solicitacaoColeta using partial update
        SolicitacaoColeta partialUpdatedSolicitacaoColeta = new SolicitacaoColeta();
        partialUpdatedSolicitacaoColeta.setId(solicitacaoColeta.getId());

        partialUpdatedSolicitacaoColeta.dataHoraColeta(UPDATED_DATA_HORA_COLETA).observacao(UPDATED_OBSERVACAO);

        restSolicitacaoColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitacaoColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitacaoColeta))
            )
            .andExpect(status().isOk());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        SolicitacaoColeta testSolicitacaoColeta = solicitacaoColetaList.get(solicitacaoColetaList.size() - 1);
        assertThat(testSolicitacaoColeta.getColetado()).isEqualTo(DEFAULT_COLETADO);
        assertThat(testSolicitacaoColeta.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
        assertThat(testSolicitacaoColeta.getEntregue()).isEqualTo(DEFAULT_ENTREGUE);
        assertThat(testSolicitacaoColeta.getDataHoraEntrega()).isEqualTo(DEFAULT_DATA_HORA_ENTREGA);
        assertThat(testSolicitacaoColeta.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testSolicitacaoColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void fullUpdateSolicitacaoColetaWithPatch() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);

        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();

        // Update the solicitacaoColeta using partial update
        SolicitacaoColeta partialUpdatedSolicitacaoColeta = new SolicitacaoColeta();
        partialUpdatedSolicitacaoColeta.setId(solicitacaoColeta.getId());

        partialUpdatedSolicitacaoColeta
            .coletado(UPDATED_COLETADO)
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .entregue(UPDATED_ENTREGUE)
            .dataHoraEntrega(UPDATED_DATA_HORA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);

        restSolicitacaoColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitacaoColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitacaoColeta))
            )
            .andExpect(status().isOk());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        SolicitacaoColeta testSolicitacaoColeta = solicitacaoColetaList.get(solicitacaoColetaList.size() - 1);
        assertThat(testSolicitacaoColeta.getColetado()).isEqualTo(UPDATED_COLETADO);
        assertThat(testSolicitacaoColeta.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
        assertThat(testSolicitacaoColeta.getEntregue()).isEqualTo(UPDATED_ENTREGUE);
        assertThat(testSolicitacaoColeta.getDataHoraEntrega()).isEqualTo(UPDATED_DATA_HORA_ENTREGA);
        assertThat(testSolicitacaoColeta.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testSolicitacaoColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void patchNonExistingSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        solicitacaoColeta.setId(longCount.incrementAndGet());

        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, solicitacaoColetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        solicitacaoColeta.setId(longCount.incrementAndGet());

        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSolicitacaoColeta() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        solicitacaoColeta.setId(longCount.incrementAndGet());

        // Create the SolicitacaoColeta
        SolicitacaoColetaDTO solicitacaoColetaDTO = solicitacaoColetaMapper.toDto(solicitacaoColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoColetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SolicitacaoColeta in the database
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSolicitacaoColeta() throws Exception {
        // Initialize the database
        solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        solicitacaoColetaRepository.save(solicitacaoColeta);
        solicitacaoColetaSearchRepository.save(solicitacaoColeta);

        int databaseSizeBeforeDelete = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the solicitacaoColeta
        restSolicitacaoColetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, solicitacaoColeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolicitacaoColeta> solicitacaoColetaList = solicitacaoColetaRepository.findAll();
        assertThat(solicitacaoColetaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSolicitacaoColeta() throws Exception {
        // Initialize the database
        solicitacaoColeta = solicitacaoColetaRepository.saveAndFlush(solicitacaoColeta);
        solicitacaoColetaSearchRepository.save(solicitacaoColeta);

        // Search the solicitacaoColeta
        restSolicitacaoColetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + solicitacaoColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacaoColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].coletado").value(hasItem(DEFAULT_COLETADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHoraColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_COLETA))))
            .andExpect(jsonPath("$.[*].entregue").value(hasItem(DEFAULT_ENTREGUE.booleanValue())))
            .andExpect(jsonPath("$.[*].dataHoraEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENTREGA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
