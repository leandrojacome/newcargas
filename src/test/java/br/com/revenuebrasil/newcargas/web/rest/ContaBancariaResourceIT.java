package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Banco;
import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.ContaBancariaRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContaBancariaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContaBancariaMapper;
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
 * Integration tests for the {@link ContaBancariaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContaBancariaResourceIT {

    private static final String DEFAULT_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_AGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_CONTA = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_PIX = "AAAAAAAAAA";
    private static final String UPDATED_PIX = "BBBBBBBBBB";

    private static final String DEFAULT_TITULAR = "AAAAAAAAAA";
    private static final String UPDATED_TITULAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conta-bancarias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/conta-bancarias/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private ContaBancariaMapper contaBancariaMapper;

    @Autowired
    private ContaBancariaSearchRepository contaBancariaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaBancariaMockMvc;

    private ContaBancaria contaBancaria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaBancaria createEntity(EntityManager em) {
        ContaBancaria contaBancaria = new ContaBancaria()
            .agencia(DEFAULT_AGENCIA)
            .conta(DEFAULT_CONTA)
            .observacao(DEFAULT_OBSERVACAO)
            .tipo(DEFAULT_TIPO)
            .pix(DEFAULT_PIX)
            .titular(DEFAULT_TITULAR);
        return contaBancaria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaBancaria createUpdatedEntity(EntityManager em) {
        ContaBancaria contaBancaria = new ContaBancaria()
            .agencia(UPDATED_AGENCIA)
            .conta(UPDATED_CONTA)
            .observacao(UPDATED_OBSERVACAO)
            .tipo(UPDATED_TIPO)
            .pix(UPDATED_PIX)
            .titular(UPDATED_TITULAR);
        return contaBancaria;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        contaBancariaSearchRepository.deleteAll();
        assertThat(contaBancariaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        contaBancaria = createEntity(em);
    }

    @Test
    @Transactional
    void createContaBancaria() throws Exception {
        int databaseSizeBeforeCreate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);
        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(DEFAULT_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(DEFAULT_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(DEFAULT_TITULAR);
    }

    @Test
    @Transactional
    void createContaBancariaWithExistingId() throws Exception {
        // Create the ContaBancaria with an existing ID
        contaBancaria.setId(1L);
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        int databaseSizeBeforeCreate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAgenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // set the field null
        contaBancaria.setAgencia(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkContaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // set the field null
        contaBancaria.setConta(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllContaBancarias() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].pix").value(hasItem(DEFAULT_PIX)))
            .andExpect(jsonPath("$.[*].titular").value(hasItem(DEFAULT_TITULAR)));
    }

    @Test
    @Transactional
    void getContaBancaria() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get the contaBancaria
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL_ID, contaBancaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaBancaria.getId().intValue()))
            .andExpect(jsonPath("$.agencia").value(DEFAULT_AGENCIA))
            .andExpect(jsonPath("$.conta").value(DEFAULT_CONTA))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.pix").value(DEFAULT_PIX))
            .andExpect(jsonPath("$.titular").value(DEFAULT_TITULAR));
    }

    @Test
    @Transactional
    void getContaBancariasByIdFiltering() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        Long id = contaBancaria.getId();

        defaultContaBancariaShouldBeFound("id.equals=" + id);
        defaultContaBancariaShouldNotBeFound("id.notEquals=" + id);

        defaultContaBancariaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContaBancariaShouldNotBeFound("id.greaterThan=" + id);

        defaultContaBancariaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContaBancariaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia equals to DEFAULT_AGENCIA
        defaultContaBancariaShouldBeFound("agencia.equals=" + DEFAULT_AGENCIA);

        // Get all the contaBancariaList where agencia equals to UPDATED_AGENCIA
        defaultContaBancariaShouldNotBeFound("agencia.equals=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaIsInShouldWork() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia in DEFAULT_AGENCIA or UPDATED_AGENCIA
        defaultContaBancariaShouldBeFound("agencia.in=" + DEFAULT_AGENCIA + "," + UPDATED_AGENCIA);

        // Get all the contaBancariaList where agencia equals to UPDATED_AGENCIA
        defaultContaBancariaShouldNotBeFound("agencia.in=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia is not null
        defaultContaBancariaShouldBeFound("agencia.specified=true");

        // Get all the contaBancariaList where agencia is null
        defaultContaBancariaShouldNotBeFound("agencia.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia contains DEFAULT_AGENCIA
        defaultContaBancariaShouldBeFound("agencia.contains=" + DEFAULT_AGENCIA);

        // Get all the contaBancariaList where agencia contains UPDATED_AGENCIA
        defaultContaBancariaShouldNotBeFound("agencia.contains=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaNotContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia does not contain DEFAULT_AGENCIA
        defaultContaBancariaShouldNotBeFound("agencia.doesNotContain=" + DEFAULT_AGENCIA);

        // Get all the contaBancariaList where agencia does not contain UPDATED_AGENCIA
        defaultContaBancariaShouldBeFound("agencia.doesNotContain=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByContaIsEqualToSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where conta equals to DEFAULT_CONTA
        defaultContaBancariaShouldBeFound("conta.equals=" + DEFAULT_CONTA);

        // Get all the contaBancariaList where conta equals to UPDATED_CONTA
        defaultContaBancariaShouldNotBeFound("conta.equals=" + UPDATED_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByContaIsInShouldWork() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where conta in DEFAULT_CONTA or UPDATED_CONTA
        defaultContaBancariaShouldBeFound("conta.in=" + DEFAULT_CONTA + "," + UPDATED_CONTA);

        // Get all the contaBancariaList where conta equals to UPDATED_CONTA
        defaultContaBancariaShouldNotBeFound("conta.in=" + UPDATED_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByContaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where conta is not null
        defaultContaBancariaShouldBeFound("conta.specified=true");

        // Get all the contaBancariaList where conta is null
        defaultContaBancariaShouldNotBeFound("conta.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByContaContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where conta contains DEFAULT_CONTA
        defaultContaBancariaShouldBeFound("conta.contains=" + DEFAULT_CONTA);

        // Get all the contaBancariaList where conta contains UPDATED_CONTA
        defaultContaBancariaShouldNotBeFound("conta.contains=" + UPDATED_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByContaNotContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where conta does not contain DEFAULT_CONTA
        defaultContaBancariaShouldNotBeFound("conta.doesNotContain=" + DEFAULT_CONTA);

        // Get all the contaBancariaList where conta does not contain UPDATED_CONTA
        defaultContaBancariaShouldBeFound("conta.doesNotContain=" + UPDATED_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where observacao equals to DEFAULT_OBSERVACAO
        defaultContaBancariaShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the contaBancariaList where observacao equals to UPDATED_OBSERVACAO
        defaultContaBancariaShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultContaBancariaShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the contaBancariaList where observacao equals to UPDATED_OBSERVACAO
        defaultContaBancariaShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where observacao is not null
        defaultContaBancariaShouldBeFound("observacao.specified=true");

        // Get all the contaBancariaList where observacao is null
        defaultContaBancariaShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where observacao contains DEFAULT_OBSERVACAO
        defaultContaBancariaShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the contaBancariaList where observacao contains UPDATED_OBSERVACAO
        defaultContaBancariaShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where observacao does not contain DEFAULT_OBSERVACAO
        defaultContaBancariaShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the contaBancariaList where observacao does not contain UPDATED_OBSERVACAO
        defaultContaBancariaShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipo equals to DEFAULT_TIPO
        defaultContaBancariaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the contaBancariaList where tipo equals to UPDATED_TIPO
        defaultContaBancariaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultContaBancariaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the contaBancariaList where tipo equals to UPDATED_TIPO
        defaultContaBancariaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipo is not null
        defaultContaBancariaShouldBeFound("tipo.specified=true");

        // Get all the contaBancariaList where tipo is null
        defaultContaBancariaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipo contains DEFAULT_TIPO
        defaultContaBancariaShouldBeFound("tipo.contains=" + DEFAULT_TIPO);

        // Get all the contaBancariaList where tipo contains UPDATED_TIPO
        defaultContaBancariaShouldNotBeFound("tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipo does not contain DEFAULT_TIPO
        defaultContaBancariaShouldNotBeFound("tipo.doesNotContain=" + DEFAULT_TIPO);

        // Get all the contaBancariaList where tipo does not contain UPDATED_TIPO
        defaultContaBancariaShouldBeFound("tipo.doesNotContain=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByPixIsEqualToSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where pix equals to DEFAULT_PIX
        defaultContaBancariaShouldBeFound("pix.equals=" + DEFAULT_PIX);

        // Get all the contaBancariaList where pix equals to UPDATED_PIX
        defaultContaBancariaShouldNotBeFound("pix.equals=" + UPDATED_PIX);
    }

    @Test
    @Transactional
    void getAllContaBancariasByPixIsInShouldWork() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where pix in DEFAULT_PIX or UPDATED_PIX
        defaultContaBancariaShouldBeFound("pix.in=" + DEFAULT_PIX + "," + UPDATED_PIX);

        // Get all the contaBancariaList where pix equals to UPDATED_PIX
        defaultContaBancariaShouldNotBeFound("pix.in=" + UPDATED_PIX);
    }

    @Test
    @Transactional
    void getAllContaBancariasByPixIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where pix is not null
        defaultContaBancariaShouldBeFound("pix.specified=true");

        // Get all the contaBancariaList where pix is null
        defaultContaBancariaShouldNotBeFound("pix.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByPixContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where pix contains DEFAULT_PIX
        defaultContaBancariaShouldBeFound("pix.contains=" + DEFAULT_PIX);

        // Get all the contaBancariaList where pix contains UPDATED_PIX
        defaultContaBancariaShouldNotBeFound("pix.contains=" + UPDATED_PIX);
    }

    @Test
    @Transactional
    void getAllContaBancariasByPixNotContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where pix does not contain DEFAULT_PIX
        defaultContaBancariaShouldNotBeFound("pix.doesNotContain=" + DEFAULT_PIX);

        // Get all the contaBancariaList where pix does not contain UPDATED_PIX
        defaultContaBancariaShouldBeFound("pix.doesNotContain=" + UPDATED_PIX);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTitularIsEqualToSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where titular equals to DEFAULT_TITULAR
        defaultContaBancariaShouldBeFound("titular.equals=" + DEFAULT_TITULAR);

        // Get all the contaBancariaList where titular equals to UPDATED_TITULAR
        defaultContaBancariaShouldNotBeFound("titular.equals=" + UPDATED_TITULAR);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTitularIsInShouldWork() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where titular in DEFAULT_TITULAR or UPDATED_TITULAR
        defaultContaBancariaShouldBeFound("titular.in=" + DEFAULT_TITULAR + "," + UPDATED_TITULAR);

        // Get all the contaBancariaList where titular equals to UPDATED_TITULAR
        defaultContaBancariaShouldNotBeFound("titular.in=" + UPDATED_TITULAR);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTitularIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where titular is not null
        defaultContaBancariaShouldBeFound("titular.specified=true");

        // Get all the contaBancariaList where titular is null
        defaultContaBancariaShouldNotBeFound("titular.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByTitularContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where titular contains DEFAULT_TITULAR
        defaultContaBancariaShouldBeFound("titular.contains=" + DEFAULT_TITULAR);

        // Get all the contaBancariaList where titular contains UPDATED_TITULAR
        defaultContaBancariaShouldNotBeFound("titular.contains=" + UPDATED_TITULAR);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTitularNotContainsSomething() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where titular does not contain DEFAULT_TITULAR
        defaultContaBancariaShouldNotBeFound("titular.doesNotContain=" + DEFAULT_TITULAR);

        // Get all the contaBancariaList where titular does not contain UPDATED_TITULAR
        defaultContaBancariaShouldBeFound("titular.doesNotContain=" + UPDATED_TITULAR);
    }

    @Test
    @Transactional
    void getAllContaBancariasByBancoIsEqualToSomething() throws Exception {
        Banco banco;
        if (TestUtil.findAll(em, Banco.class).isEmpty()) {
            contaBancariaRepository.saveAndFlush(contaBancaria);
            banco = BancoResourceIT.createEntity(em);
        } else {
            banco = TestUtil.findAll(em, Banco.class).get(0);
        }
        em.persist(banco);
        em.flush();
        contaBancaria.setBanco(banco);
        contaBancariaRepository.saveAndFlush(contaBancaria);
        Long bancoId = banco.getId();
        // Get all the contaBancariaList where banco equals to bancoId
        defaultContaBancariaShouldBeFound("bancoId.equals=" + bancoId);

        // Get all the contaBancariaList where banco equals to (bancoId + 1)
        defaultContaBancariaShouldNotBeFound("bancoId.equals=" + (bancoId + 1));
    }

    @Test
    @Transactional
    void getAllContaBancariasByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            contaBancariaRepository.saveAndFlush(contaBancaria);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        contaBancaria.setEmbarcador(embarcador);
        contaBancariaRepository.saveAndFlush(contaBancaria);
        Long embarcadorId = embarcador.getId();
        // Get all the contaBancariaList where embarcador equals to embarcadorId
        defaultContaBancariaShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the contaBancariaList where embarcador equals to (embarcadorId + 1)
        defaultContaBancariaShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllContaBancariasByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            contaBancariaRepository.saveAndFlush(contaBancaria);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        contaBancaria.setTransportadora(transportadora);
        contaBancariaRepository.saveAndFlush(contaBancaria);
        Long transportadoraId = transportadora.getId();
        // Get all the contaBancariaList where transportadora equals to transportadoraId
        defaultContaBancariaShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the contaBancariaList where transportadora equals to (transportadoraId + 1)
        defaultContaBancariaShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaBancariaShouldBeFound(String filter) throws Exception {
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].pix").value(hasItem(DEFAULT_PIX)))
            .andExpect(jsonPath("$.[*].titular").value(hasItem(DEFAULT_TITULAR)));

        // Check, that the count call also returns 1
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaBancariaShouldNotBeFound(String filter) throws Exception {
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContaBancaria() throws Exception {
        // Get the contaBancaria
        restContaBancariaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContaBancaria() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        contaBancariaSearchRepository.save(contaBancaria);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());

        // Update the contaBancaria
        ContaBancaria updatedContaBancaria = contaBancariaRepository.findById(contaBancaria.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContaBancaria are not directly saved in db
        em.detach(updatedContaBancaria);
        updatedContaBancaria
            .agencia(UPDATED_AGENCIA)
            .conta(UPDATED_CONTA)
            .observacao(UPDATED_OBSERVACAO)
            .tipo(UPDATED_TIPO)
            .pix(UPDATED_PIX)
            .titular(UPDATED_TITULAR);
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(updatedContaBancaria);

        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(UPDATED_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(UPDATED_TITULAR);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ContaBancaria> contaBancariaSearchList = IterableUtils.toList(contaBancariaSearchRepository.findAll());
                ContaBancaria testContaBancariaSearch = contaBancariaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testContaBancariaSearch.getAgencia()).isEqualTo(UPDATED_AGENCIA);
                assertThat(testContaBancariaSearch.getConta()).isEqualTo(UPDATED_CONTA);
                assertThat(testContaBancariaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testContaBancariaSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testContaBancariaSearch.getPix()).isEqualTo(UPDATED_PIX);
                assertThat(testContaBancariaSearch.getTitular()).isEqualTo(UPDATED_TITULAR);
            });
    }

    @Test
    @Transactional
    void putNonExistingContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateContaBancariaWithPatch() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();

        // Update the contaBancaria using partial update
        ContaBancaria partialUpdatedContaBancaria = new ContaBancaria();
        partialUpdatedContaBancaria.setId(contaBancaria.getId());

        partialUpdatedContaBancaria.agencia(UPDATED_AGENCIA).pix(UPDATED_PIX);

        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaBancaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaBancaria))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(UPDATED_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(DEFAULT_TITULAR);
    }

    @Test
    @Transactional
    void fullUpdateContaBancariaWithPatch() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();

        // Update the contaBancaria using partial update
        ContaBancaria partialUpdatedContaBancaria = new ContaBancaria();
        partialUpdatedContaBancaria.setId(contaBancaria.getId());

        partialUpdatedContaBancaria
            .agencia(UPDATED_AGENCIA)
            .conta(UPDATED_CONTA)
            .observacao(UPDATED_OBSERVACAO)
            .tipo(UPDATED_TIPO)
            .pix(UPDATED_PIX)
            .titular(UPDATED_TITULAR);

        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaBancaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaBancaria))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(UPDATED_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(UPDATED_TITULAR);
    }

    @Test
    @Transactional
    void patchNonExistingContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteContaBancaria() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);
        contaBancariaRepository.save(contaBancaria);
        contaBancariaSearchRepository.save(contaBancaria);

        int databaseSizeBeforeDelete = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the contaBancaria
        restContaBancariaMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaBancaria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchContaBancaria() throws Exception {
        // Initialize the database
        contaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);
        contaBancariaSearchRepository.save(contaBancaria);

        // Search the contaBancaria
        restContaBancariaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contaBancaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].pix").value(hasItem(DEFAULT_PIX)))
            .andExpect(jsonPath("$.[*].titular").value(hasItem(DEFAULT_TITULAR)));
    }
}
