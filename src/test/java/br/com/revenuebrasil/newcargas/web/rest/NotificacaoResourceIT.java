package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.domain.enumeration.TipoNotificacao;
import br.com.revenuebrasil.newcargas.repository.NotificacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.NotificacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.NotificacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.NotificacaoMapper;
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
 * Integration tests for the {@link NotificacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificacaoResourceIT {

    private static final TipoNotificacao DEFAULT_TIPO = TipoNotificacao.EMBARCADOR;
    private static final TipoNotificacao UPDATED_TIPO = TipoNotificacao.TRANSPORTADORA;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSUNTO = "AAAAAAAAAA";
    private static final String UPDATED_ASSUNTO = "BBBBBBBBBB";

    private static final String DEFAULT_MENSAGEM = "AAAAAAAAAA";
    private static final String UPDATED_MENSAGEM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_HORA_ENVIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_ENVIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_HORA_LEITURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_LEITURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_LIDO = false;
    private static final Boolean UPDATED_LIDO = true;

    private static final ZonedDateTime DEFAULT_DATA_LEITURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_LEITURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final ZonedDateTime DEFAULT_DATA_REMOCAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_REMOCAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_REMOCAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_REMOCAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/notificacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/notificacaos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private NotificacaoMapper notificacaoMapper;

    @Autowired
    private NotificacaoSearchRepository notificacaoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificacaoMockMvc;

    private Notificacao notificacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacao createEntity(EntityManager em) {
        Notificacao notificacao = new Notificacao()
            .tipo(DEFAULT_TIPO)
            .email(DEFAULT_EMAIL)
            .telefone(DEFAULT_TELEFONE)
            .assunto(DEFAULT_ASSUNTO)
            .mensagem(DEFAULT_MENSAGEM)
            .dataHoraEnvio(DEFAULT_DATA_HORA_ENVIO)
            .dataHoraLeitura(DEFAULT_DATA_HORA_LEITURA)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO)
            .lido(DEFAULT_LIDO)
            .dataLeitura(DEFAULT_DATA_LEITURA)
            .removido(DEFAULT_REMOVIDO)
            .dataRemocao(DEFAULT_DATA_REMOCAO)
            .usuarioRemocao(DEFAULT_USUARIO_REMOCAO);
        return notificacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacao createUpdatedEntity(EntityManager em) {
        Notificacao notificacao = new Notificacao()
            .tipo(UPDATED_TIPO)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .assunto(UPDATED_ASSUNTO)
            .mensagem(UPDATED_MENSAGEM)
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .dataHoraLeitura(UPDATED_DATA_HORA_LEITURA)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .lido(UPDATED_LIDO)
            .dataLeitura(UPDATED_DATA_LEITURA)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
        return notificacao;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        notificacaoSearchRepository.deleteAll();
        assertThat(notificacaoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        notificacao = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificacao() throws Exception {
        int databaseSizeBeforeCreate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);
        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Notificacao testNotificacao = notificacaoList.get(notificacaoList.size() - 1);
        assertThat(testNotificacao.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testNotificacao.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNotificacao.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testNotificacao.getAssunto()).isEqualTo(DEFAULT_ASSUNTO);
        assertThat(testNotificacao.getMensagem()).isEqualTo(DEFAULT_MENSAGEM);
        assertThat(testNotificacao.getDataHoraEnvio()).isEqualTo(DEFAULT_DATA_HORA_ENVIO);
        assertThat(testNotificacao.getDataHoraLeitura()).isEqualTo(DEFAULT_DATA_HORA_LEITURA);
        assertThat(testNotificacao.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testNotificacao.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testNotificacao.getLido()).isEqualTo(DEFAULT_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(DEFAULT_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testNotificacao.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testNotificacao.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
    }

    @Test
    @Transactional
    void createNotificacaoWithExistingId() throws Exception {
        // Create the Notificacao with an existing ID
        notificacao.setId(1L);
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        int databaseSizeBeforeCreate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        // set the field null
        notificacao.setTipo(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAssuntoIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        // set the field null
        notificacao.setAssunto(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMensagemIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        // set the field null
        notificacao.setMensagem(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataHoraEnvioIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        // set the field null
        notificacao.setDataHoraEnvio(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        // set the field null
        notificacao.setDataCadastro(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllNotificacaos() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].assunto").value(hasItem(DEFAULT_ASSUNTO)))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM)))
            .andExpect(jsonPath("$.[*].dataHoraEnvio").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENVIO))))
            .andExpect(jsonPath("$.[*].dataHoraLeitura").value(hasItem(sameInstant(DEFAULT_DATA_HORA_LEITURA))))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].lido").value(hasItem(DEFAULT_LIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataLeitura").value(hasItem(sameInstant(DEFAULT_DATA_LEITURA))))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
    }

    @Test
    @Transactional
    void getNotificacao() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get the notificacao
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, notificacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacao.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.assunto").value(DEFAULT_ASSUNTO))
            .andExpect(jsonPath("$.mensagem").value(DEFAULT_MENSAGEM))
            .andExpect(jsonPath("$.dataHoraEnvio").value(sameInstant(DEFAULT_DATA_HORA_ENVIO)))
            .andExpect(jsonPath("$.dataHoraLeitura").value(sameInstant(DEFAULT_DATA_HORA_LEITURA)))
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)))
            .andExpect(jsonPath("$.lido").value(DEFAULT_LIDO.booleanValue()))
            .andExpect(jsonPath("$.dataLeitura").value(sameInstant(DEFAULT_DATA_LEITURA)))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()))
            .andExpect(jsonPath("$.dataRemocao").value(sameInstant(DEFAULT_DATA_REMOCAO)))
            .andExpect(jsonPath("$.usuarioRemocao").value(DEFAULT_USUARIO_REMOCAO));
    }

    @Test
    @Transactional
    void getNonExistingNotificacao() throws Exception {
        // Get the notificacao
        restNotificacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificacao() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        notificacaoSearchRepository.save(notificacao);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());

        // Update the notificacao
        Notificacao updatedNotificacao = notificacaoRepository.findById(notificacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificacao are not directly saved in db
        em.detach(updatedNotificacao);
        updatedNotificacao
            .tipo(UPDATED_TIPO)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .assunto(UPDATED_ASSUNTO)
            .mensagem(UPDATED_MENSAGEM)
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .dataHoraLeitura(UPDATED_DATA_HORA_LEITURA)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .lido(UPDATED_LIDO)
            .dataLeitura(UPDATED_DATA_LEITURA)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(updatedNotificacao);

        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        Notificacao testNotificacao = notificacaoList.get(notificacaoList.size() - 1);
        assertThat(testNotificacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testNotificacao.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNotificacao.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testNotificacao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testNotificacao.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
        assertThat(testNotificacao.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testNotificacao.getDataHoraLeitura()).isEqualTo(UPDATED_DATA_HORA_LEITURA);
        assertThat(testNotificacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testNotificacao.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testNotificacao.getLido()).isEqualTo(UPDATED_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(UPDATED_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testNotificacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testNotificacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Notificacao> notificacaoSearchList = IterableUtils.toList(notificacaoSearchRepository.findAll());
                Notificacao testNotificacaoSearch = notificacaoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testNotificacaoSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testNotificacaoSearch.getEmail()).isEqualTo(UPDATED_EMAIL);
                assertThat(testNotificacaoSearch.getTelefone()).isEqualTo(UPDATED_TELEFONE);
                assertThat(testNotificacaoSearch.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
                assertThat(testNotificacaoSearch.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
                assertThat(testNotificacaoSearch.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
                assertThat(testNotificacaoSearch.getDataHoraLeitura()).isEqualTo(UPDATED_DATA_HORA_LEITURA);
                assertThat(testNotificacaoSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testNotificacaoSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testNotificacaoSearch.getLido()).isEqualTo(UPDATED_LIDO);
                assertThat(testNotificacaoSearch.getDataLeitura()).isEqualTo(UPDATED_DATA_LEITURA);
                assertThat(testNotificacaoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testNotificacaoSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testNotificacaoSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingNotificacao() throws Exception {
        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificacao() throws Exception {
        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificacao() throws Exception {
        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateNotificacaoWithPatch() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();

        // Update the notificacao using partial update
        Notificacao partialUpdatedNotificacao = new Notificacao();
        partialUpdatedNotificacao.setId(notificacao.getId());

        partialUpdatedNotificacao
            .telefone(UPDATED_TELEFONE)
            .assunto(UPDATED_ASSUNTO)
            .mensagem(UPDATED_MENSAGEM)
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .lido(UPDATED_LIDO)
            .removido(UPDATED_REMOVIDO);

        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacao))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        Notificacao testNotificacao = notificacaoList.get(notificacaoList.size() - 1);
        assertThat(testNotificacao.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testNotificacao.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNotificacao.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testNotificacao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testNotificacao.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
        assertThat(testNotificacao.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testNotificacao.getDataHoraLeitura()).isEqualTo(DEFAULT_DATA_HORA_LEITURA);
        assertThat(testNotificacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testNotificacao.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testNotificacao.getLido()).isEqualTo(UPDATED_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(DEFAULT_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testNotificacao.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testNotificacao.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
    }

    @Test
    @Transactional
    void fullUpdateNotificacaoWithPatch() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();

        // Update the notificacao using partial update
        Notificacao partialUpdatedNotificacao = new Notificacao();
        partialUpdatedNotificacao.setId(notificacao.getId());

        partialUpdatedNotificacao
            .tipo(UPDATED_TIPO)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .assunto(UPDATED_ASSUNTO)
            .mensagem(UPDATED_MENSAGEM)
            .dataHoraEnvio(UPDATED_DATA_HORA_ENVIO)
            .dataHoraLeitura(UPDATED_DATA_HORA_LEITURA)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .lido(UPDATED_LIDO)
            .dataLeitura(UPDATED_DATA_LEITURA)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);

        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacao))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        Notificacao testNotificacao = notificacaoList.get(notificacaoList.size() - 1);
        assertThat(testNotificacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testNotificacao.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNotificacao.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testNotificacao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testNotificacao.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
        assertThat(testNotificacao.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testNotificacao.getDataHoraLeitura()).isEqualTo(UPDATED_DATA_HORA_LEITURA);
        assertThat(testNotificacao.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testNotificacao.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testNotificacao.getLido()).isEqualTo(UPDATED_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(UPDATED_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testNotificacao.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testNotificacao.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
    }

    @Test
    @Transactional
    void patchNonExistingNotificacao() throws Exception {
        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificacao() throws Exception {
        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificacao() throws Exception {
        int databaseSizeBeforeUpdate = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(notificacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacao in the database
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteNotificacao() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);
        notificacaoRepository.save(notificacao);
        notificacaoSearchRepository.save(notificacao);

        int databaseSizeBeforeDelete = notificacaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the notificacao
        restNotificacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notificacao> notificacaoList = notificacaoRepository.findAll();
        assertThat(notificacaoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificacaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchNotificacao() throws Exception {
        // Initialize the database
        notificacao = notificacaoRepository.saveAndFlush(notificacao);
        notificacaoSearchRepository.save(notificacao);

        // Search the notificacao
        restNotificacaoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + notificacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].assunto").value(hasItem(DEFAULT_ASSUNTO)))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM)))
            .andExpect(jsonPath("$.[*].dataHoraEnvio").value(hasItem(sameInstant(DEFAULT_DATA_HORA_ENVIO))))
            .andExpect(jsonPath("$.[*].dataHoraLeitura").value(hasItem(sameInstant(DEFAULT_DATA_HORA_LEITURA))))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].lido").value(hasItem(DEFAULT_LIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataLeitura").value(hasItem(sameInstant(DEFAULT_DATA_LEITURA))))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
    }
}
