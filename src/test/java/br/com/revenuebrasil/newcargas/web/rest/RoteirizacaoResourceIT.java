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
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.repository.RoteirizacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.RoteirizacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.RoteirizacaoMapper;
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
 * Integration tests for the {@link RoteirizacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoteirizacaoResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_HORA_PRIMEIRA_COLETA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATA_HORA_PRIMEIRA_COLETA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_PRIMEIRA_COLETA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final ZonedDateTime DEFAULT_DATA_HORA_ULTIMA_COLETA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ULTIMA_COLETA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_ULTIMA_COLETA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATA_HORA_PRIMEIRA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_PRIMEIRA_ENTREGA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final ZonedDateTime DEFAULT_DATA_HORA_ULTIMA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ULTIMA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_ULTIMA_ENTREGA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELADO = false;
    private static final Boolean UPDATED_CANCELADO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final String ENTITY_API_URL = "/api/roteirizacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/roteirizacaos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoteirizacaoRepository roteirizacaoRepository;

    @Autowired
    private RoteirizacaoMapper roteirizacaoMapper;

    @Autowired
    private RoteirizacaoSearchRepository roteirizacaoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoteirizacaoMockMvc;

    private Roteirizacao roteirizacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roteirizacao createEntity(EntityManager em) {
        Roteirizacao roteirizacao = new Roteirizacao()
            .dataHoraPrimeiraColeta(DEFAULT_DATA_HORA_PRIMEIRA_COLETA)
            .dataHoraUltimaColeta(DEFAULT_DATA_HORA_ULTIMA_COLETA)
            .dataHoraPrimeiraEntrega(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA)
            .dataHoraUltimaEntrega(DEFAULT_DATA_HORA_ULTIMA_ENTREGA)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .observacao(DEFAULT_OBSERVACAO)
            .cancelado(DEFAULT_CANCELADO)
            .removido(DEFAULT_REMOVIDO);
        return roteirizacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roteirizacao createUpdatedEntity(EntityManager em) {
        Roteirizacao roteirizacao = new Roteirizacao()
            .dataHoraPrimeiraColeta(UPDATED_DATA_HORA_PRIMEIRA_COLETA)
            .dataHoraUltimaColeta(UPDATED_DATA_HORA_ULTIMA_COLETA)
            .dataHoraPrimeiraEntrega(UPDATED_DATA_HORA_PRIMEIRA_ENTREGA)
            .dataHoraUltimaEntrega(UPDATED_DATA_HORA_ULTIMA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        return roteirizacao;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        roteirizacaoSearchRepository.deleteAll();
        assertThat(roteirizacaoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        roteirizacao = createEntity(em);
    }

    @Test
    @Transactional
    void createRoteirizacao() throws Exception {
        int databaseSizeBeforeCreate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);
        restRoteirizacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Roteirizacao testRoteirizacao = roteirizacaoList.get(roteirizacaoList.size() - 1);
        assertThat(testRoteirizacao.getDataHoraPrimeiraColeta()).isEqualTo(DEFAULT_DATA_HORA_PRIMEIRA_COLETA);
        assertThat(testRoteirizacao.getDataHoraUltimaColeta()).isEqualTo(DEFAULT_DATA_HORA_ULTIMA_COLETA);
        assertThat(testRoteirizacao.getDataHoraPrimeiraEntrega()).isEqualTo(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);
        assertThat(testRoteirizacao.getDataHoraUltimaEntrega()).isEqualTo(DEFAULT_DATA_HORA_ULTIMA_ENTREGA);
        assertThat(testRoteirizacao.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testRoteirizacao.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void createRoteirizacaoWithExistingId() throws Exception {
        // Create the Roteirizacao with an existing ID
        roteirizacao.setId(1L);
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        int databaseSizeBeforeCreate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoteirizacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataHoraPrimeiraColetaIsRequired() throws Exception {
        int databaseSizeBeforeTest = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        // set the field null
        roteirizacao.setDataHoraPrimeiraColeta(null);

        // Create the Roteirizacao, which fails.
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        restRoteirizacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllRoteirizacaos() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roteirizacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraPrimeiraColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_COLETA))))
            .andExpect(jsonPath("$.[*].dataHoraUltimaColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ULTIMA_COLETA))))
            .andExpect(jsonPath("$.[*].dataHoraPrimeiraEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA))))
            .andExpect(jsonPath("$.[*].dataHoraUltimaEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ULTIMA_ENTREGA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }

    @Test
    @Transactional
    void getRoteirizacao() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get the roteirizacao
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, roteirizacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roteirizacao.getId().intValue()))
            .andExpect(jsonPath("$.dataHoraPrimeiraColeta").value(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_COLETA)))
            .andExpect(jsonPath("$.dataHoraUltimaColeta").value(sameInstant(DEFAULT_DATA_HORA_ULTIMA_COLETA)))
            .andExpect(jsonPath("$.dataHoraPrimeiraEntrega").value(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA)))
            .andExpect(jsonPath("$.dataHoraUltimaEntrega").value(sameInstant(DEFAULT_DATA_HORA_ULTIMA_ENTREGA)))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getRoteirizacaosByIdFiltering() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        Long id = roteirizacao.getId();

        defaultRoteirizacaoShouldBeFound("id.equals=" + id);
        defaultRoteirizacaoShouldNotBeFound("id.notEquals=" + id);

        defaultRoteirizacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoteirizacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultRoteirizacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoteirizacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta equals to DEFAULT_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraColeta.equals=" + DEFAULT_DATA_HORA_PRIMEIRA_COLETA);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta equals to UPDATED_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.equals=" + UPDATED_DATA_HORA_PRIMEIRA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta in DEFAULT_DATA_HORA_PRIMEIRA_COLETA or UPDATED_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldBeFound(
            "dataHoraPrimeiraColeta.in=" + DEFAULT_DATA_HORA_PRIMEIRA_COLETA + "," + UPDATED_DATA_HORA_PRIMEIRA_COLETA
        );

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta equals to UPDATED_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.in=" + UPDATED_DATA_HORA_PRIMEIRA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is not null
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraColeta.specified=true");

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is null
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is greater than or equal to DEFAULT_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraColeta.greaterThanOrEqual=" + DEFAULT_DATA_HORA_PRIMEIRA_COLETA);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is greater than or equal to UPDATED_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.greaterThanOrEqual=" + UPDATED_DATA_HORA_PRIMEIRA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is less than or equal to DEFAULT_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraColeta.lessThanOrEqual=" + DEFAULT_DATA_HORA_PRIMEIRA_COLETA);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is less than or equal to SMALLER_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.lessThanOrEqual=" + SMALLER_DATA_HORA_PRIMEIRA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsLessThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is less than DEFAULT_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.lessThan=" + DEFAULT_DATA_HORA_PRIMEIRA_COLETA);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is less than UPDATED_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraColeta.lessThan=" + UPDATED_DATA_HORA_PRIMEIRA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraColetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is greater than DEFAULT_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraColeta.greaterThan=" + DEFAULT_DATA_HORA_PRIMEIRA_COLETA);

        // Get all the roteirizacaoList where dataHoraPrimeiraColeta is greater than SMALLER_DATA_HORA_PRIMEIRA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraColeta.greaterThan=" + SMALLER_DATA_HORA_PRIMEIRA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta equals to DEFAULT_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaColeta.equals=" + DEFAULT_DATA_HORA_ULTIMA_COLETA);

        // Get all the roteirizacaoList where dataHoraUltimaColeta equals to UPDATED_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.equals=" + UPDATED_DATA_HORA_ULTIMA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta in DEFAULT_DATA_HORA_ULTIMA_COLETA or UPDATED_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldBeFound(
            "dataHoraUltimaColeta.in=" + DEFAULT_DATA_HORA_ULTIMA_COLETA + "," + UPDATED_DATA_HORA_ULTIMA_COLETA
        );

        // Get all the roteirizacaoList where dataHoraUltimaColeta equals to UPDATED_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.in=" + UPDATED_DATA_HORA_ULTIMA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is not null
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaColeta.specified=true");

        // Get all the roteirizacaoList where dataHoraUltimaColeta is null
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is greater than or equal to DEFAULT_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaColeta.greaterThanOrEqual=" + DEFAULT_DATA_HORA_ULTIMA_COLETA);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is greater than or equal to UPDATED_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.greaterThanOrEqual=" + UPDATED_DATA_HORA_ULTIMA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is less than or equal to DEFAULT_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaColeta.lessThanOrEqual=" + DEFAULT_DATA_HORA_ULTIMA_COLETA);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is less than or equal to SMALLER_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.lessThanOrEqual=" + SMALLER_DATA_HORA_ULTIMA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsLessThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is less than DEFAULT_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.lessThan=" + DEFAULT_DATA_HORA_ULTIMA_COLETA);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is less than UPDATED_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaColeta.lessThan=" + UPDATED_DATA_HORA_ULTIMA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaColetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is greater than DEFAULT_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaColeta.greaterThan=" + DEFAULT_DATA_HORA_ULTIMA_COLETA);

        // Get all the roteirizacaoList where dataHoraUltimaColeta is greater than SMALLER_DATA_HORA_ULTIMA_COLETA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaColeta.greaterThan=" + SMALLER_DATA_HORA_ULTIMA_COLETA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega equals to DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraEntrega.equals=" + DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega equals to UPDATED_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.equals=" + UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega in DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA or UPDATED_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldBeFound(
            "dataHoraPrimeiraEntrega.in=" + DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA + "," + UPDATED_DATA_HORA_PRIMEIRA_ENTREGA
        );

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega equals to UPDATED_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.in=" + UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is not null
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraEntrega.specified=true");

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is null
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is greater than or equal to DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraEntrega.greaterThanOrEqual=" + DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is greater than or equal to UPDATED_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.greaterThanOrEqual=" + UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is less than or equal to DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraEntrega.lessThanOrEqual=" + DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is less than or equal to SMALLER_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.lessThanOrEqual=" + SMALLER_DATA_HORA_PRIMEIRA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is less than DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.lessThan=" + DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is less than UPDATED_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraEntrega.lessThan=" + UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraPrimeiraEntregaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is greater than DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraPrimeiraEntrega.greaterThan=" + DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraPrimeiraEntrega is greater than SMALLER_DATA_HORA_PRIMEIRA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraPrimeiraEntrega.greaterThan=" + SMALLER_DATA_HORA_PRIMEIRA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega equals to DEFAULT_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaEntrega.equals=" + DEFAULT_DATA_HORA_ULTIMA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega equals to UPDATED_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.equals=" + UPDATED_DATA_HORA_ULTIMA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega in DEFAULT_DATA_HORA_ULTIMA_ENTREGA or UPDATED_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldBeFound(
            "dataHoraUltimaEntrega.in=" + DEFAULT_DATA_HORA_ULTIMA_ENTREGA + "," + UPDATED_DATA_HORA_ULTIMA_ENTREGA
        );

        // Get all the roteirizacaoList where dataHoraUltimaEntrega equals to UPDATED_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.in=" + UPDATED_DATA_HORA_ULTIMA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is not null
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaEntrega.specified=true");

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is null
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is greater than or equal to DEFAULT_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaEntrega.greaterThanOrEqual=" + DEFAULT_DATA_HORA_ULTIMA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is greater than or equal to UPDATED_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.greaterThanOrEqual=" + UPDATED_DATA_HORA_ULTIMA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is less than or equal to DEFAULT_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaEntrega.lessThanOrEqual=" + DEFAULT_DATA_HORA_ULTIMA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is less than or equal to SMALLER_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.lessThanOrEqual=" + SMALLER_DATA_HORA_ULTIMA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is less than DEFAULT_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.lessThan=" + DEFAULT_DATA_HORA_ULTIMA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is less than UPDATED_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaEntrega.lessThan=" + UPDATED_DATA_HORA_ULTIMA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByDataHoraUltimaEntregaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is greater than DEFAULT_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldNotBeFound("dataHoraUltimaEntrega.greaterThan=" + DEFAULT_DATA_HORA_ULTIMA_ENTREGA);

        // Get all the roteirizacaoList where dataHoraUltimaEntrega is greater than SMALLER_DATA_HORA_ULTIMA_ENTREGA
        defaultRoteirizacaoShouldBeFound("dataHoraUltimaEntrega.greaterThan=" + SMALLER_DATA_HORA_ULTIMA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultRoteirizacaoShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the roteirizacaoList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultRoteirizacaoShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultRoteirizacaoShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the roteirizacaoList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultRoteirizacaoShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal is not null
        defaultRoteirizacaoShouldBeFound("valorTotal.specified=true");

        // Get all the roteirizacaoList where valorTotal is null
        defaultRoteirizacaoShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultRoteirizacaoShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the roteirizacaoList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultRoteirizacaoShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultRoteirizacaoShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the roteirizacaoList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultRoteirizacaoShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultRoteirizacaoShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the roteirizacaoList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultRoteirizacaoShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultRoteirizacaoShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the roteirizacaoList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultRoteirizacaoShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where observacao equals to DEFAULT_OBSERVACAO
        defaultRoteirizacaoShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the roteirizacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultRoteirizacaoShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultRoteirizacaoShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the roteirizacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultRoteirizacaoShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where observacao is not null
        defaultRoteirizacaoShouldBeFound("observacao.specified=true");

        // Get all the roteirizacaoList where observacao is null
        defaultRoteirizacaoShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where observacao contains DEFAULT_OBSERVACAO
        defaultRoteirizacaoShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the roteirizacaoList where observacao contains UPDATED_OBSERVACAO
        defaultRoteirizacaoShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where observacao does not contain DEFAULT_OBSERVACAO
        defaultRoteirizacaoShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the roteirizacaoList where observacao does not contain UPDATED_OBSERVACAO
        defaultRoteirizacaoShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByCanceladoIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where cancelado equals to DEFAULT_CANCELADO
        defaultRoteirizacaoShouldBeFound("cancelado.equals=" + DEFAULT_CANCELADO);

        // Get all the roteirizacaoList where cancelado equals to UPDATED_CANCELADO
        defaultRoteirizacaoShouldNotBeFound("cancelado.equals=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByCanceladoIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where cancelado in DEFAULT_CANCELADO or UPDATED_CANCELADO
        defaultRoteirizacaoShouldBeFound("cancelado.in=" + DEFAULT_CANCELADO + "," + UPDATED_CANCELADO);

        // Get all the roteirizacaoList where cancelado equals to UPDATED_CANCELADO
        defaultRoteirizacaoShouldNotBeFound("cancelado.in=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByCanceladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where cancelado is not null
        defaultRoteirizacaoShouldBeFound("cancelado.specified=true");

        // Get all the roteirizacaoList where cancelado is null
        defaultRoteirizacaoShouldNotBeFound("cancelado.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where removido equals to DEFAULT_REMOVIDO
        defaultRoteirizacaoShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the roteirizacaoList where removido equals to UPDATED_REMOVIDO
        defaultRoteirizacaoShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultRoteirizacaoShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the roteirizacaoList where removido equals to UPDATED_REMOVIDO
        defaultRoteirizacaoShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        // Get all the roteirizacaoList where removido is not null
        defaultRoteirizacaoShouldBeFound("removido.specified=true");

        // Get all the roteirizacaoList where removido is null
        defaultRoteirizacaoShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByHistoricoStatusColetaIsEqualToSomething() throws Exception {
        HistoricoStatusColeta historicoStatusColeta;
        if (TestUtil.findAll(em, HistoricoStatusColeta.class).isEmpty()) {
            roteirizacaoRepository.saveAndFlush(roteirizacao);
            historicoStatusColeta = HistoricoStatusColetaResourceIT.createEntity(em);
        } else {
            historicoStatusColeta = TestUtil.findAll(em, HistoricoStatusColeta.class).get(0);
        }
        em.persist(historicoStatusColeta);
        em.flush();
        roteirizacao.addHistoricoStatusColeta(historicoStatusColeta);
        roteirizacaoRepository.saveAndFlush(roteirizacao);
        Long historicoStatusColetaId = historicoStatusColeta.getId();
        // Get all the roteirizacaoList where historicoStatusColeta equals to historicoStatusColetaId
        defaultRoteirizacaoShouldBeFound("historicoStatusColetaId.equals=" + historicoStatusColetaId);

        // Get all the roteirizacaoList where historicoStatusColeta equals to (historicoStatusColetaId + 1)
        defaultRoteirizacaoShouldNotBeFound("historicoStatusColetaId.equals=" + (historicoStatusColetaId + 1));
    }

    @Test
    @Transactional
    void getAllRoteirizacaosBySolitacaoColetaIsEqualToSomething() throws Exception {
        SolicitacaoColeta solitacaoColeta;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            roteirizacaoRepository.saveAndFlush(roteirizacao);
            solitacaoColeta = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solitacaoColeta = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solitacaoColeta);
        em.flush();
        roteirizacao.addSolitacaoColeta(solitacaoColeta);
        roteirizacaoRepository.saveAndFlush(roteirizacao);
        Long solitacaoColetaId = solitacaoColeta.getId();
        // Get all the roteirizacaoList where solitacaoColeta equals to solitacaoColetaId
        defaultRoteirizacaoShouldBeFound("solitacaoColetaId.equals=" + solitacaoColetaId);

        // Get all the roteirizacaoList where solitacaoColeta equals to (solitacaoColetaId + 1)
        defaultRoteirizacaoShouldNotBeFound("solitacaoColetaId.equals=" + (solitacaoColetaId + 1));
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByTomadaPrecoIsEqualToSomething() throws Exception {
        TomadaPreco tomadaPreco;
        if (TestUtil.findAll(em, TomadaPreco.class).isEmpty()) {
            roteirizacaoRepository.saveAndFlush(roteirizacao);
            tomadaPreco = TomadaPrecoResourceIT.createEntity(em);
        } else {
            tomadaPreco = TestUtil.findAll(em, TomadaPreco.class).get(0);
        }
        em.persist(tomadaPreco);
        em.flush();
        roteirizacao.addTomadaPreco(tomadaPreco);
        roteirizacaoRepository.saveAndFlush(roteirizacao);
        Long tomadaPrecoId = tomadaPreco.getId();
        // Get all the roteirizacaoList where tomadaPreco equals to tomadaPrecoId
        defaultRoteirizacaoShouldBeFound("tomadaPrecoId.equals=" + tomadaPrecoId);

        // Get all the roteirizacaoList where tomadaPreco equals to (tomadaPrecoId + 1)
        defaultRoteirizacaoShouldNotBeFound("tomadaPrecoId.equals=" + (tomadaPrecoId + 1));
    }

    @Test
    @Transactional
    void getAllRoteirizacaosByStatusColetaIsEqualToSomething() throws Exception {
        StatusColeta statusColeta;
        if (TestUtil.findAll(em, StatusColeta.class).isEmpty()) {
            roteirizacaoRepository.saveAndFlush(roteirizacao);
            statusColeta = StatusColetaResourceIT.createEntity(em);
        } else {
            statusColeta = TestUtil.findAll(em, StatusColeta.class).get(0);
        }
        em.persist(statusColeta);
        em.flush();
        roteirizacao.setStatusColeta(statusColeta);
        roteirizacaoRepository.saveAndFlush(roteirizacao);
        Long statusColetaId = statusColeta.getId();
        // Get all the roteirizacaoList where statusColeta equals to statusColetaId
        defaultRoteirizacaoShouldBeFound("statusColetaId.equals=" + statusColetaId);

        // Get all the roteirizacaoList where statusColeta equals to (statusColetaId + 1)
        defaultRoteirizacaoShouldNotBeFound("statusColetaId.equals=" + (statusColetaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoteirizacaoShouldBeFound(String filter) throws Exception {
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roteirizacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraPrimeiraColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_COLETA))))
            .andExpect(jsonPath("$.[*].dataHoraUltimaColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ULTIMA_COLETA))))
            .andExpect(jsonPath("$.[*].dataHoraPrimeiraEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA))))
            .andExpect(jsonPath("$.[*].dataHoraUltimaEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ULTIMA_ENTREGA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoteirizacaoShouldNotBeFound(String filter) throws Exception {
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoteirizacao() throws Exception {
        // Get the roteirizacao
        restRoteirizacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoteirizacao() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        roteirizacaoSearchRepository.save(roteirizacao);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());

        // Update the roteirizacao
        Roteirizacao updatedRoteirizacao = roteirizacaoRepository.findById(roteirizacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoteirizacao are not directly saved in db
        em.detach(updatedRoteirizacao);
        updatedRoteirizacao
            .dataHoraPrimeiraColeta(UPDATED_DATA_HORA_PRIMEIRA_COLETA)
            .dataHoraUltimaColeta(UPDATED_DATA_HORA_ULTIMA_COLETA)
            .dataHoraPrimeiraEntrega(UPDATED_DATA_HORA_PRIMEIRA_ENTREGA)
            .dataHoraUltimaEntrega(UPDATED_DATA_HORA_ULTIMA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(updatedRoteirizacao);

        restRoteirizacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roteirizacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        Roteirizacao testRoteirizacao = roteirizacaoList.get(roteirizacaoList.size() - 1);
        assertThat(testRoteirizacao.getDataHoraPrimeiraColeta()).isEqualTo(UPDATED_DATA_HORA_PRIMEIRA_COLETA);
        assertThat(testRoteirizacao.getDataHoraUltimaColeta()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_COLETA);
        assertThat(testRoteirizacao.getDataHoraPrimeiraEntrega()).isEqualTo(UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
        assertThat(testRoteirizacao.getDataHoraUltimaEntrega()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_ENTREGA);
        assertThat(testRoteirizacao.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testRoteirizacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Roteirizacao> roteirizacaoSearchList = IterableUtils.toList(roteirizacaoSearchRepository.findAll());
                Roteirizacao testRoteirizacaoSearch = roteirizacaoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRoteirizacaoSearch.getDataHoraPrimeiraColeta()).isEqualTo(UPDATED_DATA_HORA_PRIMEIRA_COLETA);
                assertThat(testRoteirizacaoSearch.getDataHoraUltimaColeta()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_COLETA);
                assertThat(testRoteirizacaoSearch.getDataHoraPrimeiraEntrega()).isEqualTo(UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
                assertThat(testRoteirizacaoSearch.getDataHoraUltimaEntrega()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_ENTREGA);
                assertThat(testRoteirizacaoSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testRoteirizacaoSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testRoteirizacaoSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testRoteirizacaoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
            });
    }

    @Test
    @Transactional
    void putNonExistingRoteirizacao() throws Exception {
        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        roteirizacao.setId(longCount.incrementAndGet());

        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoteirizacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roteirizacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoteirizacao() throws Exception {
        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        roteirizacao.setId(longCount.incrementAndGet());

        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteirizacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoteirizacao() throws Exception {
        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        roteirizacao.setId(longCount.incrementAndGet());

        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteirizacaoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateRoteirizacaoWithPatch() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();

        // Update the roteirizacao using partial update
        Roteirizacao partialUpdatedRoteirizacao = new Roteirizacao();
        partialUpdatedRoteirizacao.setId(roteirizacao.getId());

        partialUpdatedRoteirizacao
            .dataHoraUltimaColeta(UPDATED_DATA_HORA_ULTIMA_COLETA)
            .dataHoraUltimaEntrega(UPDATED_DATA_HORA_ULTIMA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .removido(UPDATED_REMOVIDO);

        restRoteirizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoteirizacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoteirizacao))
            )
            .andExpect(status().isOk());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        Roteirizacao testRoteirizacao = roteirizacaoList.get(roteirizacaoList.size() - 1);
        assertThat(testRoteirizacao.getDataHoraPrimeiraColeta()).isEqualTo(DEFAULT_DATA_HORA_PRIMEIRA_COLETA);
        assertThat(testRoteirizacao.getDataHoraUltimaColeta()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_COLETA);
        assertThat(testRoteirizacao.getDataHoraPrimeiraEntrega()).isEqualTo(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA);
        assertThat(testRoteirizacao.getDataHoraUltimaEntrega()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_ENTREGA);
        assertThat(testRoteirizacao.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testRoteirizacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void fullUpdateRoteirizacaoWithPatch() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);

        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();

        // Update the roteirizacao using partial update
        Roteirizacao partialUpdatedRoteirizacao = new Roteirizacao();
        partialUpdatedRoteirizacao.setId(roteirizacao.getId());

        partialUpdatedRoteirizacao
            .dataHoraPrimeiraColeta(UPDATED_DATA_HORA_PRIMEIRA_COLETA)
            .dataHoraUltimaColeta(UPDATED_DATA_HORA_ULTIMA_COLETA)
            .dataHoraPrimeiraEntrega(UPDATED_DATA_HORA_PRIMEIRA_ENTREGA)
            .dataHoraUltimaEntrega(UPDATED_DATA_HORA_ULTIMA_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);

        restRoteirizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoteirizacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoteirizacao))
            )
            .andExpect(status().isOk());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        Roteirizacao testRoteirizacao = roteirizacaoList.get(roteirizacaoList.size() - 1);
        assertThat(testRoteirizacao.getDataHoraPrimeiraColeta()).isEqualTo(UPDATED_DATA_HORA_PRIMEIRA_COLETA);
        assertThat(testRoteirizacao.getDataHoraUltimaColeta()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_COLETA);
        assertThat(testRoteirizacao.getDataHoraPrimeiraEntrega()).isEqualTo(UPDATED_DATA_HORA_PRIMEIRA_ENTREGA);
        assertThat(testRoteirizacao.getDataHoraUltimaEntrega()).isEqualTo(UPDATED_DATA_HORA_ULTIMA_ENTREGA);
        assertThat(testRoteirizacao.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testRoteirizacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void patchNonExistingRoteirizacao() throws Exception {
        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        roteirizacao.setId(longCount.incrementAndGet());

        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoteirizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roteirizacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoteirizacao() throws Exception {
        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        roteirizacao.setId(longCount.incrementAndGet());

        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteirizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoteirizacao() throws Exception {
        int databaseSizeBeforeUpdate = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        roteirizacao.setId(longCount.incrementAndGet());

        // Create the Roteirizacao
        RoteirizacaoDTO roteirizacaoDTO = roteirizacaoMapper.toDto(roteirizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteirizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roteirizacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roteirizacao in the database
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteRoteirizacao() throws Exception {
        // Initialize the database
        roteirizacaoRepository.saveAndFlush(roteirizacao);
        roteirizacaoRepository.save(roteirizacao);
        roteirizacaoSearchRepository.save(roteirizacao);

        int databaseSizeBeforeDelete = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the roteirizacao
        restRoteirizacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, roteirizacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roteirizacao> roteirizacaoList = roteirizacaoRepository.findAll();
        assertThat(roteirizacaoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchRoteirizacao() throws Exception {
        // Initialize the database
        roteirizacao = roteirizacaoRepository.saveAndFlush(roteirizacao);
        roteirizacaoSearchRepository.save(roteirizacao);

        // Search the roteirizacao
        restRoteirizacaoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + roteirizacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roteirizacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraPrimeiraColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_COLETA))))
            .andExpect(jsonPath("$.[*].dataHoraUltimaColeta").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ULTIMA_COLETA))))
            .andExpect(jsonPath("$.[*].dataHoraPrimeiraEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA))))
            .andExpect(jsonPath("$.[*].dataHoraUltimaEntrega").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ULTIMA_ENTREGA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
