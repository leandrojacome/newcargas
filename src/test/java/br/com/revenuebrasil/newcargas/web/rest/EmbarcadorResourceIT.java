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
import br.com.revenuebrasil.newcargas.repository.EmbarcadorRepository;
import br.com.revenuebrasil.newcargas.repository.search.EmbarcadorSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EmbarcadorMapper;
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
 * Integration tests for the {@link EmbarcadorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmbarcadorResourceIT {

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

    private static final String ENTITY_API_URL = "/api/embarcadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/embarcadors/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmbarcadorRepository embarcadorRepository;

    @Autowired
    private EmbarcadorMapper embarcadorMapper;

    @Autowired
    private EmbarcadorSearchRepository embarcadorSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmbarcadorMockMvc;

    private Embarcador embarcador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Embarcador createEntity(EntityManager em) {
        Embarcador embarcador = new Embarcador()
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
        return embarcador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Embarcador createUpdatedEntity(EntityManager em) {
        Embarcador embarcador = new Embarcador()
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
        return embarcador;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        embarcadorSearchRepository.deleteAll();
        assertThat(embarcadorSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        embarcador = createEntity(em);
    }

    @Test
    @Transactional
    void createEmbarcador() throws Exception {
        int databaseSizeBeforeCreate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);
        restEmbarcadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(embarcadorDTO)))
            .andExpect(status().isCreated());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Embarcador testEmbarcador = embarcadorList.get(embarcadorList.size() - 1);
        assertThat(testEmbarcador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEmbarcador.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testEmbarcador.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testEmbarcador.getInscricaoEstadual()).isEqualTo(DEFAULT_INSCRICAO_ESTADUAL);
        assertThat(testEmbarcador.getInscricaoMunicipal()).isEqualTo(DEFAULT_INSCRICAO_MUNICIPAL);
        assertThat(testEmbarcador.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testEmbarcador.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEmbarcador.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testEmbarcador.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEmbarcador.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEmbarcador.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEmbarcador.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testEmbarcador.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmbarcador.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testEmbarcador.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testEmbarcador.getUsuarioCadastro()).isEqualTo(DEFAULT_USUARIO_CADASTRO);
        assertThat(testEmbarcador.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testEmbarcador.getUsuarioAtualizacao()).isEqualTo(DEFAULT_USUARIO_ATUALIZACAO);
    }

    @Test
    @Transactional
    void createEmbarcadorWithExistingId() throws Exception {
        // Create the Embarcador with an existing ID
        embarcador.setId(1L);
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        int databaseSizeBeforeCreate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmbarcadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(embarcadorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        // set the field null
        embarcador.setNome(null);

        // Create the Embarcador, which fails.
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        restEmbarcadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(embarcadorDTO)))
            .andExpect(status().isBadRequest());

        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCnpjIsRequired() throws Exception {
        int databaseSizeBeforeTest = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        // set the field null
        embarcador.setCnpj(null);

        // Create the Embarcador, which fails.
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        restEmbarcadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(embarcadorDTO)))
            .andExpect(status().isBadRequest());

        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        // set the field null
        embarcador.setDataCadastro(null);

        // Create the Embarcador, which fails.
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        restEmbarcadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(embarcadorDTO)))
            .andExpect(status().isBadRequest());

        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEmbarcadors() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList
        restEmbarcadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(embarcador.getId().intValue())))
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
    void getEmbarcador() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get the embarcador
        restEmbarcadorMockMvc
            .perform(get(ENTITY_API_URL_ID, embarcador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(embarcador.getId().intValue()))
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
    void getNonExistingEmbarcador() throws Exception {
        // Get the embarcador
        restEmbarcadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmbarcador() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        embarcadorSearchRepository.save(embarcador);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());

        // Update the embarcador
        Embarcador updatedEmbarcador = embarcadorRepository.findById(embarcador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmbarcador are not directly saved in db
        em.detach(updatedEmbarcador);
        updatedEmbarcador
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
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(updatedEmbarcador);

        restEmbarcadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, embarcadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embarcadorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        Embarcador testEmbarcador = embarcadorList.get(embarcadorList.size() - 1);
        assertThat(testEmbarcador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmbarcador.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testEmbarcador.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmbarcador.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
        assertThat(testEmbarcador.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
        assertThat(testEmbarcador.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testEmbarcador.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEmbarcador.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testEmbarcador.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEmbarcador.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEmbarcador.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEmbarcador.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testEmbarcador.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmbarcador.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testEmbarcador.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testEmbarcador.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testEmbarcador.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testEmbarcador.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Embarcador> embarcadorSearchList = IterableUtils.toList(embarcadorSearchRepository.findAll());
                Embarcador testEmbarcadorSearch = embarcadorSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEmbarcadorSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testEmbarcadorSearch.getCnpj()).isEqualTo(UPDATED_CNPJ);
                assertThat(testEmbarcadorSearch.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
                assertThat(testEmbarcadorSearch.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
                assertThat(testEmbarcadorSearch.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
                assertThat(testEmbarcadorSearch.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
                assertThat(testEmbarcadorSearch.getCep()).isEqualTo(UPDATED_CEP);
                assertThat(testEmbarcadorSearch.getEndereco()).isEqualTo(UPDATED_ENDERECO);
                assertThat(testEmbarcadorSearch.getNumero()).isEqualTo(UPDATED_NUMERO);
                assertThat(testEmbarcadorSearch.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
                assertThat(testEmbarcadorSearch.getBairro()).isEqualTo(UPDATED_BAIRRO);
                assertThat(testEmbarcadorSearch.getTelefone()).isEqualTo(UPDATED_TELEFONE);
                assertThat(testEmbarcadorSearch.getEmail()).isEqualTo(UPDATED_EMAIL);
                assertThat(testEmbarcadorSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testEmbarcadorSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testEmbarcadorSearch.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
                assertThat(testEmbarcadorSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
                assertThat(testEmbarcadorSearch.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingEmbarcador() throws Exception {
        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        embarcador.setId(longCount.incrementAndGet());

        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmbarcadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, embarcadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embarcadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmbarcador() throws Exception {
        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        embarcador.setId(longCount.incrementAndGet());

        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbarcadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(embarcadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmbarcador() throws Exception {
        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        embarcador.setId(longCount.incrementAndGet());

        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbarcadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(embarcadorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEmbarcadorWithPatch() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();

        // Update the embarcador using partial update
        Embarcador partialUpdatedEmbarcador = new Embarcador();
        partialUpdatedEmbarcador.setId(embarcador.getId());

        partialUpdatedEmbarcador
            .inscricaoEstadual(UPDATED_INSCRICAO_ESTADUAL)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .email(UPDATED_EMAIL)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .usuarioCadastro(UPDATED_USUARIO_CADASTRO)
            .usuarioAtualizacao(UPDATED_USUARIO_ATUALIZACAO);

        restEmbarcadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmbarcador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmbarcador))
            )
            .andExpect(status().isOk());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        Embarcador testEmbarcador = embarcadorList.get(embarcadorList.size() - 1);
        assertThat(testEmbarcador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEmbarcador.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testEmbarcador.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testEmbarcador.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
        assertThat(testEmbarcador.getInscricaoMunicipal()).isEqualTo(DEFAULT_INSCRICAO_MUNICIPAL);
        assertThat(testEmbarcador.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testEmbarcador.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEmbarcador.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testEmbarcador.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEmbarcador.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEmbarcador.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEmbarcador.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testEmbarcador.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmbarcador.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testEmbarcador.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testEmbarcador.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testEmbarcador.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
        assertThat(testEmbarcador.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
    }

    @Test
    @Transactional
    void fullUpdateEmbarcadorWithPatch() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();

        // Update the embarcador using partial update
        Embarcador partialUpdatedEmbarcador = new Embarcador();
        partialUpdatedEmbarcador.setId(embarcador.getId());

        partialUpdatedEmbarcador
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

        restEmbarcadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmbarcador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmbarcador))
            )
            .andExpect(status().isOk());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        Embarcador testEmbarcador = embarcadorList.get(embarcadorList.size() - 1);
        assertThat(testEmbarcador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmbarcador.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testEmbarcador.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmbarcador.getInscricaoEstadual()).isEqualTo(UPDATED_INSCRICAO_ESTADUAL);
        assertThat(testEmbarcador.getInscricaoMunicipal()).isEqualTo(UPDATED_INSCRICAO_MUNICIPAL);
        assertThat(testEmbarcador.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testEmbarcador.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEmbarcador.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testEmbarcador.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEmbarcador.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEmbarcador.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEmbarcador.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testEmbarcador.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmbarcador.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testEmbarcador.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testEmbarcador.getUsuarioCadastro()).isEqualTo(UPDATED_USUARIO_CADASTRO);
        assertThat(testEmbarcador.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        assertThat(testEmbarcador.getUsuarioAtualizacao()).isEqualTo(UPDATED_USUARIO_ATUALIZACAO);
    }

    @Test
    @Transactional
    void patchNonExistingEmbarcador() throws Exception {
        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        embarcador.setId(longCount.incrementAndGet());

        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmbarcadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, embarcadorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(embarcadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmbarcador() throws Exception {
        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        embarcador.setId(longCount.incrementAndGet());

        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbarcadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(embarcadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmbarcador() throws Exception {
        int databaseSizeBeforeUpdate = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        embarcador.setId(longCount.incrementAndGet());

        // Create the Embarcador
        EmbarcadorDTO embarcadorDTO = embarcadorMapper.toDto(embarcador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmbarcadorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(embarcadorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Embarcador in the database
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEmbarcador() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);
        embarcadorRepository.save(embarcador);
        embarcadorSearchRepository.save(embarcador);

        int databaseSizeBeforeDelete = embarcadorRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the embarcador
        restEmbarcadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, embarcador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Embarcador> embarcadorList = embarcadorRepository.findAll();
        assertThat(embarcadorList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(embarcadorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEmbarcador() throws Exception {
        // Initialize the database
        embarcador = embarcadorRepository.saveAndFlush(embarcador);
        embarcadorSearchRepository.save(embarcador);

        // Search the embarcador
        restEmbarcadorMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + embarcador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(embarcador.getId().intValue())))
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
