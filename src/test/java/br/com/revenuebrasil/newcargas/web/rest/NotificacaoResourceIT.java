package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
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
    private static final ZonedDateTime SMALLER_DATA_HORA_ENVIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_HORA_LEITURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA_LEITURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA_LEITURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_LIDO = false;
    private static final Boolean UPDATED_LIDO = true;

    private static final ZonedDateTime DEFAULT_DATA_LEITURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_LEITURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_LEITURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

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
            .lido(DEFAULT_LIDO)
            .dataLeitura(DEFAULT_DATA_LEITURA)
            .removido(DEFAULT_REMOVIDO);
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
            .lido(UPDATED_LIDO)
            .dataLeitura(UPDATED_DATA_LEITURA)
            .removido(UPDATED_REMOVIDO);
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
        assertThat(testNotificacao.getLido()).isEqualTo(DEFAULT_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(DEFAULT_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
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
            .andExpect(jsonPath("$.[*].lido").value(hasItem(DEFAULT_LIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataLeitura").value(hasItem(sameInstant(DEFAULT_DATA_LEITURA))))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
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
            .andExpect(jsonPath("$.lido").value(DEFAULT_LIDO.booleanValue()))
            .andExpect(jsonPath("$.dataLeitura").value(sameInstant(DEFAULT_DATA_LEITURA)))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getNotificacaosByIdFiltering() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        Long id = notificacao.getId();

        defaultNotificacaoShouldBeFound("id.equals=" + id);
        defaultNotificacaoShouldNotBeFound("id.notEquals=" + id);

        defaultNotificacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where tipo equals to DEFAULT_TIPO
        defaultNotificacaoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the notificacaoList where tipo equals to UPDATED_TIPO
        defaultNotificacaoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultNotificacaoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the notificacaoList where tipo equals to UPDATED_TIPO
        defaultNotificacaoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where tipo is not null
        defaultNotificacaoShouldBeFound("tipo.specified=true");

        // Get all the notificacaoList where tipo is null
        defaultNotificacaoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where email equals to DEFAULT_EMAIL
        defaultNotificacaoShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the notificacaoList where email equals to UPDATED_EMAIL
        defaultNotificacaoShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNotificacaosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultNotificacaoShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the notificacaoList where email equals to UPDATED_EMAIL
        defaultNotificacaoShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNotificacaosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where email is not null
        defaultNotificacaoShouldBeFound("email.specified=true");

        // Get all the notificacaoList where email is null
        defaultNotificacaoShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByEmailContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where email contains DEFAULT_EMAIL
        defaultNotificacaoShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the notificacaoList where email contains UPDATED_EMAIL
        defaultNotificacaoShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNotificacaosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where email does not contain DEFAULT_EMAIL
        defaultNotificacaoShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the notificacaoList where email does not contain UPDATED_EMAIL
        defaultNotificacaoShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where telefone equals to DEFAULT_TELEFONE
        defaultNotificacaoShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the notificacaoList where telefone equals to UPDATED_TELEFONE
        defaultNotificacaoShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultNotificacaoShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the notificacaoList where telefone equals to UPDATED_TELEFONE
        defaultNotificacaoShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where telefone is not null
        defaultNotificacaoShouldBeFound("telefone.specified=true");

        // Get all the notificacaoList where telefone is null
        defaultNotificacaoShouldNotBeFound("telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where telefone contains DEFAULT_TELEFONE
        defaultNotificacaoShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the notificacaoList where telefone contains UPDATED_TELEFONE
        defaultNotificacaoShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where telefone does not contain DEFAULT_TELEFONE
        defaultNotificacaoShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the notificacaoList where telefone does not contain UPDATED_TELEFONE
        defaultNotificacaoShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllNotificacaosByAssuntoIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where assunto equals to DEFAULT_ASSUNTO
        defaultNotificacaoShouldBeFound("assunto.equals=" + DEFAULT_ASSUNTO);

        // Get all the notificacaoList where assunto equals to UPDATED_ASSUNTO
        defaultNotificacaoShouldNotBeFound("assunto.equals=" + UPDATED_ASSUNTO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByAssuntoIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where assunto in DEFAULT_ASSUNTO or UPDATED_ASSUNTO
        defaultNotificacaoShouldBeFound("assunto.in=" + DEFAULT_ASSUNTO + "," + UPDATED_ASSUNTO);

        // Get all the notificacaoList where assunto equals to UPDATED_ASSUNTO
        defaultNotificacaoShouldNotBeFound("assunto.in=" + UPDATED_ASSUNTO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByAssuntoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where assunto is not null
        defaultNotificacaoShouldBeFound("assunto.specified=true");

        // Get all the notificacaoList where assunto is null
        defaultNotificacaoShouldNotBeFound("assunto.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByAssuntoContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where assunto contains DEFAULT_ASSUNTO
        defaultNotificacaoShouldBeFound("assunto.contains=" + DEFAULT_ASSUNTO);

        // Get all the notificacaoList where assunto contains UPDATED_ASSUNTO
        defaultNotificacaoShouldNotBeFound("assunto.contains=" + UPDATED_ASSUNTO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByAssuntoNotContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where assunto does not contain DEFAULT_ASSUNTO
        defaultNotificacaoShouldNotBeFound("assunto.doesNotContain=" + DEFAULT_ASSUNTO);

        // Get all the notificacaoList where assunto does not contain UPDATED_ASSUNTO
        defaultNotificacaoShouldBeFound("assunto.doesNotContain=" + UPDATED_ASSUNTO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem equals to DEFAULT_MENSAGEM
        defaultNotificacaoShouldBeFound("mensagem.equals=" + DEFAULT_MENSAGEM);

        // Get all the notificacaoList where mensagem equals to UPDATED_MENSAGEM
        defaultNotificacaoShouldNotBeFound("mensagem.equals=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem in DEFAULT_MENSAGEM or UPDATED_MENSAGEM
        defaultNotificacaoShouldBeFound("mensagem.in=" + DEFAULT_MENSAGEM + "," + UPDATED_MENSAGEM);

        // Get all the notificacaoList where mensagem equals to UPDATED_MENSAGEM
        defaultNotificacaoShouldNotBeFound("mensagem.in=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem is not null
        defaultNotificacaoShouldBeFound("mensagem.specified=true");

        // Get all the notificacaoList where mensagem is null
        defaultNotificacaoShouldNotBeFound("mensagem.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem contains DEFAULT_MENSAGEM
        defaultNotificacaoShouldBeFound("mensagem.contains=" + DEFAULT_MENSAGEM);

        // Get all the notificacaoList where mensagem contains UPDATED_MENSAGEM
        defaultNotificacaoShouldNotBeFound("mensagem.contains=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemNotContainsSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem does not contain DEFAULT_MENSAGEM
        defaultNotificacaoShouldNotBeFound("mensagem.doesNotContain=" + DEFAULT_MENSAGEM);

        // Get all the notificacaoList where mensagem does not contain UPDATED_MENSAGEM
        defaultNotificacaoShouldBeFound("mensagem.doesNotContain=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio equals to DEFAULT_DATA_HORA_ENVIO
        defaultNotificacaoShouldBeFound("dataHoraEnvio.equals=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the notificacaoList where dataHoraEnvio equals to UPDATED_DATA_HORA_ENVIO
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.equals=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio in DEFAULT_DATA_HORA_ENVIO or UPDATED_DATA_HORA_ENVIO
        defaultNotificacaoShouldBeFound("dataHoraEnvio.in=" + DEFAULT_DATA_HORA_ENVIO + "," + UPDATED_DATA_HORA_ENVIO);

        // Get all the notificacaoList where dataHoraEnvio equals to UPDATED_DATA_HORA_ENVIO
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.in=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio is not null
        defaultNotificacaoShouldBeFound("dataHoraEnvio.specified=true");

        // Get all the notificacaoList where dataHoraEnvio is null
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio is greater than or equal to DEFAULT_DATA_HORA_ENVIO
        defaultNotificacaoShouldBeFound("dataHoraEnvio.greaterThanOrEqual=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the notificacaoList where dataHoraEnvio is greater than or equal to UPDATED_DATA_HORA_ENVIO
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.greaterThanOrEqual=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio is less than or equal to DEFAULT_DATA_HORA_ENVIO
        defaultNotificacaoShouldBeFound("dataHoraEnvio.lessThanOrEqual=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the notificacaoList where dataHoraEnvio is less than or equal to SMALLER_DATA_HORA_ENVIO
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.lessThanOrEqual=" + SMALLER_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsLessThanSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio is less than DEFAULT_DATA_HORA_ENVIO
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.lessThan=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the notificacaoList where dataHoraEnvio is less than UPDATED_DATA_HORA_ENVIO
        defaultNotificacaoShouldBeFound("dataHoraEnvio.lessThan=" + UPDATED_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraEnvioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraEnvio is greater than DEFAULT_DATA_HORA_ENVIO
        defaultNotificacaoShouldNotBeFound("dataHoraEnvio.greaterThan=" + DEFAULT_DATA_HORA_ENVIO);

        // Get all the notificacaoList where dataHoraEnvio is greater than SMALLER_DATA_HORA_ENVIO
        defaultNotificacaoShouldBeFound("dataHoraEnvio.greaterThan=" + SMALLER_DATA_HORA_ENVIO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura equals to DEFAULT_DATA_HORA_LEITURA
        defaultNotificacaoShouldBeFound("dataHoraLeitura.equals=" + DEFAULT_DATA_HORA_LEITURA);

        // Get all the notificacaoList where dataHoraLeitura equals to UPDATED_DATA_HORA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.equals=" + UPDATED_DATA_HORA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura in DEFAULT_DATA_HORA_LEITURA or UPDATED_DATA_HORA_LEITURA
        defaultNotificacaoShouldBeFound("dataHoraLeitura.in=" + DEFAULT_DATA_HORA_LEITURA + "," + UPDATED_DATA_HORA_LEITURA);

        // Get all the notificacaoList where dataHoraLeitura equals to UPDATED_DATA_HORA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.in=" + UPDATED_DATA_HORA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura is not null
        defaultNotificacaoShouldBeFound("dataHoraLeitura.specified=true");

        // Get all the notificacaoList where dataHoraLeitura is null
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura is greater than or equal to DEFAULT_DATA_HORA_LEITURA
        defaultNotificacaoShouldBeFound("dataHoraLeitura.greaterThanOrEqual=" + DEFAULT_DATA_HORA_LEITURA);

        // Get all the notificacaoList where dataHoraLeitura is greater than or equal to UPDATED_DATA_HORA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.greaterThanOrEqual=" + UPDATED_DATA_HORA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura is less than or equal to DEFAULT_DATA_HORA_LEITURA
        defaultNotificacaoShouldBeFound("dataHoraLeitura.lessThanOrEqual=" + DEFAULT_DATA_HORA_LEITURA);

        // Get all the notificacaoList where dataHoraLeitura is less than or equal to SMALLER_DATA_HORA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.lessThanOrEqual=" + SMALLER_DATA_HORA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsLessThanSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura is less than DEFAULT_DATA_HORA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.lessThan=" + DEFAULT_DATA_HORA_LEITURA);

        // Get all the notificacaoList where dataHoraLeitura is less than UPDATED_DATA_HORA_LEITURA
        defaultNotificacaoShouldBeFound("dataHoraLeitura.lessThan=" + UPDATED_DATA_HORA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataHoraLeituraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataHoraLeitura is greater than DEFAULT_DATA_HORA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataHoraLeitura.greaterThan=" + DEFAULT_DATA_HORA_LEITURA);

        // Get all the notificacaoList where dataHoraLeitura is greater than SMALLER_DATA_HORA_LEITURA
        defaultNotificacaoShouldBeFound("dataHoraLeitura.greaterThan=" + SMALLER_DATA_HORA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByLidoIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where lido equals to DEFAULT_LIDO
        defaultNotificacaoShouldBeFound("lido.equals=" + DEFAULT_LIDO);

        // Get all the notificacaoList where lido equals to UPDATED_LIDO
        defaultNotificacaoShouldNotBeFound("lido.equals=" + UPDATED_LIDO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByLidoIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where lido in DEFAULT_LIDO or UPDATED_LIDO
        defaultNotificacaoShouldBeFound("lido.in=" + DEFAULT_LIDO + "," + UPDATED_LIDO);

        // Get all the notificacaoList where lido equals to UPDATED_LIDO
        defaultNotificacaoShouldNotBeFound("lido.in=" + UPDATED_LIDO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByLidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where lido is not null
        defaultNotificacaoShouldBeFound("lido.specified=true");

        // Get all the notificacaoList where lido is null
        defaultNotificacaoShouldNotBeFound("lido.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura equals to DEFAULT_DATA_LEITURA
        defaultNotificacaoShouldBeFound("dataLeitura.equals=" + DEFAULT_DATA_LEITURA);

        // Get all the notificacaoList where dataLeitura equals to UPDATED_DATA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataLeitura.equals=" + UPDATED_DATA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura in DEFAULT_DATA_LEITURA or UPDATED_DATA_LEITURA
        defaultNotificacaoShouldBeFound("dataLeitura.in=" + DEFAULT_DATA_LEITURA + "," + UPDATED_DATA_LEITURA);

        // Get all the notificacaoList where dataLeitura equals to UPDATED_DATA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataLeitura.in=" + UPDATED_DATA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura is not null
        defaultNotificacaoShouldBeFound("dataLeitura.specified=true");

        // Get all the notificacaoList where dataLeitura is null
        defaultNotificacaoShouldNotBeFound("dataLeitura.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura is greater than or equal to DEFAULT_DATA_LEITURA
        defaultNotificacaoShouldBeFound("dataLeitura.greaterThanOrEqual=" + DEFAULT_DATA_LEITURA);

        // Get all the notificacaoList where dataLeitura is greater than or equal to UPDATED_DATA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataLeitura.greaterThanOrEqual=" + UPDATED_DATA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura is less than or equal to DEFAULT_DATA_LEITURA
        defaultNotificacaoShouldBeFound("dataLeitura.lessThanOrEqual=" + DEFAULT_DATA_LEITURA);

        // Get all the notificacaoList where dataLeitura is less than or equal to SMALLER_DATA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataLeitura.lessThanOrEqual=" + SMALLER_DATA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsLessThanSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura is less than DEFAULT_DATA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataLeitura.lessThan=" + DEFAULT_DATA_LEITURA);

        // Get all the notificacaoList where dataLeitura is less than UPDATED_DATA_LEITURA
        defaultNotificacaoShouldBeFound("dataLeitura.lessThan=" + UPDATED_DATA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByDataLeituraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where dataLeitura is greater than DEFAULT_DATA_LEITURA
        defaultNotificacaoShouldNotBeFound("dataLeitura.greaterThan=" + DEFAULT_DATA_LEITURA);

        // Get all the notificacaoList where dataLeitura is greater than SMALLER_DATA_LEITURA
        defaultNotificacaoShouldBeFound("dataLeitura.greaterThan=" + SMALLER_DATA_LEITURA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where removido equals to DEFAULT_REMOVIDO
        defaultNotificacaoShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the notificacaoList where removido equals to UPDATED_REMOVIDO
        defaultNotificacaoShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultNotificacaoShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the notificacaoList where removido equals to UPDATED_REMOVIDO
        defaultNotificacaoShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where removido is not null
        defaultNotificacaoShouldBeFound("removido.specified=true");

        // Get all the notificacaoList where removido is null
        defaultNotificacaoShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            notificacaoRepository.saveAndFlush(notificacao);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        notificacao.setEmbarcador(embarcador);
        notificacaoRepository.saveAndFlush(notificacao);
        Long embarcadorId = embarcador.getId();
        // Get all the notificacaoList where embarcador equals to embarcadorId
        defaultNotificacaoShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the notificacaoList where embarcador equals to (embarcadorId + 1)
        defaultNotificacaoShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllNotificacaosByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            notificacaoRepository.saveAndFlush(notificacao);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        notificacao.setTransportadora(transportadora);
        notificacaoRepository.saveAndFlush(notificacao);
        Long transportadoraId = transportadora.getId();
        // Get all the notificacaoList where transportadora equals to transportadoraId
        defaultNotificacaoShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the notificacaoList where transportadora equals to (transportadoraId + 1)
        defaultNotificacaoShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificacaoShouldBeFound(String filter) throws Exception {
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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
            .andExpect(jsonPath("$.[*].lido").value(hasItem(DEFAULT_LIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataLeitura").value(hasItem(sameInstant(DEFAULT_DATA_LEITURA))))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificacaoShouldNotBeFound(String filter) throws Exception {
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .lido(UPDATED_LIDO)
            .dataLeitura(UPDATED_DATA_LEITURA)
            .removido(UPDATED_REMOVIDO);
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
        assertThat(testNotificacao.getLido()).isEqualTo(UPDATED_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(UPDATED_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
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
                assertThat(testNotificacaoSearch.getLido()).isEqualTo(UPDATED_LIDO);
                assertThat(testNotificacaoSearch.getDataLeitura()).isEqualTo(UPDATED_DATA_LEITURA);
                assertThat(testNotificacaoSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
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
        assertThat(testNotificacao.getLido()).isEqualTo(UPDATED_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(DEFAULT_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
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
            .lido(UPDATED_LIDO)
            .dataLeitura(UPDATED_DATA_LEITURA)
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
        assertThat(testNotificacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testNotificacao.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNotificacao.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testNotificacao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testNotificacao.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
        assertThat(testNotificacao.getDataHoraEnvio()).isEqualTo(UPDATED_DATA_HORA_ENVIO);
        assertThat(testNotificacao.getDataHoraLeitura()).isEqualTo(UPDATED_DATA_HORA_LEITURA);
        assertThat(testNotificacao.getLido()).isEqualTo(UPDATED_LIDO);
        assertThat(testNotificacao.getDataLeitura()).isEqualTo(UPDATED_DATA_LEITURA);
        assertThat(testNotificacao.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
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
            .andExpect(jsonPath("$.[*].lido").value(hasItem(DEFAULT_LIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataLeitura").value(hasItem(sameInstant(DEFAULT_DATA_LEITURA))))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
