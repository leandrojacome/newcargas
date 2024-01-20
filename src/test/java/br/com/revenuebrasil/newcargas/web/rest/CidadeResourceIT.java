package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Cidade;
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
