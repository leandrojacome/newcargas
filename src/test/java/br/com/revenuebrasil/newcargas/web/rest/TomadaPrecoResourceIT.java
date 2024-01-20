package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
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

    private static final Integer DEFAULT_PRAZO_RESPOSTA = 1;
    private static final Integer UPDATED_PRAZO_RESPOSTA = 2;

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

    private static final Boolean DEFAULT_APROVADO = false;
    private static final Boolean UPDATED_APROVADO = true;

    private static final ZonedDateTime DEFAULT_DATA_APROVACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_APROVACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_APROVACAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_APROVACAO = "BBBBBBBBBB";

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
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .usuarioCadastro(DEFAULT_USUARIO_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO)
            .usuarioAtualizacao(DEFAULT_USUARIO_ATUALIZACAO)
            .aprovado(DEFAULT_APROVADO)
            .dataAprovacao(DEFAULT_DATA_APROVACAO)
            .usuarioAprovacao(DEFAULT_USUARIO_APROVACAO)
            .cancelado(DEFAULT_CANCELADO)
            .dataCancelamento(DEFAULT_DATA_CANCELAMENTO)
            .usuarioCancelamento(DEFAULT_USUARIO_CANCELAMENTO)
            .removido(DEFAULT_REMOVIDO)
            .dataRemocao(DEFAULT_DATA_REMOCAO)
            .usuarioRemocao(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .aprovado(UPDATED_APROVADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .usuarioAprovacao(UPDATED_USUARIO_APROVACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testTomadaPreco.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testTomadaPreco.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testTomadaPreco.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testTomadaPreco.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(DEFAULT_APROVADO);
        assertThat(testTomadaPreco.getDataAprovacao()).isEqualTo(DEFAULT_DATA_APROVACAO);
        assertThat(testTomadaPreco.getUsuarioAprovacao()).isEqualTo(DEFAULT_USUARIO_APROVACAO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(DEFAULT_CANCELADO);
        assertThat(testTomadaPreco.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testTomadaPreco.getUsuarioCancelamento()).isEqualTo(DEFAULT_USUARIO_CANCELAMENTO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testTomadaPreco.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testTomadaPreco.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = tomadaPrecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tomadaPrecoSearchRepository.findAll());
        // set the field null
        tomadaPreco.setDataCadastro(null);

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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)))
            .andExpect(jsonPath("$.[*].aprovado").value(hasItem(DEFAULT_APROVADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataAprovacao").value(hasItem(sameInstant(DEFAULT_DATA_APROVACAO))))
            .andExpect(jsonPath("$.[*].usuarioAprovacao").value(hasItem(DEFAULT_USUARIO_APROVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCancelamento").value(hasItem(sameInstant(DEFAULT_DATA_CANCELAMENTO))))
            .andExpect(jsonPath("$.[*].usuarioCancelamento").value(hasItem(DEFAULT_USUARIO_CANCELAMENTO)))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
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
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.usuarioCadastro").value(DEFAULT_USUARIO_CADASTRO))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)))
            .andExpect(jsonPath("$.usuarioAtualizacao").value(DEFAULT_USUARIO_ATUALIZACAO))
            .andExpect(jsonPath("$.aprovado").value(DEFAULT_APROVADO.booleanValue()))
            .andExpect(jsonPath("$.dataAprovacao").value(sameInstant(DEFAULT_DATA_APROVACAO)))
            .andExpect(jsonPath("$.usuarioAprovacao").value(DEFAULT_USUARIO_APROVACAO))
            .andExpect(jsonPath("$.cancelado").value(DEFAULT_CANCELADO.booleanValue()))
            .andExpect(jsonPath("$.dataCancelamento").value(sameInstant(DEFAULT_DATA_CANCELAMENTO)))
            .andExpect(jsonPath("$.usuarioCancelamento").value(DEFAULT_USUARIO_CANCELAMENTO))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()))
            .andExpect(jsonPath("$.dataRemocao").value(sameInstant(DEFAULT_DATA_REMOCAO)))
            .andExpect(jsonPath("$.usuarioRemocao").value(DEFAULT_USUARIO_REMOCAO));
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .aprovado(UPDATED_APROVADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .usuarioAprovacao(UPDATED_USUARIO_APROVACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testTomadaPreco.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testTomadaPreco.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testTomadaPreco.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testTomadaPreco.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(UPDATED_APROVADO);
        assertThat(testTomadaPreco.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
        assertThat(testTomadaPreco.getUsuarioAprovacao()).isEqualTo(UPDATED_USUARIO_APROVACAO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testTomadaPreco.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testTomadaPreco.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testTomadaPreco.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testTomadaPreco.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
                assertThat(testTomadaPrecoSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testTomadaPrecoSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testTomadaPrecoSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testTomadaPrecoSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
                assertThat(testTomadaPrecoSearch.getAprovado()).isEqualTo(UPDATED_APROVADO);
                assertThat(testTomadaPrecoSearch.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
                assertThat(testTomadaPrecoSearch.getUsuarioAprovacao()).isEqualTo(UPDATED_USUARIO_APROVACAO);
                assertThat(testTomadaPrecoSearch.getCancelado()).isEqualTo(UPDATED_CANCELADO);
                assertThat(testTomadaPrecoSearch.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
                assertThat(testTomadaPrecoSearch.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
                assertThat(testTomadaPrecoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testTomadaPrecoSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testTomadaPrecoSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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

        partialUpdatedTomadaPreco
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .aprovado(UPDATED_APROVADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .usuarioAprovacao(UPDATED_USUARIO_APROVACAO)
            .cancelado(UPDATED_CANCELADO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO);

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
        assertThat(testTomadaPreco.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testTomadaPreco.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testTomadaPreco.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testTomadaPreco.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(UPDATED_APROVADO);
        assertThat(testTomadaPreco.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
        assertThat(testTomadaPreco.getUsuarioAprovacao()).isEqualTo(UPDATED_USUARIO_APROVACAO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testTomadaPreco.getDataCancelamento()).isEqualTo(DEFAULT_DATA_CANCELAMENTO);
        assertThat(testTomadaPreco.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testTomadaPreco.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testTomadaPreco.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .aprovado(UPDATED_APROVADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .usuarioAprovacao(UPDATED_USUARIO_APROVACAO)
            .cancelado(UPDATED_CANCELADO)
            .dataCancelamento(UPDATED_DATA_CANCELAMENTO)
            .usuarioCancelamento(UPDATED_USUARIO_CANCELAMENTO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);

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
        assertThat(testTomadaPreco.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testTomadaPreco.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testTomadaPreco.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testTomadaPreco.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testTomadaPreco.getAprovado()).isEqualTo(UPDATED_APROVADO);
        assertThat(testTomadaPreco.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
        assertThat(testTomadaPreco.getUsuarioAprovacao()).isEqualTo(UPDATED_USUARIO_APROVACAO);
        assertThat(testTomadaPreco.getCancelado()).isEqualTo(UPDATED_CANCELADO);
        assertThat(testTomadaPreco.getDataCancelamento()).isEqualTo(UPDATED_DATA_CANCELAMENTO);
        assertThat(testTomadaPreco.getUsuarioCancelamento()).isEqualTo(UPDATED_USUARIO_CANCELAMENTO);
        assertThat(testTomadaPreco.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testTomadaPreco.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testTomadaPreco.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)))
            .andExpect(jsonPath("$.[*].aprovado").value(hasItem(DEFAULT_APROVADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataAprovacao").value(hasItem(sameInstant(DEFAULT_DATA_APROVACAO))))
            .andExpect(jsonPath("$.[*].usuarioAprovacao").value(hasItem(DEFAULT_USUARIO_APROVACAO)))
            .andExpect(jsonPath("$.[*].cancelado").value(hasItem(DEFAULT_CANCELADO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCancelamento").value(hasItem(sameInstant(DEFAULT_DATA_CANCELAMENTO))))
            .andExpect(jsonPath("$.[*].usuarioCancelamento").value(hasItem(DEFAULT_USUARIO_CANCELAMENTO)))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
    }
}
