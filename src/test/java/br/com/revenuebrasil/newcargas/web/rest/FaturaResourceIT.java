package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Fatura;
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

    private static final ZonedDateTime DEFAULT_DATA_VENCIMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_VENCIMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_PAGAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_PAGAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_NUMERO_PARCELA = 1;
    private static final Integer UPDATED_NUMERO_PARCELA = 2;

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_CADASTRO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CADASTRO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_ATUALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_ATUALIZACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELADO = false;
    private static final Boolean UPDATED_CANCELADO = true;

    private static final ZonedDateTime DEFAULT_DATA_CANCELAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CANCELAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_CANCELAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CANCELAMENTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final ZonedDateTime DEFAULT_DATA_REMOCAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_REMOCAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_REMOCAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_REMOCAO = "BBBBBBBBBB";

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
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .usuarioCadastro(DEFAULT_USUARIO_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO)
            .usuarioAtualizacao(DEFAULT_USUARIO_ATUALIZACAO)
            .cancelado(DEFAULT_CANCELADO)
            .dataCancelamento(DEFAULT_DATA_CANCELAMENTO)
            .usuarioCancelamento(DEFAULT_USUARIO_CANCELAMENTO)
            .removido(DEFAULT_REMOVIDO)
            .dataRemocao(DEFAULT_DATA_REMOCAO)
            .usuarioRemocao(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testFatura.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testFatura.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testFatura.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testFatura.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testFatura.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testFatura.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testFatura.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testFatura.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testFatura.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = faturaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(faturaSearchRepository.findAll());
        // set the field null
        fatura.setDataCadastro(null);

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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCancelamento").value(hasItem(sameInstant(DEFAULT_DATA_CANCELAMENTO))))
            .andExpect(jsonPath("$.[*].usuarioCancelamento").value(hasItem(DEFAULT_USUARIO_CANCELAMENTO)))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
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
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.usuarioCadastro").value(DEFAULT_USUARIO_CADASTRO))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)))
            .andExpect(jsonPath("$.usuarioAtualizacao").value(DEFAULT_USUARIO_ATUALIZACAO))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.dataCancelamento").value(sameInstant(DEFAULT_DATA_CANCELAMENTO)))
            .andExpect(jsonPath("$.usuarioCancelamento").value(DEFAULT_USUARIO_CANCELAMENTO))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()))
            .andExpect(jsonPath("$.dataRemocao").value(sameInstant(DEFAULT_DATA_REMOCAO)))
            .andExpect(jsonPath("$.usuarioRemocao").value(DEFAULT_USUARIO_REMOCAO));
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testFatura.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testFatura.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testFatura.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testFatura.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testFatura.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testFatura.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testFatura.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testFatura.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testFatura.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
                assertThat(testFaturaSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testFaturaSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testFaturaSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testFaturaSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
                assertThat(testFaturaSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testFaturaSearch.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
                assertThat(testFaturaSearch.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
                assertThat(testFaturaSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testFaturaSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testFaturaSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .observacao(UPDATED_OBSERVACAO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .dataRemocao(UPDATED_DATA_REMOCAO);

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
        assertThat(testFatura.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testFatura.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testFatura.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testFatura.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testFatura.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testFatura.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testFatura.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testFatura.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testFatura.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);

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
        assertThat(testFatura.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testFatura.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testFatura.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testFatura.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testFatura.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testFatura.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testFatura.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testFatura.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testFatura.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testFatura.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCancelamento").value(hasItem(sameInstant(DEFAULT_DATA_CANCELAMENTO))))
            .andExpect(jsonPath("$.[*].usuarioCancelamento").value(hasItem(DEFAULT_USUARIO_CANCELAMENTO)))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
    }
}
