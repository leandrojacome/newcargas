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
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.repository.EmbarcadorRepository;
import br.com.revenuebrasil.newcargas.repository.search.EmbarcadorSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EmbarcadorMapper;
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
            .observacao(DEFAULT_OBSERVACAO);
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
            .observacao(UPDATED_OBSERVACAO);
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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
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
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO));
    }

    @Test
    @Transactional
    void getEmbarcadorsByIdFiltering() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        Long id = embarcador.getId();

        defaultEmbarcadorShouldBeFound("id.equals=" + id);
        defaultEmbarcadorShouldNotBeFound("id.notEquals=" + id);

        defaultEmbarcadorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmbarcadorShouldNotBeFound("id.greaterThan=" + id);

        defaultEmbarcadorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmbarcadorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where nome equals to DEFAULT_NOME
        defaultEmbarcadorShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the embarcadorList where nome equals to UPDATED_NOME
        defaultEmbarcadorShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultEmbarcadorShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the embarcadorList where nome equals to UPDATED_NOME
        defaultEmbarcadorShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where nome is not null
        defaultEmbarcadorShouldBeFound("nome.specified=true");

        // Get all the embarcadorList where nome is null
        defaultEmbarcadorShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNomeContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where nome contains DEFAULT_NOME
        defaultEmbarcadorShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the embarcadorList where nome contains UPDATED_NOME
        defaultEmbarcadorShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where nome does not contain DEFAULT_NOME
        defaultEmbarcadorShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the embarcadorList where nome does not contain UPDATED_NOME
        defaultEmbarcadorShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cnpj equals to DEFAULT_CNPJ
        defaultEmbarcadorShouldBeFound("cnpj.equals=" + DEFAULT_CNPJ);

        // Get all the embarcadorList where cnpj equals to UPDATED_CNPJ
        defaultEmbarcadorShouldNotBeFound("cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cnpj in DEFAULT_CNPJ or UPDATED_CNPJ
        defaultEmbarcadorShouldBeFound("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ);

        // Get all the embarcadorList where cnpj equals to UPDATED_CNPJ
        defaultEmbarcadorShouldNotBeFound("cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cnpj is not null
        defaultEmbarcadorShouldBeFound("cnpj.specified=true");

        // Get all the embarcadorList where cnpj is null
        defaultEmbarcadorShouldNotBeFound("cnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCnpjContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cnpj contains DEFAULT_CNPJ
        defaultEmbarcadorShouldBeFound("cnpj.contains=" + DEFAULT_CNPJ);

        // Get all the embarcadorList where cnpj contains UPDATED_CNPJ
        defaultEmbarcadorShouldNotBeFound("cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cnpj does not contain DEFAULT_CNPJ
        defaultEmbarcadorShouldNotBeFound("cnpj.doesNotContain=" + DEFAULT_CNPJ);

        // Get all the embarcadorList where cnpj does not contain UPDATED_CNPJ
        defaultEmbarcadorShouldBeFound("cnpj.doesNotContain=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByRazaoSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where razaoSocial equals to DEFAULT_RAZAO_SOCIAL
        defaultEmbarcadorShouldBeFound("razaoSocial.equals=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the embarcadorList where razaoSocial equals to UPDATED_RAZAO_SOCIAL
        defaultEmbarcadorShouldNotBeFound("razaoSocial.equals=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByRazaoSocialIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where razaoSocial in DEFAULT_RAZAO_SOCIAL or UPDATED_RAZAO_SOCIAL
        defaultEmbarcadorShouldBeFound("razaoSocial.in=" + DEFAULT_RAZAO_SOCIAL + "," + UPDATED_RAZAO_SOCIAL);

        // Get all the embarcadorList where razaoSocial equals to UPDATED_RAZAO_SOCIAL
        defaultEmbarcadorShouldNotBeFound("razaoSocial.in=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByRazaoSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where razaoSocial is not null
        defaultEmbarcadorShouldBeFound("razaoSocial.specified=true");

        // Get all the embarcadorList where razaoSocial is null
        defaultEmbarcadorShouldNotBeFound("razaoSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByRazaoSocialContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where razaoSocial contains DEFAULT_RAZAO_SOCIAL
        defaultEmbarcadorShouldBeFound("razaoSocial.contains=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the embarcadorList where razaoSocial contains UPDATED_RAZAO_SOCIAL
        defaultEmbarcadorShouldNotBeFound("razaoSocial.contains=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByRazaoSocialNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where razaoSocial does not contain DEFAULT_RAZAO_SOCIAL
        defaultEmbarcadorShouldNotBeFound("razaoSocial.doesNotContain=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the embarcadorList where razaoSocial does not contain UPDATED_RAZAO_SOCIAL
        defaultEmbarcadorShouldBeFound("razaoSocial.doesNotContain=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoEstadualIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoEstadual equals to DEFAULT_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldBeFound("inscricaoEstadual.equals=" + DEFAULT_INSCRICAO_ESTADUAL);

        // Get all the embarcadorList where inscricaoEstadual equals to UPDATED_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldNotBeFound("inscricaoEstadual.equals=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoEstadualIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoEstadual in DEFAULT_INSCRICAO_ESTADUAL or UPDATED_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldBeFound("inscricaoEstadual.in=" + DEFAULT_INSCRICAO_ESTADUAL + "," + UPDATED_INSCRICAO_ESTADUAL);

        // Get all the embarcadorList where inscricaoEstadual equals to UPDATED_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldNotBeFound("inscricaoEstadual.in=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoEstadualIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoEstadual is not null
        defaultEmbarcadorShouldBeFound("inscricaoEstadual.specified=true");

        // Get all the embarcadorList where inscricaoEstadual is null
        defaultEmbarcadorShouldNotBeFound("inscricaoEstadual.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoEstadualContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoEstadual contains DEFAULT_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldBeFound("inscricaoEstadual.contains=" + DEFAULT_INSCRICAO_ESTADUAL);

        // Get all the embarcadorList where inscricaoEstadual contains UPDATED_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldNotBeFound("inscricaoEstadual.contains=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoEstadualNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoEstadual does not contain DEFAULT_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldNotBeFound("inscricaoEstadual.doesNotContain=" + DEFAULT_INSCRICAO_ESTADUAL);

        // Get all the embarcadorList where inscricaoEstadual does not contain UPDATED_INSCRICAO_ESTADUAL
        defaultEmbarcadorShouldBeFound("inscricaoEstadual.doesNotContain=" + UPDATED_INSCRICAO_ESTADUAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoMunicipalIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoMunicipal equals to DEFAULT_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldBeFound("inscricaoMunicipal.equals=" + DEFAULT_INSCRICAO_MUNICIPAL);

        // Get all the embarcadorList where inscricaoMunicipal equals to UPDATED_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldNotBeFound("inscricaoMunicipal.equals=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoMunicipalIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoMunicipal in DEFAULT_INSCRICAO_MUNICIPAL or UPDATED_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldBeFound("inscricaoMunicipal.in=" + DEFAULT_INSCRICAO_MUNICIPAL + "," + UPDATED_INSCRICAO_MUNICIPAL);

        // Get all the embarcadorList where inscricaoMunicipal equals to UPDATED_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldNotBeFound("inscricaoMunicipal.in=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoMunicipalIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoMunicipal is not null
        defaultEmbarcadorShouldBeFound("inscricaoMunicipal.specified=true");

        // Get all the embarcadorList where inscricaoMunicipal is null
        defaultEmbarcadorShouldNotBeFound("inscricaoMunicipal.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoMunicipalContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoMunicipal contains DEFAULT_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldBeFound("inscricaoMunicipal.contains=" + DEFAULT_INSCRICAO_MUNICIPAL);

        // Get all the embarcadorList where inscricaoMunicipal contains UPDATED_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldNotBeFound("inscricaoMunicipal.contains=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByInscricaoMunicipalNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where inscricaoMunicipal does not contain DEFAULT_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldNotBeFound("inscricaoMunicipal.doesNotContain=" + DEFAULT_INSCRICAO_MUNICIPAL);

        // Get all the embarcadorList where inscricaoMunicipal does not contain UPDATED_INSCRICAO_MUNICIPAL
        defaultEmbarcadorShouldBeFound("inscricaoMunicipal.doesNotContain=" + UPDATED_INSCRICAO_MUNICIPAL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where responsavel equals to DEFAULT_RESPONSAVEL
        defaultEmbarcadorShouldBeFound("responsavel.equals=" + DEFAULT_RESPONSAVEL);

        // Get all the embarcadorList where responsavel equals to UPDATED_RESPONSAVEL
        defaultEmbarcadorShouldNotBeFound("responsavel.equals=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where responsavel in DEFAULT_RESPONSAVEL or UPDATED_RESPONSAVEL
        defaultEmbarcadorShouldBeFound("responsavel.in=" + DEFAULT_RESPONSAVEL + "," + UPDATED_RESPONSAVEL);

        // Get all the embarcadorList where responsavel equals to UPDATED_RESPONSAVEL
        defaultEmbarcadorShouldNotBeFound("responsavel.in=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where responsavel is not null
        defaultEmbarcadorShouldBeFound("responsavel.specified=true");

        // Get all the embarcadorList where responsavel is null
        defaultEmbarcadorShouldNotBeFound("responsavel.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByResponsavelContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where responsavel contains DEFAULT_RESPONSAVEL
        defaultEmbarcadorShouldBeFound("responsavel.contains=" + DEFAULT_RESPONSAVEL);

        // Get all the embarcadorList where responsavel contains UPDATED_RESPONSAVEL
        defaultEmbarcadorShouldNotBeFound("responsavel.contains=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where responsavel does not contain DEFAULT_RESPONSAVEL
        defaultEmbarcadorShouldNotBeFound("responsavel.doesNotContain=" + DEFAULT_RESPONSAVEL);

        // Get all the embarcadorList where responsavel does not contain UPDATED_RESPONSAVEL
        defaultEmbarcadorShouldBeFound("responsavel.doesNotContain=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cep equals to DEFAULT_CEP
        defaultEmbarcadorShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the embarcadorList where cep equals to UPDATED_CEP
        defaultEmbarcadorShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCepIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultEmbarcadorShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the embarcadorList where cep equals to UPDATED_CEP
        defaultEmbarcadorShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cep is not null
        defaultEmbarcadorShouldBeFound("cep.specified=true");

        // Get all the embarcadorList where cep is null
        defaultEmbarcadorShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCepContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cep contains DEFAULT_CEP
        defaultEmbarcadorShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the embarcadorList where cep contains UPDATED_CEP
        defaultEmbarcadorShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCepNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where cep does not contain DEFAULT_CEP
        defaultEmbarcadorShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the embarcadorList where cep does not contain UPDATED_CEP
        defaultEmbarcadorShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where endereco equals to DEFAULT_ENDERECO
        defaultEmbarcadorShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the embarcadorList where endereco equals to UPDATED_ENDERECO
        defaultEmbarcadorShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultEmbarcadorShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the embarcadorList where endereco equals to UPDATED_ENDERECO
        defaultEmbarcadorShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where endereco is not null
        defaultEmbarcadorShouldBeFound("endereco.specified=true");

        // Get all the embarcadorList where endereco is null
        defaultEmbarcadorShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEnderecoContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where endereco contains DEFAULT_ENDERECO
        defaultEmbarcadorShouldBeFound("endereco.contains=" + DEFAULT_ENDERECO);

        // Get all the embarcadorList where endereco contains UPDATED_ENDERECO
        defaultEmbarcadorShouldNotBeFound("endereco.contains=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEnderecoNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where endereco does not contain DEFAULT_ENDERECO
        defaultEmbarcadorShouldNotBeFound("endereco.doesNotContain=" + DEFAULT_ENDERECO);

        // Get all the embarcadorList where endereco does not contain UPDATED_ENDERECO
        defaultEmbarcadorShouldBeFound("endereco.doesNotContain=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where numero equals to DEFAULT_NUMERO
        defaultEmbarcadorShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the embarcadorList where numero equals to UPDATED_NUMERO
        defaultEmbarcadorShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultEmbarcadorShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the embarcadorList where numero equals to UPDATED_NUMERO
        defaultEmbarcadorShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where numero is not null
        defaultEmbarcadorShouldBeFound("numero.specified=true");

        // Get all the embarcadorList where numero is null
        defaultEmbarcadorShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNumeroContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where numero contains DEFAULT_NUMERO
        defaultEmbarcadorShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the embarcadorList where numero contains UPDATED_NUMERO
        defaultEmbarcadorShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where numero does not contain DEFAULT_NUMERO
        defaultEmbarcadorShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the embarcadorList where numero does not contain UPDATED_NUMERO
        defaultEmbarcadorShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByComplementoIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where complemento equals to DEFAULT_COMPLEMENTO
        defaultEmbarcadorShouldBeFound("complemento.equals=" + DEFAULT_COMPLEMENTO);

        // Get all the embarcadorList where complemento equals to UPDATED_COMPLEMENTO
        defaultEmbarcadorShouldNotBeFound("complemento.equals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByComplementoIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where complemento in DEFAULT_COMPLEMENTO or UPDATED_COMPLEMENTO
        defaultEmbarcadorShouldBeFound("complemento.in=" + DEFAULT_COMPLEMENTO + "," + UPDATED_COMPLEMENTO);

        // Get all the embarcadorList where complemento equals to UPDATED_COMPLEMENTO
        defaultEmbarcadorShouldNotBeFound("complemento.in=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByComplementoIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where complemento is not null
        defaultEmbarcadorShouldBeFound("complemento.specified=true");

        // Get all the embarcadorList where complemento is null
        defaultEmbarcadorShouldNotBeFound("complemento.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByComplementoContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where complemento contains DEFAULT_COMPLEMENTO
        defaultEmbarcadorShouldBeFound("complemento.contains=" + DEFAULT_COMPLEMENTO);

        // Get all the embarcadorList where complemento contains UPDATED_COMPLEMENTO
        defaultEmbarcadorShouldNotBeFound("complemento.contains=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByComplementoNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where complemento does not contain DEFAULT_COMPLEMENTO
        defaultEmbarcadorShouldNotBeFound("complemento.doesNotContain=" + DEFAULT_COMPLEMENTO);

        // Get all the embarcadorList where complemento does not contain UPDATED_COMPLEMENTO
        defaultEmbarcadorShouldBeFound("complemento.doesNotContain=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where bairro equals to DEFAULT_BAIRRO
        defaultEmbarcadorShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the embarcadorList where bairro equals to UPDATED_BAIRRO
        defaultEmbarcadorShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultEmbarcadorShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the embarcadorList where bairro equals to UPDATED_BAIRRO
        defaultEmbarcadorShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where bairro is not null
        defaultEmbarcadorShouldBeFound("bairro.specified=true");

        // Get all the embarcadorList where bairro is null
        defaultEmbarcadorShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByBairroContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where bairro contains DEFAULT_BAIRRO
        defaultEmbarcadorShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the embarcadorList where bairro contains UPDATED_BAIRRO
        defaultEmbarcadorShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where bairro does not contain DEFAULT_BAIRRO
        defaultEmbarcadorShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the embarcadorList where bairro does not contain UPDATED_BAIRRO
        defaultEmbarcadorShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where telefone equals to DEFAULT_TELEFONE
        defaultEmbarcadorShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the embarcadorList where telefone equals to UPDATED_TELEFONE
        defaultEmbarcadorShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultEmbarcadorShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the embarcadorList where telefone equals to UPDATED_TELEFONE
        defaultEmbarcadorShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where telefone is not null
        defaultEmbarcadorShouldBeFound("telefone.specified=true");

        // Get all the embarcadorList where telefone is null
        defaultEmbarcadorShouldNotBeFound("telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where telefone contains DEFAULT_TELEFONE
        defaultEmbarcadorShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the embarcadorList where telefone contains UPDATED_TELEFONE
        defaultEmbarcadorShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where telefone does not contain DEFAULT_TELEFONE
        defaultEmbarcadorShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the embarcadorList where telefone does not contain UPDATED_TELEFONE
        defaultEmbarcadorShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where email equals to DEFAULT_EMAIL
        defaultEmbarcadorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the embarcadorList where email equals to UPDATED_EMAIL
        defaultEmbarcadorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmbarcadorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the embarcadorList where email equals to UPDATED_EMAIL
        defaultEmbarcadorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where email is not null
        defaultEmbarcadorShouldBeFound("email.specified=true");

        // Get all the embarcadorList where email is null
        defaultEmbarcadorShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where email contains DEFAULT_EMAIL
        defaultEmbarcadorShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the embarcadorList where email contains UPDATED_EMAIL
        defaultEmbarcadorShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where email does not contain DEFAULT_EMAIL
        defaultEmbarcadorShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the embarcadorList where email does not contain UPDATED_EMAIL
        defaultEmbarcadorShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where observacao equals to DEFAULT_OBSERVACAO
        defaultEmbarcadorShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the embarcadorList where observacao equals to UPDATED_OBSERVACAO
        defaultEmbarcadorShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultEmbarcadorShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the embarcadorList where observacao equals to UPDATED_OBSERVACAO
        defaultEmbarcadorShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where observacao is not null
        defaultEmbarcadorShouldBeFound("observacao.specified=true");

        // Get all the embarcadorList where observacao is null
        defaultEmbarcadorShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where observacao contains DEFAULT_OBSERVACAO
        defaultEmbarcadorShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the embarcadorList where observacao contains UPDATED_OBSERVACAO
        defaultEmbarcadorShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        embarcadorRepository.saveAndFlush(embarcador);

        // Get all the embarcadorList where observacao does not contain DEFAULT_OBSERVACAO
        defaultEmbarcadorShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the embarcadorList where observacao does not contain UPDATED_OBSERVACAO
        defaultEmbarcadorShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByEnderecoIsEqualToSomething_() throws Exception {
        Endereco endereco;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            endereco = EnderecoResourceIT.createEntity(em);
        } else {
            endereco = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(endereco);
        em.flush();
        embarcador.addEndereco(endereco);
        embarcadorRepository.saveAndFlush(embarcador);
        Long enderecoId = endereco.getId();
        // Get all the embarcadorList where endereco equals to enderecoId
        defaultEmbarcadorShouldBeFound("enderecoId.equals=" + enderecoId);

        // Get all the embarcadorList where endereco equals to (enderecoId + 1)
        defaultEmbarcadorShouldNotBeFound("enderecoId.equals=" + (enderecoId + 1));
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByContaBancariaIsEqualToSomething() throws Exception {
        ContaBancaria contaBancaria;
        if (TestUtil.findAll(em, ContaBancaria.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            contaBancaria = ContaBancariaResourceIT.createEntity(em);
        } else {
            contaBancaria = TestUtil.findAll(em, ContaBancaria.class).get(0);
        }
        em.persist(contaBancaria);
        em.flush();
        embarcador.addContaBancaria(contaBancaria);
        embarcadorRepository.saveAndFlush(embarcador);
        Long contaBancariaId = contaBancaria.getId();
        // Get all the embarcadorList where contaBancaria equals to contaBancariaId
        defaultEmbarcadorShouldBeFound("contaBancariaId.equals=" + contaBancariaId);

        // Get all the embarcadorList where contaBancaria equals to (contaBancariaId + 1)
        defaultEmbarcadorShouldNotBeFound("contaBancariaId.equals=" + (contaBancariaId + 1));
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByTabelaFreteIsEqualToSomething() throws Exception {
        TabelaFrete tabelaFrete;
        if (TestUtil.findAll(em, TabelaFrete.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            tabelaFrete = TabelaFreteResourceIT.createEntity(em);
        } else {
            tabelaFrete = TestUtil.findAll(em, TabelaFrete.class).get(0);
        }
        em.persist(tabelaFrete);
        em.flush();
        embarcador.addTabelaFrete(tabelaFrete);
        embarcadorRepository.saveAndFlush(embarcador);
        Long tabelaFreteId = tabelaFrete.getId();
        // Get all the embarcadorList where tabelaFrete equals to tabelaFreteId
        defaultEmbarcadorShouldBeFound("tabelaFreteId.equals=" + tabelaFreteId);

        // Get all the embarcadorList where tabelaFrete equals to (tabelaFreteId + 1)
        defaultEmbarcadorShouldNotBeFound("tabelaFreteId.equals=" + (tabelaFreteId + 1));
    }

    @Test
    @Transactional
    void getAllEmbarcadorsBySolitacaoColetaIsEqualToSomething() throws Exception {
        SolicitacaoColeta solitacaoColeta;
        if (TestUtil.findAll(em, SolicitacaoColeta.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            solitacaoColeta = SolicitacaoColetaResourceIT.createEntity(em);
        } else {
            solitacaoColeta = TestUtil.findAll(em, SolicitacaoColeta.class).get(0);
        }
        em.persist(solitacaoColeta);
        em.flush();
        embarcador.addSolitacaoColeta(solitacaoColeta);
        embarcadorRepository.saveAndFlush(embarcador);
        Long solitacaoColetaId = solitacaoColeta.getId();
        // Get all the embarcadorList where solitacaoColeta equals to solitacaoColetaId
        defaultEmbarcadorShouldBeFound("solitacaoColetaId.equals=" + solitacaoColetaId);

        // Get all the embarcadorList where solitacaoColeta equals to (solitacaoColetaId + 1)
        defaultEmbarcadorShouldNotBeFound("solitacaoColetaId.equals=" + (solitacaoColetaId + 1));
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByNotificacaoIsEqualToSomething() throws Exception {
        Notificacao notificacao;
        if (TestUtil.findAll(em, Notificacao.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            notificacao = NotificacaoResourceIT.createEntity(em);
        } else {
            notificacao = TestUtil.findAll(em, Notificacao.class).get(0);
        }
        em.persist(notificacao);
        em.flush();
        embarcador.addNotificacao(notificacao);
        embarcadorRepository.saveAndFlush(embarcador);
        Long notificacaoId = notificacao.getId();
        // Get all the embarcadorList where notificacao equals to notificacaoId
        defaultEmbarcadorShouldBeFound("notificacaoId.equals=" + notificacaoId);

        // Get all the embarcadorList where notificacao equals to (notificacaoId + 1)
        defaultEmbarcadorShouldNotBeFound("notificacaoId.equals=" + (notificacaoId + 1));
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByFaturaIsEqualToSomething() throws Exception {
        Fatura fatura;
        if (TestUtil.findAll(em, Fatura.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            fatura = FaturaResourceIT.createEntity(em);
        } else {
            fatura = TestUtil.findAll(em, Fatura.class).get(0);
        }
        em.persist(fatura);
        em.flush();
        embarcador.addFatura(fatura);
        embarcadorRepository.saveAndFlush(embarcador);
        Long faturaId = fatura.getId();
        // Get all the embarcadorList where fatura equals to faturaId
        defaultEmbarcadorShouldBeFound("faturaId.equals=" + faturaId);

        // Get all the embarcadorList where fatura equals to (faturaId + 1)
        defaultEmbarcadorShouldNotBeFound("faturaId.equals=" + (faturaId + 1));
    }

    @Test
    @Transactional
    void getAllEmbarcadorsByCidadeIsEqualToSomething() throws Exception {
        Cidade cidade;
        if (TestUtil.findAll(em, Cidade.class).isEmpty()) {
            embarcadorRepository.saveAndFlush(embarcador);
            cidade = CidadeResourceIT.createEntity(em);
        } else {
            cidade = TestUtil.findAll(em, Cidade.class).get(0);
        }
        em.persist(cidade);
        em.flush();
        embarcador.setCidade(cidade);
        embarcadorRepository.saveAndFlush(embarcador);
        Long cidadeId = cidade.getId();
        // Get all the embarcadorList where cidade equals to cidadeId
        defaultEmbarcadorShouldBeFound("cidadeId.equals=" + cidadeId);

        // Get all the embarcadorList where cidade equals to (cidadeId + 1)
        defaultEmbarcadorShouldNotBeFound("cidadeId.equals=" + (cidadeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmbarcadorShouldBeFound(String filter) throws Exception {
        restEmbarcadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));

        // Check, that the count call also returns 1
        restEmbarcadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmbarcadorShouldNotBeFound(String filter) throws Exception {
        restEmbarcadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmbarcadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .observacao(UPDATED_OBSERVACAO);
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
            .email(UPDATED_EMAIL);

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
            .observacao(UPDATED_OBSERVACAO);

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
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }
}
