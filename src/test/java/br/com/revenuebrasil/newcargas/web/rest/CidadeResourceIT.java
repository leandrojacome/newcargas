package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.Estado;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.CidadeRepository;
import br.com.revenuebrasil.newcargas.repository.search.CidadeSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.mapper.CidadeMapper;
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
 * Integration tests for the {@link CidadeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CidadeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODIGO_IBGE = 7;
    private static final Integer UPDATED_CODIGO_IBGE = 8;
    private static final Integer SMALLER_CODIGO_IBGE = 7 - 1;

    private static final String ENTITY_API_URL = "/api/cidades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cidades/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CidadeMapper cidadeMapper;

    @Autowired
    private CidadeSearchRepository cidadeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCidadeMockMvc;

    private Cidade cidade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cidade createEntity(EntityManager em) {
        Cidade cidade = new Cidade().nome(DEFAULT_NOME).codigoIbge(DEFAULT_CODIGO_IBGE);
        return cidade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cidade createUpdatedEntity(EntityManager em) {
        Cidade cidade = new Cidade().nome(UPDATED_NOME).codigoIbge(UPDATED_CODIGO_IBGE);
        return cidade;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        cidadeSearchRepository.deleteAll();
        assertThat(cidadeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        cidade = createEntity(em);
    }

    @Test
    @Transactional
    void createCidade() throws Exception {
        int databaseSizeBeforeCreate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);
        restCidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cidadeDTO)))
            .andExpect(status().isCreated());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Cidade testCidade = cidadeList.get(cidadeList.size() - 1);
        assertThat(testCidade.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCidade.getCodigoIbge()).isEqualTo(DEFAULT_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void createCidadeWithExistingId() throws Exception {
        // Create the Cidade with an existing ID
        cidade.setId(1L);
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        int databaseSizeBeforeCreate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cidadeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        // set the field null
        cidade.setNome(null);

        // Create the Cidade, which fails.
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        restCidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cidadeDTO)))
            .andExpect(status().isBadRequest());

        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCidades() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList
        restCidadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigoIbge").value(hasItem(DEFAULT_CODIGO_IBGE)));
    }

    @Test
    @Transactional
    void getCidade() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get the cidade
        restCidadeMockMvc
            .perform(get(ENTITY_API_URL_ID, cidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cidade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codigoIbge").value(DEFAULT_CODIGO_IBGE));
    }

    @Test
    @Transactional
    void getCidadesByIdFiltering() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        Long id = cidade.getId();

        defaultCidadeShouldBeFound("id.equals=" + id);
        defaultCidadeShouldNotBeFound("id.notEquals=" + id);

        defaultCidadeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCidadeShouldNotBeFound("id.greaterThan=" + id);

        defaultCidadeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCidadeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCidadesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where nome equals to DEFAULT_NOME
        defaultCidadeShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the cidadeList where nome equals to UPDATED_NOME
        defaultCidadeShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCidadesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCidadeShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the cidadeList where nome equals to UPDATED_NOME
        defaultCidadeShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCidadesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where nome is not null
        defaultCidadeShouldBeFound("nome.specified=true");

        // Get all the cidadeList where nome is null
        defaultCidadeShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllCidadesByNomeContainsSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where nome contains DEFAULT_NOME
        defaultCidadeShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the cidadeList where nome contains UPDATED_NOME
        defaultCidadeShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCidadesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where nome does not contain DEFAULT_NOME
        defaultCidadeShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the cidadeList where nome does not contain UPDATED_NOME
        defaultCidadeShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsEqualToSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge equals to DEFAULT_CODIGO_IBGE
        defaultCidadeShouldBeFound("codigoIbge.equals=" + DEFAULT_CODIGO_IBGE);

        // Get all the cidadeList where codigoIbge equals to UPDATED_CODIGO_IBGE
        defaultCidadeShouldNotBeFound("codigoIbge.equals=" + UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsInShouldWork() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge in DEFAULT_CODIGO_IBGE or UPDATED_CODIGO_IBGE
        defaultCidadeShouldBeFound("codigoIbge.in=" + DEFAULT_CODIGO_IBGE + "," + UPDATED_CODIGO_IBGE);

        // Get all the cidadeList where codigoIbge equals to UPDATED_CODIGO_IBGE
        defaultCidadeShouldNotBeFound("codigoIbge.in=" + UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge is not null
        defaultCidadeShouldBeFound("codigoIbge.specified=true");

        // Get all the cidadeList where codigoIbge is null
        defaultCidadeShouldNotBeFound("codigoIbge.specified=false");
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge is greater than or equal to DEFAULT_CODIGO_IBGE
        defaultCidadeShouldBeFound("codigoIbge.greaterThanOrEqual=" + DEFAULT_CODIGO_IBGE);

        // Get all the cidadeList where codigoIbge is greater than or equal to (DEFAULT_CODIGO_IBGE + 1)
        defaultCidadeShouldNotBeFound("codigoIbge.greaterThanOrEqual=" + (DEFAULT_CODIGO_IBGE + 1));
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge is less than or equal to DEFAULT_CODIGO_IBGE
        defaultCidadeShouldBeFound("codigoIbge.lessThanOrEqual=" + DEFAULT_CODIGO_IBGE);

        // Get all the cidadeList where codigoIbge is less than or equal to SMALLER_CODIGO_IBGE
        defaultCidadeShouldNotBeFound("codigoIbge.lessThanOrEqual=" + SMALLER_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsLessThanSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge is less than DEFAULT_CODIGO_IBGE
        defaultCidadeShouldNotBeFound("codigoIbge.lessThan=" + DEFAULT_CODIGO_IBGE);

        // Get all the cidadeList where codigoIbge is less than (DEFAULT_CODIGO_IBGE + 1)
        defaultCidadeShouldBeFound("codigoIbge.lessThan=" + (DEFAULT_CODIGO_IBGE + 1));
    }

    @Test
    @Transactional
    void getAllCidadesByCodigoIbgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidadeList where codigoIbge is greater than DEFAULT_CODIGO_IBGE
        defaultCidadeShouldNotBeFound("codigoIbge.greaterThan=" + DEFAULT_CODIGO_IBGE);

        // Get all the cidadeList where codigoIbge is greater than SMALLER_CODIGO_IBGE
        defaultCidadeShouldBeFound("codigoIbge.greaterThan=" + SMALLER_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void getAllCidadesByEnderecoIsEqualToSomething() throws Exception {
        Endereco endereco;
        if (TestUtil.findAll(em, Endereco.class).isEmpty()) {
            cidadeRepository.saveAndFlush(cidade);
            endereco = EnderecoResourceIT.createEntity(em);
        } else {
            endereco = TestUtil.findAll(em, Endereco.class).get(0);
        }
        em.persist(endereco);
        em.flush();
        cidade.addEndereco(endereco);
        cidadeRepository.saveAndFlush(cidade);
        Long enderecoId = endereco.getId();
        // Get all the cidadeList where endereco equals to enderecoId
        defaultCidadeShouldBeFound("enderecoId.equals=" + enderecoId);

        // Get all the cidadeList where endereco equals to (enderecoId + 1)
        defaultCidadeShouldNotBeFound("enderecoId.equals=" + (enderecoId + 1));
    }

    @Test
    @Transactional
    void getAllCidadesByEmbarcadorIsEqualToSomething() throws Exception {
        Embarcador embarcador;
        if (TestUtil.findAll(em, Embarcador.class).isEmpty()) {
            cidadeRepository.saveAndFlush(cidade);
            embarcador = EmbarcadorResourceIT.createEntity(em);
        } else {
            embarcador = TestUtil.findAll(em, Embarcador.class).get(0);
        }
        em.persist(embarcador);
        em.flush();
        cidade.addEmbarcador(embarcador);
        cidadeRepository.saveAndFlush(cidade);
        Long embarcadorId = embarcador.getId();
        // Get all the cidadeList where embarcador equals to embarcadorId
        defaultCidadeShouldBeFound("embarcadorId.equals=" + embarcadorId);

        // Get all the cidadeList where embarcador equals to (embarcadorId + 1)
        defaultCidadeShouldNotBeFound("embarcadorId.equals=" + (embarcadorId + 1));
    }

    @Test
    @Transactional
    void getAllCidadesByTransportadoraIsEqualToSomething() throws Exception {
        Transportadora transportadora;
        if (TestUtil.findAll(em, Transportadora.class).isEmpty()) {
            cidadeRepository.saveAndFlush(cidade);
            transportadora = TransportadoraResourceIT.createEntity(em);
        } else {
            transportadora = TestUtil.findAll(em, Transportadora.class).get(0);
        }
        em.persist(transportadora);
        em.flush();
        cidade.addTransportadora(transportadora);
        cidadeRepository.saveAndFlush(cidade);
        Long transportadoraId = transportadora.getId();
        // Get all the cidadeList where transportadora equals to transportadoraId
        defaultCidadeShouldBeFound("transportadoraId.equals=" + transportadoraId);

        // Get all the cidadeList where transportadora equals to (transportadoraId + 1)
        defaultCidadeShouldNotBeFound("transportadoraId.equals=" + (transportadoraId + 1));
    }

    @Test
    @Transactional
    void getAllCidadesByEstadoIsEqualToSomething() throws Exception {
        Estado estado;
        if (TestUtil.findAll(em, Estado.class).isEmpty()) {
            cidadeRepository.saveAndFlush(cidade);
            estado = EstadoResourceIT.createEntity(em);
        } else {
            estado = TestUtil.findAll(em, Estado.class).get(0);
        }
        em.persist(estado);
        em.flush();
        cidade.setEstado(estado);
        cidadeRepository.saveAndFlush(cidade);
        Long estadoId = estado.getId();
        // Get all the cidadeList where estado equals to estadoId
        defaultCidadeShouldBeFound("estadoId.equals=" + estadoId);

        // Get all the cidadeList where estado equals to (estadoId + 1)
        defaultCidadeShouldNotBeFound("estadoId.equals=" + (estadoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCidadeShouldBeFound(String filter) throws Exception {
        restCidadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigoIbge").value(hasItem(DEFAULT_CODIGO_IBGE)));

        // Check, that the count call also returns 1
        restCidadeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCidadeShouldNotBeFound(String filter) throws Exception {
        restCidadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCidadeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCidade() throws Exception {
        // Get the cidade
        restCidadeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCidade() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        cidadeSearchRepository.save(cidade);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());

        // Update the cidade
        Cidade updatedCidade = cidadeRepository.findById(cidade.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCidade are not directly saved in db
        em.detach(updatedCidade);
        updatedCidade.nome(UPDATED_NOME).codigoIbge(UPDATED_CODIGO_IBGE);
        CidadeDTO cidadeDTO = cidadeMapper.toDto(updatedCidade);

        restCidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cidadeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cidadeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        Cidade testCidade = cidadeList.get(cidadeList.size() - 1);
        assertThat(testCidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCidade.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Cidade> cidadeSearchList = IterableUtils.toList(cidadeSearchRepository.findAll());
                Cidade testCidadeSearch = cidadeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCidadeSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testCidadeSearch.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
            });
    }

    @Test
    @Transactional
    void putNonExistingCidade() throws Exception {
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        cidade.setId(longCount.incrementAndGet());

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cidadeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cidadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCidade() throws Exception {
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        cidade.setId(longCount.incrementAndGet());

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cidadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCidade() throws Exception {
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        cidade.setId(longCount.incrementAndGet());

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCidadeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cidadeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCidadeWithPatch() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();

        // Update the cidade using partial update
        Cidade partialUpdatedCidade = new Cidade();
        partialUpdatedCidade.setId(cidade.getId());

        partialUpdatedCidade.nome(UPDATED_NOME);

        restCidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCidade))
            )
            .andExpect(status().isOk());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        Cidade testCidade = cidadeList.get(cidadeList.size() - 1);
        assertThat(testCidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCidade.getCodigoIbge()).isEqualTo(DEFAULT_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void fullUpdateCidadeWithPatch() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();

        // Update the cidade using partial update
        Cidade partialUpdatedCidade = new Cidade();
        partialUpdatedCidade.setId(cidade.getId());

        partialUpdatedCidade.nome(UPDATED_NOME).codigoIbge(UPDATED_CODIGO_IBGE);

        restCidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCidade))
            )
            .andExpect(status().isOk());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        Cidade testCidade = cidadeList.get(cidadeList.size() - 1);
        assertThat(testCidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCidade.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void patchNonExistingCidade() throws Exception {
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        cidade.setId(longCount.incrementAndGet());

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cidadeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cidadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCidade() throws Exception {
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        cidade.setId(longCount.incrementAndGet());

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cidadeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCidade() throws Exception {
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        cidade.setId(longCount.incrementAndGet());

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.toDto(cidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCidadeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cidadeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cidade in the database
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCidade() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);
        cidadeRepository.save(cidade);
        cidadeSearchRepository.save(cidade);

        int databaseSizeBeforeDelete = cidadeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the cidade
        restCidadeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cidade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cidade> cidadeList = cidadeRepository.findAll();
        assertThat(cidadeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cidadeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCidade() throws Exception {
        // Initialize the database
        cidade = cidadeRepository.saveAndFlush(cidade);
        cidadeSearchRepository.save(cidade);

        // Search the cidade
        restCidadeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigoIbge").value(hasItem(DEFAULT_CODIGO_IBGE)));
    }
}
