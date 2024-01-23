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
import br.com.revenuebrasil.newcargas.repository.BancoRepository;
import br.com.revenuebrasil.newcargas.repository.search.BancoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.BancoMapper;
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
 * Integration tests for the {@link BancoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BancoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAA";
    private static final String UPDATED_CODIGO = "BBB";

    private static final String ENTITY_API_URL = "/api/bancos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/bancos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private BancoMapper bancoMapper;

    @Autowired
    private BancoSearchRepository bancoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBancoMockMvc;

    private Banco banco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banco createEntity(EntityManager em) {
        Banco banco = new Banco().nome(DEFAULT_NOME).codigo(DEFAULT_CODIGO);
        return banco;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banco createUpdatedEntity(EntityManager em) {
        Banco banco = new Banco().nome(UPDATED_NOME).codigo(UPDATED_CODIGO);
        return banco;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        bancoSearchRepository.deleteAll();
        assertThat(bancoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        banco = createEntity(em);
    }

    @Test
    @Transactional
    void createBanco() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);
        restBancoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isCreated());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testBanco.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void createBancoWithExistingId() throws Exception {
        // Create the Banco with an existing ID
        banco.setId(1L);
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        int databaseSizeBeforeCreate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBancoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        // set the field null
        banco.setNome(null);

        // Create the Banco, which fails.
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        restBancoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBancos() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList
        restBancoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }

    @Test
    @Transactional
    void getBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get the banco
        restBancoMockMvc
            .perform(get(ENTITY_API_URL_ID, banco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banco.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }

    @Test
    @Transactional
    void getBancosByIdFiltering() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        Long id = banco.getId();

        defaultBancoShouldBeFound("id.equals=" + id);
        defaultBancoShouldNotBeFound("id.notEquals=" + id);

        defaultBancoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBancoShouldNotBeFound("id.greaterThan=" + id);

        defaultBancoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBancoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBancosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where nome equals to DEFAULT_NOME
        defaultBancoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the bancoList where nome equals to UPDATED_NOME
        defaultBancoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllBancosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultBancoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the bancoList where nome equals to UPDATED_NOME
        defaultBancoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllBancosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where nome is not null
        defaultBancoShouldBeFound("nome.specified=true");

        // Get all the bancoList where nome is null
        defaultBancoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllBancosByNomeContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where nome contains DEFAULT_NOME
        defaultBancoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the bancoList where nome contains UPDATED_NOME
        defaultBancoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllBancosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where nome does not contain DEFAULT_NOME
        defaultBancoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the bancoList where nome does not contain UPDATED_NOME
        defaultBancoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllBancosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where codigo equals to DEFAULT_CODIGO
        defaultBancoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the bancoList where codigo equals to UPDATED_CODIGO
        defaultBancoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllBancosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultBancoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the bancoList where codigo equals to UPDATED_CODIGO
        defaultBancoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllBancosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where codigo is not null
        defaultBancoShouldBeFound("codigo.specified=true");

        // Get all the bancoList where codigo is null
        defaultBancoShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllBancosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where codigo contains DEFAULT_CODIGO
        defaultBancoShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the bancoList where codigo contains UPDATED_CODIGO
        defaultBancoShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllBancosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where codigo does not contain DEFAULT_CODIGO
        defaultBancoShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the bancoList where codigo does not contain UPDATED_CODIGO
        defaultBancoShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllBancosByContaBancariaIsEqualToSomething() throws Exception {
        ContaBancaria contaBancaria;
        if (TestUtil.findAll(em, ContaBancaria.class).isEmpty()) {
            bancoRepository.saveAndFlush(banco);
            contaBancaria = ContaBancariaResourceIT.createEntity(em);
        } else {
            contaBancaria = TestUtil.findAll(em, ContaBancaria.class).get(0);
        }
        em.persist(contaBancaria);
        em.flush();
        banco.addContaBancaria(contaBancaria);
        bancoRepository.saveAndFlush(banco);
        Long contaBancariaId = contaBancaria.getId();
        // Get all the bancoList where contaBancaria equals to contaBancariaId
        defaultBancoShouldBeFound("contaBancariaId.equals=" + contaBancariaId);

        // Get all the bancoList where contaBancaria equals to (contaBancariaId + 1)
        defaultBancoShouldNotBeFound("contaBancariaId.equals=" + (contaBancariaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBancoShouldBeFound(String filter) throws Exception {
        restBancoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));

        // Check, that the count call also returns 1
        restBancoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBancoShouldNotBeFound(String filter) throws Exception {
        restBancoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBancoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBanco() throws Exception {
        // Get the banco
        restBancoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        bancoSearchRepository.save(banco);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());

        // Update the banco
        Banco updatedBanco = bancoRepository.findById(banco.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBanco are not directly saved in db
        em.detach(updatedBanco);
        updatedBanco.nome(UPDATED_NOME).codigo(UPDATED_CODIGO);
        BancoDTO bancoDTO = bancoMapper.toDto(updatedBanco);

        restBancoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bancoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bancoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testBanco.getCodigo()).isEqualTo(UPDATED_CODIGO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Banco> bancoSearchList = IterableUtils.toList(bancoSearchRepository.findAll());
                Banco testBancoSearch = bancoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testBancoSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testBancoSearch.getCodigo()).isEqualTo(UPDATED_CODIGO);
            });
    }

    @Test
    @Transactional
    void putNonExistingBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        banco.setId(longCount.incrementAndGet());

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBancoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bancoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bancoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        banco.setId(longCount.incrementAndGet());

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bancoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        banco.setId(longCount.incrementAndGet());

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBancoWithPatch() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Update the banco using partial update
        Banco partialUpdatedBanco = new Banco();
        partialUpdatedBanco.setId(banco.getId());

        partialUpdatedBanco.nome(UPDATED_NOME);

        restBancoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanco))
            )
            .andExpect(status().isOk());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testBanco.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void fullUpdateBancoWithPatch() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Update the banco using partial update
        Banco partialUpdatedBanco = new Banco();
        partialUpdatedBanco.setId(banco.getId());

        partialUpdatedBanco.nome(UPDATED_NOME).codigo(UPDATED_CODIGO);

        restBancoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanco))
            )
            .andExpect(status().isOk());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testBanco.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void patchNonExistingBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        banco.setId(longCount.incrementAndGet());

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBancoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bancoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bancoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        banco.setId(longCount.incrementAndGet());

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bancoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        banco.setId(longCount.incrementAndGet());

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);
        bancoRepository.save(banco);
        bancoSearchRepository.save(banco);

        int databaseSizeBeforeDelete = bancoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the banco
        restBancoMockMvc
            .perform(delete(ENTITY_API_URL_ID, banco.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bancoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBanco() throws Exception {
        // Initialize the database
        banco = bancoRepository.saveAndFlush(banco);
        bancoSearchRepository.save(banco);

        // Search the banco
        restBancoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + banco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }
}
