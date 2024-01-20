package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.domain.enumeration.TipoTabelaFrete;
import br.com.revenuebrasil.newcargas.repository.TabelaFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TabelaFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TabelaFreteMapper;
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
 * Integration tests for the {@link TabelaFreteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TabelaFreteResourceIT {

    private static final TipoTabelaFrete DEFAULT_TIPO = TipoTabelaFrete.EMBARCADOR;
    private static final TipoTabelaFrete UPDATED_TIPO = TipoTabelaFrete.TRANSPORTADOR;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEAD_TIME = 1;
    private static final Integer UPDATED_LEAD_TIME = 2;

    private static final Double DEFAULT_FRETE_MINIMO = 1D;
    private static final Double UPDATED_FRETE_MINIMO = 2D;

    private static final Double DEFAULT_VALOR_TONELADA = 1D;
    private static final Double UPDATED_VALOR_TONELADA = 2D;

    private static final Double DEFAULT_VALOR_METRO_CUBICO = 1D;
    private static final Double UPDATED_VALOR_METRO_CUBICO = 2D;

    private static final Double DEFAULT_VALOR_UNIDADE = 1D;
    private static final Double UPDATED_VALOR_UNIDADE = 2D;

    private static final Double DEFAULT_VALOR_KM = 1D;
    private static final Double UPDATED_VALOR_KM = 2D;

    private static final Double DEFAULT_VALOR_ADICIONAL = 1D;
    private static final Double UPDATED_VALOR_ADICIONAL = 2D;

    private static final Double DEFAULT_VALOR_COLETA = 1D;
    private static final Double UPDATED_VALOR_COLETA = 2D;

    private static final Double DEFAULT_VALOR_ENTREGA = 1D;
    private static final Double UPDATED_VALOR_ENTREGA = 2D;

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    private static final Double DEFAULT_VALOR_KM_ADICIONAL = 1D;
    private static final Double UPDATED_VALOR_KM_ADICIONAL = 2D;

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/tabela-fretes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tabela-fretes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TabelaFreteRepository tabelaFreteRepository;

    @Autowired
    private TabelaFreteMapper tabelaFreteMapper;

    @Autowired
    private TabelaFreteSearchRepository tabelaFreteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTabelaFreteMockMvc;

    private TabelaFrete tabelaFrete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabelaFrete createEntity(EntityManager em) {
        TabelaFrete tabelaFrete = new TabelaFrete()
            .tipo(DEFAULT_TIPO)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .leadTime(DEFAULT_LEAD_TIME)
            .freteMinimo(DEFAULT_FRETE_MINIMO)
            .valorTonelada(DEFAULT_VALOR_TONELADA)
            .valorMetroCubico(DEFAULT_VALOR_METRO_CUBICO)
            .valorUnidade(DEFAULT_VALOR_UNIDADE)
            .valorKm(DEFAULT_VALOR_KM)
            .valorAdicional(DEFAULT_VALOR_ADICIONAL)
            .valorColeta(DEFAULT_VALOR_COLETA)
            .valorEntrega(DEFAULT_VALOR_ENTREGA)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .valorKmAdicional(DEFAULT_VALOR_KM_ADICIONAL)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO);
        return tabelaFrete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabelaFrete createUpdatedEntity(EntityManager em) {
        TabelaFrete tabelaFrete = new TabelaFrete()
            .tipo(UPDATED_TIPO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .leadTime(UPDATED_LEAD_TIME)
            .freteMinimo(UPDATED_FRETE_MINIMO)
            .valorTonelada(UPDATED_VALOR_TONELADA)
            .valorMetroCubico(UPDATED_VALOR_METRO_CUBICO)
            .valorUnidade(UPDATED_VALOR_UNIDADE)
            .valorKm(UPDATED_VALOR_KM)
            .valorAdicional(UPDATED_VALOR_ADICIONAL)
            .valorColeta(UPDATED_VALOR_COLETA)
            .valorEntrega(UPDATED_VALOR_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .valorKmAdicional(UPDATED_VALOR_KM_ADICIONAL)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);
        return tabelaFrete;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        tabelaFreteSearchRepository.deleteAll();
        assertThat(tabelaFreteSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        tabelaFrete = createEntity(em);
    }

    @Test
    @Transactional
    void createTabelaFrete() throws Exception {
        int databaseSizeBeforeCreate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);
        restTabelaFreteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TabelaFrete testTabelaFrete = tabelaFreteList.get(tabelaFreteList.size() - 1);
        assertThat(testTabelaFrete.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTabelaFrete.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTabelaFrete.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTabelaFrete.getLeadTime()).isEqualTo(DEFAULT_LEAD_TIME);
        assertThat(testTabelaFrete.getFreteMinimo()).isEqualTo(DEFAULT_FRETE_MINIMO);
        assertThat(testTabelaFrete.getValorTonelada()).isEqualTo(DEFAULT_VALOR_TONELADA);
        assertThat(testTabelaFrete.getValorMetroCubico()).isEqualTo(DEFAULT_VALOR_METRO_CUBICO);
        assertThat(testTabelaFrete.getValorUnidade()).isEqualTo(DEFAULT_VALOR_UNIDADE);
        assertThat(testTabelaFrete.getValorKm()).isEqualTo(DEFAULT_VALOR_KM);
        assertThat(testTabelaFrete.getValorAdicional()).isEqualTo(DEFAULT_VALOR_ADICIONAL);
        assertThat(testTabelaFrete.getValorColeta()).isEqualTo(DEFAULT_VALOR_COLETA);
        assertThat(testTabelaFrete.getValorEntrega()).isEqualTo(DEFAULT_VALOR_ENTREGA);
        assertThat(testTabelaFrete.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testTabelaFrete.getValorKmAdicional()).isEqualTo(DEFAULT_VALOR_KM_ADICIONAL);
        assertThat(testTabelaFrete.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testTabelaFrete.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
    }

    @Test
    @Transactional
    void createTabelaFreteWithExistingId() throws Exception {
        // Create the TabelaFrete with an existing ID
        tabelaFrete.setId(1L);
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        int databaseSizeBeforeCreate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTabelaFreteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        // set the field null
        tabelaFrete.setTipo(null);

        // Create the TabelaFrete, which fails.
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        restTabelaFreteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        // set the field null
        tabelaFrete.setNome(null);

        // Create the TabelaFrete, which fails.
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        restTabelaFreteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        // set the field null
        tabelaFrete.setDataCadastro(null);

        // Create the TabelaFrete, which fails.
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        restTabelaFreteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTabelaFretes() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList
        restTabelaFreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tabelaFrete.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].freteMinimo").value(hasItem(DEFAULT_FRETE_MINIMO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorTonelada").value(hasItem(DEFAULT_VALOR_TONELADA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorMetroCubico").value(hasItem(DEFAULT_VALOR_METRO_CUBICO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorUnidade").value(hasItem(DEFAULT_VALOR_UNIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].valorKm").value(hasItem(DEFAULT_VALOR_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].valorAdicional").value(hasItem(DEFAULT_VALOR_ADICIONAL.doubleValue())))
            .andExpect(jsonPath("$.[*].valorColeta").value(hasItem(DEFAULT_VALOR_COLETA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorEntrega").value(hasItem(DEFAULT_VALOR_ENTREGA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].valorKmAdicional").value(hasItem(DEFAULT_VALOR_KM_ADICIONAL.doubleValue())))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))));
    }

    @Test
    @Transactional
    void getTabelaFrete() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get the tabelaFrete
        restTabelaFreteMockMvc
            .perform(get(ENTITY_API_URL_ID, tabelaFrete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tabelaFrete.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.leadTime").value(DEFAULT_LEAD_TIME))
            .andExpect(jsonPath("$.freteMinimo").value(DEFAULT_FRETE_MINIMO.doubleValue()))
            .andExpect(jsonPath("$.valorTonelada").value(DEFAULT_VALOR_TONELADA.doubleValue()))
            .andExpect(jsonPath("$.valorMetroCubico").value(DEFAULT_VALOR_METRO_CUBICO.doubleValue()))
            .andExpect(jsonPath("$.valorUnidade").value(DEFAULT_VALOR_UNIDADE.doubleValue()))
            .andExpect(jsonPath("$.valorKm").value(DEFAULT_VALOR_KM.doubleValue()))
            .andExpect(jsonPath("$.valorAdicional").value(DEFAULT_VALOR_ADICIONAL.doubleValue()))
            .andExpect(jsonPath("$.valorColeta").value(DEFAULT_VALOR_COLETA.doubleValue()))
            .andExpect(jsonPath("$.valorEntrega").value(DEFAULT_VALOR_ENTREGA.doubleValue()))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.valorKmAdicional").value(DEFAULT_VALOR_KM_ADICIONAL.doubleValue()))
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)));
    }

    @Test
    @Transactional
    void getNonExistingTabelaFrete() throws Exception {
        // Get the tabelaFrete
        restTabelaFreteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTabelaFrete() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        tabelaFreteSearchRepository.save(tabelaFrete);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());

        // Update the tabelaFrete
        TabelaFrete updatedTabelaFrete = tabelaFreteRepository.findById(tabelaFrete.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTabelaFrete are not directly saved in db
        em.detach(updatedTabelaFrete);
        updatedTabelaFrete
            .tipo(UPDATED_TIPO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .leadTime(UPDATED_LEAD_TIME)
            .freteMinimo(UPDATED_FRETE_MINIMO)
            .valorTonelada(UPDATED_VALOR_TONELADA)
            .valorMetroCubico(UPDATED_VALOR_METRO_CUBICO)
            .valorUnidade(UPDATED_VALOR_UNIDADE)
            .valorKm(UPDATED_VALOR_KM)
            .valorAdicional(UPDATED_VALOR_ADICIONAL)
            .valorColeta(UPDATED_VALOR_COLETA)
            .valorEntrega(UPDATED_VALOR_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .valorKmAdicional(UPDATED_VALOR_KM_ADICIONAL)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(updatedTabelaFrete);

        restTabelaFreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tabelaFreteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isOk());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        TabelaFrete testTabelaFrete = tabelaFreteList.get(tabelaFreteList.size() - 1);
        assertThat(testTabelaFrete.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTabelaFrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTabelaFrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTabelaFrete.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testTabelaFrete.getFreteMinimo()).isEqualTo(UPDATED_FRETE_MINIMO);
        assertThat(testTabelaFrete.getValorTonelada()).isEqualTo(UPDATED_VALOR_TONELADA);
        assertThat(testTabelaFrete.getValorMetroCubico()).isEqualTo(UPDATED_VALOR_METRO_CUBICO);
        assertThat(testTabelaFrete.getValorUnidade()).isEqualTo(UPDATED_VALOR_UNIDADE);
        assertThat(testTabelaFrete.getValorKm()).isEqualTo(UPDATED_VALOR_KM);
        assertThat(testTabelaFrete.getValorAdicional()).isEqualTo(UPDATED_VALOR_ADICIONAL);
        assertThat(testTabelaFrete.getValorColeta()).isEqualTo(UPDATED_VALOR_COLETA);
        assertThat(testTabelaFrete.getValorEntrega()).isEqualTo(UPDATED_VALOR_ENTREGA);
        assertThat(testTabelaFrete.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testTabelaFrete.getValorKmAdicional()).isEqualTo(UPDATED_VALOR_KM_ADICIONAL);
        assertThat(testTabelaFrete.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testTabelaFrete.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TabelaFrete> tabelaFreteSearchList = IterableUtils.toList(tabelaFreteSearchRepository.findAll());
                TabelaFrete testTabelaFreteSearch = tabelaFreteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTabelaFreteSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testTabelaFreteSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testTabelaFreteSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
                assertThat(testTabelaFreteSearch.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
                assertThat(testTabelaFreteSearch.getFreteMinimo()).isEqualTo(UPDATED_FRETE_MINIMO);
                assertThat(testTabelaFreteSearch.getValorTonelada()).isEqualTo(UPDATED_VALOR_TONELADA);
                assertThat(testTabelaFreteSearch.getValorMetroCubico()).isEqualTo(UPDATED_VALOR_METRO_CUBICO);
                assertThat(testTabelaFreteSearch.getValorUnidade()).isEqualTo(UPDATED_VALOR_UNIDADE);
                assertThat(testTabelaFreteSearch.getValorKm()).isEqualTo(UPDATED_VALOR_KM);
                assertThat(testTabelaFreteSearch.getValorAdicional()).isEqualTo(UPDATED_VALOR_ADICIONAL);
                assertThat(testTabelaFreteSearch.getValorColeta()).isEqualTo(UPDATED_VALOR_COLETA);
                assertThat(testTabelaFreteSearch.getValorEntrega()).isEqualTo(UPDATED_VALOR_ENTREGA);
                assertThat(testTabelaFreteSearch.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
                assertThat(testTabelaFreteSearch.getValorKmAdicional()).isEqualTo(UPDATED_VALOR_KM_ADICIONAL);
                assertThat(testTabelaFreteSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testTabelaFreteSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTabelaFrete() throws Exception {
        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        tabelaFrete.setId(longCount.incrementAndGet());

        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabelaFreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tabelaFreteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTabelaFrete() throws Exception {
        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        tabelaFrete.setId(longCount.incrementAndGet());

        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaFreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTabelaFrete() throws Exception {
        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        tabelaFrete.setId(longCount.incrementAndGet());

        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaFreteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTabelaFreteWithPatch() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();

        // Update the tabelaFrete using partial update
        TabelaFrete partialUpdatedTabelaFrete = new TabelaFrete();
        partialUpdatedTabelaFrete.setId(tabelaFrete.getId());

        partialUpdatedTabelaFrete
            .tipo(UPDATED_TIPO)
            .leadTime(UPDATED_LEAD_TIME)
            .freteMinimo(UPDATED_FRETE_MINIMO)
            .valorMetroCubico(UPDATED_VALOR_METRO_CUBICO)
            .valorUnidade(UPDATED_VALOR_UNIDADE)
            .valorAdicional(UPDATED_VALOR_ADICIONAL)
            .valorColeta(UPDATED_VALOR_COLETA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);

        restTabelaFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTabelaFrete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTabelaFrete))
            )
            .andExpect(status().isOk());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        TabelaFrete testTabelaFrete = tabelaFreteList.get(tabelaFreteList.size() - 1);
        assertThat(testTabelaFrete.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTabelaFrete.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTabelaFrete.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTabelaFrete.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testTabelaFrete.getFreteMinimo()).isEqualTo(UPDATED_FRETE_MINIMO);
        assertThat(testTabelaFrete.getValorTonelada()).isEqualTo(DEFAULT_VALOR_TONELADA);
        assertThat(testTabelaFrete.getValorMetroCubico()).isEqualTo(UPDATED_VALOR_METRO_CUBICO);
        assertThat(testTabelaFrete.getValorUnidade()).isEqualTo(UPDATED_VALOR_UNIDADE);
        assertThat(testTabelaFrete.getValorKm()).isEqualTo(DEFAULT_VALOR_KM);
        assertThat(testTabelaFrete.getValorAdicional()).isEqualTo(UPDATED_VALOR_ADICIONAL);
        assertThat(testTabelaFrete.getValorColeta()).isEqualTo(UPDATED_VALOR_COLETA);
        assertThat(testTabelaFrete.getValorEntrega()).isEqualTo(DEFAULT_VALOR_ENTREGA);
        assertThat(testTabelaFrete.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testTabelaFrete.getValorKmAdicional()).isEqualTo(DEFAULT_VALOR_KM_ADICIONAL);
        assertThat(testTabelaFrete.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testTabelaFrete.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
    }

    @Test
    @Transactional
    void fullUpdateTabelaFreteWithPatch() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();

        // Update the tabelaFrete using partial update
        TabelaFrete partialUpdatedTabelaFrete = new TabelaFrete();
        partialUpdatedTabelaFrete.setId(tabelaFrete.getId());

        partialUpdatedTabelaFrete
            .tipo(UPDATED_TIPO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .leadTime(UPDATED_LEAD_TIME)
            .freteMinimo(UPDATED_FRETE_MINIMO)
            .valorTonelada(UPDATED_VALOR_TONELADA)
            .valorMetroCubico(UPDATED_VALOR_METRO_CUBICO)
            .valorUnidade(UPDATED_VALOR_UNIDADE)
            .valorKm(UPDATED_VALOR_KM)
            .valorAdicional(UPDATED_VALOR_ADICIONAL)
            .valorColeta(UPDATED_VALOR_COLETA)
            .valorEntrega(UPDATED_VALOR_ENTREGA)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .valorKmAdicional(UPDATED_VALOR_KM_ADICIONAL)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);

        restTabelaFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTabelaFrete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTabelaFrete))
            )
            .andExpect(status().isOk());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        TabelaFrete testTabelaFrete = tabelaFreteList.get(tabelaFreteList.size() - 1);
        assertThat(testTabelaFrete.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTabelaFrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTabelaFrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTabelaFrete.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testTabelaFrete.getFreteMinimo()).isEqualTo(UPDATED_FRETE_MINIMO);
        assertThat(testTabelaFrete.getValorTonelada()).isEqualTo(UPDATED_VALOR_TONELADA);
        assertThat(testTabelaFrete.getValorMetroCubico()).isEqualTo(UPDATED_VALOR_METRO_CUBICO);
        assertThat(testTabelaFrete.getValorUnidade()).isEqualTo(UPDATED_VALOR_UNIDADE);
        assertThat(testTabelaFrete.getValorKm()).isEqualTo(UPDATED_VALOR_KM);
        assertThat(testTabelaFrete.getValorAdicional()).isEqualTo(UPDATED_VALOR_ADICIONAL);
        assertThat(testTabelaFrete.getValorColeta()).isEqualTo(UPDATED_VALOR_COLETA);
        assertThat(testTabelaFrete.getValorEntrega()).isEqualTo(UPDATED_VALOR_ENTREGA);
        assertThat(testTabelaFrete.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testTabelaFrete.getValorKmAdicional()).isEqualTo(UPDATED_VALOR_KM_ADICIONAL);
        assertThat(testTabelaFrete.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testTabelaFrete.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
    }

    @Test
    @Transactional
    void patchNonExistingTabelaFrete() throws Exception {
        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        tabelaFrete.setId(longCount.incrementAndGet());

        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabelaFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tabelaFreteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTabelaFrete() throws Exception {
        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        tabelaFrete.setId(longCount.incrementAndGet());

        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaFreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTabelaFrete() throws Exception {
        int databaseSizeBeforeUpdate = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        tabelaFrete.setId(longCount.incrementAndGet());

        // Create the TabelaFrete
        TabelaFreteDTO tabelaFreteDTO = tabelaFreteMapper.toDto(tabelaFrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaFreteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tabelaFreteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TabelaFrete in the database
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTabelaFrete() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        tabelaFreteRepository.save(tabelaFrete);
        tabelaFreteSearchRepository.save(tabelaFrete);

        int databaseSizeBeforeDelete = tabelaFreteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tabelaFrete
        restTabelaFreteMockMvc
            .perform(delete(ENTITY_API_URL_ID, tabelaFrete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TabelaFrete> tabelaFreteList = tabelaFreteRepository.findAll();
        assertThat(tabelaFreteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tabelaFreteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTabelaFrete() throws Exception {
        // Initialize the database
        tabelaFrete = tabelaFreteRepository.saveAndFlush(tabelaFrete);
        tabelaFreteSearchRepository.save(tabelaFrete);

        // Search the tabelaFrete
        restTabelaFreteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tabelaFrete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tabelaFrete.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].freteMinimo").value(hasItem(DEFAULT_FRETE_MINIMO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorTonelada").value(hasItem(DEFAULT_VALOR_TONELADA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorMetroCubico").value(hasItem(DEFAULT_VALOR_METRO_CUBICO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorUnidade").value(hasItem(DEFAULT_VALOR_UNIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].valorKm").value(hasItem(DEFAULT_VALOR_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].valorAdicional").value(hasItem(DEFAULT_VALOR_ADICIONAL.doubleValue())))
            .andExpect(jsonPath("$.[*].valorColeta").value(hasItem(DEFAULT_VALOR_COLETA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorEntrega").value(hasItem(DEFAULT_VALOR_ENTREGA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].valorKmAdicional").value(hasItem(DEFAULT_VALOR_KM_ADICIONAL.doubleValue())))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))));
    }
}
