package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.repository.StatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.StatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.StatusColetaService;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.StatusColetaMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_CADASTRO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CADASTRO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_ATUALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_ATUALIZACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_REMOVIDO = false;
    private static final Boolean UPDATED_REMOVIDO = true;

    private static final ZonedDateTime DEFAULT_DATA_REMOCAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_REMOCAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_REMOCAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_REMOCAO = "BBBBBBBBBB";

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
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .usuarioCadastro(DEFAULT_USUARIO_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO)
            .usuarioAtualizacao(DEFAULT_USUARIO_ATUALIZACAO)
            .ativo(DEFAULT_ATIVO)
            .removido(DEFAULT_REMOVIDO)
            .dataRemocao(DEFAULT_DATA_REMOCAO)
            .usuarioRemocao(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testStatusColeta.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testStatusColeta.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testStatusColeta.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testStatusColeta.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(DEFAULT_REMOVIDO);
        assertThat(testStatusColeta.getDataRemocao()).isEqualTo(DEFAULT_DATA_REMOCAO);
        assertThat(testStatusColeta.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(statusColetaSearchRepository.findAll());
        // set the field null
        statusColeta.setDataCadastro(null);

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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
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
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.usuarioCadastro").value(DEFAULT_USUARIO_CADASTRO))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)))
            .andExpect(jsonPath("$.usuarioAtualizacao").value(DEFAULT_USUARIO_ATUALIZACAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.removido").value(DEFAULT_REMOVIDO.booleanValue()))
            .andExpect(jsonPath("$.dataRemocao").value(sameInstant(DEFAULT_DATA_REMOCAO)))
            .andExpect(jsonPath("$.usuarioRemocao").value(DEFAULT_USUARIO_REMOCAO));
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);
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
        assertThat(testStatusColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testStatusColeta.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testStatusColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testStatusColeta.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testStatusColeta.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testStatusColeta.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
                assertThat(testStatusColetaSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testStatusColetaSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testStatusColetaSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testStatusColetaSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
                assertThat(testStatusColetaSearch.getAtivo()).isEqualTo(UPDATED_ATIVO);
                assertThat(testStatusColetaSearch.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
                assertThat(testStatusColetaSearch.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
                assertThat(testStatusColetaSearch.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO);

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
        assertThat(testStatusColeta.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testStatusColeta.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testStatusColeta.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testStatusColeta.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testStatusColeta.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testStatusColeta.getUsuarioRemocao()).isEqualTo(DEFAULT_USUARIO_REMOCAO);
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
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO)
            .ativo(UPDATED_ATIVO)
            .removido(UPDATED_REMOVIDO)
            .dataRemocao(UPDATED_DATA_REMOCAO)
            .usuarioRemocao(UPDATED_USUARIO_REMOCAO);

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
        assertThat(testStatusColeta.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testStatusColeta.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testStatusColeta.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testStatusColeta.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        assertThat(testStatusColeta.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testStatusColeta.getRemovido()).isEqualTo(UPDATED_REMOVIDO);
        assertThat(testStatusColeta.getDataRemocao()).isEqualTo(UPDATED_DATA_REMOCAO);
        assertThat(testStatusColeta.getUsuarioRemocao()).isEqualTo(UPDATED_USUARIO_REMOCAO);
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
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].removido").value(hasItem(DEFAULT_REMOVIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataRemocao").value(hasItem(sameInstant(DEFAULT_DATA_REMOCAO))))
            .andExpect(jsonPath("$.[*].usuarioRemocao").value(hasItem(DEFAULT_USUARIO_REMOCAO)));
    }
}
