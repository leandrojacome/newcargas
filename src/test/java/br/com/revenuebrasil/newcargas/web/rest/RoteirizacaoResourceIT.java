package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
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

    private static final ZonedDateTime DEFAULT_DATA_HORA_ULTIMA_COLETA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ULTIMA_COLETA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_HORA_PRIMEIRA_ENTREGA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATA_HORA_PRIMEIRA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_HORA_ULTIMA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ULTIMA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
        assertThat(testRoteirizacao.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testRoteirizacao.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testRoteirizacao.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testRoteirizacao.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testRoteirizacao.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testRoteirizacao.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testRoteirizacao.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testRoteirizacao.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = roteirizacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roteirizacaoSearchRepository.findAll());
        // set the field null
        roteirizacao.setDataCadastro(null);

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
        assertThat(testRoteirizacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testRoteirizacao.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testRoteirizacao.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testRoteirizacao.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testRoteirizacao.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testRoteirizacao.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testRoteirizacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testRoteirizacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
                assertThat(testRoteirizacaoSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testRoteirizacaoSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testRoteirizacaoSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testRoteirizacaoSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
                assertThat(testRoteirizacaoSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testRoteirizacaoSearch.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
                assertThat(testRoteirizacaoSearch.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
                assertThat(testRoteirizacaoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testRoteirizacaoSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testRoteirizacaoSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);

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
        assertThat(testRoteirizacao.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testRoteirizacao.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testRoteirizacao.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testRoteirizacao.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testRoteirizacao.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testRoteirizacao.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testRoteirizacao.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testRoteirizacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testRoteirizacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testRoteirizacao.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testRoteirizacao.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testRoteirizacao.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testRoteirizacao.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testRoteirizacao.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testRoteirizacao.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testRoteirizacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testRoteirizacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testRoteirizacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
