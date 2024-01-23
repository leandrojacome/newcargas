package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.ContratacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContratacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContratacaoMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ContratacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratacaoResourceIT {

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final Integer DEFAULT_VALIDADE_EM_DIAS = 1;
    private static final Integer UPDATED_VALIDADE_EM_DIAS = 2;
    private static final Integer SMALLER_VALIDADE_EM_DIAS = 1 - 1;

    private static final LocalDate DEFAULT_DATA_VALIDADE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VALIDADE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_VALIDADE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELADO = false;
    private static final Boolean UPDATED_CANCELADO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final String ENTITY_API_URL = "/api/contratacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/contratacaos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContratacaoRepository contratacaoRepository;

    @Autowired
    private ContratacaoMapper contratacaoMapper;

    @Autowired
    private ContratacaoSearchRepository contratacaoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratacaoMockMvc;

    private Contratacao contratacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contratacao createEntity(EntityManager em) {
        Contratacao contratacao = new Contratacao()
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .validadeEmDias(DEFAULT_VALIDADE_EM_DIAS)
            .dataValidade(DEFAULT_DATA_VALIDADE)
            .observacao(DEFAULT_OBSERVACAO)
            .cancelado(DEFAULT_CANCELADO)
            .removido(DEFAULT_REMOVIDO);
        return contratacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contratacao createUpdatedEntity(EntityManager em) {
        Contratacao contratacao = new Contratacao()
            .valorTotal(UPDATED_VALOR_TOTAL)
            .validadeEmDias(UPDATED_VALIDADE_EM_DIAS)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        return contratacao;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        contratacaoSearchRepository.deleteAll();
        assertThat(contratacaoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        contratacao = createEntity(em);
    }

    @Test
    @Transactional
    void createContratacao() throws Exception {
        int databaseSizeBeforeCreate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);
        restContratacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Contratacao testContratacao = contratacaoList.get(contratacaoList.size() - 1);
        assertThat(testContratacao.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testContratacao.getValidadeEmDias()).isEqualTo(DEFAULT_VALIDADE_EM_DIAS);
        assertThat(testContratacao.getDataValidade()).isEqualTo(DEFAULT_DATA_VALIDADE);
        assertThat(testContratacao.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testContratacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void createContratacaoWithExistingId() throws Exception {
        // Create the Contratacao with an existing ID
        contratacao.setId(1L);
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        int databaseSizeBeforeCreate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkValorTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        // set the field null
        contratacao.setValorTotal(null);

        // Create the Contratacao, which fails.
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        restContratacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkValidadeEmDiasIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        // set the field null
        contratacao.setValidadeEmDias(null);

        // Create the Contratacao, which fails.
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        restContratacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataValidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        // set the field null
        contratacao.setDataValidade(null);

        // Create the Contratacao, which fails.
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        restContratacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllContratacaos() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList
        restContratacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contratacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].validadeEmDias").value(hasItem(DEFAULT_VALIDADE_EM_DIAS)))
            .andExpect(jsonPath("$.[*].dataValidade").value(hasItem(DEFAULT_DATA_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }

    @Test
    @Transactional
    void getContratacao() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get the contratacao
        restContratacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, contratacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contratacao.getId().intValue()))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.validadeEmDias").value(DEFAULT_VALIDADE_EM_DIAS))
            .andExpect(jsonPath("$.dataValidade").value(DEFAULT_DATA_VALIDADE.toString()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getContratacaosByIdFiltering() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        Long id = contratacao.getId();

        defaultContratacaoShouldBeFound("id.equals=" + id);
        defaultContratacaoShouldNotBeFound("id.notEquals=" + id);

        defaultContratacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContratacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultContratacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContratacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultContratacaoShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the contratacaoList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultContratacaoShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultContratacaoShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the contratacaoList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultContratacaoShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal is not null
        defaultContratacaoShouldBeFound("valorTotal.specified=true");

        // Get all the contratacaoList where valorTotal is null
        defaultContratacaoShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultContratacaoShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the contratacaoList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultContratacaoShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultContratacaoShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the contratacaoList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultContratacaoShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultContratacaoShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the contratacaoList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultContratacaoShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllContratacaosByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultContratacaoShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the contratacaoList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultContratacaoShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias equals to DEFAULT_VALIDADE_EM_DIAS
        defaultContratacaoShouldBeFound("validadeEmDias.equals=" + DEFAULT_VALIDADE_EM_DIAS);

        // Get all the contratacaoList where validadeEmDias equals to UPDATED_VALIDADE_EM_DIAS
        defaultContratacaoShouldNotBeFound("validadeEmDias.equals=" + UPDATED_VALIDADE_EM_DIAS);
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsInShouldWork() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias in DEFAULT_VALIDADE_EM_DIAS or UPDATED_VALIDADE_EM_DIAS
        defaultContratacaoShouldBeFound("validadeEmDias.in=" + DEFAULT_VALIDADE_EM_DIAS + "," + UPDATED_VALIDADE_EM_DIAS);

        // Get all the contratacaoList where validadeEmDias equals to UPDATED_VALIDADE_EM_DIAS
        defaultContratacaoShouldNotBeFound("validadeEmDias.in=" + UPDATED_VALIDADE_EM_DIAS);
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias is not null
        defaultContratacaoShouldBeFound("validadeEmDias.specified=true");

        // Get all the contratacaoList where validadeEmDias is null
        defaultContratacaoShouldNotBeFound("validadeEmDias.specified=false");
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias is greater than or equal to DEFAULT_VALIDADE_EM_DIAS
        defaultContratacaoShouldBeFound("validadeEmDias.greaterThanOrEqual=" + DEFAULT_VALIDADE_EM_DIAS);

        // Get all the contratacaoList where validadeEmDias is greater than or equal to (DEFAULT_VALIDADE_EM_DIAS + 1)
        defaultContratacaoShouldNotBeFound("validadeEmDias.greaterThanOrEqual=" + (DEFAULT_VALIDADE_EM_DIAS + 1));
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias is less than or equal to DEFAULT_VALIDADE_EM_DIAS
        defaultContratacaoShouldBeFound("validadeEmDias.lessThanOrEqual=" + DEFAULT_VALIDADE_EM_DIAS);

        // Get all the contratacaoList where validadeEmDias is less than or equal to SMALLER_VALIDADE_EM_DIAS
        defaultContratacaoShouldNotBeFound("validadeEmDias.lessThanOrEqual=" + SMALLER_VALIDADE_EM_DIAS);
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsLessThanSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias is less than DEFAULT_VALIDADE_EM_DIAS
        defaultContratacaoShouldNotBeFound("validadeEmDias.lessThan=" + DEFAULT_VALIDADE_EM_DIAS);

        // Get all the contratacaoList where validadeEmDias is less than (DEFAULT_VALIDADE_EM_DIAS + 1)
        defaultContratacaoShouldBeFound("validadeEmDias.lessThan=" + (DEFAULT_VALIDADE_EM_DIAS + 1));
    }

    @Test
    @Transactional
    void getAllContratacaosByValidadeEmDiasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where validadeEmDias is greater than DEFAULT_VALIDADE_EM_DIAS
        defaultContratacaoShouldNotBeFound("validadeEmDias.greaterThan=" + DEFAULT_VALIDADE_EM_DIAS);

        // Get all the contratacaoList where validadeEmDias is greater than SMALLER_VALIDADE_EM_DIAS
        defaultContratacaoShouldBeFound("validadeEmDias.greaterThan=" + SMALLER_VALIDADE_EM_DIAS);
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade equals to DEFAULT_DATA_VALIDADE
        defaultContratacaoShouldBeFound("dataValidade.equals=" + DEFAULT_DATA_VALIDADE);

        // Get all the contratacaoList where dataValidade equals to UPDATED_DATA_VALIDADE
        defaultContratacaoShouldNotBeFound("dataValidade.equals=" + UPDATED_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsInShouldWork() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade in DEFAULT_DATA_VALIDADE or UPDATED_DATA_VALIDADE
        defaultContratacaoShouldBeFound("dataValidade.in=" + DEFAULT_DATA_VALIDADE + "," + UPDATED_DATA_VALIDADE);

        // Get all the contratacaoList where dataValidade equals to UPDATED_DATA_VALIDADE
        defaultContratacaoShouldNotBeFound("dataValidade.in=" + UPDATED_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade is not null
        defaultContratacaoShouldBeFound("dataValidade.specified=true");

        // Get all the contratacaoList where dataValidade is null
        defaultContratacaoShouldNotBeFound("dataValidade.specified=false");
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade is greater than or equal to DEFAULT_DATA_VALIDADE
        defaultContratacaoShouldBeFound("dataValidade.greaterThanOrEqual=" + DEFAULT_DATA_VALIDADE);

        // Get all the contratacaoList where dataValidade is greater than or equal to UPDATED_DATA_VALIDADE
        defaultContratacaoShouldNotBeFound("dataValidade.greaterThanOrEqual=" + UPDATED_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade is less than or equal to DEFAULT_DATA_VALIDADE
        defaultContratacaoShouldBeFound("dataValidade.lessThanOrEqual=" + DEFAULT_DATA_VALIDADE);

        // Get all the contratacaoList where dataValidade is less than or equal to SMALLER_DATA_VALIDADE
        defaultContratacaoShouldNotBeFound("dataValidade.lessThanOrEqual=" + SMALLER_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsLessThanSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade is less than DEFAULT_DATA_VALIDADE
        defaultContratacaoShouldNotBeFound("dataValidade.lessThan=" + DEFAULT_DATA_VALIDADE);

        // Get all the contratacaoList where dataValidade is less than UPDATED_DATA_VALIDADE
        defaultContratacaoShouldBeFound("dataValidade.lessThan=" + UPDATED_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllContratacaosByDataValidadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where dataValidade is greater than DEFAULT_DATA_VALIDADE
        defaultContratacaoShouldNotBeFound("dataValidade.greaterThan=" + DEFAULT_DATA_VALIDADE);

        // Get all the contratacaoList where dataValidade is greater than SMALLER_DATA_VALIDADE
        defaultContratacaoShouldBeFound("dataValidade.greaterThan=" + SMALLER_DATA_VALIDADE);
    }

    @Test
    @Transactional
    void getAllContratacaosByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where observacao equals to DEFAULT_OBSERVACAO
        defaultContratacaoShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the contratacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultContratacaoShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContratacaosByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultContratacaoShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the contratacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultContratacaoShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContratacaosByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where observacao is not null
        defaultContratacaoShouldBeFound("observacao.specified=true");

        // Get all the contratacaoList where observacao is null
        defaultContratacaoShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllContratacaosByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where observacao contains DEFAULT_OBSERVACAO
        defaultContratacaoShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the contratacaoList where observacao contains UPDATED_OBSERVACAO
        defaultContratacaoShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContratacaosByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where observacao does not contain DEFAULT_OBSERVACAO
        defaultContratacaoShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the contratacaoList where observacao does not contain UPDATED_OBSERVACAO
        defaultContratacaoShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContratacaosByCanceladoIsEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where cancelado equals to DEFAULT_CANCELADO
        defaultContratacaoShouldBeFound("cancelado.equals=" + DEFAULT_CANCELADO);

        // Get all the contratacaoList where cancelado equals to UPDATED_CANCELADO
        defaultContratacaoShouldNotBeFound("cancelado.equals=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllContratacaosByCanceladoIsInShouldWork() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where cancelado in DEFAULT_CANCELADO or UPDATED_CANCELADO
        defaultContratacaoShouldBeFound("cancelado.in=" + DEFAULT_CANCELADO + "," + UPDATED_CANCELADO);

        // Get all the contratacaoList where cancelado equals to UPDATED_CANCELADO
        defaultContratacaoShouldNotBeFound("cancelado.in=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllContratacaosByCanceladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where cancelado is not null
        defaultContratacaoShouldBeFound("cancelado.specified=true");

        // Get all the contratacaoList where cancelado is null
        defaultContratacaoShouldNotBeFound("cancelado.specified=false");
    }

    @Test
    @Transactional
    void getAllContratacaosByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where removido equals to DEFAULT_REMOVIDO
        defaultContratacaoShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the contratacaoList where removido equals to UPDATED_REMOVIDO
        defaultContratacaoShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllContratacaosByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultContratacaoShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the contratacaoList where removido equals to UPDATED_REMOVIDO
        defaultContratacaoShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllContratacaosByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        // Get all the contratacaoList where removido is not null
        defaultContratacaoShouldBeFound("removido.specified=true");

        // Get all the contratacaoList where removido is null
        defaultContratacaoShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllContratacaosByFaturaIsEqualToSomething() throws Exception {
        Fatura fatura;
        if (TestUtil.findAll(em, Fatura.class).isEmpty()) {
            contratacaoRepository.saveAndFlush(contratacao);
            fatura = FaturaResourceIT.createEntity(em);
        } else {
            fatura = TestUtil.findAll(em, Fatura.class).get(0);
        }
        em.persist(fatura);
        em.flush();
        contratacao.addFatura(fatura);
        contratacaoRepository.saveAndFlush(contratacao);
        Long faturaId = fatura.getId();
        // Get all the contratacaoList where fatura equals to faturaId
        defaultContratacaoShouldBeFound("faturaId.equals=" + faturaId);

        // Get all the contratacaoList where fatura equals to (faturaId + 1)
        defaultContratacaoShouldNotBeFound("faturaId.equals=" + (faturaId + 1));
    }

    @Test
    @Transactional
    void getAllContratacaosBySolicitacaoColetaIsEqualToSomething() throws Exception {
        TomadaPreco solicitacaoColeta;
        if (TestUtil.findAll(em, TomadaPreco.class).isEmpty()) {
            contratacaoRepository.saveAndFlush(contratacao);
            solicitacaoColeta = TomadaPrecoResourceIT.createEntity(em);
        } else {
            solicitacaoColeta = TestUtil.findAll(em, TomadaPreco.class).get(0);
        }
        em.persist(solicitacaoColeta);
        em.flush();
        contratacao.setSolicitacaoColeta(solicitacaoColeta);
        solicitacaoColeta.setContratacao(contratacao);
        contratacaoRepository.saveAndFlush(contratacao);
        Long solicitacaoColetaId = solicitacaoColeta.getId();
        // Get all the contratacaoList where solicitacaoColeta equals to solicitacaoColetaId
        defaultContratacaoShouldBeFound("solicitacaoColetaId.equals=" + solicitacaoColetaId);

        // Get all the contratacaoList where solicitacaoColeta equals to (solicitacaoColetaId + 1)
        defaultContratacaoShouldNotBeFound("solicitacaoColetaId.equals=" + (solicitacaoColetaId + 1));
    }

    @Test
    @Transactional
    void getAllContratacaosByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            contratacaoRepository.saveAndFlush(contratacao);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        contratacao.setTransportadora(transportadora);
        contratacaoRepository.saveAndFlush(contratacao);
        Long transportadoraId = transportadora.getId();
        // Get all the contratacaoList where transportadora equals to transportadoraId
        defaultContratacaoShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the contratacaoList where transportadora equals to (transportadoraId + 1)
        defaultContratacaoShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContratacaoShouldBeFound(String filter) throws Exception {
        restContratacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contratacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].validadeEmDias").value(hasItem(DEFAULT_VALIDADE_EM_DIAS)))
            .andExpect(jsonPath("$.[*].dataValidade").value(hasItem(DEFAULT_DATA_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restContratacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContratacaoShouldNotBeFound(String filter) throws Exception {
        restContratacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContratacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContratacao() throws Exception {
        // Get the contratacao
        restContratacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContratacao() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        contratacaoSearchRepository.save(contratacao);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());

        // Update the contratacao
        Contratacao updatedContratacao = contratacaoRepository.findById(contratacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContratacao are not directly saved in db
        em.detach(updatedContratacao);
        updatedContratacao
            .valorTotal(UPDATED_VALOR_TOTAL)
            .validadeEmDias(UPDATED_VALIDADE_EM_DIAS)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(updatedContratacao);

        restContratacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        Contratacao testContratacao = contratacaoList.get(contratacaoList.size() - 1);
        assertThat(testContratacao.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testContratacao.getValidadeEmDias()).isEqualTo(UPDATED_VALIDADE_EM_DIAS);
        assertThat(testContratacao.getDataValidade()).isEqualTo(UPDATED_DATA_VALIDADE);
        assertThat(testContratacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testContratacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Contratacao> contratacaoSearchList = IterableUtils.toList(contratacaoSearchRepository.findAll());
                Contratacao testContratacaoSearch = contratacaoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testContratacaoSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testContratacaoSearch.getValidadeEmDias()).isEqualTo(UPDATED_VALIDADE_EM_DIAS);
                assertThat(testContratacaoSearch.getDataValidade()).isEqualTo(UPDATED_DATA_VALIDADE);
                assertThat(testContratacaoSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testContratacaoSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testContratacaoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
            });
    }

    @Test
    @Transactional
    void putNonExistingContratacao() throws Exception {
        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        contratacao.setId(longCount.incrementAndGet());

        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchContratacao() throws Exception {
        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        contratacao.setId(longCount.incrementAndGet());

        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContratacao() throws Exception {
        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        contratacao.setId(longCount.incrementAndGet());

        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateContratacaoWithPatch() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();

        // Update the contratacao using partial update
        Contratacao partialUpdatedContratacao = new Contratacao();
        partialUpdatedContratacao.setId(contratacao.getId());

        partialUpdatedContratacao.valorTotal(UPDATED_VALOR_TOTAL).dataValidade(UPDATED_DATA_VALIDADE);

        restContratacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContratacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContratacao))
            )
            .andExpect(status().isOk());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        Contratacao testContratacao = contratacaoList.get(contratacaoList.size() - 1);
        assertThat(testContratacao.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testContratacao.getValidadeEmDias()).isEqualTo(DEFAULT_VALIDADE_EM_DIAS);
        assertThat(testContratacao.getDataValidade()).isEqualTo(UPDATED_DATA_VALIDADE);
        assertThat(testContratacao.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testContratacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void fullUpdateContratacaoWithPatch() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);

        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();

        // Update the contratacao using partial update
        Contratacao partialUpdatedContratacao = new Contratacao();
        partialUpdatedContratacao.setId(contratacao.getId());

        partialUpdatedContratacao
            .valorTotal(UPDATED_VALOR_TOTAL)
            .validadeEmDias(UPDATED_VALIDADE_EM_DIAS)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .observacao(UPDATED_OBSERVACAO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);

        restContratacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContratacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContratacao))
            )
            .andExpect(status().isOk());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        Contratacao testContratacao = contratacaoList.get(contratacaoList.size() - 1);
        assertThat(testContratacao.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testContratacao.getValidadeEmDias()).isEqualTo(UPDATED_VALIDADE_EM_DIAS);
        assertThat(testContratacao.getDataValidade()).isEqualTo(UPDATED_DATA_VALIDADE);
        assertThat(testContratacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testContratacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void patchNonExistingContratacao() throws Exception {
        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        contratacao.setId(longCount.incrementAndGet());

        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contratacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContratacao() throws Exception {
        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        contratacao.setId(longCount.incrementAndGet());

        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContratacao() throws Exception {
        int databaseSizeBeforeUpdate = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        contratacao.setId(longCount.incrementAndGet());

        // Create the Contratacao
        ContratacaoDTO contratacaoDTO = contratacaoMapper.toDto(contratacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contratacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contratacao in the database
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteContratacao() throws Exception {
        // Initialize the database
        contratacaoRepository.saveAndFlush(contratacao);
        contratacaoRepository.save(contratacao);
        contratacaoSearchRepository.save(contratacao);

        int databaseSizeBeforeDelete = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the contratacao
        restContratacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contratacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contratacao> contratacaoList = contratacaoRepository.findAll();
        assertThat(contratacaoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchContratacao() throws Exception {
        // Initialize the database
        contratacao = contratacaoRepository.saveAndFlush(contratacao);
        contratacaoSearchRepository.save(contratacao);

        // Search the contratacao
        restContratacaoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contratacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contratacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].validadeEmDias").value(hasItem(DEFAULT_VALIDADE_EM_DIAS)))
            .andExpect(jsonPath("$.[*].dataValidade").value(hasItem(DEFAULT_DATA_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
