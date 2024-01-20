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
import br.com.revenuebrasil.newcargas.repository.ContratacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContratacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContratacaoMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link ContratacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratacaoResourceIT {

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    private static final Integer DEFAULT_VALIDADE_EM_DIAS = 1;
    private static final Integer UPDATED_VALIDADE_EM_DIAS = 2;

    private static final LocalDate DEFAULT_DATA_VALIDADE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VALIDADE = LocalDate.now(ZoneId.systemDefault());

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
        assertThat(testContratacao.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testContratacao.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testContratacao.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testContratacao.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testContratacao.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testContratacao.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testContratacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testContratacao.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testContratacao.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contratacaoSearchRepository.findAll());
        // set the field null
        contratacao.setDataCadastro(null);

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
        assertThat(testContratacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testContratacao.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testContratacao.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testContratacao.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testContratacao.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testContratacao.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testContratacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testContratacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testContratacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
                assertThat(testContratacaoSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testContratacaoSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testContratacaoSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testContratacaoSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
                assertThat(testContratacaoSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testContratacaoSearch.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
                assertThat(testContratacaoSearch.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
                assertThat(testContratacaoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testContratacaoSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testContratacaoSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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

        partialUpdatedContratacao
            .valorTotal(UPDATED_VALOR_TOTAL)
            .dataValidade(UPDATED_DATA_VALIDADE)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .dataRemocao(UPDATED_DATA_REMOCAO);

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
        assertThat(testContratacao.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testContratacao.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testContratacao.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testContratacao.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testContratacao.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testContratacao.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testContratacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testContratacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testContratacao.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
        assertThat(testContratacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testContratacao.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testContratacao.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testContratacao.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testContratacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testContratacao.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testContratacao.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testContratacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testContratacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testContratacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
