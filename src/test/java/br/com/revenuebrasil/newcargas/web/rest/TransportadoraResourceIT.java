package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.TransportadoraRepository;
import br.com.revenuebrasil.newcargas.repository.search.TransportadoraSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TransportadoraMapper;
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
 * Integration tests for the {@link TransportadoraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransportadoraResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBBBBBB";

    private static final String DEFAULT_RAZAO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_INSCRICAO_ESTADUAL = "AAAAAAAAAA";
    private static final String UPDATED_INSCRICAO_ESTADUAL = "BBBBBBBBBB";

    private static final String DEFAULT_INSCRICAO_MUNICIPAL = "AAAAAAAAAA";
    private static final String UPDATED_INSCRICAO_MUNICIPAL = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSAVEL = "BBBBBBBBBB";

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

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transportadoras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/transportadoras/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    @Autowired
    private TransportadoraMapper transportadoraMapper;

    @Autowired
    private TransportadoraSearchRepository transportadoraSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransportadoraMockMvc;

    private Transportadora transportadora;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transportadora createEntity(EntityManager em) {
        Transportadora transportadora = new Transportadora()
            .nome(DEFAULT_NOME)
            .cnpj(DEFAULT_CNPJ)
            .razaoSocial(DEFAULT_RAZAO_SOCIAL)
            .inscricaoEstadual(DEFAULT_INSCRICAO_ESTADUAL)
            .inscricaoMunicipal(DEFAULT_INSCRICAO_MUNICIPAL)
            .responsavel(DEFAULT_RESPONSAVEL)
            .cep(DEFAULT_CEP)
            .endereco(DEFAULT_ENDERECO)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .bairro(DEFAULT_BAIRRO)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL)
            .observacao(DEFAULT_OBSERVACAO);
        return transportadora;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transportadora createUpdatedEntity(EntityManager em) {
        Transportadora transportadora = new Transportadora()
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .inscricaoEstadual(UPDATED_INSCRICAO_ESTADUAL)
            .inscricaoMunicipal(UPDATED_INSCRICAO_MUNICIPAL)
            .responsavel(UPDATED_RESPONSAVEL)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .observacao(UPDATED_OBSERVACAO);
        return transportadora;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        transportadoraSearchRepository.deleteAll();
        assertThat(transportadoraSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        transportadora = createEntity(em);
    }

    @Test
    @Transactional
    void createTransportadora() throws Exception {
        int databaseSizeBeforeCreate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);
        restTransportadoraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Transportadora testTransportadora = transportadoraList.get(transportadoraList.size() - 1);
        assertThat(testTransportadora.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTransportadora.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testTransportadora.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testTransportadora.getInscricaoEstadual()).isEqualTo(DEFAULT_INSCRICAO_ESTADUAL);
        assertThat(testTransportadora.getInscricaoMunicipal()).isEqualTo(DEFAULT_INSCRICAO_MUNICIPAL);
        assertThat(testTransportadora.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testTransportadora.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testTransportadora.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testTransportadora.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testTransportadora.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testTransportadora.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testTransportadora.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testTransportadora.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTransportadora.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void createTransportadoraWithExistingId() throws Exception {
        // Create the Transportadora with an existing ID
        transportadora.setId(1L);
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        int databaseSizeBeforeCreate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportadoraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        // set the field null
        transportadora.setNome(null);

        // Create the Transportadora, which fails.
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        restTransportadoraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCnpjIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        // set the field null
        transportadora.setCnpj(null);

        // Create the Transportadora, which fails.
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        restTransportadoraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTransportadoras() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList
        restTransportadoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportadora.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].inscricaoEstadual").value(hasItem(DEFAULT_INSCRICAO_ESTADUAL)))
            .andExpect(jsonPath("$.[*].inscricaoMunicipal").value(hasItem(DEFAULT_INSCRICAO_MUNICIPAL)))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }

    @Test
    @Transactional
    void getTransportadora() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get the transportadora
        restTransportadoraMockMvc
            .perform(get(ENTITY_API_URL_ID, transportadora.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transportadora.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL))
            .andExpect(jsonPath("$.inscricaoEstadual").value(DEFAULT_INSCRICAO_ESTADUAL))
            .andExpect(jsonPath("$.inscricaoMunicipal").value(DEFAULT_INSCRICAO_MUNICIPAL))
            .andExpect(jsonPath("$.responsavel").value(DEFAULT_RESPONSAVEL))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO));
    }

    @Test
    @Transactional
    void getTransportadorasByIdFiltering() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        Long id = transportadora.getId();

        defaultTransportadoraShouldBeFound("id.equals=" + id);
        defaultTransportadoraShouldNotBeFound("id.notEquals=" + id);

        defaultTransportadoraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransportadoraShouldNotBeFound("id.greaterThan=" + id);

        defaultTransportadoraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransportadoraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where nome equals to DEFAULT_NOME
        defaultTransportadoraShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the transportadoraList where nome equals to UPDATED_NOME
        defaultTransportadoraShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTransportadoraShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the transportadoraList where nome equals to UPDATED_NOME
        defaultTransportadoraShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where nome is not null
        defaultTransportadoraShouldBeFound("nome.specified=true");

        // Get all the transportadoraList where nome is null
        defaultTransportadoraShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByNomeContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where nome contains DEFAULT_NOME
        defaultTransportadoraShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the transportadoraList where nome contains UPDATED_NOME
        defaultTransportadoraShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where nome does not contain DEFAULT_NOME
        defaultTransportadoraShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the transportadoraList where nome does not contain UPDATED_NOME
        defaultTransportadoraShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cnpj equals to DEFAULT_CNPJ
        defaultTransportadoraShouldBeFound("cnpj.equals=" + DEFAULT_CNPJ);

        // Get all the transportadoraList where cnpj equals to UPDATED_CNPJ
        defaultTransportadoraShouldNotBeFound("cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cnpj in DEFAULT_CNPJ or UPDATED_CNPJ
        defaultTransportadoraShouldBeFound("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ);

        // Get all the transportadoraList where cnpj equals to UPDATED_CNPJ
        defaultTransportadoraShouldNotBeFound("cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cnpj is not null
        defaultTransportadoraShouldBeFound("cnpj.specified=true");

        // Get all the transportadoraList where cnpj is null
        defaultTransportadoraShouldNotBeFound("cnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByCnpjContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cnpj contains DEFAULT_CNPJ
        defaultTransportadoraShouldBeFound("cnpj.contains=" + DEFAULT_CNPJ);

        // Get all the transportadoraList where cnpj contains UPDATED_CNPJ
        defaultTransportadoraShouldNotBeFound("cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cnpj does not contain DEFAULT_CNPJ
        defaultTransportadoraShouldNotBeFound("cnpj.doesNotContain=" + DEFAULT_CNPJ);

        // Get all the transportadoraList where cnpj does not contain UPDATED_CNPJ
        defaultTransportadoraShouldBeFound("cnpj.doesNotContain=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllTransportadorasByRazaoSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where razaoSocial equals to DEFAULT_RAZAO_SOCIAL
        defaultTransportadoraShouldBeFound("razaoSocial.equals=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the transportadoraList where razaoSocial equals to UPDATED_RAZAO_SOCIAL
        defaultTransportadoraShouldNotBeFound("razaoSocial.equals=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByRazaoSocialIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where razaoSocial in DEFAULT_RAZAO_SOCIAL or UPDATED_RAZAO_SOCIAL
        defaultTransportadoraShouldBeFound("razaoSocial.in=" + DEFAULT_RAZAO_SOCIAL + "," + UPDATED_RAZAO_SOCIAL);

        // Get all the transportadoraList where razaoSocial equals to UPDATED_RAZAO_SOCIAL
        defaultTransportadoraShouldNotBeFound("razaoSocial.in=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByRazaoSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where razaoSocial is not null
        defaultTransportadoraShouldBeFound("razaoSocial.specified=true");

        // Get all the transportadoraList where razaoSocial is null
        defaultTransportadoraShouldNotBeFound("razaoSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByRazaoSocialContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where razaoSocial contains DEFAULT_RAZAO_SOCIAL
        defaultTransportadoraShouldBeFound("razaoSocial.contains=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the transportadoraList where razaoSocial contains UPDATED_RAZAO_SOCIAL
        defaultTransportadoraShouldNotBeFound("razaoSocial.contains=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByRazaoSocialNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where razaoSocial does not contain DEFAULT_RAZAO_SOCIAL
        defaultTransportadoraShouldNotBeFound("razaoSocial.doesNotContain=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the transportadoraList where razaoSocial does not contain UPDATED_RAZAO_SOCIAL
        defaultTransportadoraShouldBeFound("razaoSocial.doesNotContain=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoEstadualIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoEstadual equals to DEFAULT_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldBeFound("inscricaoEstadual.equals=" + DEFAULT_INSCRICAO_ESTADUAL);

        // Get all the transportadoraList where inscricaoEstadual equals to UPDATED_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldNotBeFound("inscricaoEstadual.equals=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoEstadualIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoEstadual in DEFAULT_INSCRICAO_ESTADUAL or UPDATED_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldBeFound("inscricaoEstadual.in=" + DEFAULT_INSCRICAO_ESTADUAL + "," + UPDATED_INSCRICAO_ESTADUAL);

        // Get all the transportadoraList where inscricaoEstadual equals to UPDATED_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldNotBeFound("inscricaoEstadual.in=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoEstadualIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoEstadual is not null
        defaultTransportadoraShouldBeFound("inscricaoEstadual.specified=true");

        // Get all the transportadoraList where inscricaoEstadual is null
        defaultTransportadoraShouldNotBeFound("inscricaoEstadual.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoEstadualContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoEstadual contains DEFAULT_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldBeFound("inscricaoEstadual.contains=" + DEFAULT_INSCRICAO_ESTADUAL);

        // Get all the transportadoraList where inscricaoEstadual contains UPDATED_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldNotBeFound("inscricaoEstadual.contains=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoEstadualNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoEstadual does not contain DEFAULT_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldNotBeFound("inscricaoEstadual.doesNotContain=" + DEFAULT_INSCRICAO_ESTADUAL);

        // Get all the transportadoraList where inscricaoEstadual does not contain UPDATED_INSCRICAO_ESTADUAL
        defaultTransportadoraShouldBeFound("inscricaoEstadual.doesNotContain=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoMunicipalIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoMunicipal equals to DEFAULT_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldBeFound("inscricaoMunicipal.equals=" + DEFAULT_INSCRICAO_MUNICIPAL);

        // Get all the transportadoraList where inscricaoMunicipal equals to UPDATED_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldNotBeFound("inscricaoMunicipal.equals=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoMunicipalIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoMunicipal in DEFAULT_INSCRICAO_MUNICIPAL or UPDATED_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldBeFound("inscricaoMunicipal.in=" + DEFAULT_INSCRICAO_MUNICIPAL + "," + UPDATED_INSCRICAO_MUNICIPAL);

        // Get all the transportadoraList where inscricaoMunicipal equals to UPDATED_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldNotBeFound("inscricaoMunicipal.in=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoMunicipalIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoMunicipal is not null
        defaultTransportadoraShouldBeFound("inscricaoMunicipal.specified=true");

        // Get all the transportadoraList where inscricaoMunicipal is null
        defaultTransportadoraShouldNotBeFound("inscricaoMunicipal.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoMunicipalContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoMunicipal contains DEFAULT_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldBeFound("inscricaoMunicipal.contains=" + DEFAULT_INSCRICAO_MUNICIPAL);

        // Get all the transportadoraList where inscricaoMunicipal contains UPDATED_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldNotBeFound("inscricaoMunicipal.contains=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByInscricaoMunicipalNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where inscricaoMunicipal does not contain DEFAULT_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldNotBeFound("inscricaoMunicipal.doesNotContain=" + DEFAULT_INSCRICAO_MUNICIPAL);

        // Get all the transportadoraList where inscricaoMunicipal does not contain UPDATED_INSCRICAO_MUNICIPAL
        defaultTransportadoraShouldBeFound("inscricaoMunicipal.doesNotContain=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where responsavel equals to DEFAULT_RESPONSAVEL
        defaultTransportadoraShouldBeFound("responsavel.equals=" + DEFAULT_RESPONSAVEL);

        // Get all the transportadoraList where responsavel equals to UPDATED_RESPONSAVEL
        defaultTransportadoraShouldNotBeFound("responsavel.equals=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where responsavel in DEFAULT_RESPONSAVEL or UPDATED_RESPONSAVEL
        defaultTransportadoraShouldBeFound("responsavel.in=" + DEFAULT_RESPONSAVEL + "," + UPDATED_RESPONSAVEL);

        // Get all the transportadoraList where responsavel equals to UPDATED_RESPONSAVEL
        defaultTransportadoraShouldNotBeFound("responsavel.in=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where responsavel is not null
        defaultTransportadoraShouldBeFound("responsavel.specified=true");

        // Get all the transportadoraList where responsavel is null
        defaultTransportadoraShouldNotBeFound("responsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByResponsavelContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where responsavel contains DEFAULT_RESPONSAVEL
        defaultTransportadoraShouldBeFound("responsavel.contains=" + DEFAULT_RESPONSAVEL);

        // Get all the transportadoraList where responsavel contains UPDATED_RESPONSAVEL
        defaultTransportadoraShouldNotBeFound("responsavel.contains=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where responsavel does not contain DEFAULT_RESPONSAVEL
        defaultTransportadoraShouldNotBeFound("responsavel.doesNotContain=" + DEFAULT_RESPONSAVEL);

        // Get all the transportadoraList where responsavel does not contain UPDATED_RESPONSAVEL
        defaultTransportadoraShouldBeFound("responsavel.doesNotContain=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cep equals to DEFAULT_CEP
        defaultTransportadoraShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the transportadoraList where cep equals to UPDATED_CEP
        defaultTransportadoraShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCepIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultTransportadoraShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the transportadoraList where cep equals to UPDATED_CEP
        defaultTransportadoraShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cep is not null
        defaultTransportadoraShouldBeFound("cep.specified=true");

        // Get all the transportadoraList where cep is null
        defaultTransportadoraShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByCepContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cep contains DEFAULT_CEP
        defaultTransportadoraShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the transportadoraList where cep contains UPDATED_CEP
        defaultTransportadoraShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllTransportadorasByCepNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where cep does not contain DEFAULT_CEP
        defaultTransportadoraShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the transportadoraList where cep does not contain UPDATED_CEP
        defaultTransportadoraShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where endereco equals to DEFAULT_ENDERECO
        defaultTransportadoraShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the transportadoraList where endereco equals to UPDATED_ENDERECO
        defaultTransportadoraShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultTransportadoraShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the transportadoraList where endereco equals to UPDATED_ENDERECO
        defaultTransportadoraShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where endereco is not null
        defaultTransportadoraShouldBeFound("endereco.specified=true");

        // Get all the transportadoraList where endereco is null
        defaultTransportadoraShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByEnderecoContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where endereco contains DEFAULT_ENDERECO
        defaultTransportadoraShouldBeFound("endereco.contains=" + DEFAULT_ENDERECO);

        // Get all the transportadoraList where endereco contains UPDATED_ENDERECO
        defaultTransportadoraShouldNotBeFound("endereco.contains=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEnderecoNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where endereco does not contain DEFAULT_ENDERECO
        defaultTransportadoraShouldNotBeFound("endereco.doesNotContain=" + DEFAULT_ENDERECO);

        // Get all the transportadoraList where endereco does not contain UPDATED_ENDERECO
        defaultTransportadoraShouldBeFound("endereco.doesNotContain=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where numero equals to DEFAULT_NUMERO
        defaultTransportadoraShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the transportadoraList where numero equals to UPDATED_NUMERO
        defaultTransportadoraShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultTransportadoraShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the transportadoraList where numero equals to UPDATED_NUMERO
        defaultTransportadoraShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where numero is not null
        defaultTransportadoraShouldBeFound("numero.specified=true");

        // Get all the transportadoraList where numero is null
        defaultTransportadoraShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByNumeroContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where numero contains DEFAULT_NUMERO
        defaultTransportadoraShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the transportadoraList where numero contains UPDATED_NUMERO
        defaultTransportadoraShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where numero does not contain DEFAULT_NUMERO
        defaultTransportadoraShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the transportadoraList where numero does not contain UPDATED_NUMERO
        defaultTransportadoraShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByComplementoIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where complemento equals to DEFAULT_COMPLEMENTO
        defaultTransportadoraShouldBeFound("complemento.equals=" + DEFAULT_COMPLEMENTO);

        // Get all the transportadoraList where complemento equals to UPDATED_COMPLEMENTO
        defaultTransportadoraShouldNotBeFound("complemento.equals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByComplementoIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where complemento in DEFAULT_COMPLEMENTO or UPDATED_COMPLEMENTO
        defaultTransportadoraShouldBeFound("complemento.in=" + DEFAULT_COMPLEMENTO + "," + UPDATED_COMPLEMENTO);

        // Get all the transportadoraList where complemento equals to UPDATED_COMPLEMENTO
        defaultTransportadoraShouldNotBeFound("complemento.in=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByComplementoIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where complemento is not null
        defaultTransportadoraShouldBeFound("complemento.specified=true");

        // Get all the transportadoraList where complemento is null
        defaultTransportadoraShouldNotBeFound("complemento.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByComplementoContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where complemento contains DEFAULT_COMPLEMENTO
        defaultTransportadoraShouldBeFound("complemento.contains=" + DEFAULT_COMPLEMENTO);

        // Get all the transportadoraList where complemento contains UPDATED_COMPLEMENTO
        defaultTransportadoraShouldNotBeFound("complemento.contains=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByComplementoNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where complemento does not contain DEFAULT_COMPLEMENTO
        defaultTransportadoraShouldNotBeFound("complemento.doesNotContain=" + DEFAULT_COMPLEMENTO);

        // Get all the transportadoraList where complemento does not contain UPDATED_COMPLEMENTO
        defaultTransportadoraShouldBeFound("complemento.doesNotContain=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where bairro equals to DEFAULT_BAIRRO
        defaultTransportadoraShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the transportadoraList where bairro equals to UPDATED_BAIRRO
        defaultTransportadoraShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultTransportadoraShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the transportadoraList where bairro equals to UPDATED_BAIRRO
        defaultTransportadoraShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where bairro is not null
        defaultTransportadoraShouldBeFound("bairro.specified=true");

        // Get all the transportadoraList where bairro is null
        defaultTransportadoraShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByBairroContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where bairro contains DEFAULT_BAIRRO
        defaultTransportadoraShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the transportadoraList where bairro contains UPDATED_BAIRRO
        defaultTransportadoraShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where bairro does not contain DEFAULT_BAIRRO
        defaultTransportadoraShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the transportadoraList where bairro does not contain UPDATED_BAIRRO
        defaultTransportadoraShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where telefone equals to DEFAULT_TELEFONE
        defaultTransportadoraShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the transportadoraList where telefone equals to UPDATED_TELEFONE
        defaultTransportadoraShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllTransportadorasByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultTransportadoraShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the transportadoraList where telefone equals to UPDATED_TELEFONE
        defaultTransportadoraShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllTransportadorasByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where telefone is not null
        defaultTransportadoraShouldBeFound("telefone.specified=true");

        // Get all the transportadoraList where telefone is null
        defaultTransportadoraShouldNotBeFound("telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where telefone contains DEFAULT_TELEFONE
        defaultTransportadoraShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the transportadoraList where telefone contains UPDATED_TELEFONE
        defaultTransportadoraShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllTransportadorasByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where telefone does not contain DEFAULT_TELEFONE
        defaultTransportadoraShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the transportadoraList where telefone does not contain UPDATED_TELEFONE
        defaultTransportadoraShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where email equals to DEFAULT_EMAIL
        defaultTransportadoraShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the transportadoraList where email equals to UPDATED_EMAIL
        defaultTransportadoraShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTransportadoraShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the transportadoraList where email equals to UPDATED_EMAIL
        defaultTransportadoraShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where email is not null
        defaultTransportadoraShouldBeFound("email.specified=true");

        // Get all the transportadoraList where email is null
        defaultTransportadoraShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByEmailContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where email contains DEFAULT_EMAIL
        defaultTransportadoraShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the transportadoraList where email contains UPDATED_EMAIL
        defaultTransportadoraShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where email does not contain DEFAULT_EMAIL
        defaultTransportadoraShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the transportadoraList where email does not contain UPDATED_EMAIL
        defaultTransportadoraShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTransportadorasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where observacao equals to DEFAULT_OBSERVACAO
        defaultTransportadoraShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the transportadoraList where observacao equals to UPDATED_OBSERVACAO
        defaultTransportadoraShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultTransportadoraShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the transportadoraList where observacao equals to UPDATED_OBSERVACAO
        defaultTransportadoraShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where observacao is not null
        defaultTransportadoraShouldBeFound("observacao.specified=true");

        // Get all the transportadoraList where observacao is null
        defaultTransportadoraShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportadorasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where observacao contains DEFAULT_OBSERVACAO
        defaultTransportadoraShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the transportadoraList where observacao contains UPDATED_OBSERVACAO
        defaultTransportadoraShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        // Get all the transportadoraList where observacao does not contain DEFAULT_OBSERVACAO
        defaultTransportadoraShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the transportadoraList where observacao does not contain UPDATED_OBSERVACAO
        defaultTransportadoraShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllTransportadorasByEnderecoIsEqualToSomething__() throws Exception {
        Endereco endereco;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            endereco = EnderecoResourceIT.createEntity(em);
        } else {
            endereco = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(endereco);
        em.flush();
        transportadora.addEndereco(endereco);
        transportadoraRepository.saveAndFlush(transportadora);
        Long enderecoId = endereco.getId();
        // Get all the transportadoraList where endereco equals to enderecoId
        defaultTransportadoraShouldBeFound("enderecoId.equals=" + enderecoId);

        // Get all the transportadoraList where endereco equals to (enderecoId + 1)
        defaultTransportadoraShouldNotBeFound("enderecoId.equals=" + (enderecoId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByContaBancariaIsEqualToSomething() throws Exception {
        ContaBancaria contaBancaria;
        if (TestUtil.findAll(em, ContaBancaria.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            contaBancaria = ContaBancariaResourceIT.createEntity(em);
        } else {
            contaBancaria = TestUtil.findAll(em, ContaBancaria.class).get(0);
        }
        em.persist(contaBancaria);
        em.flush();
        transportadora.addContaBancaria(contaBancaria);
        transportadoraRepository.saveAndFlush(transportadora);
        Long contaBancariaId = contaBancaria.getId();
        // Get all the transportadoraList where contaBancaria equals to contaBancariaId
        defaultTransportadoraShouldBeFound("contaBancariaId.equals=" + contaBancariaId);

        // Get all the transportadoraList where contaBancaria equals to (contaBancariaId + 1)
        defaultTransportadoraShouldNotBeFound("contaBancariaId.equals=" + (contaBancariaId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByTabelaFreteIsEqualToSomething() throws Exception {
        TabelaFrete tabelaFrete;
        if (TestUtil.findAll(em, TabelaFrete.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            tabelaFrete = TabelaFreteResourceIT.createEntity(em);
        } else {
            tabelaFrete = TestUtil.findAll(em, TabelaFrete.class).get(0);
        }
        em.persist(tabelaFrete);
        em.flush();
        transportadora.addTabelaFrete(tabelaFrete);
        transportadoraRepository.saveAndFlush(transportadora);
        Long tabelaFreteId = tabelaFrete.getId();
        // Get all the transportadoraList where tabelaFrete equals to tabelaFreteId
        defaultTransportadoraShouldBeFound("tabelaFreteId.equals=" + tabelaFreteId);

        // Get all the transportadoraList where tabelaFrete equals to (tabelaFreteId + 1)
        defaultTransportadoraShouldNotBeFound("tabelaFreteId.equals=" + (tabelaFreteId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByTomadaPrecoIsEqualToSomething() throws Exception {
        TomadaPreco tomadaPreco;
        if (TestUtil.findAll(em, TomadaPreco.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            tomadaPreco = TomadaPrecoResourceIT.createEntity(em);
        } else {
            tomadaPreco = TestUtil.findAll(em, TomadaPreco.class).get(0);
        }
        em.persist(tomadaPreco);
        em.flush();
        transportadora.addTomadaPreco(tomadaPreco);
        transportadoraRepository.saveAndFlush(transportadora);
        Long tomadaPrecoId = tomadaPreco.getId();
        // Get all the transportadoraList where tomadaPreco equals to tomadaPrecoId
        defaultTransportadoraShouldBeFound("tomadaPrecoId.equals=" + tomadaPrecoId);

        // Get all the transportadoraList where tomadaPreco equals to (tomadaPrecoId + 1)
        defaultTransportadoraShouldNotBeFound("tomadaPrecoId.equals=" + (tomadaPrecoId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByContratacaoIsEqualToSomething() throws Exception {
        Contratacao contratacao;
        if (TestUtil.findAll(em, Contratacao.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            contratacao = ContratacaoResourceIT.createEntity(em);
        } else {
            contratacao = TestUtil.findAll(em, Contratacao.class).get(0);
        }
        em.persist(contratacao);
        em.flush();
        transportadora.addContratacao(contratacao);
        transportadoraRepository.saveAndFlush(transportadora);
        Long contratacaoId = contratacao.getId();
        // Get all the transportadoraList where contratacao equals to contratacaoId
        defaultTransportadoraShouldBeFound("contratacaoId.equals=" + contratacaoId);

        // Get all the transportadoraList where contratacao equals to (contratacaoId + 1)
        defaultTransportadoraShouldNotBeFound("contratacaoId.equals=" + (contratacaoId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByNotificacaoIsEqualToSomething() throws Exception {
        Notificacao notificacao;
        if (TestUtil.findAll(em, Notificacao.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            notificacao = NotificacaoResourceIT.createEntity(em);
        } else {
            notificacao = TestUtil.findAll(em, Notificacao.class).get(0);
        }
        em.persist(notificacao);
        em.flush();
        transportadora.addNotificacao(notificacao);
        transportadoraRepository.saveAndFlush(transportadora);
        Long notificacaoId = notificacao.getId();
        // Get all the transportadoraList where notificacao equals to notificacaoId
        defaultTransportadoraShouldBeFound("notificacaoId.equals=" + notificacaoId);

        // Get all the transportadoraList where notificacao equals to (notificacaoId + 1)
        defaultTransportadoraShouldNotBeFound("notificacaoId.equals=" + (notificacaoId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByFaturaIsEqualToSomething() throws Exception {
        Fatura fatura;
        if (TestUtil.findAll(em, Fatura.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            fatura = FaturaResourceIT.createEntity(em);
        } else {
            fatura = TestUtil.findAll(em, Fatura.class).get(0);
        }
        em.persist(fatura);
        em.flush();
        transportadora.addFatura(fatura);
        transportadoraRepository.saveAndFlush(transportadora);
        Long faturaId = fatura.getId();
        // Get all the transportadoraList where fatura equals to faturaId
        defaultTransportadoraShouldBeFound("faturaId.equals=" + faturaId);

        // Get all the transportadoraList where fatura equals to (faturaId + 1)
        defaultTransportadoraShouldNotBeFound("faturaId.equals=" + (faturaId + 1));
    }

    @Test
    @Transactional
    void getAllTransportadorasByCidadeIsEqualToSomething() throws Exception {
        Cidade cidade;
        if (TestUtil.findAll(em, Cidade.class).isEmpty()) {
            transportadoraRepository.saveAndFlush(transportadora);
            cidade = CidadeResourceIT.createEntity(em);
        } else {
            cidade = TestUtil.findAll(em, Cidade.class).get(0);
        }
        em.persist(cidade);
        em.flush();
        transportadora.setCidade(cidade);
        transportadoraRepository.saveAndFlush(transportadora);
        Long cidadeId = cidade.getId();
        // Get all the transportadoraList where cidade equals to cidadeId
        defaultTransportadoraShouldBeFound("cidadeId.equals=" + cidadeId);

        // Get all the transportadoraList where cidade equals to (cidadeId + 1)
        defaultTransportadoraShouldNotBeFound("cidadeId.equals=" + (cidadeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransportadoraShouldBeFound(String filter) throws Exception {
        restTransportadoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportadora.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].inscricaoEstadual").value(hasItem(DEFAULT_INSCRICAO_ESTADUAL)))
            .andExpect(jsonPath("$.[*].inscricaoMunicipal").value(hasItem(DEFAULT_INSCRICAO_MUNICIPAL)))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));

        // Check, that the count call also returns 1
        restTransportadoraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransportadoraShouldNotBeFound(String filter) throws Exception {
        restTransportadoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransportadoraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransportadora() throws Exception {
        // Get the transportadora
        restTransportadoraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransportadora() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        transportadoraSearchRepository.save(transportadora);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());

        // Update the transportadora
        Transportadora updatedTransportadora = transportadoraRepository.findById(transportadora.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransportadora are not directly saved in db
        em.detach(updatedTransportadora);
        updatedTransportadora
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .inscricaoEstadual(UPDATED_INSCRICAO_ESTADUAL)
            .inscricaoMunicipal(UPDATED_INSCRICAO_MUNICIPAL)
            .responsavel(UPDATED_RESPONSAVEL)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .observacao(UPDATED_OBSERVACAO);
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(updatedTransportadora);

        restTransportadoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportadoraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        Transportadora testTransportadora = transportadoraList.get(transportadoraList.size() - 1);
        assertThat(testTransportadora.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTransportadora.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testTransportadora.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testTransportadora.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
        assertThat(testTransportadora.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
        assertThat(testTransportadora.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testTransportadora.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testTransportadora.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testTransportadora.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTransportadora.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testTransportadora.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testTransportadora.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testTransportadora.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTransportadora.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Transportadora> transportadoraSearchList = IterableUtils.toList(transportadoraSearchRepository.findAll());
                Transportadora testTransportadoraSearch = transportadoraSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTransportadoraSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testTransportadoraSearch.getCnpj()).isEqualTo(UPDATED_CNPJ);
                assertThat(testTransportadoraSearch.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
                assertThat(testTransportadoraSearch.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
                assertThat(testTransportadoraSearch.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
                assertThat(testTransportadoraSearch.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
                assertThat(testTransportadoraSearch.getCep()).isEqualTo(UPDATED_CEP);
                assertThat(testTransportadoraSearch.getEndereco()).isEqualTo(UPDATED_ENDERECO);
                assertThat(testTransportadoraSearch.getNumero()).isEqualTo(UPDATED_NUMERO);
                assertThat(testTransportadoraSearch.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
                assertThat(testTransportadoraSearch.getBairro()).isEqualTo(UPDATED_BAIRRO);
                assertThat(testTransportadoraSearch.getTelefone()).isEqualTo(UPDATED_TELEFONE);
                assertThat(testTransportadoraSearch.getEmail()).isEqualTo(UPDATED_EMAIL);
                assertThat(testTransportadoraSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTransportadora() throws Exception {
        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        transportadora.setId(longCount.incrementAndGet());

        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportadoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportadoraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransportadora() throws Exception {
        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        transportadora.setId(longCount.incrementAndGet());

        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransportadora() throws Exception {
        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        transportadora.setId(longCount.incrementAndGet());

        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadoraMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTransportadoraWithPatch() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();

        // Update the transportadora using partial update
        Transportadora partialUpdatedTransportadora = new Transportadora();
        partialUpdatedTransportadora.setId(transportadora.getId());

        partialUpdatedTransportadora
            .inscricaoEstadual(UPDATED_INSCRICAO_ESTADUAL)
            .inscricaoMunicipal(UPDATED_INSCRICAO_MUNICIPAL)
            .responsavel(UPDATED_RESPONSAVEL)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .observacao(UPDATED_OBSERVACAO);

        restTransportadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportadora.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransportadora))
            )
            .andExpect(status().isOk());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        Transportadora testTransportadora = transportadoraList.get(transportadoraList.size() - 1);
        assertThat(testTransportadora.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTransportadora.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testTransportadora.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testTransportadora.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
        assertThat(testTransportadora.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
        assertThat(testTransportadora.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testTransportadora.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testTransportadora.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testTransportadora.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTransportadora.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testTransportadora.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testTransportadora.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testTransportadora.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTransportadora.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void fullUpdateTransportadoraWithPatch() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);

        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();

        // Update the transportadora using partial update
        Transportadora partialUpdatedTransportadora = new Transportadora();
        partialUpdatedTransportadora.setId(transportadora.getId());

        partialUpdatedTransportadora
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .inscricaoEstadual(UPDATED_INSCRICAO_ESTADUAL)
            .inscricaoMunicipal(UPDATED_INSCRICAO_MUNICIPAL)
            .responsavel(UPDATED_RESPONSAVEL)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .observacao(UPDATED_OBSERVACAO);

        restTransportadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportadora.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransportadora))
            )
            .andExpect(status().isOk());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        Transportadora testTransportadora = transportadoraList.get(transportadoraList.size() - 1);
        assertThat(testTransportadora.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTransportadora.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testTransportadora.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testTransportadora.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
        assertThat(testTransportadora.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
        assertThat(testTransportadora.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testTransportadora.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testTransportadora.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testTransportadora.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTransportadora.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testTransportadora.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testTransportadora.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testTransportadora.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTransportadora.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void patchNonExistingTransportadora() throws Exception {
        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        transportadora.setId(longCount.incrementAndGet());

        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transportadoraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransportadora() throws Exception {
        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        transportadora.setId(longCount.incrementAndGet());

        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransportadora() throws Exception {
        int databaseSizeBeforeUpdate = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        transportadora.setId(longCount.incrementAndGet());

        // Create the Transportadora
        TransportadoraDTO transportadoraDTO = transportadoraMapper.toDto(transportadora);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadoraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transportadoraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transportadora in the database
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTransportadora() throws Exception {
        // Initialize the database
        transportadoraRepository.saveAndFlush(transportadora);
        transportadoraRepository.save(transportadora);
        transportadoraSearchRepository.save(transportadora);

        int databaseSizeBeforeDelete = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the transportadora
        restTransportadoraMockMvc
            .perform(delete(ENTITY_API_URL_ID, transportadora.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transportadora> transportadoraList = transportadoraRepository.findAll();
        assertThat(transportadoraList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTransportadora() throws Exception {
        // Initialize the database
        transportadora = transportadoraRepository.saveAndFlush(transportadora);
        transportadoraSearchRepository.save(transportadora);

        // Search the transportadora
        restTransportadoraMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transportadora.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportadora.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].inscricaoEstadual").value(hasItem(DEFAULT_INSCRICAO_ESTADUAL)))
            .andExpect(jsonPath("$.[*].inscricaoMunicipal").value(hasItem(DEFAULT_INSCRICAO_MUNICIPAL)))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }
}
