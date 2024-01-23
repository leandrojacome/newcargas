package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.repository.StatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.StatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.StatusColetaService;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.StatusColetaMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StatusColetaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatusColetaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COR = "AAAAAAAA";
    private static final String UPDATED_COR = "BBBBBBBB";

    private static final Integer DEFAULT_ORDEM = 1;
    private static final Integer UPDATED_ORDEM = 2;
    private static final Integer SMALLER_ORDEM = 1 - 1;

    private static final Boolean DEFAULT_ESTADO_INICIAL = false;
    private static final Boolean UPDATED_ESTADO_INICIAL = true;

    private static final Boolean DEFAULT_ESTADO_FINAL = false;
    private static final Boolean UPDATED_ESTADO_FINAL = true;

    private static final Boolean DEFAULT_PERMITE_CANCELAR = false;
    private static final Boolean UPDATED_PERMITE_CANCELAR = true;

    private static final Boolean DEFAULT_PERMITE_EDITAR = false;
    private static final Boolean UPDATED_PERMITE_EDITAR = true;

    private static final Boolean DEFAULT_PERMITE_EXCLUIR = false;
    private static final Boolean UPDATED_PERMITE_EXCLUIR = true;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final String ENTITY_API_URL = "/api/status-coletas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/status-coletas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatusColetaRepository statusColetaRepository;

    @Mock
    private StatusColetaRepository statusColetaRepositoryMock;

    @Autowired
    private StatusColetaMapper statusColetaMapper;

    @Mock
    private StatusColetaService statusColetaServiceMock;

    @Autowired
    private StatusColetaSearchRepository statusColetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusColetaMockMvc;

    private StatusColeta statusColeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusColeta createEntity(EntityManager em) {
        StatusColeta statusColeta = new StatusColeta()
            .nome(DEFAULT_NOME)
            .cor(DEFAULT_COR)
            .ordem(DEFAULT_ORDEM)
            .estadoInicial(DEFAULT_ESTADO_INICIAL)
            .estadoFinal(DEFAULT_ESTADO_FINAL)
            .permiteCancelar(DEFAULT_PERMITE_CANCELAR)
            .permiteEditar(DEFAULT_PERMITE_EDITAR)
            .permiteExcluir(DEFAULT_PERMITE_EXCLUIR)
            .descricao(DEFAULT_DESCRICAO)
            .ativo(DEFAULT_ATIVO)
            .removido(DEFAULT_REMOVIDO);
        return statusColeta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusColeta createUpdatedEntity(EntityManager em) {
        StatusColeta statusColeta = new StatusColeta()
            .nome(UPDATED_NOME)
            .cor(UPDATED_COR)
            .ordem(UPDATED_ORDEM)
            .estadoInicial(UPDATED_ESTADO_INICIAL)
            .estadoFinal(UPDATED_ESTADO_FINAL)
            .permiteCancelar(UPDATED_PERMITE_CANCELAR)
            .permiteEditar(UPDATED_PERMITE_EDITAR)
            .permiteExcluir(UPDATED_PERMITE_EXCLUIR)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO);
        return statusColeta;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        statusColetaSearchRepository.deleteAll();
        assertThat(statusColetaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        statusColeta = createEntity(em);
    }

    @Test
    @Transactional
    void createStatusColeta() throws Exception {
        int databaseSizeBeforeCreate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);
        restStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        StatusColeta testStatusColeta = statusColetaList.get(statusColetaList.size() - 1);
        assertThat(testStatusColeta.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testStatusColeta.getCor()).isEqualTo(DEFAULT_COR);
        assertThat(testStatusColeta.getOrdem()).isEqualTo(DEFAULT_ORDEM);
        assertThat(testStatusColeta.getEstadoInicial()).isEqualTo(DEFAULT_ESTADO_INICIAL);
        assertThat(testStatusColeta.getEstadoFinal()).isEqualTo(DEFAULT_ESTADO_FINAL);
        assertThat(testStatusColeta.getPermiteCancelar()).isEqualTo(DEFAULT_PERMITE_CANCELAR);
        assertThat(testStatusColeta.getPermiteEditar()).isEqualTo(DEFAULT_PERMITE_EDITAR);
        assertThat(testStatusColeta.getPermiteExcluir()).isEqualTo(DEFAULT_PERMITE_EXCLUIR);
        assertThat(testStatusColeta.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
    }

    @Test
    @Transactional
    void createStatusColetaWithExistingId() throws Exception {
        // Create the StatusColeta with an existing ID
        statusColeta.setId(1L);
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        int databaseSizeBeforeCreate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        // set the field null
        statusColeta.setNome(null);

        // Create the StatusColeta, which fails.
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        restStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllStatusColetas() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList
        restStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR)))
            .andExpect(jsonPath("$.[*].ordem").value(hasItem(DEFAULT_ORDEM)))
            .andExpect(jsonPath("$.[*].estadoInicial").value(hasItem(DEFAULT_ESTADO_INICIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].estadoFinal").value(hasItem(DEFAULT_ESTADO_FINAL.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteCancelar").value(hasItem(DEFAULT_PERMITE_CANCELAR.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteEditar").value(hasItem(DEFAULT_PERMITE_EDITAR.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteExcluir").value(hasItem(DEFAULT_PERMITE_EXCLUIR.booleanValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusColetasWithEagerRelationshipsIsEnabled() throws Exception {
        when(statusColetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatusColetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statusColetaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusColetasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statusColetaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatusColetaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(statusColetaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStatusColeta() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get the statusColeta
        restStatusColetaMockMvc
            .perform(get(ENTITY_API_URL_ID, statusColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusColeta.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cor").value(DEFAULT_COR))
            .andExpect(jsonPath("$.ordem").value(DEFAULT_ORDEM))
            .andExpect(jsonPath("$.estadoInicial").value(DEFAULT_ESTADO_INICIAL.booleanValue()))
            .andExpect(jsonPath("$.estadoFinal").value(DEFAULT_ESTADO_FINAL.booleanValue()))
            .andExpect(jsonPath("$.permiteCancelar").value(DEFAULT_PERMITE_CANCELAR.booleanValue()))
            .andExpect(jsonPath("$.permiteEditar").value(DEFAULT_PERMITE_EDITAR.booleanValue()))
            .andExpect(jsonPath("$.permiteExcluir").value(DEFAULT_PERMITE_EXCLUIR.booleanValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()));
    }

    @Test
    @Transactional
    void getStatusColetasByIdFiltering() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        Long id = statusColeta.getId();

        defaultStatusColetaShouldBeFound("id.equals=" + id);
        defaultStatusColetaShouldNotBeFound("id.notEquals=" + id);

        defaultStatusColetaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStatusColetaShouldNotBeFound("id.greaterThan=" + id);

        defaultStatusColetaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStatusColetaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStatusColetasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where nome equals to DEFAULT_NOME
        defaultStatusColetaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the statusColetaList where nome equals to UPDATED_NOME
        defaultStatusColetaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllStatusColetasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultStatusColetaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the statusColetaList where nome equals to UPDATED_NOME
        defaultStatusColetaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllStatusColetasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where nome is not null
        defaultStatusColetaShouldBeFound("nome.specified=true");

        // Get all the statusColetaList where nome is null
        defaultStatusColetaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByNomeContainsSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where nome contains DEFAULT_NOME
        defaultStatusColetaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the statusColetaList where nome contains UPDATED_NOME
        defaultStatusColetaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllStatusColetasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where nome does not contain DEFAULT_NOME
        defaultStatusColetaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the statusColetaList where nome does not contain UPDATED_NOME
        defaultStatusColetaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllStatusColetasByCorIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where cor equals to DEFAULT_COR
        defaultStatusColetaShouldBeFound("cor.equals=" + DEFAULT_COR);

        // Get all the statusColetaList where cor equals to UPDATED_COR
        defaultStatusColetaShouldNotBeFound("cor.equals=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByCorIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where cor in DEFAULT_COR or UPDATED_COR
        defaultStatusColetaShouldBeFound("cor.in=" + DEFAULT_COR + "," + UPDATED_COR);

        // Get all the statusColetaList where cor equals to UPDATED_COR
        defaultStatusColetaShouldNotBeFound("cor.in=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByCorIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where cor is not null
        defaultStatusColetaShouldBeFound("cor.specified=true");

        // Get all the statusColetaList where cor is null
        defaultStatusColetaShouldNotBeFound("cor.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByCorContainsSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where cor contains DEFAULT_COR
        defaultStatusColetaShouldBeFound("cor.contains=" + DEFAULT_COR);

        // Get all the statusColetaList where cor contains UPDATED_COR
        defaultStatusColetaShouldNotBeFound("cor.contains=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByCorNotContainsSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where cor does not contain DEFAULT_COR
        defaultStatusColetaShouldNotBeFound("cor.doesNotContain=" + DEFAULT_COR);

        // Get all the statusColetaList where cor does not contain UPDATED_COR
        defaultStatusColetaShouldBeFound("cor.doesNotContain=" + UPDATED_COR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem equals to DEFAULT_ORDEM
        defaultStatusColetaShouldBeFound("ordem.equals=" + DEFAULT_ORDEM);

        // Get all the statusColetaList where ordem equals to UPDATED_ORDEM
        defaultStatusColetaShouldNotBeFound("ordem.equals=" + UPDATED_ORDEM);
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem in DEFAULT_ORDEM or UPDATED_ORDEM
        defaultStatusColetaShouldBeFound("ordem.in=" + DEFAULT_ORDEM + "," + UPDATED_ORDEM);

        // Get all the statusColetaList where ordem equals to UPDATED_ORDEM
        defaultStatusColetaShouldNotBeFound("ordem.in=" + UPDATED_ORDEM);
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem is not null
        defaultStatusColetaShouldBeFound("ordem.specified=true");

        // Get all the statusColetaList where ordem is null
        defaultStatusColetaShouldNotBeFound("ordem.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem is greater than or equal to DEFAULT_ORDEM
        defaultStatusColetaShouldBeFound("ordem.greaterThanOrEqual=" + DEFAULT_ORDEM);

        // Get all the statusColetaList where ordem is greater than or equal to (DEFAULT_ORDEM + 1)
        defaultStatusColetaShouldNotBeFound("ordem.greaterThanOrEqual=" + (DEFAULT_ORDEM + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem is less than or equal to DEFAULT_ORDEM
        defaultStatusColetaShouldBeFound("ordem.lessThanOrEqual=" + DEFAULT_ORDEM);

        // Get all the statusColetaList where ordem is less than or equal to SMALLER_ORDEM
        defaultStatusColetaShouldNotBeFound("ordem.lessThanOrEqual=" + SMALLER_ORDEM);
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsLessThanSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem is less than DEFAULT_ORDEM
        defaultStatusColetaShouldNotBeFound("ordem.lessThan=" + DEFAULT_ORDEM);

        // Get all the statusColetaList where ordem is less than (DEFAULT_ORDEM + 1)
        defaultStatusColetaShouldBeFound("ordem.lessThan=" + (DEFAULT_ORDEM + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByOrdemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ordem is greater than DEFAULT_ORDEM
        defaultStatusColetaShouldNotBeFound("ordem.greaterThan=" + DEFAULT_ORDEM);

        // Get all the statusColetaList where ordem is greater than SMALLER_ORDEM
        defaultStatusColetaShouldBeFound("ordem.greaterThan=" + SMALLER_ORDEM);
    }

    @Test
    @Transactional
    void getAllStatusColetasByEstadoInicialIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where estadoInicial equals to DEFAULT_ESTADO_INICIAL
        defaultStatusColetaShouldBeFound("estadoInicial.equals=" + DEFAULT_ESTADO_INICIAL);

        // Get all the statusColetaList where estadoInicial equals to UPDATED_ESTADO_INICIAL
        defaultStatusColetaShouldNotBeFound("estadoInicial.equals=" + UPDATED_ESTADO_INICIAL);
    }

    @Test
    @Transactional
    void getAllStatusColetasByEstadoInicialIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where estadoInicial in DEFAULT_ESTADO_INICIAL or UPDATED_ESTADO_INICIAL
        defaultStatusColetaShouldBeFound("estadoInicial.in=" + DEFAULT_ESTADO_INICIAL + "," + UPDATED_ESTADO_INICIAL);

        // Get all the statusColetaList where estadoInicial equals to UPDATED_ESTADO_INICIAL
        defaultStatusColetaShouldNotBeFound("estadoInicial.in=" + UPDATED_ESTADO_INICIAL);
    }

    @Test
    @Transactional
    void getAllStatusColetasByEstadoInicialIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where estadoInicial is not null
        defaultStatusColetaShouldBeFound("estadoInicial.specified=true");

        // Get all the statusColetaList where estadoInicial is null
        defaultStatusColetaShouldNotBeFound("estadoInicial.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByEstadoFinalIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where estadoFinal equals to DEFAULT_ESTADO_FINAL
        defaultStatusColetaShouldBeFound("estadoFinal.equals=" + DEFAULT_ESTADO_FINAL);

        // Get all the statusColetaList where estadoFinal equals to UPDATED_ESTADO_FINAL
        defaultStatusColetaShouldNotBeFound("estadoFinal.equals=" + UPDATED_ESTADO_FINAL);
    }

    @Test
    @Transactional
    void getAllStatusColetasByEstadoFinalIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where estadoFinal in DEFAULT_ESTADO_FINAL or UPDATED_ESTADO_FINAL
        defaultStatusColetaShouldBeFound("estadoFinal.in=" + DEFAULT_ESTADO_FINAL + "," + UPDATED_ESTADO_FINAL);

        // Get all the statusColetaList where estadoFinal equals to UPDATED_ESTADO_FINAL
        defaultStatusColetaShouldNotBeFound("estadoFinal.in=" + UPDATED_ESTADO_FINAL);
    }

    @Test
    @Transactional
    void getAllStatusColetasByEstadoFinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where estadoFinal is not null
        defaultStatusColetaShouldBeFound("estadoFinal.specified=true");

        // Get all the statusColetaList where estadoFinal is null
        defaultStatusColetaShouldNotBeFound("estadoFinal.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteCancelarIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteCancelar equals to DEFAULT_PERMITE_CANCELAR
        defaultStatusColetaShouldBeFound("permiteCancelar.equals=" + DEFAULT_PERMITE_CANCELAR);

        // Get all the statusColetaList where permiteCancelar equals to UPDATED_PERMITE_CANCELAR
        defaultStatusColetaShouldNotBeFound("permiteCancelar.equals=" + UPDATED_PERMITE_CANCELAR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteCancelarIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteCancelar in DEFAULT_PERMITE_CANCELAR or UPDATED_PERMITE_CANCELAR
        defaultStatusColetaShouldBeFound("permiteCancelar.in=" + DEFAULT_PERMITE_CANCELAR + "," + UPDATED_PERMITE_CANCELAR);

        // Get all the statusColetaList where permiteCancelar equals to UPDATED_PERMITE_CANCELAR
        defaultStatusColetaShouldNotBeFound("permiteCancelar.in=" + UPDATED_PERMITE_CANCELAR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteCancelarIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteCancelar is not null
        defaultStatusColetaShouldBeFound("permiteCancelar.specified=true");

        // Get all the statusColetaList where permiteCancelar is null
        defaultStatusColetaShouldNotBeFound("permiteCancelar.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteEditarIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteEditar equals to DEFAULT_PERMITE_EDITAR
        defaultStatusColetaShouldBeFound("permiteEditar.equals=" + DEFAULT_PERMITE_EDITAR);

        // Get all the statusColetaList where permiteEditar equals to UPDATED_PERMITE_EDITAR
        defaultStatusColetaShouldNotBeFound("permiteEditar.equals=" + UPDATED_PERMITE_EDITAR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteEditarIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteEditar in DEFAULT_PERMITE_EDITAR or UPDATED_PERMITE_EDITAR
        defaultStatusColetaShouldBeFound("permiteEditar.in=" + DEFAULT_PERMITE_EDITAR + "," + UPDATED_PERMITE_EDITAR);

        // Get all the statusColetaList where permiteEditar equals to UPDATED_PERMITE_EDITAR
        defaultStatusColetaShouldNotBeFound("permiteEditar.in=" + UPDATED_PERMITE_EDITAR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteEditarIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteEditar is not null
        defaultStatusColetaShouldBeFound("permiteEditar.specified=true");

        // Get all the statusColetaList where permiteEditar is null
        defaultStatusColetaShouldNotBeFound("permiteEditar.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteExcluirIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteExcluir equals to DEFAULT_PERMITE_EXCLUIR
        defaultStatusColetaShouldBeFound("permiteExcluir.equals=" + DEFAULT_PERMITE_EXCLUIR);

        // Get all the statusColetaList where permiteExcluir equals to UPDATED_PERMITE_EXCLUIR
        defaultStatusColetaShouldNotBeFound("permiteExcluir.equals=" + UPDATED_PERMITE_EXCLUIR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteExcluirIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteExcluir in DEFAULT_PERMITE_EXCLUIR or UPDATED_PERMITE_EXCLUIR
        defaultStatusColetaShouldBeFound("permiteExcluir.in=" + DEFAULT_PERMITE_EXCLUIR + "," + UPDATED_PERMITE_EXCLUIR);

        // Get all the statusColetaList where permiteExcluir equals to UPDATED_PERMITE_EXCLUIR
        defaultStatusColetaShouldNotBeFound("permiteExcluir.in=" + UPDATED_PERMITE_EXCLUIR);
    }

    @Test
    @Transactional
    void getAllStatusColetasByPermiteExcluirIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where permiteExcluir is not null
        defaultStatusColetaShouldBeFound("permiteExcluir.specified=true");

        // Get all the statusColetaList where permiteExcluir is null
        defaultStatusColetaShouldNotBeFound("permiteExcluir.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where descricao equals to DEFAULT_DESCRICAO
        defaultStatusColetaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the statusColetaList where descricao equals to UPDATED_DESCRICAO
        defaultStatusColetaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultStatusColetaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the statusColetaList where descricao equals to UPDATED_DESCRICAO
        defaultStatusColetaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where descricao is not null
        defaultStatusColetaShouldBeFound("descricao.specified=true");

        // Get all the statusColetaList where descricao is null
        defaultStatusColetaShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where descricao contains DEFAULT_DESCRICAO
        defaultStatusColetaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the statusColetaList where descricao contains UPDATED_DESCRICAO
        defaultStatusColetaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where descricao does not contain DEFAULT_DESCRICAO
        defaultStatusColetaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the statusColetaList where descricao does not contain UPDATED_DESCRICAO
        defaultStatusColetaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ativo equals to DEFAULT_ATIVO
        defaultStatusColetaShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the statusColetaList where ativo equals to UPDATED_ATIVO
        defaultStatusColetaShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultStatusColetaShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the statusColetaList where ativo equals to UPDATED_ATIVO
        defaultStatusColetaShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where ativo is not null
        defaultStatusColetaShouldBeFound("ativo.specified=true");

        // Get all the statusColetaList where ativo is null
        defaultStatusColetaShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasByRemovidoIsEqualToSomething() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where removido equals to DEFAULT_REMOVIDO
        defaultStatusColetaShouldBeFound("removido.equals=" + DEFAULT_REMOVIDO);

        // Get all the statusColetaList where removido equals to UPDATED_REMOVIDO
        defaultStatusColetaShouldNotBeFound("removido.equals=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByRemovidoIsInShouldWork() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where removido in DEFAULT_REMOVIDO or UPDATED_REMOVIDO
        defaultStatusColetaShouldBeFound("removido.in=" + DEFAULT_REMOVIDO + "," + UPDATED_REMOVIDO);

        // Get all the statusColetaList where removido equals to UPDATED_REMOVIDO
        defaultStatusColetaShouldNotBeFound("removido.in=" + UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void getAllStatusColetasByRemovidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        // Get all the statusColetaList where removido is not null
        defaultStatusColetaShouldBeFound("removido.specified=true");

        // Get all the statusColetaList where removido is null
        defaultStatusColetaShouldNotBeFound("removido.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusColetasBySolicitacaoColetaIsEqualToSomething() throws Exception {
        SolicitacaoColeta solicitacaoColeta;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            statusColetaRepository.saveAndFlush(statusColeta);
            solicitacaoColeta = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solicitacaoColeta = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solicitacaoColeta);
        em.flush();
        statusColeta.addSolicitacaoColeta(solicitacaoColeta);
        statusColetaRepository.saveAndFlush(statusColeta);
        Long solicitacaoColetaId = solicitacaoColeta.getId();
        // Get all the statusColetaList where solicitacaoColeta equals to solicitacaoColetaId
        defaultStatusColetaShouldBeFound("solicitacaoColetaId.equals=" + solicitacaoColetaId);

        // Get all the statusColetaList where solicitacaoColeta equals to (solicitacaoColetaId + 1)
        defaultStatusColetaShouldNotBeFound("solicitacaoColetaId.equals=" + (solicitacaoColetaId + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByHistoricoStatusColetaOrigemIsEqualToSomething() throws Exception {
        HistoricoStatusColeta historicoStatusColetaOrigem;
        if (TestUtil.findAll(em, HistoricoStatusColeta.class).isEmpty()) {
            statusColetaRepository.saveAndFlush(statusColeta);
            historicoStatusColetaOrigem = HistoricoStatusColetaResourceIT.createEntity(em);
        } else {
            historicoStatusColetaOrigem = TestUtil.findAll(em, HistoricoStatusColeta.class).get(0);
        }
        em.persist(historicoStatusColetaOrigem);
        em.flush();
        statusColeta.addHistoricoStatusColetaOrigem(historicoStatusColetaOrigem);
        statusColetaRepository.saveAndFlush(statusColeta);
        Long historicoStatusColetaOrigemId = historicoStatusColetaOrigem.getId();
        // Get all the statusColetaList where historicoStatusColetaOrigem equals to historicoStatusColetaOrigemId
        defaultStatusColetaShouldBeFound("historicoStatusColetaOrigemId.equals=" + historicoStatusColetaOrigemId);

        // Get all the statusColetaList where historicoStatusColetaOrigem equals to (historicoStatusColetaOrigemId + 1)
        defaultStatusColetaShouldNotBeFound("historicoStatusColetaOrigemId.equals=" + (historicoStatusColetaOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByHistoricoStatusColetaDestinoIsEqualToSomething() throws Exception {
        HistoricoStatusColeta historicoStatusColetaDestino;
        if (TestUtil.findAll(em, HistoricoStatusColeta.class).isEmpty()) {
            statusColetaRepository.saveAndFlush(statusColeta);
            historicoStatusColetaDestino = HistoricoStatusColetaResourceIT.createEntity(em);
        } else {
            historicoStatusColetaDestino = TestUtil.findAll(em, HistoricoStatusColeta.class).get(0);
        }
        em.persist(historicoStatusColetaDestino);
        em.flush();
        statusColeta.addHistoricoStatusColetaDestino(historicoStatusColetaDestino);
        statusColetaRepository.saveAndFlush(statusColeta);
        Long historicoStatusColetaDestinoId = historicoStatusColetaDestino.getId();
        // Get all the statusColetaList where historicoStatusColetaDestino equals to historicoStatusColetaDestinoId
        defaultStatusColetaShouldBeFound("historicoStatusColetaDestinoId.equals=" + historicoStatusColetaDestinoId);

        // Get all the statusColetaList where historicoStatusColetaDestino equals to (historicoStatusColetaDestinoId + 1)
        defaultStatusColetaShouldNotBeFound("historicoStatusColetaDestinoId.equals=" + (historicoStatusColetaDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByRoteirizacaoIsEqualToSomething() throws Exception {
        Roteirizacao roteirizacao;
        if (TestUtil.findAll(em, Roteirizacao.class).isEmpty()) {
            statusColetaRepository.saveAndFlush(statusColeta);
            roteirizacao = RoteirizacaoResourceIT.createEntity(em);
        } else {
            roteirizacao = TestUtil.findAll(em, Roteirizacao.class).get(0);
        }
        em.persist(roteirizacao);
        em.flush();
        statusColeta.addRoteirizacao(roteirizacao);
        statusColetaRepository.saveAndFlush(statusColeta);
        Long roteirizacaoId = roteirizacao.getId();
        // Get all the statusColetaList where roteirizacao equals to roteirizacaoId
        defaultStatusColetaShouldBeFound("roteirizacaoId.equals=" + roteirizacaoId);

        // Get all the statusColetaList where roteirizacao equals to (roteirizacaoId + 1)
        defaultStatusColetaShouldNotBeFound("roteirizacaoId.equals=" + (roteirizacaoId + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByStatusColetaOrigemIsEqualToSomething() throws Exception {
        StatusColeta statusColetaOrigem;
        if (TestUtil.findAll(em, StatusColeta.class).isEmpty()) {
            statusColetaRepository.saveAndFlush(statusColeta);
            statusColetaOrigem = StatusColetaResourceIT.createEntity(em);
        } else {
            statusColetaOrigem = TestUtil.findAll(em, StatusColeta.class).get(0);
        }
        em.persist(statusColetaOrigem);
        em.flush();
        statusColeta.addStatusColetaOrigem(statusColetaOrigem);
        statusColetaRepository.saveAndFlush(statusColeta);
        Long statusColetaOrigemId = statusColetaOrigem.getId();
        // Get all the statusColetaList where statusColetaOrigem equals to statusColetaOrigemId
        defaultStatusColetaShouldBeFound("statusColetaOrigemId.equals=" + statusColetaOrigemId);

        // Get all the statusColetaList where statusColetaOrigem equals to (statusColetaOrigemId + 1)
        defaultStatusColetaShouldNotBeFound("statusColetaOrigemId.equals=" + (statusColetaOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllStatusColetasByStatusColetaDestinoIsEqualToSomething() throws Exception {
        StatusColeta statusColetaDestino;
        if (TestUtil.findAll(em, StatusColeta.class).isEmpty()) {
            statusColetaRepository.saveAndFlush(statusColeta);
            statusColetaDestino = StatusColetaResourceIT.createEntity(em);
        } else {
            statusColetaDestino = TestUtil.findAll(em, StatusColeta.class).get(0);
        }
        em.persist(statusColetaDestino);
        em.flush();
        statusColeta.addStatusColetaDestino(statusColetaDestino);
        statusColetaRepository.saveAndFlush(statusColeta);
        Long statusColetaDestinoId = statusColetaDestino.getId();
        // Get all the statusColetaList where statusColetaDestino equals to statusColetaDestinoId
        defaultStatusColetaShouldBeFound("statusColetaDestinoId.equals=" + statusColetaDestinoId);

        // Get all the statusColetaList where statusColetaDestino equals to (statusColetaDestinoId + 1)
        defaultStatusColetaShouldNotBeFound("statusColetaDestinoId.equals=" + (statusColetaDestinoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStatusColetaShouldBeFound(String filter) throws Exception {
        restStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR)))
            .andExpect(jsonPath("$.[*].ordem").value(hasItem(DEFAULT_ORDEM)))
            .andExpect(jsonPath("$.[*].estadoInicial").value(hasItem(DEFAULT_ESTADO_INICIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].estadoFinal").value(hasItem(DEFAULT_ESTADO_FINAL.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteCancelar").value(hasItem(DEFAULT_PERMITE_CANCELAR.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteEditar").value(hasItem(DEFAULT_PERMITE_EDITAR.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteExcluir").value(hasItem(DEFAULT_PERMITE_EXCLUIR.booleanValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));

        // Check, that the count call also returns 1
        restStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStatusColetaShouldNotBeFound(String filter) throws Exception {
        restStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStatusColeta() throws Exception {
        // Get the statusColeta
        restStatusColetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatusColeta() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        statusColetaSearchRepository.save(statusColeta);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());

        // Update the statusColeta
        StatusColeta updatedStatusColeta = statusColetaRepository.findById(statusColeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatusColeta are not directly saved in db
        em.detach(updatedStatusColeta);
        updatedStatusColeta
            .nome(UPDATED_NOME)
            .cor(UPDATED_COR)
            .ordem(UPDATED_ORDEM)
            .estadoInicial(UPDATED_ESTADO_INICIAL)
            .estadoFinal(UPDATED_ESTADO_FINAL)
            .permiteCancelar(UPDATED_PERMITE_CANCELAR)
            .permiteEditar(UPDATED_PERMITE_EDITAR)
            .permiteExcluir(UPDATED_PERMITE_EXCLUIR)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO);
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(updatedStatusColeta);

        restStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        StatusColeta testStatusColeta = statusColetaList.get(statusColetaList.size() - 1);
        assertThat(testStatusColeta.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testStatusColeta.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testStatusColeta.getOrdem()).isEqualTo(UPDATED_ORDEM);
        assertThat(testStatusColeta.getEstadoInicial()).isEqualTo(UPDATED_ESTADO_INICIAL);
        assertThat(testStatusColeta.getEstadoFinal()).isEqualTo(UPDATED_ESTADO_FINAL);
        assertThat(testStatusColeta.getPermiteCancelar()).isEqualTo(UPDATED_PERMITE_CANCELAR);
        assertThat(testStatusColeta.getPermiteEditar()).isEqualTo(UPDATED_PERMITE_EDITAR);
        assertThat(testStatusColeta.getPermiteExcluir()).isEqualTo(UPDATED_PERMITE_EXCLUIR);
        assertThat(testStatusColeta.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<StatusColeta> statusColetaSearchList = IterableUtils.toList(statusColetaSearchRepository.findAll());
                StatusColeta testStatusColetaSearch = statusColetaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testStatusColetaSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testStatusColetaSearch.getCor()).isEqualTo(UPDATED_COR);
                assertThat(testStatusColetaSearch.getOrdem()).isEqualTo(UPDATED_ORDEM);
                assertThat(testStatusColetaSearch.getEstadoInicial()).isEqualTo(UPDATED_ESTADO_INICIAL);
                assertThat(testStatusColetaSearch.getEstadoFinal()).isEqualTo(UPDATED_ESTADO_FINAL);
                assertThat(testStatusColetaSearch.getPermiteCancelar()).isEqualTo(UPDATED_PERMITE_CANCELAR);
                assertThat(testStatusColetaSearch.getPermiteEditar()).isEqualTo(UPDATED_PERMITE_EDITAR);
                assertThat(testStatusColetaSearch.getPermiteExcluir()).isEqualTo(UPDATED_PERMITE_EXCLUIR);
                assertThat(testStatusColetaSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
                assertThat(testStatusColetaSearch.getAtivo()).isEqualTo(UPDATED_ATIVO);
                assertThat(testStatusColetaSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
            });
    }

    @Test
    @Transactional
    void putNonExistingStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        statusColeta.setId(longCount.incrementAndGet());

        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        statusColeta.setId(longCount.incrementAndGet());

        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        statusColeta.setId(longCount.incrementAndGet());

        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateStatusColetaWithPatch() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();

        // Update the statusColeta using partial update
        StatusColeta partialUpdatedStatusColeta = new StatusColeta();
        partialUpdatedStatusColeta.setId(statusColeta.getId());

        partialUpdatedStatusColeta
            .cor(UPDATED_COR)
            .ordem(UPDATED_ORDEM)
            .estadoInicial(UPDATED_ESTADO_INICIAL)
            .estadoFinal(UPDATED_ESTADO_FINAL)
            .permiteCancelar(UPDATED_PERMITE_CANCELAR)
            .permiteEditar(UPDATED_PERMITE_EDITAR)
            .permiteExcluir(UPDATED_PERMITE_EXCLUIR)
            .descricao(UPDATED_DESCRICAO)
            .removido(UPDATED_REMOVIDO);

        restStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatusColeta))
            )
            .andExpect(status().isOk());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        StatusColeta testStatusColeta = statusColetaList.get(statusColetaList.size() - 1);
        assertThat(testStatusColeta.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testStatusColeta.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testStatusColeta.getOrdem()).isEqualTo(UPDATED_ORDEM);
        assertThat(testStatusColeta.getEstadoInicial()).isEqualTo(UPDATED_ESTADO_INICIAL);
        assertThat(testStatusColeta.getEstadoFinal()).isEqualTo(UPDATED_ESTADO_FINAL);
        assertThat(testStatusColeta.getPermiteCancelar()).isEqualTo(UPDATED_PERMITE_CANCELAR);
        assertThat(testStatusColeta.getPermiteEditar()).isEqualTo(UPDATED_PERMITE_EDITAR);
        assertThat(testStatusColeta.getPermiteExcluir()).isEqualTo(UPDATED_PERMITE_EXCLUIR);
        assertThat(testStatusColeta.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void fullUpdateStatusColetaWithPatch() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);

        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();

        // Update the statusColeta using partial update
        StatusColeta partialUpdatedStatusColeta = new StatusColeta();
        partialUpdatedStatusColeta.setId(statusColeta.getId());

        partialUpdatedStatusColeta
            .nome(UPDATED_NOME)
            .cor(UPDATED_COR)
            .ordem(UPDATED_ORDEM)
            .estadoInicial(UPDATED_ESTADO_INICIAL)
            .estadoFinal(UPDATED_ESTADO_FINAL)
            .permiteCancelar(UPDATED_PERMITE_CANCELAR)
            .permiteEditar(UPDATED_PERMITE_EDITAR)
            .permiteExcluir(UPDATED_PERMITE_EXCLUIR)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO);

        restStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatusColeta))
            )
            .andExpect(status().isOk());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        StatusColeta testStatusColeta = statusColetaList.get(statusColetaList.size() - 1);
        assertThat(testStatusColeta.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testStatusColeta.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testStatusColeta.getOrdem()).isEqualTo(UPDATED_ORDEM);
        assertThat(testStatusColeta.getEstadoInicial()).isEqualTo(UPDATED_ESTADO_INICIAL);
        assertThat(testStatusColeta.getEstadoFinal()).isEqualTo(UPDATED_ESTADO_FINAL);
        assertThat(testStatusColeta.getPermiteCancelar()).isEqualTo(UPDATED_PERMITE_CANCELAR);
        assertThat(testStatusColeta.getPermiteEditar()).isEqualTo(UPDATED_PERMITE_EDITAR);
        assertThat(testStatusColeta.getPermiteExcluir()).isEqualTo(UPDATED_PERMITE_EXCLUIR);
        assertThat(testStatusColeta.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
    }

    @Test
    @Transactional
    void patchNonExistingStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        statusColeta.setId(longCount.incrementAndGet());

        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusColetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        statusColeta.setId(longCount.incrementAndGet());

        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        statusColeta.setId(longCount.incrementAndGet());

        // Create the StatusColeta
        StatusColetaDTO statusColetaDTO = statusColetaMapper.toDto(statusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusColeta in the database
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteStatusColeta() throws Exception {
        // Initialize the database
        statusColetaRepository.saveAndFlush(statusColeta);
        statusColetaRepository.save(statusColeta);
        statusColetaSearchRepository.save(statusColeta);

        int databaseSizeBeforeDelete = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the statusColeta
        restStatusColetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, statusColeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StatusColeta> statusColetaList = statusColetaRepository.findAll();
        assertThat(statusColetaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchStatusColeta() throws Exception {
        // Initialize the database
        statusColeta = statusColetaRepository.saveAndFlush(statusColeta);
        statusColetaSearchRepository.save(statusColeta);

        // Search the statusColeta
        restStatusColetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + statusColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR)))
            .andExpect(jsonPath("$.[*].ordem").value(hasItem(DEFAULT_ORDEM)))
            .andExpect(jsonPath("$.[*].estadoInicial").value(hasItem(DEFAULT_ESTADO_INICIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].estadoFinal").value(hasItem(DEFAULT_ESTADO_FINAL.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteCancelar").value(hasItem(DEFAULT_PERMITE_CANCELAR.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteEditar").value(hasItem(DEFAULT_PERMITE_EDITAR.booleanValue())))
            .andExpect(jsonPath("$.[*].permiteExcluir").value(hasItem(DEFAULT_PERMITE_EXCLUIR.booleanValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())));
    }
}
