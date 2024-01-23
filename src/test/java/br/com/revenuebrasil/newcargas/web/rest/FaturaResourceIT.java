package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.domain.enumeration.TipoFatura;
import br.com.revenuebrasil.newcargas.repository.FaturaRepository;
import br.com.revenuebrasil.newcargas.repository.search.FaturaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.FaturaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.FaturaMapper;
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
 * Integration tests for the {@link FaturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FaturaResourceIT {

    private static final TipoFatura DEFAULT_TIPO = TipoFatura.EMBARCADOR;
    private static final TipoFatura UPDATED_TIPO = TipoFatura.TRANSPORTADORA;

    private static final ZonedDateTime DEFAULT_DATA_FATURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_FATURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_FATURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_VENCIMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_VENCIMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_VENCIMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_PAGAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_PAGAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_PAGAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_NUMERO_PARCELA = 1;
    private static final Integer UPDATED_NUMERO_PARCELA = 2;
    private static final Integer SMALLER_NUMERO_PARCELA = 1 - 1;

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELADO = false;
    private static final Boolean UPDATED_CANCELADO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final String ENTITY_API_URL = "/api/faturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/faturas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private FaturaMapper faturaMapper;

    @Autowired
    private FaturaSearchRepository faturaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFaturaMockMvc;

    private Fatura fatura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fatura createEntity(EntityManager em) {
        Fatura fatura = new Fatura()
            .tipo(DEFAULT_TIPO)
            .dataFatura(DEFAULT_DATA_FATURA)
            .dataVencimento(DEFAULT_DATA_VENCIMENTO)
            .dataPagamento(DEFAULT_DATA_PAGAMENTO)
            .numeroParcela(DEFAULT_NUMERO_PARCELA)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .observacao(DEFAULT_OBSERVACAO)
            .cancelado(DEFAULT_CANCELADO)
            .removido(DEFAULT_REMOVIDO);
        return fatura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fatura createUpdatedEntity(EntityManager em) {
        Fatura fatura = new Fatura()
            .tipo(UPDATED_TIPO)
            .dataFatura(UPDATED_DATA_FATURA)
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .numeroParcela(UPDATED_NUMERO_PARCELA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        return fatura;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        faturaSearchRepository.deleteAll();
        assertThat(faturaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        fatura = createEntity(em);
    }

    @Test
    @Transactional
    void createFatura() throws Exception {
        int databaseSizeBeforeCreate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);
        restFaturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isCreated());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Fatura testFatura = faturaList.get(faturaList.size() - 1);
        assertThat(testFatura.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testFatura.getDataFatura()).isEqualTo(DEFAULT_DATA_FATURA);
        assertThat(testFatura.getDataVencimento()).isEqualTo(DEFAULT_DATA_VENCIMENTO);
        assertThat(testFatura.getDataPagamento()).isEqualTo(DEFAULT_DATA_PAGAMENTO);
        assertThat(testFatura.getNumeroParcela()).isEqualTo(DEFAULT_NUMERO_PARCELA);
        assertThat(testFatura.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testFatura.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testFatura.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void createFaturaWithExistingId() throws Exception {
        // Create the Fatura with an existing ID
        fatura.setId(1L);
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        int databaseSizeBeforeCreate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFaturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        // set the field null
        fatura.setTipo(null);

        // Create the Fatura, which fails.
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        restFaturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isBadRequest());

        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataFaturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        // set the field null
        fatura.setDataFatura(null);

        // Create the Fatura, which fails.
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        restFaturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isBadRequest());

        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataVencimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        // set the field null
        fatura.setDataVencimento(null);

        // Create the Fatura, which fails.
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        restFaturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isBadRequest());

        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkValorTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        // set the field null
        fatura.setValorTotal(null);

        // Create the Fatura, which fails.
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        restFaturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isBadRequest());

        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFaturas() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList
        restFaturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fatura.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].dataFatura").value(hasItem(sameInstant(DEFAULT_DATA_FATURA))))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(sameInstant(DEFAULT_DATA_VENCIMENTO))))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(sameInstant(DEFAULT_DATA_PAGAMENTO))))
            .andExpect(jsonPath("$.[*].numeroParcela").value(hasItem(DEFAULT_NUMERO_PARCELA)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }

    @Test
    @Transactional
    void getFatura() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get the fatura
        restFaturaMockMvc
            .perform(get(ENTITY_API_URL_ID, fatura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fatura.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.dataFatura").value(sameInstant(DEFAULT_DATA_FATURA)))
            .andExpect(jsonPath("$.dataVencimento").value(sameInstant(DEFAULT_DATA_VENCIMENTO)))
            .andExpect(jsonPath("$.dataPagamento").value(sameInstant(DEFAULT_DATA_PAGAMENTO)))
            .andExpect(jsonPath("$.numeroParcela").value(DEFAULT_NUMERO_PARCELA))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getFaturasByIdFiltering() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        Long id = fatura.getId();

        defaultFaturaShouldBeFound("id.equals=" + id);
        defaultFaturaShouldNotBeFound("id.notEquals=" + id);

        defaultFaturaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFaturaShouldNotBeFound("id.greaterThan=" + id);

        defaultFaturaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFaturaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFaturasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where tipo equals to DEFAULT_TIPO
        defaultFaturaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the faturaList where tipo equals to UPDATED_TIPO
        defaultFaturaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFaturasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultFaturaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the faturaList where tipo equals to UPDATED_TIPO
        defaultFaturaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFaturasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where tipo is not null
        defaultFaturaShouldBeFound("tipo.specified=true");

        // Get all the faturaList where tipo is null
        defaultFaturaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura equals to DEFAULT_DATA_FATURA
        defaultFaturaShouldBeFound("dataFatura.equals=" + DEFAULT_DATA_FATURA);

        // Get all the faturaList where dataFatura equals to UPDATED_DATA_FATURA
        defaultFaturaShouldNotBeFound("dataFatura.equals=" + UPDATED_DATA_FATURA);
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura in DEFAULT_DATA_FATURA or UPDATED_DATA_FATURA
        defaultFaturaShouldBeFound("dataFatura.in=" + DEFAULT_DATA_FATURA + "," + UPDATED_DATA_FATURA);

        // Get all the faturaList where dataFatura equals to UPDATED_DATA_FATURA
        defaultFaturaShouldNotBeFound("dataFatura.in=" + UPDATED_DATA_FATURA);
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura is not null
        defaultFaturaShouldBeFound("dataFatura.specified=true");

        // Get all the faturaList where dataFatura is null
        defaultFaturaShouldNotBeFound("dataFatura.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura is greater than or equal to DEFAULT_DATA_FATURA
        defaultFaturaShouldBeFound("dataFatura.greaterThanOrEqual=" + DEFAULT_DATA_FATURA);

        // Get all the faturaList where dataFatura is greater than or equal to UPDATED_DATA_FATURA
        defaultFaturaShouldNotBeFound("dataFatura.greaterThanOrEqual=" + UPDATED_DATA_FATURA);
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura is less than or equal to DEFAULT_DATA_FATURA
        defaultFaturaShouldBeFound("dataFatura.lessThanOrEqual=" + DEFAULT_DATA_FATURA);

        // Get all the faturaList where dataFatura is less than or equal to SMALLER_DATA_FATURA
        defaultFaturaShouldNotBeFound("dataFatura.lessThanOrEqual=" + SMALLER_DATA_FATURA);
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura is less than DEFAULT_DATA_FATURA
        defaultFaturaShouldNotBeFound("dataFatura.lessThan=" + DEFAULT_DATA_FATURA);

        // Get all the faturaList where dataFatura is less than UPDATED_DATA_FATURA
        defaultFaturaShouldBeFound("dataFatura.lessThan=" + UPDATED_DATA_FATURA);
    }

    @Test
    @Transactional
    void getAllFaturasByDataFaturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataFatura is greater than DEFAULT_DATA_FATURA
        defaultFaturaShouldNotBeFound("dataFatura.greaterThan=" + DEFAULT_DATA_FATURA);

        // Get all the faturaList where dataFatura is greater than SMALLER_DATA_FATURA
        defaultFaturaShouldBeFound("dataFatura.greaterThan=" + SMALLER_DATA_FATURA);
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento equals to DEFAULT_DATA_VENCIMENTO
        defaultFaturaShouldBeFound("dataVencimento.equals=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the faturaList where dataVencimento equals to UPDATED_DATA_VENCIMENTO
        defaultFaturaShouldNotBeFound("dataVencimento.equals=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento in DEFAULT_DATA_VENCIMENTO or UPDATED_DATA_VENCIMENTO
        defaultFaturaShouldBeFound("dataVencimento.in=" + DEFAULT_DATA_VENCIMENTO + "," + UPDATED_DATA_VENCIMENTO);

        // Get all the faturaList where dataVencimento equals to UPDATED_DATA_VENCIMENTO
        defaultFaturaShouldNotBeFound("dataVencimento.in=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento is not null
        defaultFaturaShouldBeFound("dataVencimento.specified=true");

        // Get all the faturaList where dataVencimento is null
        defaultFaturaShouldNotBeFound("dataVencimento.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento is greater than or equal to DEFAULT_DATA_VENCIMENTO
        defaultFaturaShouldBeFound("dataVencimento.greaterThanOrEqual=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the faturaList where dataVencimento is greater than or equal to UPDATED_DATA_VENCIMENTO
        defaultFaturaShouldNotBeFound("dataVencimento.greaterThanOrEqual=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento is less than or equal to DEFAULT_DATA_VENCIMENTO
        defaultFaturaShouldBeFound("dataVencimento.lessThanOrEqual=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the faturaList where dataVencimento is less than or equal to SMALLER_DATA_VENCIMENTO
        defaultFaturaShouldNotBeFound("dataVencimento.lessThanOrEqual=" + SMALLER_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento is less than DEFAULT_DATA_VENCIMENTO
        defaultFaturaShouldNotBeFound("dataVencimento.lessThan=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the faturaList where dataVencimento is less than UPDATED_DATA_VENCIMENTO
        defaultFaturaShouldBeFound("dataVencimento.lessThan=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataVencimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataVencimento is greater than DEFAULT_DATA_VENCIMENTO
        defaultFaturaShouldNotBeFound("dataVencimento.greaterThan=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the faturaList where dataVencimento is greater than SMALLER_DATA_VENCIMENTO
        defaultFaturaShouldBeFound("dataVencimento.greaterThan=" + SMALLER_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento equals to DEFAULT_DATA_PAGAMENTO
        defaultFaturaShouldBeFound("dataPagamento.equals=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the faturaList where dataPagamento equals to UPDATED_DATA_PAGAMENTO
        defaultFaturaShouldNotBeFound("dataPagamento.equals=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento in DEFAULT_DATA_PAGAMENTO or UPDATED_DATA_PAGAMENTO
        defaultFaturaShouldBeFound("dataPagamento.in=" + DEFAULT_DATA_PAGAMENTO + "," + UPDATED_DATA_PAGAMENTO);

        // Get all the faturaList where dataPagamento equals to UPDATED_DATA_PAGAMENTO
        defaultFaturaShouldNotBeFound("dataPagamento.in=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento is not null
        defaultFaturaShouldBeFound("dataPagamento.specified=true");

        // Get all the faturaList where dataPagamento is null
        defaultFaturaShouldNotBeFound("dataPagamento.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento is greater than or equal to DEFAULT_DATA_PAGAMENTO
        defaultFaturaShouldBeFound("dataPagamento.greaterThanOrEqual=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the faturaList where dataPagamento is greater than or equal to UPDATED_DATA_PAGAMENTO
        defaultFaturaShouldNotBeFound("dataPagamento.greaterThanOrEqual=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento is less than or equal to DEFAULT_DATA_PAGAMENTO
        defaultFaturaShouldBeFound("dataPagamento.lessThanOrEqual=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the faturaList where dataPagamento is less than or equal to SMALLER_DATA_PAGAMENTO
        defaultFaturaShouldNotBeFound("dataPagamento.lessThanOrEqual=" + SMALLER_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento is less than DEFAULT_DATA_PAGAMENTO
        defaultFaturaShouldNotBeFound("dataPagamento.lessThan=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the faturaList where dataPagamento is less than UPDATED_DATA_PAGAMENTO
        defaultFaturaShouldBeFound("dataPagamento.lessThan=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByDataPagamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where dataPagamento is greater than DEFAULT_DATA_PAGAMENTO
        defaultFaturaShouldNotBeFound("dataPagamento.greaterThan=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the faturaList where dataPagamento is greater than SMALLER_DATA_PAGAMENTO
        defaultFaturaShouldBeFound("dataPagamento.greaterThan=" + SMALLER_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela equals to DEFAULT_NUMERO_PARCELA
        defaultFaturaShouldBeFound("numeroParcela.equals=" + DEFAULT_NUMERO_PARCELA);

        // Get all the faturaList where numeroParcela equals to UPDATED_NUMERO_PARCELA
        defaultFaturaShouldNotBeFound("numeroParcela.equals=" + UPDATED_NUMERO_PARCELA);
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela in DEFAULT_NUMERO_PARCELA or UPDATED_NUMERO_PARCELA
        defaultFaturaShouldBeFound("numeroParcela.in=" + DEFAULT_NUMERO_PARCELA + "," + UPDATED_NUMERO_PARCELA);

        // Get all the faturaList where numeroParcela equals to UPDATED_NUMERO_PARCELA
        defaultFaturaShouldNotBeFound("numeroParcela.in=" + UPDATED_NUMERO_PARCELA);
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela is not null
        defaultFaturaShouldBeFound("numeroParcela.specified=true");

        // Get all the faturaList where numeroParcela is null
        defaultFaturaShouldNotBeFound("numeroParcela.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela is greater than or equal to DEFAULT_NUMERO_PARCELA
        defaultFaturaShouldBeFound("numeroParcela.greaterThanOrEqual=" + DEFAULT_NUMERO_PARCELA);

        // Get all the faturaList where numeroParcela is greater than or equal to (DEFAULT_NUMERO_PARCELA + 1)
        defaultFaturaShouldNotBeFound("numeroParcela.greaterThanOrEqual=" + (DEFAULT_NUMERO_PARCELA + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela is less than or equal to DEFAULT_NUMERO_PARCELA
        defaultFaturaShouldBeFound("numeroParcela.lessThanOrEqual=" + DEFAULT_NUMERO_PARCELA);

        // Get all the faturaList where numeroParcela is less than or equal to SMALLER_NUMERO_PARCELA
        defaultFaturaShouldNotBeFound("numeroParcela.lessThanOrEqual=" + SMALLER_NUMERO_PARCELA);
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela is less than DEFAULT_NUMERO_PARCELA
        defaultFaturaShouldNotBeFound("numeroParcela.lessThan=" + DEFAULT_NUMERO_PARCELA);

        // Get all the faturaList where numeroParcela is less than (DEFAULT_NUMERO_PARCELA + 1)
        defaultFaturaShouldBeFound("numeroParcela.lessThan=" + (DEFAULT_NUMERO_PARCELA + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByNumeroParcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where numeroParcela is greater than DEFAULT_NUMERO_PARCELA
        defaultFaturaShouldNotBeFound("numeroParcela.greaterThan=" + DEFAULT_NUMERO_PARCELA);

        // Get all the faturaList where numeroParcela is greater than SMALLER_NUMERO_PARCELA
        defaultFaturaShouldBeFound("numeroParcela.greaterThan=" + SMALLER_NUMERO_PARCELA);
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultFaturaShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the faturaList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultFaturaShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultFaturaShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the faturaList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultFaturaShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal is not null
        defaultFaturaShouldBeFound("valorTotal.specified=true");

        // Get all the faturaList where valorTotal is null
        defaultFaturaShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultFaturaShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the faturaList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultFaturaShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultFaturaShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the faturaList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultFaturaShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultFaturaShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the faturaList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultFaturaShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultFaturaShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the faturaList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultFaturaShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllFaturasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where observacao equals to DEFAULT_OBSERVACAO
        defaultFaturaShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the faturaList where observacao equals to UPDATED_OBSERVACAO
        defaultFaturaShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllFaturasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultFaturaShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the faturaList where observacao equals to UPDATED_OBSERVACAO
        defaultFaturaShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllFaturasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where observacao is not null
        defaultFaturaShouldBeFound("observacao.specified=true");

        // Get all the faturaList where observacao is null
        defaultFaturaShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where observacao contains DEFAULT_OBSERVACAO
        defaultFaturaShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the faturaList where observacao contains UPDATED_OBSERVACAO
        defaultFaturaShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllFaturasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where observacao does not contain DEFAULT_OBSERVACAO
        defaultFaturaShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the faturaList where observacao does not contain UPDATED_OBSERVACAO
        defaultFaturaShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllFaturasByCanceladoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where cancelado equals to DEFAULT_CANCELADO
        defaultFaturaShouldBeFound("cancelado.equals=" + DEFAULT_CANCELADO);

        // Get all the faturaList where cancelado equals to UPDATED_CANCELADO
        defaultFaturaShouldNotBeFound("cancelado.equals=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllFaturasByCanceladoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where cancelado in DEFAULT_CANCELADO or UPDATED_CANCELADO
        defaultFaturaShouldBeFound("cancelado.in=" + DEFAULT_CANCELADO + "," + UPDATED_CANCELADO);

        // Get all the faturaList where cancelado equals to UPDATED_CANCELADO
        defaultFaturaShouldNotBeFound("cancelado.in=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllFaturasByCanceladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where cancelado is not null
        defaultFaturaShouldBeFound("cancelado.specified=true");

        // Get all the faturaList where cancelado is null
        defaultFaturaShouldNotBeFound("cancelado.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where removido equals to DEFAULT_REMOVIDO
        defaultFaturaShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the faturaList where removido equals to UPDATED_REMOVIDO
        defaultFaturaShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllFaturasByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultFaturaShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the faturaList where removido equals to UPDATED_REMOVIDO
        defaultFaturaShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllFaturasByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        // Get all the faturaList where removido is not null
        defaultFaturaShouldBeFound("removido.specified=true");

        // Get all the faturaList where removido is null
        defaultFaturaShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturasByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            faturaRepository.saveAndFlush(fatura);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        fatura.setEmbarcador(embarcador);
        faturaRepository.saveAndFlush(fatura);
        Long embarcadorId = embarcador.getId();
        // Get all the faturaList where embarcador equals to embarcadorId
        defaultFaturaShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the faturaList where embarcador equals to (embarcadorId + 1)
        defaultFaturaShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            faturaRepository.saveAndFlush(fatura);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        fatura.setTransportadora(transportadora);
        faturaRepository.saveAndFlush(fatura);
        Long transportadoraId = transportadora.getId();
        // Get all the faturaList where transportadora equals to transportadoraId
        defaultFaturaShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the faturaList where transportadora equals to (transportadoraId + 1)
        defaultFaturaShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByContratacaoIsEqualToSomething() throws Exception {
        Contratacao contratacao;
        if (TestUtil.findAll(em, Contratacao.class).isEmpty()) {
            faturaRepository.saveAndFlush(fatura);
            contratacao = ContratacaoResourceIT.createEntity(em);
        } else {
            contratacao = TestUtil.findAll(em, Contratacao.class).get(0);
        }
        em.persist(contratacao);
        em.flush();
        fatura.setContratacao(contratacao);
        faturaRepository.saveAndFlush(fatura);
        Long contratacaoId = contratacao.getId();
        // Get all the faturaList where contratacao equals to contratacaoId
        defaultFaturaShouldBeFound("contratacaoId.equals=" + contratacaoId);

        // Get all the faturaList where contratacao equals to (contratacaoId + 1)
        defaultFaturaShouldNotBeFound("contratacaoId.equals=" + (contratacaoId + 1));
    }

    @Test
    @Transactional
    void getAllFaturasByFormaCobrancaIsEqualToSomething() throws Exception {
        FormaCobranca formaCobranca;
        if (TestUtil.findAll(em, FormaCobranca.class).isEmpty()) {
            faturaRepository.saveAndFlush(fatura);
            formaCobranca = FormaCobrancaResourceIT.createEntity(em);
        } else {
            formaCobranca = TestUtil.findAll(em, FormaCobranca.class).get(0);
        }
        em.persist(formaCobranca);
        em.flush();
        fatura.setFormaCobranca(formaCobranca);
        faturaRepository.saveAndFlush(fatura);
        Long formaCobrancaId = formaCobranca.getId();
        // Get all the faturaList where formaCobranca equals to formaCobrancaId
        defaultFaturaShouldBeFound("formaCobrancaId.equals=" + formaCobrancaId);

        // Get all the faturaList where formaCobranca equals to (formaCobrancaId + 1)
        defaultFaturaShouldNotBeFound("formaCobrancaId.equals=" + (formaCobrancaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFaturaShouldBeFound(String filter) throws Exception {
        restFaturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fatura.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].dataFatura").value(hasItem(sameInstant(DEFAULT_DATA_FATURA))))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(sameInstant(DEFAULT_DATA_VENCIMENTO))))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(sameInstant(DEFAULT_DATA_PAGAMENTO))))
            .andExpect(jsonPath("$.[*].numeroParcela").value(hasItem(DEFAULT_NUMERO_PARCELA)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restFaturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFaturaShouldNotBeFound(String filter) throws Exception {
        restFaturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFaturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFatura() throws Exception {
        // Get the fatura
        restFaturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFatura() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        faturaSearchRepository.save(fatura);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());

        // Update the fatura
        Fatura updatedFatura = faturaRepository.findById(fatura.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFatura are not directly saved in db
        em.detach(updatedFatura);
        updatedFatura
            .tipo(UPDATED_TIPO)
            .dataFatura(UPDATED_DATA_FATURA)
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .numeroParcela(UPDATED_NUMERO_PARCELA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        FaturaDTO faturaDTO = faturaMapper.toDto(updatedFatura);

        restFaturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, faturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(faturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        Fatura testFatura = faturaList.get(faturaList.size() - 1);
        assertThat(testFatura.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFatura.getDataFatura()).isEqualTo(UPDATED_DATA_FATURA);
        assertThat(testFatura.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testFatura.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
        assertThat(testFatura.getNumeroParcela()).isEqualTo(UPDATED_NUMERO_PARCELA);
        assertThat(testFatura.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testFatura.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testFatura.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Fatura> faturaSearchList = IterableUtils.toList(faturaSearchRepository.findAll());
                Fatura testFaturaSearch = faturaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFaturaSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testFaturaSearch.getDataFatura()).isEqualTo(UPDATED_DATA_FATURA);
                assertThat(testFaturaSearch.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
                assertThat(testFaturaSearch.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
                assertThat(testFaturaSearch.getNumeroParcela()).isEqualTo(UPDATED_NUMERO_PARCELA);
                assertThat(testFaturaSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testFaturaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testFaturaSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testFaturaSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
            });
    }

    @Test
    @Transactional
    void putNonExistingFatura() throws Exception {
        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        fatura.setId(longCount.incrementAndGet());

        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFaturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, faturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(faturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFatura() throws Exception {
        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        fatura.setId(longCount.incrementAndGet());

        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(faturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFatura() throws Exception {
        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        fatura.setId(longCount.incrementAndGet());

        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFaturaWithPatch() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();

        // Update the fatura using partial update
        Fatura partialUpdatedFatura = new Fatura();
        partialUpdatedFatura.setId(fatura.getId());

        partialUpdatedFatura
            .tipo(UPDATED_TIPO)
            .dataFatura(UPDATED_DATA_FATURA)
            .numeroParcela(UPDATED_NUMERO_PARCELA)
            .observacao(UPDATED_OBSERVACAO);

        restFaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFatura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFatura))
            )
            .andExpect(status().isOk());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        Fatura testFatura = faturaList.get(faturaList.size() - 1);
        assertThat(testFatura.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFatura.getDataFatura()).isEqualTo(UPDATED_DATA_FATURA);
        assertThat(testFatura.getDataVencimento()).isEqualTo(DEFAULT_DATA_VENCIMENTO);
        assertThat(testFatura.getDataPagamento()).isEqualTo(DEFAULT_DATA_PAGAMENTO);
        assertThat(testFatura.getNumeroParcela()).isEqualTo(UPDATED_NUMERO_PARCELA);
        assertThat(testFatura.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testFatura.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testFatura.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void fullUpdateFaturaWithPatch() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);

        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();

        // Update the fatura using partial update
        Fatura partialUpdatedFatura = new Fatura();
        partialUpdatedFatura.setId(fatura.getId());

        partialUpdatedFatura
            .tipo(UPDATED_TIPO)
            .dataFatura(UPDATED_DATA_FATURA)
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .numeroParcela(UPDATED_NUMERO_PARCELA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);

        restFaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFatura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFatura))
            )
            .andExpect(status().isOk());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        Fatura testFatura = faturaList.get(faturaList.size() - 1);
        assertThat(testFatura.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFatura.getDataFatura()).isEqualTo(UPDATED_DATA_FATURA);
        assertThat(testFatura.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testFatura.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
        assertThat(testFatura.getNumeroParcela()).isEqualTo(UPDATED_NUMERO_PARCELA);
        assertThat(testFatura.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testFatura.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testFatura.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void patchNonExistingFatura() throws Exception {
        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        fatura.setId(longCount.incrementAndGet());

        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, faturaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(faturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFatura() throws Exception {
        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        fatura.setId(longCount.incrementAndGet());

        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(faturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFatura() throws Exception {
        int databaseSizeBeforeUpdate = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        fatura.setId(longCount.incrementAndGet());

        // Create the Fatura
        FaturaDTO faturaDTO = faturaMapper.toDto(fatura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(faturaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fatura in the database
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFatura() throws Exception {
        // Initialize the database
        faturaRepository.saveAndFlush(fatura);
        faturaRepository.save(fatura);
        faturaSearchRepository.save(fatura);

        int databaseSizeBeforeDelete = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the fatura
        restFaturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, fatura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fatura> faturaList = faturaRepository.findAll();
        assertThat(faturaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFatura() throws Exception {
        // Initialize the database
        fatura = faturaRepository.saveAndFlush(fatura);
        faturaSearchRepository.save(fatura);

        // Search the fatura
        restFaturaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fatura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fatura.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].dataFatura").value(hasItem(sameInstant(DEFAULT_DATA_FATURA))))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(sameInstant(DEFAULT_DATA_VENCIMENTO))))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(sameInstant(DEFAULT_DATA_PAGAMENTO))))
            .andExpect(jsonPath("$.[*].numeroParcela").value(hasItem(DEFAULT_NUMERO_PARCELA)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
