package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.repository.HistoricoStatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.HistoricoStatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.HistoricoStatusColetaMapper;
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
 * Integration tests for the {@link HistoricoStatusColetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoricoStatusColetaResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CRIACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historico-status-coletas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/historico-status-coletas/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoricoStatusColetaRepository historicoStatusColetaRepository;

    @Autowired
    private HistoricoStatusColetaMapper historicoStatusColetaMapper;

    @Autowired
    private HistoricoStatusColetaSearchRepository historicoStatusColetaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoricoStatusColetaMockMvc;

    private HistoricoStatusColeta historicoStatusColeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricoStatusColeta createEntity(EntityManager em) {
        HistoricoStatusColeta historicoStatusColeta = new HistoricoStatusColeta()
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .observacao(DEFAULT_OBSERVACAO);
        return historicoStatusColeta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricoStatusColeta createUpdatedEntity(EntityManager em) {
        HistoricoStatusColeta historicoStatusColeta = new HistoricoStatusColeta()
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .observacao(UPDATED_OBSERVACAO);
        return historicoStatusColeta;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        historicoStatusColetaSearchRepository.deleteAll();
        assertThat(historicoStatusColetaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        historicoStatusColeta = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeCreate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);
        restHistoricoStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void createHistoricoStatusColetaWithExistingId() throws Exception {
        // Create the HistoricoStatusColeta with an existing ID
        historicoStatusColeta.setId(1L);
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        int databaseSizeBeforeCreate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricoStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        // set the field null
        historicoStatusColeta.setDataCriacao(null);

        // Create the HistoricoStatusColeta, which fails.
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        restHistoricoStatusColetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllHistoricoStatusColetas() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get all the historicoStatusColetaList
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoStatusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }

    @Test
    @Transactional
    void getHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        // Get the historicoStatusColeta
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_API_URL_ID, historicoStatusColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historicoStatusColeta.getId().intValue()))
            .andExpect(jsonPath("$.dataCriacao").value(sameInstant(DEFAULT_DATA_CRIACAO)))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO));
    }

    @Test
    @Transactional
    void getNonExistingHistoricoStatusColeta() throws Exception {
        // Get the historicoStatusColeta
        restHistoricoStatusColetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        historicoStatusColetaSearchRepository.save(historicoStatusColeta);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());

        // Update the historicoStatusColeta
        HistoricoStatusColeta updatedHistoricoStatusColeta = historicoStatusColetaRepository
            .findById(historicoStatusColeta.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedHistoricoStatusColeta are not directly saved in db
        em.detach(updatedHistoricoStatusColeta);
        updatedHistoricoStatusColeta.dataCriacao(UPDATED_DATA_CRIACAO).observacao(UPDATED_OBSERVACAO);
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(updatedHistoricoStatusColeta);

        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historicoStatusColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HistoricoStatusColeta> historicoStatusColetaSearchList = IterableUtils.toList(
                    historicoStatusColetaSearchRepository.findAll()
                );
                HistoricoStatusColeta testHistoricoStatusColetaSearch = historicoStatusColetaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHistoricoStatusColetaSearch.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
                assertThat(testHistoricoStatusColetaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historicoStatusColetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateHistoricoStatusColetaWithPatch() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();

        // Update the historicoStatusColeta using partial update
        HistoricoStatusColeta partialUpdatedHistoricoStatusColeta = new HistoricoStatusColeta();
        partialUpdatedHistoricoStatusColeta.setId(historicoStatusColeta.getId());

        partialUpdatedHistoricoStatusColeta.dataCriacao(UPDATED_DATA_CRIACAO).observacao(UPDATED_OBSERVACAO);

        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoricoStatusColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoricoStatusColeta))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void fullUpdateHistoricoStatusColetaWithPatch() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);

        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();

        // Update the historicoStatusColeta using partial update
        HistoricoStatusColeta partialUpdatedHistoricoStatusColeta = new HistoricoStatusColeta();
        partialUpdatedHistoricoStatusColeta.setId(historicoStatusColeta.getId());

        partialUpdatedHistoricoStatusColeta.dataCriacao(UPDATED_DATA_CRIACAO).observacao(UPDATED_OBSERVACAO);

        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoricoStatusColeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoricoStatusColeta))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        HistoricoStatusColeta testHistoricoStatusColeta = historicoStatusColetaList.get(historicoStatusColetaList.size() - 1);
        assertThat(testHistoricoStatusColeta.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testHistoricoStatusColeta.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void patchNonExistingHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historicoStatusColetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoricoStatusColeta() throws Exception {
        int databaseSizeBeforeUpdate = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        historicoStatusColeta.setId(longCount.incrementAndGet());

        // Create the HistoricoStatusColeta
        HistoricoStatusColetaDTO historicoStatusColetaDTO = historicoStatusColetaMapper.toDto(historicoStatusColeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoStatusColetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historicoStatusColetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoricoStatusColeta in the database
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        historicoStatusColetaRepository.save(historicoStatusColeta);
        historicoStatusColetaSearchRepository.save(historicoStatusColeta);

        int databaseSizeBeforeDelete = historicoStatusColetaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the historicoStatusColeta
        restHistoricoStatusColetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, historicoStatusColeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoricoStatusColeta> historicoStatusColetaList = historicoStatusColetaRepository.findAll();
        assertThat(historicoStatusColetaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historicoStatusColetaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchHistoricoStatusColeta() throws Exception {
        // Initialize the database
        historicoStatusColeta = historicoStatusColetaRepository.saveAndFlush(historicoStatusColeta);
        historicoStatusColetaSearchRepository.save(historicoStatusColeta);

        // Search the historicoStatusColeta
        restHistoricoStatusColetaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + historicoStatusColeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoStatusColeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }
}
