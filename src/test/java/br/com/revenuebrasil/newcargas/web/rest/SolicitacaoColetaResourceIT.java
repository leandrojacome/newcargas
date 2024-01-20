package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
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

    private static final Boolean DEFAULT_ENTREGUE = false;
    private static final Boolean UPDATED_ENTREGUE = true;

    private static final ZonedDateTime DEFAULT_DATA_HORA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO)
            .cancelado(DEFAULT_CANCELADO)
            .dataCancelamento(DEFAULT_DATA_CANCELAMENTO)
            .usuarioCancelamento(DEFAULT_USUARIO_CANCELAMENTO)
            .removido(DEFAULT_REMOVIDO)
            .dataRemocao(DEFAULT_DATA_REMOCAO)
            .usuarioRemocao(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testSolicitacaoColeta.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testSolicitacaoColeta.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testSolicitacaoColeta.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testSolicitacaoColeta.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testSolicitacaoColeta.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(solicitacaoColetaSearchRepository.findAll());
        // set the field null
        solicitacaoColeta.setDataCadastro(null);

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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCancelamento").value(hasItem(sameInstant(DEFAULT_DATA_CANCELAMENTO))))
            .andExpect(jsonPath("$.[*].usuarioCancelamento").value(hasItem(DEFAULT_USUARIO_CANCELAMENTO)))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
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
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.dataCancelamento").value(sameInstant(DEFAULT_DATA_CANCELAMENTO)))
            .andExpect(jsonPath("$.usuarioCancelamento").value(DEFAULT_USUARIO_CANCELAMENTO))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()))
            .andExpect(jsonPath("$.dataRemocao").value(sameInstant(DEFAULT_DATA_REMOCAO)))
            .andExpect(jsonPath("$.usuarioRemocao").value(DEFAULT_USUARIO_REMOCAO));
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testSolicitacaoColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testSolicitacaoColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testSolicitacaoColeta.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testSolicitacaoColeta.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testSolicitacaoColeta.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
                assertThat(testSolicitacaoColetaSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testSolicitacaoColetaSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testSolicitacaoColetaSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testSolicitacaoColetaSearch.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
                assertThat(testSolicitacaoColetaSearch.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
                assertThat(testSolicitacaoColetaSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testSolicitacaoColetaSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testSolicitacaoColetaSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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

        partialUpdatedSolicitacaoColeta
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .observacao(UPDATED_OBSERVACAO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
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
        assertThat(testSolicitacaoColeta.getColetado()).isEqualTo(DEFAULT_COLETADO);
        assertThat(testSolicitacaoColeta.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
        assertThat(testSolicitacaoColeta.getEntregue()).isEqualTo(DEFAULT_ENTREGUE);
        assertThat(testSolicitacaoColeta.getDataHoraEntrega()).isEqualTo(DEFAULT_DATA_HORA_ENTREGA);
        assertThat(testSolicitacaoColeta.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testSolicitacaoColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testSolicitacaoColeta.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testSolicitacaoColeta.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testSolicitacaoColeta.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testSolicitacaoColeta.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testSolicitacaoColeta.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);

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
        assertThat(testSolicitacaoColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testSolicitacaoColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testSolicitacaoColeta.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testSolicitacaoColeta.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testSolicitacaoColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testSolicitacaoColeta.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testSolicitacaoColeta.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCancelamento").value(hasItem(sameInstant(DEFAULT_DATA_CANCELAMENTO))))
            .andExpect(jsonPath("$.[*].usuarioCancelamento").value(hasItem(DEFAULT_USUARIO_CANCELAMENTO)))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
    }
}
