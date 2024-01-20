package br.com.revenuebrasil.newcargas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.repository.RegiaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.RegiaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.RegiaoMapper;
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
 * Integration tests for the {@link RegiaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegiaoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAA";
    private static final String UPDATED_SIGLA = "BBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/regiaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/regiaos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegiaoRepository regiaoRepository;

    @Autowired
    private RegiaoMapper regiaoMapper;

    @Autowired
    private RegiaoSearchRepository regiaoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegiaoMockMvc;

    private Regiao regiao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regiao createEntity(EntityManager em) {
        Regiao regiao = new Regiao().nome(DEFAULT_NOME).sigla(DEFAULT_SIGLA).descricao(DEFAULT_DESCRICAO);
        return regiao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regiao createUpdatedEntity(EntityManager em) {
        Regiao regiao = new Regiao().nome(UPDATED_NOME).sigla(UPDATED_SIGLA).descricao(UPDATED_DESCRICAO);
        return regiao;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        regiaoSearchRepository.deleteAll();
        assertThat(regiaoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        regiao = createEntity(em);
    }

    @Test
    @Transactional
    void createRegiao() throws Exception {
        int databaseSizeBeforeCreate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);
        restRegiaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regiaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Regiao testRegiao = regiaoList.get(regiaoList.size() - 1);
        assertThat(testRegiao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testRegiao.getSigla()).isEqualTo(DEFAULT_SIGLA);
        assertThat(testRegiao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createRegiaoWithExistingId() throws Exception {
        // Create the Regiao with an existing ID
        regiao.setId(1L);
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        int databaseSizeBeforeCreate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegiaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regiaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        // set the field null
        regiao.setNome(null);

        // Create the Regiao, which fails.
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        restRegiaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regiaoDTO)))
            .andExpect(status().isBadRequest());

        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllRegiaos() throws Exception {
        // Initialize the database
        regiaoRepository.saveAndFlush(regiao);

        // Get all the regiaoList
        restRegiaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regiao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getRegiao() throws Exception {
        // Initialize the database
        regiaoRepository.saveAndFlush(regiao);

        // Get the regiao
        restRegiaoMockMvc
            .perform(get(ENTITY_API_URL_ID, regiao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regiao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingRegiao() throws Exception {
        // Get the regiao
        restRegiaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegiao() throws Exception {
        // Initialize the database
        regiaoRepository.saveAndFlush(regiao);

        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        regiaoSearchRepository.save(regiao);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());

        // Update the regiao
        Regiao updatedRegiao = regiaoRepository.findById(regiao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRegiao are not directly saved in db
        em.detach(updatedRegiao);
        updatedRegiao.nome(UPDATED_NOME).sigla(UPDATED_SIGLA).descricao(UPDATED_DESCRICAO);
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(updatedRegiao);

        restRegiaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regiaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regiaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        Regiao testRegiao = regiaoList.get(regiaoList.size() - 1);
        assertThat(testRegiao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testRegiao.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testRegiao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Regiao> regiaoSearchList = IterableUtils.toList(regiaoSearchRepository.findAll());
                Regiao testRegiaoSearch = regiaoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRegiaoSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testRegiaoSearch.getSigla()).isEqualTo(UPDATED_SIGLA);
                assertThat(testRegiaoSearch.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingRegiao() throws Exception {
        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        regiao.setId(longCount.incrementAndGet());

        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegiaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regiaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regiaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegiao() throws Exception {
        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        regiao.setId(longCount.incrementAndGet());

        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegiaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regiaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegiao() throws Exception {
        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        regiao.setId(longCount.incrementAndGet());

        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegiaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regiaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateRegiaoWithPatch() throws Exception {
        // Initialize the database
        regiaoRepository.saveAndFlush(regiao);

        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();

        // Update the regiao using partial update
        Regiao partialUpdatedRegiao = new Regiao();
        partialUpdatedRegiao.setId(regiao.getId());

        partialUpdatedRegiao.sigla(UPDATED_SIGLA);

        restRegiaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegiao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegiao))
            )
            .andExpect(status().isOk());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        Regiao testRegiao = regiaoList.get(regiaoList.size() - 1);
        assertThat(testRegiao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testRegiao.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testRegiao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateRegiaoWithPatch() throws Exception {
        // Initialize the database
        regiaoRepository.saveAndFlush(regiao);

        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();

        // Update the regiao using partial update
        Regiao partialUpdatedRegiao = new Regiao();
        partialUpdatedRegiao.setId(regiao.getId());

        partialUpdatedRegiao.nome(UPDATED_NOME).sigla(UPDATED_SIGLA).descricao(UPDATED_DESCRICAO);

        restRegiaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegiao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegiao))
            )
            .andExpect(status().isOk());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        Regiao testRegiao = regiaoList.get(regiaoList.size() - 1);
        assertThat(testRegiao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testRegiao.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testRegiao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingRegiao() throws Exception {
        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        regiao.setId(longCount.incrementAndGet());

        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegiaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, regiaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regiaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegiao() throws Exception {
        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        regiao.setId(longCount.incrementAndGet());

        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegiaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regiaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegiao() throws Exception {
        int databaseSizeBeforeUpdate = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        regiao.setId(longCount.incrementAndGet());

        // Create the Regiao
        RegiaoDTO regiaoDTO = regiaoMapper.toDto(regiao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegiaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(regiaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Regiao in the database
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteRegiao() throws Exception {
        // Initialize the database
        regiaoRepository.saveAndFlush(regiao);
        regiaoRepository.save(regiao);
        regiaoSearchRepository.save(regiao);

        int databaseSizeBeforeDelete = regiaoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the regiao
        restRegiaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, regiao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Regiao> regiaoList = regiaoRepository.findAll();
        assertThat(regiaoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(regiaoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchRegiao() throws Exception {
        // Initialize the database
        regiao = regiaoRepository.saveAndFlush(regiao);
        regiaoSearchRepository.save(regiao);

        // Search the regiao
        restRegiaoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + regiao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regiao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
