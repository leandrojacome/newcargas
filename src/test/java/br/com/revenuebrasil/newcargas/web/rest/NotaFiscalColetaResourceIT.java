package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.repository.NotaFiscalColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.NotaFiscalColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.NotaFiscalColetaMapper;
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
 * Integration tests for the {@link NotaFiscalColetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotaFiscalColetaResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_REMETENTE = "AAAAAAAAAA";
    private static final String UPDATED_REMETENTE = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATARIO = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATARIO = "BBBBBBBBBB";

    private static final Double DEFAULT_METRO_CUBICO = 1D;
    private static final Double UPDATED_METRO_CUBICO = 2D;
    private static final Double SMALLER_METRO_CUBICO = 1D - 1D;

    private static final Double DEFAULT_QUANTIDADE = 1D;
    private static final Double UPDATED_QUANTIDADE = 2D;
    private static final Double SMALLER_QUANTIDADE = 1D - 1D;

    private static final Double DEFAULT_PESO = 1D;
    private static final Double UPDATED_PESO = 2D;
    private static final Double SMALLER_PESO = 1D - 1D;

    private static final ZonedDateTime DEFAULT_DATA_EMISSAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_EMISSAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_EMISSAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_SAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_SAIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_SAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final Double DEFAULT_PESO_TOTAL = 1D;
    private static final Double UPDATED_PESO_TOTAL = 2D;
    private static final Double SMALLER_PESO_TOTAL = 1D - 1D;

    private static final Integer DEFAULT_QUANTIDADE_TOTAL = 1;
    private static final Integer UPDATED_QUANTIDADE_TOTAL = 2;
    private static final Integer SMALLER_QUANTIDADE_TOTAL = 1 - 1;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nota-fiscal-coletas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/nota-fiscal-coletas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotaFiscalColetaRepository notaFiscalColetaRepository;

    @Autowired
    private NotaFiscalColetaMapper notaFiscalColetaMapper;

    @Autowired
    private NotaFiscalColetaSearchRepository notaFiscalColetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotaFiscalColetaMockMvc;

    private NotaFiscalColeta notaFiscalColeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotaFiscalColeta createEntity(EntityManager em) {
        NotaFiscalColeta notaFiscalColeta = new NotaFiscalColeta()
            .numero(DEFAULT_NUMERO)
            .serie(DEFAULT_SERIE)
            .remetente(DEFAULT_REMETENTE)
            .destinatario(DEFAULT_DESTINATARIO)
            .metroCubico(DEFAULT_METRO_CUBICO)
            .quantidade(DEFAULT_QUANTIDADE)
            .peso(DEFAULT_PESO)
            .dataEmissao(DEFAULT_DATA_EMISSAO)
            .dataSaida(DEFAULT_DATA_SAIDA)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .pesoTotal(DEFAULT_PESO_TOTAL)
            .quantidadeTotal(DEFAULT_QUANTIDADE_TOTAL)
            .observacao(DEFAULT_OBSERVACAO);
        return notaFiscalColeta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotaFiscalColeta createUpdatedEntity(EntityManager em) {
        NotaFiscalColeta notaFiscalColeta = new NotaFiscalColeta()
            .numero(UPDATED_NUMERO)
            .serie(UPDATED_SERIE)
            .remetente(UPDATED_REMETENTE)
            .destinatario(UPDATED_DESTINATARIO)
            .metroCubico(UPDATED_METRO_CUBICO)
            .quantidade(UPDATED_QUANTIDADE)
            .peso(UPDATED_PESO)
            .dataEmissao(UPDATED_DATA_EMISSAO)
            .dataSaida(UPDATED_DATA_SAIDA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .pesoTotal(UPDATED_PESO_TOTAL)
            .quantidadeTotal(UPDATED_QUANTIDADE_TOTAL)
            .observacao(UPDATED_OBSERVACAO);
        return notaFiscalColeta;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        notaFiscalColetaSearchRepository.deleteAll();
        assertThat(notaFiscalColetaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        notaFiscalColeta = createEntity(em);
    }

    @Test
    @Transactional
    void createNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeCreate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);
        restNotaFiscalColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        NotaFiscalColeta testNotaFiscalColeta = notaFiscalColetaList.get(notaFiscalColetaList.size() - 1);
        assertThat(testNotaFiscalColeta.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testNotaFiscalColeta.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testNotaFiscalColeta.getRemetente()).isEqualTo(DEFAULT_REMETENTE);
        assertThat(testNotaFiscalColeta.getDestinatario()).isEqualTo(DEFAULT_DESTINATARIO);
        assertThat(testNotaFiscalColeta.getMetroCubico()).isEqualTo(DEFAULT_METRO_CUBICO);
        assertThat(testNotaFiscalColeta.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testNotaFiscalColeta.getPeso()).isEqualTo(DEFAULT_PESO);
        assertThat(testNotaFiscalColeta.getDataEmissao()).isEqualTo(DEFAULT_DATA_EMISSAO);
        assertThat(testNotaFiscalColeta.getDataSaida()).isEqualTo(DEFAULT_DATA_SAIDA);
        assertThat(testNotaFiscalColeta.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testNotaFiscalColeta.getPesoTotal()).isEqualTo(DEFAULT_PESO_TOTAL);
        assertThat(testNotaFiscalColeta.getQuantidadeTotal()).isEqualTo(DEFAULT_QUANTIDADE_TOTAL);
        assertThat(testNotaFiscalColeta.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void createNotaFiscalColetaWithExistingId() throws Exception {
        // Create the NotaFiscalColeta with an existing ID
        notaFiscalColeta.setId(1L);
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        int databaseSizeBeforeCreate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotaFiscalColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        // set the field null
        notaFiscalColeta.setNumero(null);

        // Create the NotaFiscalColeta, which fails.
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        restNotaFiscalColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSerieIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        // set the field null
        notaFiscalColeta.setSerie(null);

        // Create the NotaFiscalColeta, which fails.
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        restNotaFiscalColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetas() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notaFiscalColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].remetente").value(hasItem(DEFAULT_REMETENTE)))
            .andExpect(jsonPath("$.[*].destinatario").value(hasItem(DEFAULT_DESTINATARIO)))
            .andExpect(jsonPath("$.[*].metroCubico").value(hasItem(DEFAULT_METRO_CUBICO.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].dataEmissao").value(hasItem(sameInstant(DEFAULT_DATA_EMISSAO))))
            .andExpect(jsonPath("$.[*].dataSaida").value(hasItem(sameInstant(DEFAULT_DATA_SAIDA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].pesoTotal").value(hasItem(DEFAULT_PESO_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidadeTotal").value(hasItem(DEFAULT_QUANTIDADE_TOTAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }

    @Test
    @Transactional
    void getNotaFiscalColeta() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get the notaFiscalColeta
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_API_URL_ID, notaFiscalColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notaFiscalColeta.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE))
            .andExpect(jsonPath("$.remetente").value(DEFAULT_REMETENTE))
            .andExpect(jsonPath("$.destinatario").value(DEFAULT_DESTINATARIO))
            .andExpect(jsonPath("$.metroCubico").value(DEFAULT_METRO_CUBICO.doubleValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE.doubleValue()))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.doubleValue()))
            .andExpect(jsonPath("$.dataEmissao").value(sameInstant(DEFAULT_DATA_EMISSAO)))
            .andExpect(jsonPath("$.dataSaida").value(sameInstant(DEFAULT_DATA_SAIDA)))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.pesoTotal").value(DEFAULT_PESO_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.quantidadeTotal").value(DEFAULT_QUANTIDADE_TOTAL))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO));
    }

    @Test
    @Transactional
    void getNotaFiscalColetasByIdFiltering() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        Long id = notaFiscalColeta.getId();

        defaultNotaFiscalColetaShouldBeFound("id.equals=" + id);
        defaultNotaFiscalColetaShouldNotBeFound("id.notEquals=" + id);

        defaultNotaFiscalColetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotaFiscalColetaShouldNotBeFound("id.greaterThan=" + id);

        defaultNotaFiscalColetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotaFiscalColetaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where numero equals to DEFAULT_NUMERO
        defaultNotaFiscalColetaShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the notaFiscalColetaList where numero equals to UPDATED_NUMERO
        defaultNotaFiscalColetaShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultNotaFiscalColetaShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the notaFiscalColetaList where numero equals to UPDATED_NUMERO
        defaultNotaFiscalColetaShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where numero is not null
        defaultNotaFiscalColetaShouldBeFound("numero.specified=true");

        // Get all the notaFiscalColetaList where numero is null
        defaultNotaFiscalColetaShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByNumeroContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where numero contains DEFAULT_NUMERO
        defaultNotaFiscalColetaShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the notaFiscalColetaList where numero contains UPDATED_NUMERO
        defaultNotaFiscalColetaShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where numero does not contain DEFAULT_NUMERO
        defaultNotaFiscalColetaShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the notaFiscalColetaList where numero does not contain UPDATED_NUMERO
        defaultNotaFiscalColetaShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasBySerieIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where serie equals to DEFAULT_SERIE
        defaultNotaFiscalColetaShouldBeFound("serie.equals=" + DEFAULT_SERIE);

        // Get all the notaFiscalColetaList where serie equals to UPDATED_SERIE
        defaultNotaFiscalColetaShouldNotBeFound("serie.equals=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasBySerieIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where serie in DEFAULT_SERIE or UPDATED_SERIE
        defaultNotaFiscalColetaShouldBeFound("serie.in=" + DEFAULT_SERIE + "," + UPDATED_SERIE);

        // Get all the notaFiscalColetaList where serie equals to UPDATED_SERIE
        defaultNotaFiscalColetaShouldNotBeFound("serie.in=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasBySerieIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where serie is not null
        defaultNotaFiscalColetaShouldBeFound("serie.specified=true");

        // Get all the notaFiscalColetaList where serie is null
        defaultNotaFiscalColetaShouldNotBeFound("serie.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasBySerieContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where serie contains DEFAULT_SERIE
        defaultNotaFiscalColetaShouldBeFound("serie.contains=" + DEFAULT_SERIE);

        // Get all the notaFiscalColetaList where serie contains UPDATED_SERIE
        defaultNotaFiscalColetaShouldNotBeFound("serie.contains=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasBySerieNotContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where serie does not contain DEFAULT_SERIE
        defaultNotaFiscalColetaShouldNotBeFound("serie.doesNotContain=" + DEFAULT_SERIE);

        // Get all the notaFiscalColetaList where serie does not contain UPDATED_SERIE
        defaultNotaFiscalColetaShouldBeFound("serie.doesNotContain=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByRemetenteIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where remetente equals to DEFAULT_REMETENTE
        defaultNotaFiscalColetaShouldBeFound("remetente.equals=" + DEFAULT_REMETENTE);

        // Get all the notaFiscalColetaList where remetente equals to UPDATED_REMETENTE
        defaultNotaFiscalColetaShouldNotBeFound("remetente.equals=" + UPDATED_REMETENTE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByRemetenteIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where remetente in DEFAULT_REMETENTE or UPDATED_REMETENTE
        defaultNotaFiscalColetaShouldBeFound("remetente.in=" + DEFAULT_REMETENTE + "," + UPDATED_REMETENTE);

        // Get all the notaFiscalColetaList where remetente equals to UPDATED_REMETENTE
        defaultNotaFiscalColetaShouldNotBeFound("remetente.in=" + UPDATED_REMETENTE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByRemetenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where remetente is not null
        defaultNotaFiscalColetaShouldBeFound("remetente.specified=true");

        // Get all the notaFiscalColetaList where remetente is null
        defaultNotaFiscalColetaShouldNotBeFound("remetente.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByRemetenteContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where remetente contains DEFAULT_REMETENTE
        defaultNotaFiscalColetaShouldBeFound("remetente.contains=" + DEFAULT_REMETENTE);

        // Get all the notaFiscalColetaList where remetente contains UPDATED_REMETENTE
        defaultNotaFiscalColetaShouldNotBeFound("remetente.contains=" + UPDATED_REMETENTE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByRemetenteNotContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where remetente does not contain DEFAULT_REMETENTE
        defaultNotaFiscalColetaShouldNotBeFound("remetente.doesNotContain=" + DEFAULT_REMETENTE);

        // Get all the notaFiscalColetaList where remetente does not contain UPDATED_REMETENTE
        defaultNotaFiscalColetaShouldBeFound("remetente.doesNotContain=" + UPDATED_REMETENTE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDestinatarioIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where destinatario equals to DEFAULT_DESTINATARIO
        defaultNotaFiscalColetaShouldBeFound("destinatario.equals=" + DEFAULT_DESTINATARIO);

        // Get all the notaFiscalColetaList where destinatario equals to UPDATED_DESTINATARIO
        defaultNotaFiscalColetaShouldNotBeFound("destinatario.equals=" + UPDATED_DESTINATARIO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDestinatarioIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where destinatario in DEFAULT_DESTINATARIO or UPDATED_DESTINATARIO
        defaultNotaFiscalColetaShouldBeFound("destinatario.in=" + DEFAULT_DESTINATARIO + "," + UPDATED_DESTINATARIO);

        // Get all the notaFiscalColetaList where destinatario equals to UPDATED_DESTINATARIO
        defaultNotaFiscalColetaShouldNotBeFound("destinatario.in=" + UPDATED_DESTINATARIO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDestinatarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where destinatario is not null
        defaultNotaFiscalColetaShouldBeFound("destinatario.specified=true");

        // Get all the notaFiscalColetaList where destinatario is null
        defaultNotaFiscalColetaShouldNotBeFound("destinatario.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDestinatarioContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where destinatario contains DEFAULT_DESTINATARIO
        defaultNotaFiscalColetaShouldBeFound("destinatario.contains=" + DEFAULT_DESTINATARIO);

        // Get all the notaFiscalColetaList where destinatario contains UPDATED_DESTINATARIO
        defaultNotaFiscalColetaShouldNotBeFound("destinatario.contains=" + UPDATED_DESTINATARIO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDestinatarioNotContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where destinatario does not contain DEFAULT_DESTINATARIO
        defaultNotaFiscalColetaShouldNotBeFound("destinatario.doesNotContain=" + DEFAULT_DESTINATARIO);

        // Get all the notaFiscalColetaList where destinatario does not contain UPDATED_DESTINATARIO
        defaultNotaFiscalColetaShouldBeFound("destinatario.doesNotContain=" + UPDATED_DESTINATARIO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico equals to DEFAULT_METRO_CUBICO
        defaultNotaFiscalColetaShouldBeFound("metroCubico.equals=" + DEFAULT_METRO_CUBICO);

        // Get all the notaFiscalColetaList where metroCubico equals to UPDATED_METRO_CUBICO
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.equals=" + UPDATED_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico in DEFAULT_METRO_CUBICO or UPDATED_METRO_CUBICO
        defaultNotaFiscalColetaShouldBeFound("metroCubico.in=" + DEFAULT_METRO_CUBICO + "," + UPDATED_METRO_CUBICO);

        // Get all the notaFiscalColetaList where metroCubico equals to UPDATED_METRO_CUBICO
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.in=" + UPDATED_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico is not null
        defaultNotaFiscalColetaShouldBeFound("metroCubico.specified=true");

        // Get all the notaFiscalColetaList where metroCubico is null
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico is greater than or equal to DEFAULT_METRO_CUBICO
        defaultNotaFiscalColetaShouldBeFound("metroCubico.greaterThanOrEqual=" + DEFAULT_METRO_CUBICO);

        // Get all the notaFiscalColetaList where metroCubico is greater than or equal to (DEFAULT_METRO_CUBICO + 1)
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.greaterThanOrEqual=" + (DEFAULT_METRO_CUBICO + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico is less than or equal to DEFAULT_METRO_CUBICO
        defaultNotaFiscalColetaShouldBeFound("metroCubico.lessThanOrEqual=" + DEFAULT_METRO_CUBICO);

        // Get all the notaFiscalColetaList where metroCubico is less than or equal to SMALLER_METRO_CUBICO
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.lessThanOrEqual=" + SMALLER_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico is less than DEFAULT_METRO_CUBICO
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.lessThan=" + DEFAULT_METRO_CUBICO);

        // Get all the notaFiscalColetaList where metroCubico is less than (DEFAULT_METRO_CUBICO + 1)
        defaultNotaFiscalColetaShouldBeFound("metroCubico.lessThan=" + (DEFAULT_METRO_CUBICO + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByMetroCubicoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where metroCubico is greater than DEFAULT_METRO_CUBICO
        defaultNotaFiscalColetaShouldNotBeFound("metroCubico.greaterThan=" + DEFAULT_METRO_CUBICO);

        // Get all the notaFiscalColetaList where metroCubico is greater than SMALLER_METRO_CUBICO
        defaultNotaFiscalColetaShouldBeFound("metroCubico.greaterThan=" + SMALLER_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade equals to DEFAULT_QUANTIDADE
        defaultNotaFiscalColetaShouldBeFound("quantidade.equals=" + DEFAULT_QUANTIDADE);

        // Get all the notaFiscalColetaList where quantidade equals to UPDATED_QUANTIDADE
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.equals=" + UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade in DEFAULT_QUANTIDADE or UPDATED_QUANTIDADE
        defaultNotaFiscalColetaShouldBeFound("quantidade.in=" + DEFAULT_QUANTIDADE + "," + UPDATED_QUANTIDADE);

        // Get all the notaFiscalColetaList where quantidade equals to UPDATED_QUANTIDADE
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.in=" + UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade is not null
        defaultNotaFiscalColetaShouldBeFound("quantidade.specified=true");

        // Get all the notaFiscalColetaList where quantidade is null
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade is greater than or equal to DEFAULT_QUANTIDADE
        defaultNotaFiscalColetaShouldBeFound("quantidade.greaterThanOrEqual=" + DEFAULT_QUANTIDADE);

        // Get all the notaFiscalColetaList where quantidade is greater than or equal to (DEFAULT_QUANTIDADE + 1)
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.greaterThanOrEqual=" + (DEFAULT_QUANTIDADE + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade is less than or equal to DEFAULT_QUANTIDADE
        defaultNotaFiscalColetaShouldBeFound("quantidade.lessThanOrEqual=" + DEFAULT_QUANTIDADE);

        // Get all the notaFiscalColetaList where quantidade is less than or equal to SMALLER_QUANTIDADE
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.lessThanOrEqual=" + SMALLER_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade is less than DEFAULT_QUANTIDADE
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.lessThan=" + DEFAULT_QUANTIDADE);

        // Get all the notaFiscalColetaList where quantidade is less than (DEFAULT_QUANTIDADE + 1)
        defaultNotaFiscalColetaShouldBeFound("quantidade.lessThan=" + (DEFAULT_QUANTIDADE + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidade is greater than DEFAULT_QUANTIDADE
        defaultNotaFiscalColetaShouldNotBeFound("quantidade.greaterThan=" + DEFAULT_QUANTIDADE);

        // Get all the notaFiscalColetaList where quantidade is greater than SMALLER_QUANTIDADE
        defaultNotaFiscalColetaShouldBeFound("quantidade.greaterThan=" + SMALLER_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso equals to DEFAULT_PESO
        defaultNotaFiscalColetaShouldBeFound("peso.equals=" + DEFAULT_PESO);

        // Get all the notaFiscalColetaList where peso equals to UPDATED_PESO
        defaultNotaFiscalColetaShouldNotBeFound("peso.equals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso in DEFAULT_PESO or UPDATED_PESO
        defaultNotaFiscalColetaShouldBeFound("peso.in=" + DEFAULT_PESO + "," + UPDATED_PESO);

        // Get all the notaFiscalColetaList where peso equals to UPDATED_PESO
        defaultNotaFiscalColetaShouldNotBeFound("peso.in=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso is not null
        defaultNotaFiscalColetaShouldBeFound("peso.specified=true");

        // Get all the notaFiscalColetaList where peso is null
        defaultNotaFiscalColetaShouldNotBeFound("peso.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso is greater than or equal to DEFAULT_PESO
        defaultNotaFiscalColetaShouldBeFound("peso.greaterThanOrEqual=" + DEFAULT_PESO);

        // Get all the notaFiscalColetaList where peso is greater than or equal to (DEFAULT_PESO + 1)
        defaultNotaFiscalColetaShouldNotBeFound("peso.greaterThanOrEqual=" + (DEFAULT_PESO + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso is less than or equal to DEFAULT_PESO
        defaultNotaFiscalColetaShouldBeFound("peso.lessThanOrEqual=" + DEFAULT_PESO);

        // Get all the notaFiscalColetaList where peso is less than or equal to SMALLER_PESO
        defaultNotaFiscalColetaShouldNotBeFound("peso.lessThanOrEqual=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso is less than DEFAULT_PESO
        defaultNotaFiscalColetaShouldNotBeFound("peso.lessThan=" + DEFAULT_PESO);

        // Get all the notaFiscalColetaList where peso is less than (DEFAULT_PESO + 1)
        defaultNotaFiscalColetaShouldBeFound("peso.lessThan=" + (DEFAULT_PESO + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where peso is greater than DEFAULT_PESO
        defaultNotaFiscalColetaShouldNotBeFound("peso.greaterThan=" + DEFAULT_PESO);

        // Get all the notaFiscalColetaList where peso is greater than SMALLER_PESO
        defaultNotaFiscalColetaShouldBeFound("peso.greaterThan=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao equals to DEFAULT_DATA_EMISSAO
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.equals=" + DEFAULT_DATA_EMISSAO);

        // Get all the notaFiscalColetaList where dataEmissao equals to UPDATED_DATA_EMISSAO
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.equals=" + UPDATED_DATA_EMISSAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao in DEFAULT_DATA_EMISSAO or UPDATED_DATA_EMISSAO
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.in=" + DEFAULT_DATA_EMISSAO + "," + UPDATED_DATA_EMISSAO);

        // Get all the notaFiscalColetaList where dataEmissao equals to UPDATED_DATA_EMISSAO
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.in=" + UPDATED_DATA_EMISSAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao is not null
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.specified=true");

        // Get all the notaFiscalColetaList where dataEmissao is null
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao is greater than or equal to DEFAULT_DATA_EMISSAO
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.greaterThanOrEqual=" + DEFAULT_DATA_EMISSAO);

        // Get all the notaFiscalColetaList where dataEmissao is greater than or equal to UPDATED_DATA_EMISSAO
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.greaterThanOrEqual=" + UPDATED_DATA_EMISSAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao is less than or equal to DEFAULT_DATA_EMISSAO
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.lessThanOrEqual=" + DEFAULT_DATA_EMISSAO);

        // Get all the notaFiscalColetaList where dataEmissao is less than or equal to SMALLER_DATA_EMISSAO
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.lessThanOrEqual=" + SMALLER_DATA_EMISSAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao is less than DEFAULT_DATA_EMISSAO
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.lessThan=" + DEFAULT_DATA_EMISSAO);

        // Get all the notaFiscalColetaList where dataEmissao is less than UPDATED_DATA_EMISSAO
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.lessThan=" + UPDATED_DATA_EMISSAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataEmissaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataEmissao is greater than DEFAULT_DATA_EMISSAO
        defaultNotaFiscalColetaShouldNotBeFound("dataEmissao.greaterThan=" + DEFAULT_DATA_EMISSAO);

        // Get all the notaFiscalColetaList where dataEmissao is greater than SMALLER_DATA_EMISSAO
        defaultNotaFiscalColetaShouldBeFound("dataEmissao.greaterThan=" + SMALLER_DATA_EMISSAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida equals to DEFAULT_DATA_SAIDA
        defaultNotaFiscalColetaShouldBeFound("dataSaida.equals=" + DEFAULT_DATA_SAIDA);

        // Get all the notaFiscalColetaList where dataSaida equals to UPDATED_DATA_SAIDA
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.equals=" + UPDATED_DATA_SAIDA);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida in DEFAULT_DATA_SAIDA or UPDATED_DATA_SAIDA
        defaultNotaFiscalColetaShouldBeFound("dataSaida.in=" + DEFAULT_DATA_SAIDA + "," + UPDATED_DATA_SAIDA);

        // Get all the notaFiscalColetaList where dataSaida equals to UPDATED_DATA_SAIDA
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.in=" + UPDATED_DATA_SAIDA);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida is not null
        defaultNotaFiscalColetaShouldBeFound("dataSaida.specified=true");

        // Get all the notaFiscalColetaList where dataSaida is null
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida is greater than or equal to DEFAULT_DATA_SAIDA
        defaultNotaFiscalColetaShouldBeFound("dataSaida.greaterThanOrEqual=" + DEFAULT_DATA_SAIDA);

        // Get all the notaFiscalColetaList where dataSaida is greater than or equal to UPDATED_DATA_SAIDA
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.greaterThanOrEqual=" + UPDATED_DATA_SAIDA);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida is less than or equal to DEFAULT_DATA_SAIDA
        defaultNotaFiscalColetaShouldBeFound("dataSaida.lessThanOrEqual=" + DEFAULT_DATA_SAIDA);

        // Get all the notaFiscalColetaList where dataSaida is less than or equal to SMALLER_DATA_SAIDA
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.lessThanOrEqual=" + SMALLER_DATA_SAIDA);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida is less than DEFAULT_DATA_SAIDA
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.lessThan=" + DEFAULT_DATA_SAIDA);

        // Get all the notaFiscalColetaList where dataSaida is less than UPDATED_DATA_SAIDA
        defaultNotaFiscalColetaShouldBeFound("dataSaida.lessThan=" + UPDATED_DATA_SAIDA);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByDataSaidaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where dataSaida is greater than DEFAULT_DATA_SAIDA
        defaultNotaFiscalColetaShouldNotBeFound("dataSaida.greaterThan=" + DEFAULT_DATA_SAIDA);

        // Get all the notaFiscalColetaList where dataSaida is greater than SMALLER_DATA_SAIDA
        defaultNotaFiscalColetaShouldBeFound("dataSaida.greaterThan=" + SMALLER_DATA_SAIDA);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultNotaFiscalColetaShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the notaFiscalColetaList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultNotaFiscalColetaShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the notaFiscalColetaList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal is not null
        defaultNotaFiscalColetaShouldBeFound("valorTotal.specified=true");

        // Get all the notaFiscalColetaList where valorTotal is null
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultNotaFiscalColetaShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the notaFiscalColetaList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultNotaFiscalColetaShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the notaFiscalColetaList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the notaFiscalColetaList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultNotaFiscalColetaShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the notaFiscalColetaList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultNotaFiscalColetaShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal equals to DEFAULT_PESO_TOTAL
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.equals=" + DEFAULT_PESO_TOTAL);

        // Get all the notaFiscalColetaList where pesoTotal equals to UPDATED_PESO_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.equals=" + UPDATED_PESO_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal in DEFAULT_PESO_TOTAL or UPDATED_PESO_TOTAL
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.in=" + DEFAULT_PESO_TOTAL + "," + UPDATED_PESO_TOTAL);

        // Get all the notaFiscalColetaList where pesoTotal equals to UPDATED_PESO_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.in=" + UPDATED_PESO_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal is not null
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.specified=true");

        // Get all the notaFiscalColetaList where pesoTotal is null
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal is greater than or equal to DEFAULT_PESO_TOTAL
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.greaterThanOrEqual=" + DEFAULT_PESO_TOTAL);

        // Get all the notaFiscalColetaList where pesoTotal is greater than or equal to (DEFAULT_PESO_TOTAL + 1)
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.greaterThanOrEqual=" + (DEFAULT_PESO_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal is less than or equal to DEFAULT_PESO_TOTAL
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.lessThanOrEqual=" + DEFAULT_PESO_TOTAL);

        // Get all the notaFiscalColetaList where pesoTotal is less than or equal to SMALLER_PESO_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.lessThanOrEqual=" + SMALLER_PESO_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal is less than DEFAULT_PESO_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.lessThan=" + DEFAULT_PESO_TOTAL);

        // Get all the notaFiscalColetaList where pesoTotal is less than (DEFAULT_PESO_TOTAL + 1)
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.lessThan=" + (DEFAULT_PESO_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByPesoTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where pesoTotal is greater than DEFAULT_PESO_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("pesoTotal.greaterThan=" + DEFAULT_PESO_TOTAL);

        // Get all the notaFiscalColetaList where pesoTotal is greater than SMALLER_PESO_TOTAL
        defaultNotaFiscalColetaShouldBeFound("pesoTotal.greaterThan=" + SMALLER_PESO_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal equals to DEFAULT_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.equals=" + DEFAULT_QUANTIDADE_TOTAL);

        // Get all the notaFiscalColetaList where quantidadeTotal equals to UPDATED_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.equals=" + UPDATED_QUANTIDADE_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal in DEFAULT_QUANTIDADE_TOTAL or UPDATED_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.in=" + DEFAULT_QUANTIDADE_TOTAL + "," + UPDATED_QUANTIDADE_TOTAL);

        // Get all the notaFiscalColetaList where quantidadeTotal equals to UPDATED_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.in=" + UPDATED_QUANTIDADE_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal is not null
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.specified=true");

        // Get all the notaFiscalColetaList where quantidadeTotal is null
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal is greater than or equal to DEFAULT_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.greaterThanOrEqual=" + DEFAULT_QUANTIDADE_TOTAL);

        // Get all the notaFiscalColetaList where quantidadeTotal is greater than or equal to (DEFAULT_QUANTIDADE_TOTAL + 1)
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.greaterThanOrEqual=" + (DEFAULT_QUANTIDADE_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal is less than or equal to DEFAULT_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.lessThanOrEqual=" + DEFAULT_QUANTIDADE_TOTAL);

        // Get all the notaFiscalColetaList where quantidadeTotal is less than or equal to SMALLER_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.lessThanOrEqual=" + SMALLER_QUANTIDADE_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal is less than DEFAULT_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.lessThan=" + DEFAULT_QUANTIDADE_TOTAL);

        // Get all the notaFiscalColetaList where quantidadeTotal is less than (DEFAULT_QUANTIDADE_TOTAL + 1)
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.lessThan=" + (DEFAULT_QUANTIDADE_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByQuantidadeTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where quantidadeTotal is greater than DEFAULT_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldNotBeFound("quantidadeTotal.greaterThan=" + DEFAULT_QUANTIDADE_TOTAL);

        // Get all the notaFiscalColetaList where quantidadeTotal is greater than SMALLER_QUANTIDADE_TOTAL
        defaultNotaFiscalColetaShouldBeFound("quantidadeTotal.greaterThan=" + SMALLER_QUANTIDADE_TOTAL);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where observacao equals to DEFAULT_OBSERVACAO
        defaultNotaFiscalColetaShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the notaFiscalColetaList where observacao equals to UPDATED_OBSERVACAO
        defaultNotaFiscalColetaShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultNotaFiscalColetaShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the notaFiscalColetaList where observacao equals to UPDATED_OBSERVACAO
        defaultNotaFiscalColetaShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where observacao is not null
        defaultNotaFiscalColetaShouldBeFound("observacao.specified=true");

        // Get all the notaFiscalColetaList where observacao is null
        defaultNotaFiscalColetaShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where observacao contains DEFAULT_OBSERVACAO
        defaultNotaFiscalColetaShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the notaFiscalColetaList where observacao contains UPDATED_OBSERVACAO
        defaultNotaFiscalColetaShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        // Get all the notaFiscalColetaList where observacao does not contain DEFAULT_OBSERVACAO
        defaultNotaFiscalColetaShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the notaFiscalColetaList where observacao does not contain UPDATED_OBSERVACAO
        defaultNotaFiscalColetaShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByEnderecoOrigemIsEqualToSomething() throws Exception {
        Endereco enderecoOrigem;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
            enderecoOrigem = EnderecoResourceIT.createEntity(em);
        } else {
            enderecoOrigem = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(enderecoOrigem);
        em.flush();
        notaFiscalColeta.addEnderecoOrigem(enderecoOrigem);
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
        Long enderecoOrigemId = enderecoOrigem.getId();
        // Get all the notaFiscalColetaList where enderecoOrigem equals to enderecoOrigemId
        defaultNotaFiscalColetaShouldBeFound("enderecoOrigemId.equals=" + enderecoOrigemId);

        // Get all the notaFiscalColetaList where enderecoOrigem equals to (enderecoOrigemId + 1)
        defaultNotaFiscalColetaShouldNotBeFound("enderecoOrigemId.equals=" + (enderecoOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasByEnderecoDestinoIsEqualToSomething() throws Exception {
        Endereco enderecoDestino;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
            enderecoDestino = EnderecoResourceIT.createEntity(em);
        } else {
            enderecoDestino = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(enderecoDestino);
        em.flush();
        notaFiscalColeta.addEnderecoDestino(enderecoDestino);
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
        Long enderecoDestinoId = enderecoDestino.getId();
        // Get all the notaFiscalColetaList where enderecoDestino equals to enderecoDestinoId
        defaultNotaFiscalColetaShouldBeFound("enderecoDestinoId.equals=" + enderecoDestinoId);

        // Get all the notaFiscalColetaList where enderecoDestino equals to (enderecoDestinoId + 1)
        defaultNotaFiscalColetaShouldNotBeFound("enderecoDestinoId.equals=" + (enderecoDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllNotaFiscalColetasBySolicitacaoColetaIsEqualToSomething() throws Exception {
        SolicitacaoColeta solicitacaoColeta;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
            solicitacaoColeta = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solicitacaoColeta = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solicitacaoColeta);
        em.flush();
        notaFiscalColeta.setSolicitacaoColeta(solicitacaoColeta);
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
        Long solicitacaoColetaId = solicitacaoColeta.getId();
        // Get all the notaFiscalColetaList where solicitacaoColeta equals to solicitacaoColetaId
        defaultNotaFiscalColetaShouldBeFound("solicitacaoColetaId.equals=" + solicitacaoColetaId);

        // Get all the notaFiscalColetaList where solicitacaoColeta equals to (solicitacaoColetaId + 1)
        defaultNotaFiscalColetaShouldNotBeFound("solicitacaoColetaId.equals=" + (solicitacaoColetaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotaFiscalColetaShouldBeFound(String filter) throws Exception {
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notaFiscalColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].remetente").value(hasItem(DEFAULT_REMETENTE)))
            .andExpect(jsonPath("$.[*].destinatario").value(hasItem(DEFAULT_DESTINATARIO)))
            .andExpect(jsonPath("$.[*].metroCubico").value(hasItem(DEFAULT_METRO_CUBICO.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].dataEmissao").value(hasItem(sameInstant(DEFAULT_DATA_EMISSAO))))
            .andExpect(jsonPath("$.[*].dataSaida").value(hasItem(sameInstant(DEFAULT_DATA_SAIDA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].pesoTotal").value(hasItem(DEFAULT_PESO_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidadeTotal").value(hasItem(DEFAULT_QUANTIDADE_TOTAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));

        // Check, that the count call also returns 1
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotaFiscalColetaShouldNotBeFound(String filter) throws Exception {
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotaFiscalColeta() throws Exception {
        // Get the notaFiscalColeta
        restNotaFiscalColetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotaFiscalColeta() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        notaFiscalColetaSearchRepository.save(notaFiscalColeta);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());

        // Update the notaFiscalColeta
        NotaFiscalColeta updatedNotaFiscalColeta = notaFiscalColetaRepository.findById(notaFiscalColeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotaFiscalColeta are not directly saved in db
        em.detach(updatedNotaFiscalColeta);
        updatedNotaFiscalColeta
            .numero(UPDATED_NUMERO)
            .serie(UPDATED_SERIE)
            .remetente(UPDATED_REMETENTE)
            .destinatario(UPDATED_DESTINATARIO)
            .metroCubico(UPDATED_METRO_CUBICO)
            .quantidade(UPDATED_QUANTIDADE)
            .peso(UPDATED_PESO)
            .dataEmissao(UPDATED_DATA_EMISSAO)
            .dataSaida(UPDATED_DATA_SAIDA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .pesoTotal(UPDATED_PESO_TOTAL)
            .quantidadeTotal(UPDATED_QUANTIDADE_TOTAL)
            .observacao(UPDATED_OBSERVACAO);
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(updatedNotaFiscalColeta);

        restNotaFiscalColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notaFiscalColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        NotaFiscalColeta testNotaFiscalColeta = notaFiscalColetaList.get(notaFiscalColetaList.size() - 1);
        assertThat(testNotaFiscalColeta.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testNotaFiscalColeta.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testNotaFiscalColeta.getRemetente()).isEqualTo(UPDATED_REMETENTE);
        assertThat(testNotaFiscalColeta.getDestinatario()).isEqualTo(UPDATED_DESTINATARIO);
        assertThat(testNotaFiscalColeta.getMetroCubico()).isEqualTo(UPDATED_METRO_CUBICO);
        assertThat(testNotaFiscalColeta.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testNotaFiscalColeta.getPeso()).isEqualTo(UPDATED_PESO);
        assertThat(testNotaFiscalColeta.getDataEmissao()).isEqualTo(UPDATED_DATA_EMISSAO);
        assertThat(testNotaFiscalColeta.getDataSaida()).isEqualTo(UPDATED_DATA_SAIDA);
        assertThat(testNotaFiscalColeta.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testNotaFiscalColeta.getPesoTotal()).isEqualTo(UPDATED_PESO_TOTAL);
        assertThat(testNotaFiscalColeta.getQuantidadeTotal()).isEqualTo(UPDATED_QUANTIDADE_TOTAL);
        assertThat(testNotaFiscalColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<NotaFiscalColeta> notaFiscalColetaSearchList = IterableUtils.toList(notaFiscalColetaSearchRepository.findAll());
                NotaFiscalColeta testNotaFiscalColetaSearch = notaFiscalColetaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testNotaFiscalColetaSearch.getNumero()).isEqualTo(UPDATED_NUMERO);
                assertThat(testNotaFiscalColetaSearch.getSerie()).isEqualTo(UPDATED_SERIE);
                assertThat(testNotaFiscalColetaSearch.getRemetente()).isEqualTo(UPDATED_REMETENTE);
                assertThat(testNotaFiscalColetaSearch.getDestinatario()).isEqualTo(UPDATED_DESTINATARIO);
                assertThat(testNotaFiscalColetaSearch.getMetroCubico()).isEqualTo(UPDATED_METRO_CUBICO);
                assertThat(testNotaFiscalColetaSearch.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
                assertThat(testNotaFiscalColetaSearch.getPeso()).isEqualTo(UPDATED_PESO);
                assertThat(testNotaFiscalColetaSearch.getDataEmissao()).isEqualTo(UPDATED_DATA_EMISSAO);
                assertThat(testNotaFiscalColetaSearch.getDataSaida()).isEqualTo(UPDATED_DATA_SAIDA);
                assertThat(testNotaFiscalColetaSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testNotaFiscalColetaSearch.getPesoTotal()).isEqualTo(UPDATED_PESO_TOTAL);
                assertThat(testNotaFiscalColetaSearch.getQuantidadeTotal()).isEqualTo(UPDATED_QUANTIDADE_TOTAL);
                assertThat(testNotaFiscalColetaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        notaFiscalColeta.setId(longCount.incrementAndGet());

        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaFiscalColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notaFiscalColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        notaFiscalColeta.setId(longCount.incrementAndGet());

        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaFiscalColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        notaFiscalColeta.setId(longCount.incrementAndGet());

        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaFiscalColetaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateNotaFiscalColetaWithPatch() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();

        // Update the notaFiscalColeta using partial update
        NotaFiscalColeta partialUpdatedNotaFiscalColeta = new NotaFiscalColeta();
        partialUpdatedNotaFiscalColeta.setId(notaFiscalColeta.getId());

        partialUpdatedNotaFiscalColeta
            .serie(UPDATED_SERIE)
            .destinatario(UPDATED_DESTINATARIO)
            .metroCubico(UPDATED_METRO_CUBICO)
            .valorTotal(UPDATED_VALOR_TOTAL);

        restNotaFiscalColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotaFiscalColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotaFiscalColeta))
            )
            .andExpect(status().isOk());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        NotaFiscalColeta testNotaFiscalColeta = notaFiscalColetaList.get(notaFiscalColetaList.size() - 1);
        assertThat(testNotaFiscalColeta.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testNotaFiscalColeta.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testNotaFiscalColeta.getRemetente()).isEqualTo(DEFAULT_REMETENTE);
        assertThat(testNotaFiscalColeta.getDestinatario()).isEqualTo(UPDATED_DESTINATARIO);
        assertThat(testNotaFiscalColeta.getMetroCubico()).isEqualTo(UPDATED_METRO_CUBICO);
        assertThat(testNotaFiscalColeta.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testNotaFiscalColeta.getPeso()).isEqualTo(DEFAULT_PESO);
        assertThat(testNotaFiscalColeta.getDataEmissao()).isEqualTo(DEFAULT_DATA_EMISSAO);
        assertThat(testNotaFiscalColeta.getDataSaida()).isEqualTo(DEFAULT_DATA_SAIDA);
        assertThat(testNotaFiscalColeta.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testNotaFiscalColeta.getPesoTotal()).isEqualTo(DEFAULT_PESO_TOTAL);
        assertThat(testNotaFiscalColeta.getQuantidadeTotal()).isEqualTo(DEFAULT_QUANTIDADE_TOTAL);
        assertThat(testNotaFiscalColeta.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void fullUpdateNotaFiscalColetaWithPatch() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);

        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();

        // Update the notaFiscalColeta using partial update
        NotaFiscalColeta partialUpdatedNotaFiscalColeta = new NotaFiscalColeta();
        partialUpdatedNotaFiscalColeta.setId(notaFiscalColeta.getId());

        partialUpdatedNotaFiscalColeta
            .numero(UPDATED_NUMERO)
            .serie(UPDATED_SERIE)
            .remetente(UPDATED_REMETENTE)
            .destinatario(UPDATED_DESTINATARIO)
            .metroCubico(UPDATED_METRO_CUBICO)
            .quantidade(UPDATED_QUANTIDADE)
            .peso(UPDATED_PESO)
            .dataEmissao(UPDATED_DATA_EMISSAO)
            .dataSaida(UPDATED_DATA_SAIDA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .pesoTotal(UPDATED_PESO_TOTAL)
            .quantidadeTotal(UPDATED_QUANTIDADE_TOTAL)
            .observacao(UPDATED_OBSERVACAO);

        restNotaFiscalColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotaFiscalColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotaFiscalColeta))
            )
            .andExpect(status().isOk());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        NotaFiscalColeta testNotaFiscalColeta = notaFiscalColetaList.get(notaFiscalColetaList.size() - 1);
        assertThat(testNotaFiscalColeta.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testNotaFiscalColeta.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testNotaFiscalColeta.getRemetente()).isEqualTo(UPDATED_REMETENTE);
        assertThat(testNotaFiscalColeta.getDestinatario()).isEqualTo(UPDATED_DESTINATARIO);
        assertThat(testNotaFiscalColeta.getMetroCubico()).isEqualTo(UPDATED_METRO_CUBICO);
        assertThat(testNotaFiscalColeta.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testNotaFiscalColeta.getPeso()).isEqualTo(UPDATED_PESO);
        assertThat(testNotaFiscalColeta.getDataEmissao()).isEqualTo(UPDATED_DATA_EMISSAO);
        assertThat(testNotaFiscalColeta.getDataSaida()).isEqualTo(UPDATED_DATA_SAIDA);
        assertThat(testNotaFiscalColeta.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testNotaFiscalColeta.getPesoTotal()).isEqualTo(UPDATED_PESO_TOTAL);
        assertThat(testNotaFiscalColeta.getQuantidadeTotal()).isEqualTo(UPDATED_QUANTIDADE_TOTAL);
        assertThat(testNotaFiscalColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void patchNonExistingNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        notaFiscalColeta.setId(longCount.incrementAndGet());

        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaFiscalColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notaFiscalColetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        notaFiscalColeta.setId(longCount.incrementAndGet());

        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaFiscalColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotaFiscalColeta() throws Exception {
        int databaseSizeBeforeUpdate = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        notaFiscalColeta.setId(longCount.incrementAndGet());

        // Create the NotaFiscalColeta
        NotaFiscalColetaDTO notaFiscalColetaDTO = notaFiscalColetaMapper.toDto(notaFiscalColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaFiscalColetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notaFiscalColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotaFiscalColeta in the database
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteNotaFiscalColeta() throws Exception {
        // Initialize the database
        notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
        notaFiscalColetaRepository.save(notaFiscalColeta);
        notaFiscalColetaSearchRepository.save(notaFiscalColeta);

        int databaseSizeBeforeDelete = notaFiscalColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the notaFiscalColeta
        restNotaFiscalColetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, notaFiscalColeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotaFiscalColeta> notaFiscalColetaList = notaFiscalColetaRepository.findAll();
        assertThat(notaFiscalColetaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notaFiscalColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchNotaFiscalColeta() throws Exception {
        // Initialize the database
        notaFiscalColeta = notaFiscalColetaRepository.saveAndFlush(notaFiscalColeta);
        notaFiscalColetaSearchRepository.save(notaFiscalColeta);

        // Search the notaFiscalColeta
        restNotaFiscalColetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + notaFiscalColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notaFiscalColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].remetente").value(hasItem(DEFAULT_REMETENTE)))
            .andExpect(jsonPath("$.[*].destinatario").value(hasItem(DEFAULT_DESTINATARIO)))
            .andExpect(jsonPath("$.[*].metroCubico").value(hasItem(DEFAULT_METRO_CUBICO.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].dataEmissao").value(hasItem(sameInstant(DEFAULT_DATA_EMISSAO))))
            .andExpect(jsonPath("$.[*].dataSaida").value(hasItem(sameInstant(DEFAULT_DATA_SAIDA))))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].pesoTotal").value(hasItem(DEFAULT_PESO_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidadeTotal").value(hasItem(DEFAULT_QUANTIDADE_TOTAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }
}
