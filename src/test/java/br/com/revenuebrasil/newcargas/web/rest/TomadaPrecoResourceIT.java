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
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.TomadaPrecoRepository;
import br.com.revenuebrasil.newcargas.repository.search.TomadaPrecoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TomadaPrecoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TomadaPrecoMapper;
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
 * Integration tests for the {@link TomadaPrecoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TomadaPrecoResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_HORA_ENVIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ENVIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_ENVIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_PRAZO_RESPOSTA = 1;
    private static final Integer UPDATED_PRAZO_RESPOSTA = 2;
    private static final Integer SMALLER_PRAZO_RESPOSTA = 1 - 1;

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_APROVADO = false;
    private static final Boolean UPDATED_APROVADO = true;

    private static final Boolean DEFAULT_CANCELADO = false;
    private static final Boolean UPDATED_CANCELADO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final String ENTITY_API_URL = "/api/tomada-precos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tomada-precos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TomadaPrecoRepository tomadaPrecoRepository;

    @Autowired
    private TomadaPrecoMapper tomadaPrecoMapper;

    @Autowired
    private TomadaPrecoSearchRepository tomadaPrecoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTomadaPrecoMockMvc;

    private TomadaPreco tomadaPreco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TomadaPreco createEntity(EntityManager em) {
        TomadaPreco tomadaPreco = new TomadaPreco()
            .dataHoraEnvio(DEFAULT_DATA_HORA_ENVIO)
            .prazoResposta(DEFAULT_PRAZO_RESPOSTA)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .observacao(DEFAULT_OBSERVACAO)
            .aprovado(DEFAULT_APROVADO)
            .cancelado(DEFAULT_CANCELADO)
            .removido(DEFAULT_REMOVIDO);
        return tomadaPreco;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TomadaPreco createUpdatedEntity(EntityManager em) {
        TomadaPreco tomadaPreco = new TomadaPreco()
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .prazoResposta(UPDATED_PRAZO_RESPOSTA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .aprovado(UPDATED_APROVADO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        return tomadaPreco;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        tomadaPrecoSearchRepository.deleteAll();
        assertThat(tomadaPrecoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        tomadaPreco = createEntity(em);
    }

    @Test
    @Transactional
    void createTomadaPreco() throws Exception {
        int databaseSizeBeforeCreate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);
        restTomadaPrecoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TomadaPreco testTomadaPreco = tomadaPrecoList.get(tomadaPrecoList.size() - 1);
        assertThat(testTomadaPreco.getDataHoraEnvio()).isEqualTo(DEFAULT_DATA_HORA_ENVIO);
        assertThat(testTomadaPreco.getPrazoResposta()).isEqualTo(DEFAULT_PRAZO_RESPOSTA);
        assertThat(testTomadaPreco.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testTomadaPreco.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(DEFAULT_APROVADO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void createTomadaPrecoWithExistingId() throws Exception {
        // Create the TomadaPreco with an existing ID
        tomadaPreco.setId(1L);
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        int databaseSizeBeforeCreate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTomadaPrecoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataHoraEnvioIsRequired() throws Exception {
        int databaseSizeBeforeTest = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        // set the field null
        tomadaPreco.setDataHoraEnvio(null);

        // Create the TomadaPreco, which fails.
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        restTomadaPrecoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isBadRequest());

        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTomadaPrecos() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tomadaPreco.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraEnvio").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENVIO))))
            .andExpect(jsonPath("$.[*].prazoResposta").value(hasItem(DEFAULT_PRAZO_RESPOSTA)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].aprovado").value(hasItem(DEFAULT_APROVADO.booleanValue())))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }

    @Test
    @Transactional
    void getTomadaPreco() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get the tomadaPreco
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_API_URL_ID, tomadaPreco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tomadaPreco.getId().intValue()))
            .andExpect(jsonPath("$.dataHoraEnvio").value(sameInstant(DEFAULT_DATA_HORA_ENVIO)))
            .andExpect(jsonPath("$.prazoResposta").value(DEFAULT_PRAZO_RESPOSTA))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.aprovado").value(DEFAULT_APROVADO.booleanValue()))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getTomadaPrecosByIdFiltering() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        Long id = tomadaPreco.getId();

        defaultTomadaPrecoShouldBeFound("id.equals=" + id);
        defaultTomadaPrecoShouldNotBeFound("id.notEquals=" + id);

        defaultTomadaPrecoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTomadaPrecoShouldNotBeFound("id.greaterThan=" + id);

        defaultTomadaPrecoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTomadaPrecoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio equals to DEFAULT_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.equals=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the tomadaPrecoList where dataHoraEnvio equals to UPDATED_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.equals=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio in DEFAULT_DATA_HORA_ENVIO or UPDATED_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.in=" + DEFAULT_DATA_HORA_ENVIO + "," + UPDATED_DATA_HORA_ENVIO);

        // Get all the tomadaPrecoList where dataHoraEnvio equals to UPDATED_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.in=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio is not null
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.specified=true");

        // Get all the tomadaPrecoList where dataHoraEnvio is null
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio is greater than or equal to DEFAULT_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.greaterThanOrEqual=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the tomadaPrecoList where dataHoraEnvio is greater than or equal to UPDATED_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.greaterThanOrEqual=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio is less than or equal to DEFAULT_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.lessThanOrEqual=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the tomadaPrecoList where dataHoraEnvio is less than or equal to SMALLER_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.lessThanOrEqual=" + SMALLER_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsLessThanSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio is less than DEFAULT_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.lessThan=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the tomadaPrecoList where dataHoraEnvio is less than UPDATED_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.lessThan=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByDataHoraEnvioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where dataHoraEnvio is greater than DEFAULT_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldNotBeFound("dataHoraEnvio.greaterThan=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the tomadaPrecoList where dataHoraEnvio is greater than SMALLER_DATA_HORA_ENVIO
        defaultTomadaPrecoShouldBeFound("dataHoraEnvio.greaterThan=" + SMALLER_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta equals to DEFAULT_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldBeFound("prazoResposta.equals=" + DEFAULT_PRAZO_RESPOSTA);

        // Get all the tomadaPrecoList where prazoResposta equals to UPDATED_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.equals=" + UPDATED_PRAZO_RESPOSTA);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta in DEFAULT_PRAZO_RESPOSTA or UPDATED_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldBeFound("prazoResposta.in=" + DEFAULT_PRAZO_RESPOSTA + "," + UPDATED_PRAZO_RESPOSTA);

        // Get all the tomadaPrecoList where prazoResposta equals to UPDATED_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.in=" + UPDATED_PRAZO_RESPOSTA);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta is not null
        defaultTomadaPrecoShouldBeFound("prazoResposta.specified=true");

        // Get all the tomadaPrecoList where prazoResposta is null
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta is greater than or equal to DEFAULT_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldBeFound("prazoResposta.greaterThanOrEqual=" + DEFAULT_PRAZO_RESPOSTA);

        // Get all the tomadaPrecoList where prazoResposta is greater than or equal to (DEFAULT_PRAZO_RESPOSTA + 1)
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.greaterThanOrEqual=" + (DEFAULT_PRAZO_RESPOSTA + 1));
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta is less than or equal to DEFAULT_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldBeFound("prazoResposta.lessThanOrEqual=" + DEFAULT_PRAZO_RESPOSTA);

        // Get all the tomadaPrecoList where prazoResposta is less than or equal to SMALLER_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.lessThanOrEqual=" + SMALLER_PRAZO_RESPOSTA);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsLessThanSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta is less than DEFAULT_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.lessThan=" + DEFAULT_PRAZO_RESPOSTA);

        // Get all the tomadaPrecoList where prazoResposta is less than (DEFAULT_PRAZO_RESPOSTA + 1)
        defaultTomadaPrecoShouldBeFound("prazoResposta.lessThan=" + (DEFAULT_PRAZO_RESPOSTA + 1));
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByPrazoRespostaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where prazoResposta is greater than DEFAULT_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldNotBeFound("prazoResposta.greaterThan=" + DEFAULT_PRAZO_RESPOSTA);

        // Get all the tomadaPrecoList where prazoResposta is greater than SMALLER_PRAZO_RESPOSTA
        defaultTomadaPrecoShouldBeFound("prazoResposta.greaterThan=" + SMALLER_PRAZO_RESPOSTA);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultTomadaPrecoShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the tomadaPrecoList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultTomadaPrecoShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultTomadaPrecoShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the tomadaPrecoList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultTomadaPrecoShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal is not null
        defaultTomadaPrecoShouldBeFound("valorTotal.specified=true");

        // Get all the tomadaPrecoList where valorTotal is null
        defaultTomadaPrecoShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultTomadaPrecoShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the tomadaPrecoList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultTomadaPrecoShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultTomadaPrecoShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the tomadaPrecoList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultTomadaPrecoShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultTomadaPrecoShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the tomadaPrecoList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultTomadaPrecoShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultTomadaPrecoShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the tomadaPrecoList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultTomadaPrecoShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where observacao equals to DEFAULT_OBSERVACAO
        defaultTomadaPrecoShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the tomadaPrecoList where observacao equals to UPDATED_OBSERVACAO
        defaultTomadaPrecoShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultTomadaPrecoShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the tomadaPrecoList where observacao equals to UPDATED_OBSERVACAO
        defaultTomadaPrecoShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where observacao is not null
        defaultTomadaPrecoShouldBeFound("observacao.specified=true");

        // Get all the tomadaPrecoList where observacao is null
        defaultTomadaPrecoShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where observacao contains DEFAULT_OBSERVACAO
        defaultTomadaPrecoShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the tomadaPrecoList where observacao contains UPDATED_OBSERVACAO
        defaultTomadaPrecoShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where observacao does not contain DEFAULT_OBSERVACAO
        defaultTomadaPrecoShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the tomadaPrecoList where observacao does not contain UPDATED_OBSERVACAO
        defaultTomadaPrecoShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByAprovadoIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where aprovado equals to DEFAULT_APROVADO
        defaultTomadaPrecoShouldBeFound("aprovado.equals=" + DEFAULT_APROVADO);

        // Get all the tomadaPrecoList where aprovado equals to UPDATED_APROVADO
        defaultTomadaPrecoShouldNotBeFound("aprovado.equals=" + UPDATED_APROVADO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByAprovadoIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where aprovado in DEFAULT_APROVADO or UPDATED_APROVADO
        defaultTomadaPrecoShouldBeFound("aprovado.in=" + DEFAULT_APROVADO + "," + UPDATED_APROVADO);

        // Get all the tomadaPrecoList where aprovado equals to UPDATED_APROVADO
        defaultTomadaPrecoShouldNotBeFound("aprovado.in=" + UPDATED_APROVADO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByAprovadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where aprovado is not null
        defaultTomadaPrecoShouldBeFound("aprovado.specified=true");

        // Get all the tomadaPrecoList where aprovado is null
        defaultTomadaPrecoShouldNotBeFound("aprovado.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByCanceladoIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where cancelado equals to DEFAULT_CANCELADO
        defaultTomadaPrecoShouldBeFound("cancelado.equals=" + DEFAULT_CANCELADO);

        // Get all the tomadaPrecoList where cancelado equals to UPDATED_CANCELADO
        defaultTomadaPrecoShouldNotBeFound("cancelado.equals=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByCanceladoIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where cancelado in DEFAULT_CANCELADO or UPDATED_CANCELADO
        defaultTomadaPrecoShouldBeFound("cancelado.in=" + DEFAULT_CANCELADO + "," + UPDATED_CANCELADO);

        // Get all the tomadaPrecoList where cancelado equals to UPDATED_CANCELADO
        defaultTomadaPrecoShouldNotBeFound("cancelado.in=" + UPDATED_CANCELADO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByCanceladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where cancelado is not null
        defaultTomadaPrecoShouldBeFound("cancelado.specified=true");

        // Get all the tomadaPrecoList where cancelado is null
        defaultTomadaPrecoShouldNotBeFound("cancelado.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where removido equals to DEFAULT_REMOVIDO
        defaultTomadaPrecoShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the tomadaPrecoList where removido equals to UPDATED_REMOVIDO
        defaultTomadaPrecoShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultTomadaPrecoShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the tomadaPrecoList where removido equals to UPDATED_REMOVIDO
        defaultTomadaPrecoShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        // Get all the tomadaPrecoList where removido is not null
        defaultTomadaPrecoShouldBeFound("removido.specified=true");

        // Get all the tomadaPrecoList where removido is null
        defaultTomadaPrecoShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByContratacaoIsEqualToSomething() throws Exception {
        Contratacao contratacao;
        if (TestUtil.findAll(em, Contratacao.class).isEmpty()) {
            tomadaPrecoRepository.saveAndFlush(tomadaPreco);
            contratacao = ContratacaoResourceIT.createEntity(em);
        } else {
            contratacao = TestUtil.findAll(em, Contratacao.class).get(0);
        }
        em.persist(contratacao);
        em.flush();
        tomadaPreco.setContratacao(contratacao);
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);
        Long contratacaoId = contratacao.getId();
        // Get all the tomadaPrecoList where contratacao equals to contratacaoId
        defaultTomadaPrecoShouldBeFound("contratacaoId.equals=" + contratacaoId);

        // Get all the tomadaPrecoList where contratacao equals to (contratacaoId + 1)
        defaultTomadaPrecoShouldNotBeFound("contratacaoId.equals=" + (contratacaoId + 1));
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            tomadaPrecoRepository.saveAndFlush(tomadaPreco);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        tomadaPreco.setTransportadora(transportadora);
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);
        Long transportadoraId = transportadora.getId();
        // Get all the tomadaPrecoList where transportadora equals to transportadoraId
        defaultTomadaPrecoShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the tomadaPrecoList where transportadora equals to (transportadoraId + 1)
        defaultTomadaPrecoShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    @Test
    @Transactional
    void getAllTomadaPrecosByRoteirizacaoIsEqualToSomething() throws Exception {
        Roteirizacao roteirizacao;
        if (TestUtil.findAll(em, Roteirizacao.class).isEmpty()) {
            tomadaPrecoRepository.saveAndFlush(tomadaPreco);
            roteirizacao = RoteirizacaoResourceIT.createEntity(em);
        } else {
            roteirizacao = TestUtil.findAll(em, Roteirizacao.class).get(0);
        }
        em.persist(roteirizacao);
        em.flush();
        tomadaPreco.setRoteirizacao(roteirizacao);
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);
        Long roteirizacaoId = roteirizacao.getId();
        // Get all the tomadaPrecoList where roteirizacao equals to roteirizacaoId
        defaultTomadaPrecoShouldBeFound("roteirizacaoId.equals=" + roteirizacaoId);

        // Get all the tomadaPrecoList where roteirizacao equals to (roteirizacaoId + 1)
        defaultTomadaPrecoShouldNotBeFound("roteirizacaoId.equals=" + (roteirizacaoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTomadaPrecoShouldBeFound(String filter) throws Exception {
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tomadaPreco.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraEnvio").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENVIO))))
            .andExpect(jsonPath("$.[*].prazoResposta").value(hasItem(DEFAULT_PRAZO_RESPOSTA)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].aprovado").value(hasItem(DEFAULT_APROVADO.booleanValue())))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTomadaPrecoShouldNotBeFound(String filter) throws Exception {
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTomadaPreco() throws Exception {
        // Get the tomadaPreco
        restTomadaPrecoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTomadaPreco() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        tomadaPrecoSearchRepository.save(tomadaPreco);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());

        // Update the tomadaPreco
        TomadaPreco updatedTomadaPreco = tomadaPrecoRepository.findById(tomadaPreco.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTomadaPreco are not directly saved in db
        em.detach(updatedTomadaPreco);
        updatedTomadaPreco
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .prazoResposta(UPDATED_PRAZO_RESPOSTA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .aprovado(UPDATED_APROVADO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(updatedTomadaPreco);

        restTomadaPrecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tomadaPrecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        TomadaPreco testTomadaPreco = tomadaPrecoList.get(tomadaPrecoList.size() - 1);
        assertThat(testTomadaPreco.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testTomadaPreco.getPrazoResposta()).isEqualTo(UPDATED_PRAZO_RESPOSTA);
        assertThat(testTomadaPreco.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testTomadaPreco.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(UPDATED_APROVADO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TomadaPreco> tomadaPrecoSearchList = IterableUtils.toList(tomadaPrecoSearchRepository.findAll());
                TomadaPreco testTomadaPrecoSearch = tomadaPrecoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTomadaPrecoSearch.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
                assertThat(testTomadaPrecoSearch.getPrazoResposta()).isEqualTo(UPDATED_PRAZO_RESPOSTA);
                assertThat(testTomadaPrecoSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testTomadaPrecoSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testTomadaPrecoSearch.getAprovado()).isEqualTo(UPDATED_APROVADO);
                assertThat(testTomadaPrecoSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testTomadaPrecoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTomadaPreco() throws Exception {
        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        tomadaPreco.setId(longCount.incrementAndGet());

        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTomadaPrecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tomadaPrecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTomadaPreco() throws Exception {
        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        tomadaPreco.setId(longCount.incrementAndGet());

        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTomadaPrecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTomadaPreco() throws Exception {
        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        tomadaPreco.setId(longCount.incrementAndGet());

        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTomadaPrecoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTomadaPrecoWithPatch() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();

        // Update the tomadaPreco using partial update
        TomadaPreco partialUpdatedTomadaPreco = new TomadaPreco();
        partialUpdatedTomadaPreco.setId(tomadaPreco.getId());

        partialUpdatedTomadaPreco.dataHoraEnvio(UPDATED_DATA_HORA_ENVIO).cancelado(UPDATED_CANCELADO);

        restTomadaPrecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTomadaPreco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTomadaPreco))
            )
            .andExpect(status().isOk());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        TomadaPreco testTomadaPreco = tomadaPrecoList.get(tomadaPrecoList.size() - 1);
        assertThat(testTomadaPreco.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testTomadaPreco.getPrazoResposta()).isEqualTo(DEFAULT_PRAZO_RESPOSTA);
        assertThat(testTomadaPreco.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testTomadaPreco.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(DEFAULT_APROVADO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void fullUpdateTomadaPrecoWithPatch() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);

        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();

        // Update the tomadaPreco using partial update
        TomadaPreco partialUpdatedTomadaPreco = new TomadaPreco();
        partialUpdatedTomadaPreco.setId(tomadaPreco.getId());

        partialUpdatedTomadaPreco
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .prazoResposta(UPDATED_PRAZO_RESPOSTA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .observacao(UPDATED_OBSERVACAO)
            .aprovado(UPDATED_APROVADO)
            .cancelado(UPDATED_CANCELADO)
            .removido(UPDATED_REMOVIDO);

        restTomadaPrecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTomadaPreco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTomadaPreco))
            )
            .andExpect(status().isOk());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        TomadaPreco testTomadaPreco = tomadaPrecoList.get(tomadaPrecoList.size() - 1);
        assertThat(testTomadaPreco.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testTomadaPreco.getPrazoResposta()).isEqualTo(UPDATED_PRAZO_RESPOSTA);
        assertThat(testTomadaPreco.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testTomadaPreco.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(UPDATED_APROVADO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void patchNonExistingTomadaPreco() throws Exception {
        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        tomadaPreco.setId(longCount.incrementAndGet());

        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTomadaPrecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tomadaPrecoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTomadaPreco() throws Exception {
        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        tomadaPreco.setId(longCount.incrementAndGet());

        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTomadaPrecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTomadaPreco() throws Exception {
        int databaseSizeBeforeUpdate = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        tomadaPreco.setId(longCount.incrementAndGet());

        // Create the TomadaPreco
        TomadaPrecoDTO tomadaPrecoDTO = tomadaPrecoMapper.toDto(tomadaPreco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTomadaPrecoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tomadaPrecoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TomadaPreco in the database
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTomadaPreco() throws Exception {
        // Initialize the database
        tomadaPrecoRepository.saveAndFlush(tomadaPreco);
        tomadaPrecoRepository.save(tomadaPreco);
        tomadaPrecoSearchRepository.save(tomadaPreco);

        int databaseSizeBeforeDelete = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tomadaPreco
        restTomadaPrecoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tomadaPreco.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TomadaPreco> tomadaPrecoList = tomadaPrecoRepository.findAll();
        assertThat(tomadaPrecoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTomadaPreco() throws Exception {
        // Initialize the database
        tomadaPreco = tomadaPrecoRepository.saveAndFlush(tomadaPreco);
        tomadaPrecoSearchRepository.save(tomadaPreco);

        // Search the tomadaPreco
        restTomadaPrecoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tomadaPreco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tomadaPreco.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraEnvio").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENVIO))))
            .andExpect(jsonPath("$.[*].prazoResposta").value(hasItem(DEFAULT_PRAZO_RESPOSTA)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].aprovado").value(hasItem(DEFAULT_APROVADO.booleanValue())))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
