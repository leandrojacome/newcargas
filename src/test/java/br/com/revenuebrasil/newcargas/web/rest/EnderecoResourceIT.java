package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.domain.enumeration.TipoEndereco;
import br.com.revenuebrasil.newcargas.repository.EnderecoRepository;
import br.com.revenuebrasil.newcargas.repository.search.EnderecoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.EnderecoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EnderecoMapper;
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
 * Integration tests for the {@link EnderecoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnderecoResourceIT {

    private static final TipoEndereco DEFAULT_TIPO = TipoEndereco.TRANSPORTADORA;
    private static final TipoEndereco UPDATED_TIPO = TipoEndereco.EMBARCADOR;

    private static final String DEFAULT_CEP = "AAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enderecos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/enderecos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Autowired
    private EnderecoSearchRepository enderecoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .tipo(DEFAULT_TIPO)
            .cep(DEFAULT_CEP)
            .endereco(DEFAULT_ENDERECO)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .bairro(DEFAULT_BAIRRO);
        return endereco;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createUpdatedEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .tipo(UPDATED_TIPO)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO);
        return endereco;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        enderecoSearchRepository.deleteAll();
        assertThat(enderecoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        endereco = createEntity(em);
    }

    @Test
    @Transactional
    void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);
        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEndereco.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
    }

    @Test
    @Transactional
    void createEnderecoWithExistingId() throws Exception {
        // Create the Endereco with an existing ID
        endereco.setId(1L);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        // set the field null
        endereco.setTipo(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        // set the field null
        endereco.setCep(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEnderecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        // set the field null
        endereco.setEndereco(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkBairroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        // set the field null
        endereco.setBairro(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)));
    }

    @Test
    @Transactional
    void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL_ID, endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO));
    }

    @Test
    @Transactional
    void getEnderecosByIdFiltering() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        Long id = endereco.getId();

        defaultEnderecoShouldBeFound("id.equals=" + id);
        defaultEnderecoShouldNotBeFound("id.notEquals=" + id);

        defaultEnderecoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnderecoShouldNotBeFound("id.greaterThan=" + id);

        defaultEnderecoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnderecoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnderecosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where tipo equals to DEFAULT_TIPO
        defaultEnderecoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the enderecoList where tipo equals to UPDATED_TIPO
        defaultEnderecoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEnderecosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultEnderecoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the enderecoList where tipo equals to UPDATED_TIPO
        defaultEnderecoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEnderecosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where tipo is not null
        defaultEnderecoShouldBeFound("tipo.specified=true");

        // Get all the enderecoList where tipo is null
        defaultEnderecoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep equals to DEFAULT_CEP
        defaultEnderecoShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the enderecoList where cep equals to UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the enderecoList where cep equals to UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep is not null
        defaultEnderecoShouldBeFound("cep.specified=true");

        // Get all the enderecoList where cep is null
        defaultEnderecoShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByCepContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep contains DEFAULT_CEP
        defaultEnderecoShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the enderecoList where cep contains UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep does not contain DEFAULT_CEP
        defaultEnderecoShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the enderecoList where cep does not contain UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where endereco equals to DEFAULT_ENDERECO
        defaultEnderecoShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the enderecoList where endereco equals to UPDATED_ENDERECO
        defaultEnderecoShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEnderecosByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultEnderecoShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the enderecoList where endereco equals to UPDATED_ENDERECO
        defaultEnderecoShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEnderecosByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where endereco is not null
        defaultEnderecoShouldBeFound("endereco.specified=true");

        // Get all the enderecoList where endereco is null
        defaultEnderecoShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByEnderecoContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where endereco contains DEFAULT_ENDERECO
        defaultEnderecoShouldBeFound("endereco.contains=" + DEFAULT_ENDERECO);

        // Get all the enderecoList where endereco contains UPDATED_ENDERECO
        defaultEnderecoShouldNotBeFound("endereco.contains=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEnderecosByEnderecoNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where endereco does not contain DEFAULT_ENDERECO
        defaultEnderecoShouldNotBeFound("endereco.doesNotContain=" + DEFAULT_ENDERECO);

        // Get all the enderecoList where endereco does not contain UPDATED_ENDERECO
        defaultEnderecoShouldBeFound("endereco.doesNotContain=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero equals to DEFAULT_NUMERO
        defaultEnderecoShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero equals to UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the enderecoList where numero equals to UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero is not null
        defaultEnderecoShouldBeFound("numero.specified=true");

        // Get all the enderecoList where numero is null
        defaultEnderecoShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero contains DEFAULT_NUMERO
        defaultEnderecoShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero contains UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero does not contain DEFAULT_NUMERO
        defaultEnderecoShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero does not contain UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento equals to DEFAULT_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.equals=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.equals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento in DEFAULT_COMPLEMENTO or UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.in=" + DEFAULT_COMPLEMENTO + "," + UPDATED_COMPLEMENTO);

        // Get all the enderecoList where complemento equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.in=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento is not null
        defaultEnderecoShouldBeFound("complemento.specified=true");

        // Get all the enderecoList where complemento is null
        defaultEnderecoShouldNotBeFound("complemento.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento contains DEFAULT_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.contains=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento contains UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.contains=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento does not contain DEFAULT_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.doesNotContain=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento does not contain UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.doesNotContain=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro equals to DEFAULT_BAIRRO
        defaultEnderecoShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the enderecoList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro is not null
        defaultEnderecoShouldBeFound("bairro.specified=true");

        // Get all the enderecoList where bairro is null
        defaultEnderecoShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro contains DEFAULT_BAIRRO
        defaultEnderecoShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro contains UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro does not contain DEFAULT_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro does not contain UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCidadeIsEqualToSomething() throws Exception {
        Cidade cidade;
        if (TestUtil.findAll(em, Cidade.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            cidade = CidadeResourceIT.createEntity(em);
        } else {
            cidade = TestUtil.findAll(em, Cidade.class).get(0);
        }
        em.persist(cidade);
        em.flush();
        endereco.setCidade(cidade);
        enderecoRepository.saveAndFlush(endereco);
        Long cidadeId = cidade.getId();
        // Get all the enderecoList where cidade equals to cidadeId
        defaultEnderecoShouldBeFound("cidadeId.equals=" + cidadeId);

        // Get all the enderecoList where cidade equals to (cidadeId + 1)
        defaultEnderecoShouldNotBeFound("cidadeId.equals=" + (cidadeId + 1));
    }

    @Test
    @Transactional
    void getAllEnderecosByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        endereco.setEmbarcador(embarcador);
        enderecoRepository.saveAndFlush(endereco);
        Long embarcadorId = embarcador.getId();
        // Get all the enderecoList where embarcador equals to embarcadorId
        defaultEnderecoShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the enderecoList where embarcador equals to (embarcadorId + 1)
        defaultEnderecoShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllEnderecosByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        endereco.setTransportadora(transportadora);
        enderecoRepository.saveAndFlush(endereco);
        Long transportadoraId = transportadora.getId();
        // Get all the enderecoList where transportadora equals to transportadoraId
        defaultEnderecoShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the enderecoList where transportadora equals to (transportadoraId + 1)
        defaultEnderecoShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    @Test
    @Transactional
    void getAllEnderecosByNotaFiscalColetaOrigemIsEqualToSomething() throws Exception {
        NotaFiscalColeta notaFiscalColetaOrigem;
        if (TestUtil.findAll(em, NotaFiscalColeta.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            notaFiscalColetaOrigem = NotaFiscalColetaResourceIT.createEntity(em);
        } else {
            notaFiscalColetaOrigem = TestUtil.findAll(em, NotaFiscalColeta.class).get(0);
        }
        em.persist(notaFiscalColetaOrigem);
        em.flush();
        endereco.setNotaFiscalColetaOrigem(notaFiscalColetaOrigem);
        enderecoRepository.saveAndFlush(endereco);
        Long notaFiscalColetaOrigemId = notaFiscalColetaOrigem.getId();
        // Get all the enderecoList where notaFiscalColetaOrigem equals to notaFiscalColetaOrigemId
        defaultEnderecoShouldBeFound("notaFiscalColetaOrigemId.equals=" + notaFiscalColetaOrigemId);

        // Get all the enderecoList where notaFiscalColetaOrigem equals to (notaFiscalColetaOrigemId + 1)
        defaultEnderecoShouldNotBeFound("notaFiscalColetaOrigemId.equals=" + (notaFiscalColetaOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllEnderecosByNotaFiscalColetaDestinoIsEqualToSomething() throws Exception {
        NotaFiscalColeta notaFiscalColetaDestino;
        if (TestUtil.findAll(em, NotaFiscalColeta.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            notaFiscalColetaDestino = NotaFiscalColetaResourceIT.createEntity(em);
        } else {
            notaFiscalColetaDestino = TestUtil.findAll(em, NotaFiscalColeta.class).get(0);
        }
        em.persist(notaFiscalColetaDestino);
        em.flush();
        endereco.setNotaFiscalColetaDestino(notaFiscalColetaDestino);
        enderecoRepository.saveAndFlush(endereco);
        Long notaFiscalColetaDestinoId = notaFiscalColetaDestino.getId();
        // Get all the enderecoList where notaFiscalColetaDestino equals to notaFiscalColetaDestinoId
        defaultEnderecoShouldBeFound("notaFiscalColetaDestinoId.equals=" + notaFiscalColetaDestinoId);

        // Get all the enderecoList where notaFiscalColetaDestino equals to (notaFiscalColetaDestinoId + 1)
        defaultEnderecoShouldNotBeFound("notaFiscalColetaDestinoId.equals=" + (notaFiscalColetaDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllEnderecosBySolicitacaoColetaOrigemIsEqualToSomething() throws Exception {
        SolicitacaoColeta solicitacaoColetaOrigem;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            solicitacaoColetaOrigem = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solicitacaoColetaOrigem = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solicitacaoColetaOrigem);
        em.flush();
        endereco.setSolicitacaoColetaOrigem(solicitacaoColetaOrigem);
        enderecoRepository.saveAndFlush(endereco);
        Long solicitacaoColetaOrigemId = solicitacaoColetaOrigem.getId();
        // Get all the enderecoList where solicitacaoColetaOrigem equals to solicitacaoColetaOrigemId
        defaultEnderecoShouldBeFound("solicitacaoColetaOrigemId.equals=" + solicitacaoColetaOrigemId);

        // Get all the enderecoList where solicitacaoColetaOrigem equals to (solicitacaoColetaOrigemId + 1)
        defaultEnderecoShouldNotBeFound("solicitacaoColetaOrigemId.equals=" + (solicitacaoColetaOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllEnderecosBySolicitacaoColetaDestinoIsEqualToSomething() throws Exception {
        SolicitacaoColeta solicitacaoColetaDestino;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            enderecoRepository.saveAndFlush(endereco);
            solicitacaoColetaDestino = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solicitacaoColetaDestino = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solicitacaoColetaDestino);
        em.flush();
        endereco.setSolicitacaoColetaDestino(solicitacaoColetaDestino);
        enderecoRepository.saveAndFlush(endereco);
        Long solicitacaoColetaDestinoId = solicitacaoColetaDestino.getId();
        // Get all the enderecoList where solicitacaoColetaDestino equals to solicitacaoColetaDestinoId
        defaultEnderecoShouldBeFound("solicitacaoColetaDestinoId.equals=" + solicitacaoColetaDestinoId);

        // Get all the enderecoList where solicitacaoColetaDestino equals to (solicitacaoColetaDestinoId + 1)
        defaultEnderecoShouldNotBeFound("solicitacaoColetaDestinoId.equals=" + (solicitacaoColetaDestinoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnderecoShouldBeFound(String filter) throws Exception {
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)));

        // Check, that the count call also returns 1
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnderecoShouldNotBeFound(String filter) throws Exception {
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        enderecoSearchRepository.save(endereco);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findById(endereco.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEndereco are not directly saved in db
        em.detach(updatedEndereco);
        updatedEndereco
            .tipo(UPDATED_TIPO)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(updatedEndereco);

        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Endereco> enderecoSearchList = IterableUtils.toList(enderecoSearchRepository.findAll());
                Endereco testEnderecoSearch = enderecoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEnderecoSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testEnderecoSearch.getCep()).isEqualTo(UPDATED_CEP);
                assertThat(testEnderecoSearch.getEndereco()).isEqualTo(UPDATED_ENDERECO);
                assertThat(testEnderecoSearch.getNumero()).isEqualTo(UPDATED_NUMERO);
                assertThat(testEnderecoSearch.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
                assertThat(testEnderecoSearch.getBairro()).isEqualTo(UPDATED_BAIRRO);
            });
    }

    @Test
    @Transactional
    void putNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco.cep(UPDATED_CEP).bairro(UPDATED_BAIRRO);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void fullUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco
            .tipo(UPDATED_TIPO)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void patchNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoRepository.save(endereco);
        enderecoSearchRepository.save(endereco);

        int databaseSizeBeforeDelete = enderecoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the endereco
        restEnderecoMockMvc
            .perform(delete(ENTITY_API_URL_ID, endereco.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enderecoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEndereco() throws Exception {
        // Initialize the database
        endereco = enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);

        // Search the endereco
        restEnderecoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)));
    }
}
