package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
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

    private static final Double DEFAULT_QUANTIDADE = 1D;
    private static final Double UPDATED_QUANTIDADE = 2D;

    private static final Double DEFAULT_PESO = 1D;
    private static final Double UPDATED_PESO = 2D;

    private static final ZonedDateTime DEFAULT_DATA_EMISSAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_EMISSAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_SAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_SAIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    private static final Double DEFAULT_PESO_TOTAL = 1D;
    private static final Double UPDATED_PESO_TOTAL = 2D;

    private static final Integer DEFAULT_QUANTIDADE_TOTAL = 1;
    private static final Integer UPDATED_QUANTIDADE_TOTAL = 2;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
            .observacao(DEFAULT_OBSERVACAO)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO);
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
            .observacao(UPDATED_OBSERVACAO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);
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
        assertThat(testNotaFiscalColeta.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testNotaFiscalColeta.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))));
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
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)));
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
            .observacao(UPDATED_OBSERVACAO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);
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
        assertThat(testNotaFiscalColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testNotaFiscalColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
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
                assertThat(testNotaFiscalColetaSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testNotaFiscalColetaSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
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
            .valorTotal(UPDATED_VALOR_TOTAL)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);

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
        assertThat(testNotaFiscalColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testNotaFiscalColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
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
            .observacao(UPDATED_OBSERVACAO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);

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
        assertThat(testNotaFiscalColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testNotaFiscalColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))));
    }
}
