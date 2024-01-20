package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.TransportadoraRepository;
import br.com.revenuebrasil.newcargas.repository.search.TransportadoraSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TransportadoraMapper;
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

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_CADASTRO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CADASTRO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USUARIO_ATUALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_ATUALIZACAO = "BBBBBBBBBB";

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
            .observacao(DEFAULT_OBSERVACAO)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .usuarioCadastro(DEFAULT_USUARIO_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO)
            .usuarioAtualizacao(DEFAULT_USUARIO_ATUALIZACAO);
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
            .observacao(UPDATED_OBSERVACAO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO);
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
        assertThat(testTransportadora.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testTransportadora.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testTransportadora.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testTransportadora.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
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
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportadoraRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transportadoraSearchRepository.findAll());
        // set the field null
        transportadora.setDataCadastro(null);

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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)));
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
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.usuarioCadastro").value(DEFAULT_USUARIO_CADASTRO))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)))
            .andExpect(jsonPath("$.usuarioAtualizacao").value(DEFAULT_USUARIO_ATUALIZACAO));
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
            .observacao(UPDATED_OBSERVACAO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO);
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
        assertThat(testTransportadora.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testTransportadora.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testTransportadora.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testTransportadora.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
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
                assertThat(testTransportadoraSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testTransportadoraSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testTransportadoraSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testTransportadoraSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
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
            .observacao(UPDATED_OBSERVACAO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO);

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
        assertThat(testTransportadora.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testTransportadora.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testTransportadora.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testTransportadora.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
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
            .observacao(UPDATED_OBSERVACAO)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO);

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
        assertThat(testTransportadora.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testTransportadora.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testTransportadora.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testTransportadora.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].usuarioCadastro").value(hasItem(DEFAULT_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))))
            .andExpect(jsonPath("$.[*].usuarioAtualizacao").value(hasItem(DEFAULT_USUARIO_ATUALIZACAO)));
    }
}
