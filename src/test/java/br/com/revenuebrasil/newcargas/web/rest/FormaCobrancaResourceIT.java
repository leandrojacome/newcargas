package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.repository.FormaCobrancaRepository;
import br.com.revenuebrasil.newcargas.repository.search.FormaCobrancaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.FormaCobrancaMapper;
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
 * Integration tests for the {@link FormaCobrancaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormaCobrancaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/forma-cobrancas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/forma-cobrancas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormaCobrancaRepository formaCobrancaRepository;

    @Autowired
    private FormaCobrancaMapper formaCobrancaMapper;

    @Autowired
    private FormaCobrancaSearchRepository formaCobrancaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormaCobrancaMockMvc;

    private FormaCobranca formaCobranca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormaCobranca createEntity(EntityManager em) {
        FormaCobranca formaCobranca = new FormaCobranca().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return formaCobranca;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormaCobranca createUpdatedEntity(EntityManager em) {
        FormaCobranca formaCobranca = new FormaCobranca().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return formaCobranca;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        formaCobrancaSearchRepository.deleteAll();
        assertThat(formaCobrancaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        formaCobranca = createEntity(em);
    }

    @Test
    @Transactional
    void createFormaCobranca() throws Exception {
        int databaseSizeBeforeCreate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);
        restFormaCobrancaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        FormaCobranca testFormaCobranca = formaCobrancaList.get(formaCobrancaList.size() - 1);
        assertThat(testFormaCobranca.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFormaCobranca.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createFormaCobrancaWithExistingId() throws Exception {
        // Create the FormaCobranca with an existing ID
        formaCobranca.setId(1L);
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        int databaseSizeBeforeCreate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormaCobrancaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        // set the field null
        formaCobranca.setNome(null);

        // Create the FormaCobranca, which fails.
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        restFormaCobrancaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFormaCobrancas() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaCobranca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getFormaCobranca() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get the formaCobranca
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_API_URL_ID, formaCobranca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formaCobranca.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getFormaCobrancasByIdFiltering() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        Long id = formaCobranca.getId();

        defaultFormaCobrancaShouldBeFound("id.equals=" + id);
        defaultFormaCobrancaShouldNotBeFound("id.notEquals=" + id);

        defaultFormaCobrancaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormaCobrancaShouldNotBeFound("id.greaterThan=" + id);

        defaultFormaCobrancaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormaCobrancaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where nome equals to DEFAULT_NOME
        defaultFormaCobrancaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the formaCobrancaList where nome equals to UPDATED_NOME
        defaultFormaCobrancaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFormaCobrancaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the formaCobrancaList where nome equals to UPDATED_NOME
        defaultFormaCobrancaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where nome is not null
        defaultFormaCobrancaShouldBeFound("nome.specified=true");

        // Get all the formaCobrancaList where nome is null
        defaultFormaCobrancaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByNomeContainsSomething() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where nome contains DEFAULT_NOME
        defaultFormaCobrancaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the formaCobrancaList where nome contains UPDATED_NOME
        defaultFormaCobrancaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where nome does not contain DEFAULT_NOME
        defaultFormaCobrancaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the formaCobrancaList where nome does not contain UPDATED_NOME
        defaultFormaCobrancaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where descricao equals to DEFAULT_DESCRICAO
        defaultFormaCobrancaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the formaCobrancaList where descricao equals to UPDATED_DESCRICAO
        defaultFormaCobrancaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultFormaCobrancaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the formaCobrancaList where descricao equals to UPDATED_DESCRICAO
        defaultFormaCobrancaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where descricao is not null
        defaultFormaCobrancaShouldBeFound("descricao.specified=true");

        // Get all the formaCobrancaList where descricao is null
        defaultFormaCobrancaShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where descricao contains DEFAULT_DESCRICAO
        defaultFormaCobrancaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the formaCobrancaList where descricao contains UPDATED_DESCRICAO
        defaultFormaCobrancaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        // Get all the formaCobrancaList where descricao does not contain DEFAULT_DESCRICAO
        defaultFormaCobrancaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the formaCobrancaList where descricao does not contain UPDATED_DESCRICAO
        defaultFormaCobrancaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByTabelaFreteIsEqualToSomething() throws Exception {
        TabelaFrete tabelaFrete;
        if (TestUtil.findAll(em, TabelaFrete.class).isEmpty()) {
            formaCobrancaRepository.saveAndFlush(formaCobranca);
            tabelaFrete = TabelaFreteResourceIT.createEntity(em);
        } else {
            tabelaFrete = TestUtil.findAll(em, TabelaFrete.class).get(0);
        }
        em.persist(tabelaFrete);
        em.flush();
        formaCobranca.addTabelaFrete(tabelaFrete);
        formaCobrancaRepository.saveAndFlush(formaCobranca);
        Long tabelaFreteId = tabelaFrete.getId();
        // Get all the formaCobrancaList where tabelaFrete equals to tabelaFreteId
        defaultFormaCobrancaShouldBeFound("tabelaFreteId.equals=" + tabelaFreteId);

        // Get all the formaCobrancaList where tabelaFrete equals to (tabelaFreteId + 1)
        defaultFormaCobrancaShouldNotBeFound("tabelaFreteId.equals=" + (tabelaFreteId + 1));
    }

    @Test
    @Transactional
    void getAllFormaCobrancasByFatutaIsEqualToSomething() throws Exception {
        Fatura fatuta;
        if (TestUtil.findAll(em, Fatura.class).isEmpty()) {
            formaCobrancaRepository.saveAndFlush(formaCobranca);
            fatuta = FaturaResourceIT.createEntity(em);
        } else {
            fatuta = TestUtil.findAll(em, Fatura.class).get(0);
        }
        em.persist(fatuta);
        em.flush();
        formaCobranca.addFatuta(fatuta);
        formaCobrancaRepository.saveAndFlush(formaCobranca);
        Long fatutaId = fatuta.getId();
        // Get all the formaCobrancaList where fatuta equals to fatutaId
        defaultFormaCobrancaShouldBeFound("fatutaId.equals=" + fatutaId);

        // Get all the formaCobrancaList where fatuta equals to (fatutaId + 1)
        defaultFormaCobrancaShouldNotBeFound("fatutaId.equals=" + (fatutaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormaCobrancaShouldBeFound(String filter) throws Exception {
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaCobranca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormaCobrancaShouldNotBeFound(String filter) throws Exception {
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormaCobranca() throws Exception {
        // Get the formaCobranca
        restFormaCobrancaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormaCobranca() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        formaCobrancaSearchRepository.save(formaCobranca);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());

        // Update the formaCobranca
        FormaCobranca updatedFormaCobranca = formaCobrancaRepository.findById(formaCobranca.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormaCobranca are not directly saved in db
        em.detach(updatedFormaCobranca);
        updatedFormaCobranca.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(updatedFormaCobranca);

        restFormaCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formaCobrancaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        FormaCobranca testFormaCobranca = formaCobrancaList.get(formaCobrancaList.size() - 1);
        assertThat(testFormaCobranca.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFormaCobranca.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FormaCobranca> formaCobrancaSearchList = IterableUtils.toList(formaCobrancaSearchRepository.findAll());
                FormaCobranca testFormaCobrancaSearch = formaCobrancaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFormaCobrancaSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testFormaCobrancaSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingFormaCobranca() throws Exception {
        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        formaCobranca.setId(longCount.incrementAndGet());

        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formaCobrancaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormaCobranca() throws Exception {
        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        formaCobranca.setId(longCount.incrementAndGet());

        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormaCobranca() throws Exception {
        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        formaCobranca.setId(longCount.incrementAndGet());

        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFormaCobrancaWithPatch() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();

        // Update the formaCobranca using partial update
        FormaCobranca partialUpdatedFormaCobranca = new FormaCobranca();
        partialUpdatedFormaCobranca.setId(formaCobranca.getId());

        partialUpdatedFormaCobranca.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restFormaCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormaCobranca.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormaCobranca))
            )
            .andExpect(status().isOk());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        FormaCobranca testFormaCobranca = formaCobrancaList.get(formaCobrancaList.size() - 1);
        assertThat(testFormaCobranca.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFormaCobranca.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateFormaCobrancaWithPatch() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);

        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();

        // Update the formaCobranca using partial update
        FormaCobranca partialUpdatedFormaCobranca = new FormaCobranca();
        partialUpdatedFormaCobranca.setId(formaCobranca.getId());

        partialUpdatedFormaCobranca.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restFormaCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormaCobranca.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormaCobranca))
            )
            .andExpect(status().isOk());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        FormaCobranca testFormaCobranca = formaCobrancaList.get(formaCobrancaList.size() - 1);
        assertThat(testFormaCobranca.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFormaCobranca.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingFormaCobranca() throws Exception {
        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        formaCobranca.setId(longCount.incrementAndGet());

        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formaCobrancaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormaCobranca() throws Exception {
        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        formaCobranca.setId(longCount.incrementAndGet());

        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormaCobranca() throws Exception {
        int databaseSizeBeforeUpdate = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        formaCobranca.setId(longCount.incrementAndGet());

        // Create the FormaCobranca
        FormaCobrancaDTO formaCobrancaDTO = formaCobrancaMapper.toDto(formaCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formaCobrancaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormaCobranca in the database
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFormaCobranca() throws Exception {
        // Initialize the database
        formaCobrancaRepository.saveAndFlush(formaCobranca);
        formaCobrancaRepository.save(formaCobranca);
        formaCobrancaSearchRepository.save(formaCobranca);

        int databaseSizeBeforeDelete = formaCobrancaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the formaCobranca
        restFormaCobrancaMockMvc
            .perform(delete(ENTITY_API_URL_ID, formaCobranca.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormaCobranca> formaCobrancaList = formaCobrancaRepository.findAll();
        assertThat(formaCobrancaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formaCobrancaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFormaCobranca() throws Exception {
        // Initialize the database
        formaCobranca = formaCobrancaRepository.saveAndFlush(formaCobranca);
        formaCobrancaSearchRepository.save(formaCobranca);

        // Search the formaCobranca
        restFormaCobrancaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + formaCobranca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaCobranca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
