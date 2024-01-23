package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.domain.enumeration.TipoTabelaFrete;
import br.com.revenuebrasil.newcargas.repository.TabelaFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TabelaFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TabelaFreteMapper;
import jakarta.persistence.EntityManager;
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
    private static final Integer SMALLER_LEAD_TIME = 1 - 1;

    private static final Double DEFAULT_FRETE_MINIMO = 1D;
    private static final Double UPDATED_FRETE_MINIMO = 2D;
    private static final Double SMALLER_FRETE_MINIMO = 1D - 1D;

    private static final Double DEFAULT_VALOR_TONELADA = 1D;
    private static final Double UPDATED_VALOR_TONELADA = 2D;
    private static final Double SMALLER_VALOR_TONELADA = 1D - 1D;

    private static final Double DEFAULT_VALOR_METRO_CUBICO = 1D;
    private static final Double UPDATED_VALOR_METRO_CUBICO = 2D;
    private static final Double SMALLER_VALOR_METRO_CUBICO = 1D - 1D;

    private static final Double DEFAULT_VALOR_UNIDADE = 1D;
    private static final Double UPDATED_VALOR_UNIDADE = 2D;
    private static final Double SMALLER_VALOR_UNIDADE = 1D - 1D;

    private static final Double DEFAULT_VALOR_KM = 1D;
    private static final Double UPDATED_VALOR_KM = 2D;
    private static final Double SMALLER_VALOR_KM = 1D - 1D;

    private static final Double DEFAULT_VALOR_ADICIONAL = 1D;
    private static final Double UPDATED_VALOR_ADICIONAL = 2D;
    private static final Double SMALLER_VALOR_ADICIONAL = 1D - 1D;

    private static final Double DEFAULT_VALOR_COLETA = 1D;
    private static final Double UPDATED_VALOR_COLETA = 2D;
    private static final Double SMALLER_VALOR_COLETA = 1D - 1D;

    private static final Double DEFAULT_VALOR_ENTREGA = 1D;
    private static final Double UPDATED_VALOR_ENTREGA = 2D;
    private static final Double SMALLER_VALOR_ENTREGA = 1D - 1D;

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;
    private static final Double SMALLER_VALOR_TOTAL = 1D - 1D;

    private static final Double DEFAULT_VALOR_KM_ADICIONAL = 1D;
    private static final Double UPDATED_VALOR_KM_ADICIONAL = 2D;
    private static final Double SMALLER_VALOR_KM_ADICIONAL = 1D - 1D;

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
            .valorKmAdicional(DEFAULT_VALOR_KM_ADICIONAL);
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
            .valorKmAdicional(UPDATED_VALOR_KM_ADICIONAL);
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
            .andExpect(jsonPath("$.[*].valorKmAdicional").value(hasItem(DEFAULT_VALOR_KM_ADICIONAL.doubleValue())));
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
            .andExpect(jsonPath("$.valorKmAdicional").value(DEFAULT_VALOR_KM_ADICIONAL.doubleValue()));
    }

    @Test
    @Transactional
    void getTabelaFretesByIdFiltering() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        Long id = tabelaFrete.getId();

        defaultTabelaFreteShouldBeFound("id.equals=" + id);
        defaultTabelaFreteShouldNotBeFound("id.notEquals=" + id);

        defaultTabelaFreteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTabelaFreteShouldNotBeFound("id.greaterThan=" + id);

        defaultTabelaFreteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTabelaFreteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where tipo equals to DEFAULT_TIPO
        defaultTabelaFreteShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the tabelaFreteList where tipo equals to UPDATED_TIPO
        defaultTabelaFreteShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultTabelaFreteShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the tabelaFreteList where tipo equals to UPDATED_TIPO
        defaultTabelaFreteShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where tipo is not null
        defaultTabelaFreteShouldBeFound("tipo.specified=true");

        // Get all the tabelaFreteList where tipo is null
        defaultTabelaFreteShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where nome equals to DEFAULT_NOME
        defaultTabelaFreteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the tabelaFreteList where nome equals to UPDATED_NOME
        defaultTabelaFreteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTabelaFreteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the tabelaFreteList where nome equals to UPDATED_NOME
        defaultTabelaFreteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where nome is not null
        defaultTabelaFreteShouldBeFound("nome.specified=true");

        // Get all the tabelaFreteList where nome is null
        defaultTabelaFreteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByNomeContainsSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where nome contains DEFAULT_NOME
        defaultTabelaFreteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the tabelaFreteList where nome contains UPDATED_NOME
        defaultTabelaFreteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where nome does not contain DEFAULT_NOME
        defaultTabelaFreteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the tabelaFreteList where nome does not contain UPDATED_NOME
        defaultTabelaFreteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where descricao equals to DEFAULT_DESCRICAO
        defaultTabelaFreteShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the tabelaFreteList where descricao equals to UPDATED_DESCRICAO
        defaultTabelaFreteShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultTabelaFreteShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the tabelaFreteList where descricao equals to UPDATED_DESCRICAO
        defaultTabelaFreteShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where descricao is not null
        defaultTabelaFreteShouldBeFound("descricao.specified=true");

        // Get all the tabelaFreteList where descricao is null
        defaultTabelaFreteShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where descricao contains DEFAULT_DESCRICAO
        defaultTabelaFreteShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the tabelaFreteList where descricao contains UPDATED_DESCRICAO
        defaultTabelaFreteShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where descricao does not contain DEFAULT_DESCRICAO
        defaultTabelaFreteShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the tabelaFreteList where descricao does not contain UPDATED_DESCRICAO
        defaultTabelaFreteShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime equals to DEFAULT_LEAD_TIME
        defaultTabelaFreteShouldBeFound("leadTime.equals=" + DEFAULT_LEAD_TIME);

        // Get all the tabelaFreteList where leadTime equals to UPDATED_LEAD_TIME
        defaultTabelaFreteShouldNotBeFound("leadTime.equals=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime in DEFAULT_LEAD_TIME or UPDATED_LEAD_TIME
        defaultTabelaFreteShouldBeFound("leadTime.in=" + DEFAULT_LEAD_TIME + "," + UPDATED_LEAD_TIME);

        // Get all the tabelaFreteList where leadTime equals to UPDATED_LEAD_TIME
        defaultTabelaFreteShouldNotBeFound("leadTime.in=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime is not null
        defaultTabelaFreteShouldBeFound("leadTime.specified=true");

        // Get all the tabelaFreteList where leadTime is null
        defaultTabelaFreteShouldNotBeFound("leadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime is greater than or equal to DEFAULT_LEAD_TIME
        defaultTabelaFreteShouldBeFound("leadTime.greaterThanOrEqual=" + DEFAULT_LEAD_TIME);

        // Get all the tabelaFreteList where leadTime is greater than or equal to (DEFAULT_LEAD_TIME + 1)
        defaultTabelaFreteShouldNotBeFound("leadTime.greaterThanOrEqual=" + (DEFAULT_LEAD_TIME + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime is less than or equal to DEFAULT_LEAD_TIME
        defaultTabelaFreteShouldBeFound("leadTime.lessThanOrEqual=" + DEFAULT_LEAD_TIME);

        // Get all the tabelaFreteList where leadTime is less than or equal to SMALLER_LEAD_TIME
        defaultTabelaFreteShouldNotBeFound("leadTime.lessThanOrEqual=" + SMALLER_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime is less than DEFAULT_LEAD_TIME
        defaultTabelaFreteShouldNotBeFound("leadTime.lessThan=" + DEFAULT_LEAD_TIME);

        // Get all the tabelaFreteList where leadTime is less than (DEFAULT_LEAD_TIME + 1)
        defaultTabelaFreteShouldBeFound("leadTime.lessThan=" + (DEFAULT_LEAD_TIME + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByLeadTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where leadTime is greater than DEFAULT_LEAD_TIME
        defaultTabelaFreteShouldNotBeFound("leadTime.greaterThan=" + DEFAULT_LEAD_TIME);

        // Get all the tabelaFreteList where leadTime is greater than SMALLER_LEAD_TIME
        defaultTabelaFreteShouldBeFound("leadTime.greaterThan=" + SMALLER_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo equals to DEFAULT_FRETE_MINIMO
        defaultTabelaFreteShouldBeFound("freteMinimo.equals=" + DEFAULT_FRETE_MINIMO);

        // Get all the tabelaFreteList where freteMinimo equals to UPDATED_FRETE_MINIMO
        defaultTabelaFreteShouldNotBeFound("freteMinimo.equals=" + UPDATED_FRETE_MINIMO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo in DEFAULT_FRETE_MINIMO or UPDATED_FRETE_MINIMO
        defaultTabelaFreteShouldBeFound("freteMinimo.in=" + DEFAULT_FRETE_MINIMO + "," + UPDATED_FRETE_MINIMO);

        // Get all the tabelaFreteList where freteMinimo equals to UPDATED_FRETE_MINIMO
        defaultTabelaFreteShouldNotBeFound("freteMinimo.in=" + UPDATED_FRETE_MINIMO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo is not null
        defaultTabelaFreteShouldBeFound("freteMinimo.specified=true");

        // Get all the tabelaFreteList where freteMinimo is null
        defaultTabelaFreteShouldNotBeFound("freteMinimo.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo is greater than or equal to DEFAULT_FRETE_MINIMO
        defaultTabelaFreteShouldBeFound("freteMinimo.greaterThanOrEqual=" + DEFAULT_FRETE_MINIMO);

        // Get all the tabelaFreteList where freteMinimo is greater than or equal to (DEFAULT_FRETE_MINIMO + 1)
        defaultTabelaFreteShouldNotBeFound("freteMinimo.greaterThanOrEqual=" + (DEFAULT_FRETE_MINIMO + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo is less than or equal to DEFAULT_FRETE_MINIMO
        defaultTabelaFreteShouldBeFound("freteMinimo.lessThanOrEqual=" + DEFAULT_FRETE_MINIMO);

        // Get all the tabelaFreteList where freteMinimo is less than or equal to SMALLER_FRETE_MINIMO
        defaultTabelaFreteShouldNotBeFound("freteMinimo.lessThanOrEqual=" + SMALLER_FRETE_MINIMO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo is less than DEFAULT_FRETE_MINIMO
        defaultTabelaFreteShouldNotBeFound("freteMinimo.lessThan=" + DEFAULT_FRETE_MINIMO);

        // Get all the tabelaFreteList where freteMinimo is less than (DEFAULT_FRETE_MINIMO + 1)
        defaultTabelaFreteShouldBeFound("freteMinimo.lessThan=" + (DEFAULT_FRETE_MINIMO + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFreteMinimoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where freteMinimo is greater than DEFAULT_FRETE_MINIMO
        defaultTabelaFreteShouldNotBeFound("freteMinimo.greaterThan=" + DEFAULT_FRETE_MINIMO);

        // Get all the tabelaFreteList where freteMinimo is greater than SMALLER_FRETE_MINIMO
        defaultTabelaFreteShouldBeFound("freteMinimo.greaterThan=" + SMALLER_FRETE_MINIMO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada equals to DEFAULT_VALOR_TONELADA
        defaultTabelaFreteShouldBeFound("valorTonelada.equals=" + DEFAULT_VALOR_TONELADA);

        // Get all the tabelaFreteList where valorTonelada equals to UPDATED_VALOR_TONELADA
        defaultTabelaFreteShouldNotBeFound("valorTonelada.equals=" + UPDATED_VALOR_TONELADA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada in DEFAULT_VALOR_TONELADA or UPDATED_VALOR_TONELADA
        defaultTabelaFreteShouldBeFound("valorTonelada.in=" + DEFAULT_VALOR_TONELADA + "," + UPDATED_VALOR_TONELADA);

        // Get all the tabelaFreteList where valorTonelada equals to UPDATED_VALOR_TONELADA
        defaultTabelaFreteShouldNotBeFound("valorTonelada.in=" + UPDATED_VALOR_TONELADA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada is not null
        defaultTabelaFreteShouldBeFound("valorTonelada.specified=true");

        // Get all the tabelaFreteList where valorTonelada is null
        defaultTabelaFreteShouldNotBeFound("valorTonelada.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada is greater than or equal to DEFAULT_VALOR_TONELADA
        defaultTabelaFreteShouldBeFound("valorTonelada.greaterThanOrEqual=" + DEFAULT_VALOR_TONELADA);

        // Get all the tabelaFreteList where valorTonelada is greater than or equal to (DEFAULT_VALOR_TONELADA + 1)
        defaultTabelaFreteShouldNotBeFound("valorTonelada.greaterThanOrEqual=" + (DEFAULT_VALOR_TONELADA + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada is less than or equal to DEFAULT_VALOR_TONELADA
        defaultTabelaFreteShouldBeFound("valorTonelada.lessThanOrEqual=" + DEFAULT_VALOR_TONELADA);

        // Get all the tabelaFreteList where valorTonelada is less than or equal to SMALLER_VALOR_TONELADA
        defaultTabelaFreteShouldNotBeFound("valorTonelada.lessThanOrEqual=" + SMALLER_VALOR_TONELADA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada is less than DEFAULT_VALOR_TONELADA
        defaultTabelaFreteShouldNotBeFound("valorTonelada.lessThan=" + DEFAULT_VALOR_TONELADA);

        // Get all the tabelaFreteList where valorTonelada is less than (DEFAULT_VALOR_TONELADA + 1)
        defaultTabelaFreteShouldBeFound("valorTonelada.lessThan=" + (DEFAULT_VALOR_TONELADA + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorToneladaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTonelada is greater than DEFAULT_VALOR_TONELADA
        defaultTabelaFreteShouldNotBeFound("valorTonelada.greaterThan=" + DEFAULT_VALOR_TONELADA);

        // Get all the tabelaFreteList where valorTonelada is greater than SMALLER_VALOR_TONELADA
        defaultTabelaFreteShouldBeFound("valorTonelada.greaterThan=" + SMALLER_VALOR_TONELADA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico equals to DEFAULT_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldBeFound("valorMetroCubico.equals=" + DEFAULT_VALOR_METRO_CUBICO);

        // Get all the tabelaFreteList where valorMetroCubico equals to UPDATED_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.equals=" + UPDATED_VALOR_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico in DEFAULT_VALOR_METRO_CUBICO or UPDATED_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldBeFound("valorMetroCubico.in=" + DEFAULT_VALOR_METRO_CUBICO + "," + UPDATED_VALOR_METRO_CUBICO);

        // Get all the tabelaFreteList where valorMetroCubico equals to UPDATED_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.in=" + UPDATED_VALOR_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico is not null
        defaultTabelaFreteShouldBeFound("valorMetroCubico.specified=true");

        // Get all the tabelaFreteList where valorMetroCubico is null
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico is greater than or equal to DEFAULT_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldBeFound("valorMetroCubico.greaterThanOrEqual=" + DEFAULT_VALOR_METRO_CUBICO);

        // Get all the tabelaFreteList where valorMetroCubico is greater than or equal to (DEFAULT_VALOR_METRO_CUBICO + 1)
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.greaterThanOrEqual=" + (DEFAULT_VALOR_METRO_CUBICO + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico is less than or equal to DEFAULT_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldBeFound("valorMetroCubico.lessThanOrEqual=" + DEFAULT_VALOR_METRO_CUBICO);

        // Get all the tabelaFreteList where valorMetroCubico is less than or equal to SMALLER_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.lessThanOrEqual=" + SMALLER_VALOR_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico is less than DEFAULT_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.lessThan=" + DEFAULT_VALOR_METRO_CUBICO);

        // Get all the tabelaFreteList where valorMetroCubico is less than (DEFAULT_VALOR_METRO_CUBICO + 1)
        defaultTabelaFreteShouldBeFound("valorMetroCubico.lessThan=" + (DEFAULT_VALOR_METRO_CUBICO + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorMetroCubicoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorMetroCubico is greater than DEFAULT_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldNotBeFound("valorMetroCubico.greaterThan=" + DEFAULT_VALOR_METRO_CUBICO);

        // Get all the tabelaFreteList where valorMetroCubico is greater than SMALLER_VALOR_METRO_CUBICO
        defaultTabelaFreteShouldBeFound("valorMetroCubico.greaterThan=" + SMALLER_VALOR_METRO_CUBICO);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade equals to DEFAULT_VALOR_UNIDADE
        defaultTabelaFreteShouldBeFound("valorUnidade.equals=" + DEFAULT_VALOR_UNIDADE);

        // Get all the tabelaFreteList where valorUnidade equals to UPDATED_VALOR_UNIDADE
        defaultTabelaFreteShouldNotBeFound("valorUnidade.equals=" + UPDATED_VALOR_UNIDADE);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade in DEFAULT_VALOR_UNIDADE or UPDATED_VALOR_UNIDADE
        defaultTabelaFreteShouldBeFound("valorUnidade.in=" + DEFAULT_VALOR_UNIDADE + "," + UPDATED_VALOR_UNIDADE);

        // Get all the tabelaFreteList where valorUnidade equals to UPDATED_VALOR_UNIDADE
        defaultTabelaFreteShouldNotBeFound("valorUnidade.in=" + UPDATED_VALOR_UNIDADE);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade is not null
        defaultTabelaFreteShouldBeFound("valorUnidade.specified=true");

        // Get all the tabelaFreteList where valorUnidade is null
        defaultTabelaFreteShouldNotBeFound("valorUnidade.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade is greater than or equal to DEFAULT_VALOR_UNIDADE
        defaultTabelaFreteShouldBeFound("valorUnidade.greaterThanOrEqual=" + DEFAULT_VALOR_UNIDADE);

        // Get all the tabelaFreteList where valorUnidade is greater than or equal to (DEFAULT_VALOR_UNIDADE + 1)
        defaultTabelaFreteShouldNotBeFound("valorUnidade.greaterThanOrEqual=" + (DEFAULT_VALOR_UNIDADE + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade is less than or equal to DEFAULT_VALOR_UNIDADE
        defaultTabelaFreteShouldBeFound("valorUnidade.lessThanOrEqual=" + DEFAULT_VALOR_UNIDADE);

        // Get all the tabelaFreteList where valorUnidade is less than or equal to SMALLER_VALOR_UNIDADE
        defaultTabelaFreteShouldNotBeFound("valorUnidade.lessThanOrEqual=" + SMALLER_VALOR_UNIDADE);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade is less than DEFAULT_VALOR_UNIDADE
        defaultTabelaFreteShouldNotBeFound("valorUnidade.lessThan=" + DEFAULT_VALOR_UNIDADE);

        // Get all the tabelaFreteList where valorUnidade is less than (DEFAULT_VALOR_UNIDADE + 1)
        defaultTabelaFreteShouldBeFound("valorUnidade.lessThan=" + (DEFAULT_VALOR_UNIDADE + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorUnidadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorUnidade is greater than DEFAULT_VALOR_UNIDADE
        defaultTabelaFreteShouldNotBeFound("valorUnidade.greaterThan=" + DEFAULT_VALOR_UNIDADE);

        // Get all the tabelaFreteList where valorUnidade is greater than SMALLER_VALOR_UNIDADE
        defaultTabelaFreteShouldBeFound("valorUnidade.greaterThan=" + SMALLER_VALOR_UNIDADE);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm equals to DEFAULT_VALOR_KM
        defaultTabelaFreteShouldBeFound("valorKm.equals=" + DEFAULT_VALOR_KM);

        // Get all the tabelaFreteList where valorKm equals to UPDATED_VALOR_KM
        defaultTabelaFreteShouldNotBeFound("valorKm.equals=" + UPDATED_VALOR_KM);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm in DEFAULT_VALOR_KM or UPDATED_VALOR_KM
        defaultTabelaFreteShouldBeFound("valorKm.in=" + DEFAULT_VALOR_KM + "," + UPDATED_VALOR_KM);

        // Get all the tabelaFreteList where valorKm equals to UPDATED_VALOR_KM
        defaultTabelaFreteShouldNotBeFound("valorKm.in=" + UPDATED_VALOR_KM);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm is not null
        defaultTabelaFreteShouldBeFound("valorKm.specified=true");

        // Get all the tabelaFreteList where valorKm is null
        defaultTabelaFreteShouldNotBeFound("valorKm.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm is greater than or equal to DEFAULT_VALOR_KM
        defaultTabelaFreteShouldBeFound("valorKm.greaterThanOrEqual=" + DEFAULT_VALOR_KM);

        // Get all the tabelaFreteList where valorKm is greater than or equal to (DEFAULT_VALOR_KM + 1)
        defaultTabelaFreteShouldNotBeFound("valorKm.greaterThanOrEqual=" + (DEFAULT_VALOR_KM + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm is less than or equal to DEFAULT_VALOR_KM
        defaultTabelaFreteShouldBeFound("valorKm.lessThanOrEqual=" + DEFAULT_VALOR_KM);

        // Get all the tabelaFreteList where valorKm is less than or equal to SMALLER_VALOR_KM
        defaultTabelaFreteShouldNotBeFound("valorKm.lessThanOrEqual=" + SMALLER_VALOR_KM);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm is less than DEFAULT_VALOR_KM
        defaultTabelaFreteShouldNotBeFound("valorKm.lessThan=" + DEFAULT_VALOR_KM);

        // Get all the tabelaFreteList where valorKm is less than (DEFAULT_VALOR_KM + 1)
        defaultTabelaFreteShouldBeFound("valorKm.lessThan=" + (DEFAULT_VALOR_KM + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKm is greater than DEFAULT_VALOR_KM
        defaultTabelaFreteShouldNotBeFound("valorKm.greaterThan=" + DEFAULT_VALOR_KM);

        // Get all the tabelaFreteList where valorKm is greater than SMALLER_VALOR_KM
        defaultTabelaFreteShouldBeFound("valorKm.greaterThan=" + SMALLER_VALOR_KM);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional equals to DEFAULT_VALOR_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorAdicional.equals=" + DEFAULT_VALOR_ADICIONAL);

        // Get all the tabelaFreteList where valorAdicional equals to UPDATED_VALOR_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorAdicional.equals=" + UPDATED_VALOR_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional in DEFAULT_VALOR_ADICIONAL or UPDATED_VALOR_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorAdicional.in=" + DEFAULT_VALOR_ADICIONAL + "," + UPDATED_VALOR_ADICIONAL);

        // Get all the tabelaFreteList where valorAdicional equals to UPDATED_VALOR_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorAdicional.in=" + UPDATED_VALOR_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional is not null
        defaultTabelaFreteShouldBeFound("valorAdicional.specified=true");

        // Get all the tabelaFreteList where valorAdicional is null
        defaultTabelaFreteShouldNotBeFound("valorAdicional.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional is greater than or equal to DEFAULT_VALOR_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorAdicional.greaterThanOrEqual=" + DEFAULT_VALOR_ADICIONAL);

        // Get all the tabelaFreteList where valorAdicional is greater than or equal to (DEFAULT_VALOR_ADICIONAL + 1)
        defaultTabelaFreteShouldNotBeFound("valorAdicional.greaterThanOrEqual=" + (DEFAULT_VALOR_ADICIONAL + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional is less than or equal to DEFAULT_VALOR_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorAdicional.lessThanOrEqual=" + DEFAULT_VALOR_ADICIONAL);

        // Get all the tabelaFreteList where valorAdicional is less than or equal to SMALLER_VALOR_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorAdicional.lessThanOrEqual=" + SMALLER_VALOR_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional is less than DEFAULT_VALOR_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorAdicional.lessThan=" + DEFAULT_VALOR_ADICIONAL);

        // Get all the tabelaFreteList where valorAdicional is less than (DEFAULT_VALOR_ADICIONAL + 1)
        defaultTabelaFreteShouldBeFound("valorAdicional.lessThan=" + (DEFAULT_VALOR_ADICIONAL + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorAdicionalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorAdicional is greater than DEFAULT_VALOR_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorAdicional.greaterThan=" + DEFAULT_VALOR_ADICIONAL);

        // Get all the tabelaFreteList where valorAdicional is greater than SMALLER_VALOR_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorAdicional.greaterThan=" + SMALLER_VALOR_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta equals to DEFAULT_VALOR_COLETA
        defaultTabelaFreteShouldBeFound("valorColeta.equals=" + DEFAULT_VALOR_COLETA);

        // Get all the tabelaFreteList where valorColeta equals to UPDATED_VALOR_COLETA
        defaultTabelaFreteShouldNotBeFound("valorColeta.equals=" + UPDATED_VALOR_COLETA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta in DEFAULT_VALOR_COLETA or UPDATED_VALOR_COLETA
        defaultTabelaFreteShouldBeFound("valorColeta.in=" + DEFAULT_VALOR_COLETA + "," + UPDATED_VALOR_COLETA);

        // Get all the tabelaFreteList where valorColeta equals to UPDATED_VALOR_COLETA
        defaultTabelaFreteShouldNotBeFound("valorColeta.in=" + UPDATED_VALOR_COLETA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta is not null
        defaultTabelaFreteShouldBeFound("valorColeta.specified=true");

        // Get all the tabelaFreteList where valorColeta is null
        defaultTabelaFreteShouldNotBeFound("valorColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta is greater than or equal to DEFAULT_VALOR_COLETA
        defaultTabelaFreteShouldBeFound("valorColeta.greaterThanOrEqual=" + DEFAULT_VALOR_COLETA);

        // Get all the tabelaFreteList where valorColeta is greater than or equal to (DEFAULT_VALOR_COLETA + 1)
        defaultTabelaFreteShouldNotBeFound("valorColeta.greaterThanOrEqual=" + (DEFAULT_VALOR_COLETA + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta is less than or equal to DEFAULT_VALOR_COLETA
        defaultTabelaFreteShouldBeFound("valorColeta.lessThanOrEqual=" + DEFAULT_VALOR_COLETA);

        // Get all the tabelaFreteList where valorColeta is less than or equal to SMALLER_VALOR_COLETA
        defaultTabelaFreteShouldNotBeFound("valorColeta.lessThanOrEqual=" + SMALLER_VALOR_COLETA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta is less than DEFAULT_VALOR_COLETA
        defaultTabelaFreteShouldNotBeFound("valorColeta.lessThan=" + DEFAULT_VALOR_COLETA);

        // Get all the tabelaFreteList where valorColeta is less than (DEFAULT_VALOR_COLETA + 1)
        defaultTabelaFreteShouldBeFound("valorColeta.lessThan=" + (DEFAULT_VALOR_COLETA + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorColetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorColeta is greater than DEFAULT_VALOR_COLETA
        defaultTabelaFreteShouldNotBeFound("valorColeta.greaterThan=" + DEFAULT_VALOR_COLETA);

        // Get all the tabelaFreteList where valorColeta is greater than SMALLER_VALOR_COLETA
        defaultTabelaFreteShouldBeFound("valorColeta.greaterThan=" + SMALLER_VALOR_COLETA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega equals to DEFAULT_VALOR_ENTREGA
        defaultTabelaFreteShouldBeFound("valorEntrega.equals=" + DEFAULT_VALOR_ENTREGA);

        // Get all the tabelaFreteList where valorEntrega equals to UPDATED_VALOR_ENTREGA
        defaultTabelaFreteShouldNotBeFound("valorEntrega.equals=" + UPDATED_VALOR_ENTREGA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega in DEFAULT_VALOR_ENTREGA or UPDATED_VALOR_ENTREGA
        defaultTabelaFreteShouldBeFound("valorEntrega.in=" + DEFAULT_VALOR_ENTREGA + "," + UPDATED_VALOR_ENTREGA);

        // Get all the tabelaFreteList where valorEntrega equals to UPDATED_VALOR_ENTREGA
        defaultTabelaFreteShouldNotBeFound("valorEntrega.in=" + UPDATED_VALOR_ENTREGA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega is not null
        defaultTabelaFreteShouldBeFound("valorEntrega.specified=true");

        // Get all the tabelaFreteList where valorEntrega is null
        defaultTabelaFreteShouldNotBeFound("valorEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega is greater than or equal to DEFAULT_VALOR_ENTREGA
        defaultTabelaFreteShouldBeFound("valorEntrega.greaterThanOrEqual=" + DEFAULT_VALOR_ENTREGA);

        // Get all the tabelaFreteList where valorEntrega is greater than or equal to (DEFAULT_VALOR_ENTREGA + 1)
        defaultTabelaFreteShouldNotBeFound("valorEntrega.greaterThanOrEqual=" + (DEFAULT_VALOR_ENTREGA + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega is less than or equal to DEFAULT_VALOR_ENTREGA
        defaultTabelaFreteShouldBeFound("valorEntrega.lessThanOrEqual=" + DEFAULT_VALOR_ENTREGA);

        // Get all the tabelaFreteList where valorEntrega is less than or equal to SMALLER_VALOR_ENTREGA
        defaultTabelaFreteShouldNotBeFound("valorEntrega.lessThanOrEqual=" + SMALLER_VALOR_ENTREGA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega is less than DEFAULT_VALOR_ENTREGA
        defaultTabelaFreteShouldNotBeFound("valorEntrega.lessThan=" + DEFAULT_VALOR_ENTREGA);

        // Get all the tabelaFreteList where valorEntrega is less than (DEFAULT_VALOR_ENTREGA + 1)
        defaultTabelaFreteShouldBeFound("valorEntrega.lessThan=" + (DEFAULT_VALOR_ENTREGA + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorEntregaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorEntrega is greater than DEFAULT_VALOR_ENTREGA
        defaultTabelaFreteShouldNotBeFound("valorEntrega.greaterThan=" + DEFAULT_VALOR_ENTREGA);

        // Get all the tabelaFreteList where valorEntrega is greater than SMALLER_VALOR_ENTREGA
        defaultTabelaFreteShouldBeFound("valorEntrega.greaterThan=" + SMALLER_VALOR_ENTREGA);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal equals to DEFAULT_VALOR_TOTAL
        defaultTabelaFreteShouldBeFound("valorTotal.equals=" + DEFAULT_VALOR_TOTAL);

        // Get all the tabelaFreteList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultTabelaFreteShouldNotBeFound("valorTotal.equals=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal in DEFAULT_VALOR_TOTAL or UPDATED_VALOR_TOTAL
        defaultTabelaFreteShouldBeFound("valorTotal.in=" + DEFAULT_VALOR_TOTAL + "," + UPDATED_VALOR_TOTAL);

        // Get all the tabelaFreteList where valorTotal equals to UPDATED_VALOR_TOTAL
        defaultTabelaFreteShouldNotBeFound("valorTotal.in=" + UPDATED_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal is not null
        defaultTabelaFreteShouldBeFound("valorTotal.specified=true");

        // Get all the tabelaFreteList where valorTotal is null
        defaultTabelaFreteShouldNotBeFound("valorTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal is greater than or equal to DEFAULT_VALOR_TOTAL
        defaultTabelaFreteShouldBeFound("valorTotal.greaterThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the tabelaFreteList where valorTotal is greater than or equal to (DEFAULT_VALOR_TOTAL + 1)
        defaultTabelaFreteShouldNotBeFound("valorTotal.greaterThanOrEqual=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal is less than or equal to DEFAULT_VALOR_TOTAL
        defaultTabelaFreteShouldBeFound("valorTotal.lessThanOrEqual=" + DEFAULT_VALOR_TOTAL);

        // Get all the tabelaFreteList where valorTotal is less than or equal to SMALLER_VALOR_TOTAL
        defaultTabelaFreteShouldNotBeFound("valorTotal.lessThanOrEqual=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal is less than DEFAULT_VALOR_TOTAL
        defaultTabelaFreteShouldNotBeFound("valorTotal.lessThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the tabelaFreteList where valorTotal is less than (DEFAULT_VALOR_TOTAL + 1)
        defaultTabelaFreteShouldBeFound("valorTotal.lessThan=" + (DEFAULT_VALOR_TOTAL + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorTotal is greater than DEFAULT_VALOR_TOTAL
        defaultTabelaFreteShouldNotBeFound("valorTotal.greaterThan=" + DEFAULT_VALOR_TOTAL);

        // Get all the tabelaFreteList where valorTotal is greater than SMALLER_VALOR_TOTAL
        defaultTabelaFreteShouldBeFound("valorTotal.greaterThan=" + SMALLER_VALOR_TOTAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional equals to DEFAULT_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorKmAdicional.equals=" + DEFAULT_VALOR_KM_ADICIONAL);

        // Get all the tabelaFreteList where valorKmAdicional equals to UPDATED_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.equals=" + UPDATED_VALOR_KM_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsInShouldWork() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional in DEFAULT_VALOR_KM_ADICIONAL or UPDATED_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorKmAdicional.in=" + DEFAULT_VALOR_KM_ADICIONAL + "," + UPDATED_VALOR_KM_ADICIONAL);

        // Get all the tabelaFreteList where valorKmAdicional equals to UPDATED_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.in=" + UPDATED_VALOR_KM_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional is not null
        defaultTabelaFreteShouldBeFound("valorKmAdicional.specified=true");

        // Get all the tabelaFreteList where valorKmAdicional is null
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.specified=false");
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional is greater than or equal to DEFAULT_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorKmAdicional.greaterThanOrEqual=" + DEFAULT_VALOR_KM_ADICIONAL);

        // Get all the tabelaFreteList where valorKmAdicional is greater than or equal to (DEFAULT_VALOR_KM_ADICIONAL + 1)
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.greaterThanOrEqual=" + (DEFAULT_VALOR_KM_ADICIONAL + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional is less than or equal to DEFAULT_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorKmAdicional.lessThanOrEqual=" + DEFAULT_VALOR_KM_ADICIONAL);

        // Get all the tabelaFreteList where valorKmAdicional is less than or equal to SMALLER_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.lessThanOrEqual=" + SMALLER_VALOR_KM_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsLessThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional is less than DEFAULT_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.lessThan=" + DEFAULT_VALOR_KM_ADICIONAL);

        // Get all the tabelaFreteList where valorKmAdicional is less than (DEFAULT_VALOR_KM_ADICIONAL + 1)
        defaultTabelaFreteShouldBeFound("valorKmAdicional.lessThan=" + (DEFAULT_VALOR_KM_ADICIONAL + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByValorKmAdicionalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tabelaFreteRepository.saveAndFlush(tabelaFrete);

        // Get all the tabelaFreteList where valorKmAdicional is greater than DEFAULT_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldNotBeFound("valorKmAdicional.greaterThan=" + DEFAULT_VALOR_KM_ADICIONAL);

        // Get all the tabelaFreteList where valorKmAdicional is greater than SMALLER_VALOR_KM_ADICIONAL
        defaultTabelaFreteShouldBeFound("valorKmAdicional.greaterThan=" + SMALLER_VALOR_KM_ADICIONAL);
    }

    @Test
    @Transactional
    void getAllTabelaFretesByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        tabelaFrete.setEmbarcador(embarcador);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long embarcadorId = embarcador.getId();
        // Get all the tabelaFreteList where embarcador equals to embarcadorId
        defaultTabelaFreteShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the tabelaFreteList where embarcador equals to (embarcadorId + 1)
        defaultTabelaFreteShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        tabelaFrete.setTransportadora(transportadora);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long transportadoraId = transportadora.getId();
        // Get all the tabelaFreteList where transportadora equals to transportadoraId
        defaultTabelaFreteShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the tabelaFreteList where transportadora equals to (transportadoraId + 1)
        defaultTabelaFreteShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByTipoCargaIsEqualToSomething() throws Exception {
        TipoCarga tipoCarga;
        if (TestUtil.findAll(em, TipoCarga.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            tipoCarga = TipoCargaResourceIT.createEntity(em);
        } else {
            tipoCarga = TestUtil.findAll(em, TipoCarga.class).get(0);
        }
        em.persist(tipoCarga);
        em.flush();
        tabelaFrete.setTipoCarga(tipoCarga);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long tipoCargaId = tipoCarga.getId();
        // Get all the tabelaFreteList where tipoCarga equals to tipoCargaId
        defaultTabelaFreteShouldBeFound("tipoCargaId.equals=" + tipoCargaId);

        // Get all the tabelaFreteList where tipoCarga equals to (tipoCargaId + 1)
        defaultTabelaFreteShouldNotBeFound("tipoCargaId.equals=" + (tipoCargaId + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByTipoFreteIsEqualToSomething() throws Exception {
        TipoFrete tipoFrete;
        if (TestUtil.findAll(em, TipoFrete.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            tipoFrete = TipoFreteResourceIT.createEntity(em);
        } else {
            tipoFrete = TestUtil.findAll(em, TipoFrete.class).get(0);
        }
        em.persist(tipoFrete);
        em.flush();
        tabelaFrete.setTipoFrete(tipoFrete);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long tipoFreteId = tipoFrete.getId();
        // Get all the tabelaFreteList where tipoFrete equals to tipoFreteId
        defaultTabelaFreteShouldBeFound("tipoFreteId.equals=" + tipoFreteId);

        // Get all the tabelaFreteList where tipoFrete equals to (tipoFreteId + 1)
        defaultTabelaFreteShouldNotBeFound("tipoFreteId.equals=" + (tipoFreteId + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByFormaCobrancaIsEqualToSomething() throws Exception {
        FormaCobranca formaCobranca;
        if (TestUtil.findAll(em, FormaCobranca.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            formaCobranca = FormaCobrancaResourceIT.createEntity(em);
        } else {
            formaCobranca = TestUtil.findAll(em, FormaCobranca.class).get(0);
        }
        em.persist(formaCobranca);
        em.flush();
        tabelaFrete.setFormaCobranca(formaCobranca);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long formaCobrancaId = formaCobranca.getId();
        // Get all the tabelaFreteList where formaCobranca equals to formaCobrancaId
        defaultTabelaFreteShouldBeFound("formaCobrancaId.equals=" + formaCobrancaId);

        // Get all the tabelaFreteList where formaCobranca equals to (formaCobrancaId + 1)
        defaultTabelaFreteShouldNotBeFound("formaCobrancaId.equals=" + (formaCobrancaId + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByRegiaoOrigemIsEqualToSomething() throws Exception {
        Regiao regiaoOrigem;
        if (TestUtil.findAll(em, Regiao.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            regiaoOrigem = RegiaoResourceIT.createEntity(em);
        } else {
            regiaoOrigem = TestUtil.findAll(em, Regiao.class).get(0);
        }
        em.persist(regiaoOrigem);
        em.flush();
        tabelaFrete.setRegiaoOrigem(regiaoOrigem);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long regiaoOrigemId = regiaoOrigem.getId();
        // Get all the tabelaFreteList where regiaoOrigem equals to regiaoOrigemId
        defaultTabelaFreteShouldBeFound("regiaoOrigemId.equals=" + regiaoOrigemId);

        // Get all the tabelaFreteList where regiaoOrigem equals to (regiaoOrigemId + 1)
        defaultTabelaFreteShouldNotBeFound("regiaoOrigemId.equals=" + (regiaoOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllTabelaFretesByRegiaoDestinoIsEqualToSomething() throws Exception {
        Regiao regiaoDestino;
        if (TestUtil.findAll(em, Regiao.class).isEmpty()) {
            tabelaFreteRepository.saveAndFlush(tabelaFrete);
            regiaoDestino = RegiaoResourceIT.createEntity(em);
        } else {
            regiaoDestino = TestUtil.findAll(em, Regiao.class).get(0);
        }
        em.persist(regiaoDestino);
        em.flush();
        tabelaFrete.setRegiaoDestino(regiaoDestino);
        tabelaFreteRepository.saveAndFlush(tabelaFrete);
        Long regiaoDestinoId = regiaoDestino.getId();
        // Get all the tabelaFreteList where regiaoDestino equals to regiaoDestinoId
        defaultTabelaFreteShouldBeFound("regiaoDestinoId.equals=" + regiaoDestinoId);

        // Get all the tabelaFreteList where regiaoDestino equals to (regiaoDestinoId + 1)
        defaultTabelaFreteShouldNotBeFound("regiaoDestinoId.equals=" + (regiaoDestinoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTabelaFreteShouldBeFound(String filter) throws Exception {
        restTabelaFreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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
            .andExpect(jsonPath("$.[*].valorKmAdicional").value(hasItem(DEFAULT_VALOR_KM_ADICIONAL.doubleValue())));

        // Check, that the count call also returns 1
        restTabelaFreteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTabelaFreteShouldNotBeFound(String filter) throws Exception {
        restTabelaFreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTabelaFreteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .valorKmAdicional(UPDATED_VALOR_KM_ADICIONAL);
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
            .valorTotal(UPDATED_VALOR_TOTAL);

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
            .valorKmAdicional(UPDATED_VALOR_KM_ADICIONAL);

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
            .andExpect(jsonPath("$.[*].valorKmAdicional").value(hasItem(DEFAULT_VALOR_KM_ADICIONAL.doubleValue())));
    }
}
