package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Estado;
import br.com.revenuebrasil.newcargas.repository.EstadoRepository;
import br.com.revenuebrasil.newcargas.repository.search.EstadoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.EstadoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EstadoMapper;
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
 * Integration tests for the {@link EstadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstadoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AA";
    private static final String UPDATED_SIGLA = "BB";

    private static final Integer DEFAULT_CODIGO_IBGE = 7;
    private static final Integer UPDATED_CODIGO_IBGE = 8;

    private static final String ENTITY_API_URL = "/api/estados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/estados/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private EstadoMapper estadoMapper;

    @Autowired
    private EstadoSearchRepository estadoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoMockMvc;

    private Estado estado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estado createEntity(EntityManager em) {
        Estado estado = new Estado().nome(DEFAULT_NOME).sigla(DEFAULT_SIGLA).codigoIbge(DEFAULT_CODIGO_IBGE);
        return estado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estado createUpdatedEntity(EntityManager em) {
        Estado estado = new Estado().nome(UPDATED_NOME).sigla(UPDATED_SIGLA).codigoIbge(UPDATED_CODIGO_IBGE);
        return estado;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        estadoSearchRepository.deleteAll();
        assertThat(estadoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        estado = createEntity(em);
    }

    @Test
    @Transactional
    void createEstado() throws Exception {
        int databaseSizeBeforeCreate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);
        restEstadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
            .andExpect(status().isCreated());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Estado testEstado = estadoList.get(estadoList.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEstado.getSigla()).isEqualTo(DEFAULT_SIGLA);
        assertThat(testEstado.getCodigoIbge()).isEqualTo(DEFAULT_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void createEstadoWithExistingId() throws Exception {
        // Create the Estado with an existing ID
        estado.setId(1L);
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        int databaseSizeBeforeCreate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        // set the field null
        estado.setNome(null);

        // Create the Estado, which fails.
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        restEstadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
            .andExpect(status().isBadRequest());

        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSiglaIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        // set the field null
        estado.setSigla(null);

        // Create the Estado, which fails.
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        restEstadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
            .andExpect(status().isBadRequest());

        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEstados() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get all the estadoList
        restEstadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].codigoIbge").value(hasItem(DEFAULT_CODIGO_IBGE)));
    }

    @Test
    @Transactional
    void getEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get the estado
        restEstadoMockMvc
            .perform(get(ENTITY_API_URL_ID, estado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA))
            .andExpect(jsonPath("$.codigoIbge").value(DEFAULT_CODIGO_IBGE));
    }

    @Test
    @Transactional
    void getNonExistingEstado() throws Exception {
        // Get the estado
        restEstadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        estadoSearchRepository.save(estado);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());

        // Update the estado
        Estado updatedEstado = estadoRepository.findById(estado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstado are not directly saved in db
        em.detach(updatedEstado);
        updatedEstado.nome(UPDATED_NOME).sigla(UPDATED_SIGLA).codigoIbge(UPDATED_CODIGO_IBGE);
        EstadoDTO estadoDTO = estadoMapper.toDto(updatedEstado);

        restEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        Estado testEstado = estadoList.get(estadoList.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEstado.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testEstado.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Estado> estadoSearchList = IterableUtils.toList(estadoSearchRepository.findAll());
                Estado testEstadoSearch = estadoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEstadoSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testEstadoSearch.getSigla()).isEqualTo(UPDATED_SIGLA);
                assertThat(testEstadoSearch.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        estado.setId(longCount.incrementAndGet());

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        estado.setId(longCount.incrementAndGet());

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        estado.setId(longCount.incrementAndGet());

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEstadoWithPatch() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();

        // Update the estado using partial update
        Estado partialUpdatedEstado = new Estado();
        partialUpdatedEstado.setId(estado.getId());

        partialUpdatedEstado.sigla(UPDATED_SIGLA).codigoIbge(UPDATED_CODIGO_IBGE);

        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstado))
            )
            .andExpect(status().isOk());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        Estado testEstado = estadoList.get(estadoList.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEstado.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testEstado.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void fullUpdateEstadoWithPatch() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();

        // Update the estado using partial update
        Estado partialUpdatedEstado = new Estado();
        partialUpdatedEstado.setId(estado.getId());

        partialUpdatedEstado.nome(UPDATED_NOME).sigla(UPDATED_SIGLA).codigoIbge(UPDATED_CODIGO_IBGE);

        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstado))
            )
            .andExpect(status().isOk());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        Estado testEstado = estadoList.get(estadoList.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEstado.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testEstado.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void patchNonExistingEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        estado.setId(longCount.incrementAndGet());

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        estado.setId(longCount.incrementAndGet());

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        estado.setId(longCount.incrementAndGet());

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.toDto(estado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(estadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        estadoRepository.save(estado);
        estadoSearchRepository.save(estado);

        int databaseSizeBeforeDelete = estadoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the estado
        restEstadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, estado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(estadoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEstado() throws Exception {
        // Initialize the database
        estado = estadoRepository.saveAndFlush(estado);
        estadoSearchRepository.save(estado);

        // Search the estado
        restEstadoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + estado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].codigoIbge").value(hasItem(DEFAULT_CODIGO_IBGE)));
    }
}
