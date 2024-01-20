package br.com.revenuebrasil.newcargas.web.rest;

import static br.com.revenuebrasil.newcargas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.revenuebrasil.newcargas.IntegrationTest;
import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.repository.ContaBancariaRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContaBancariaSearchRepository;
import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContaBancariaMapper;
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
 * Integration tests for the {@link ContaBancariaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContaBancariaResourceIT {

    private static final String DEFAULT_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_AGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_CONTA = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_PIX = "AAAAAAAAAA";
    private static final String UPDATED_PIX = "BBBBBBBBBB";

    private static final String DEFAULT_TITULAR = "AAAAAAAAAA";
    private static final String UPDATED_TITULAR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_CADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ATUALIZACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ATUALIZACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/conta-bancarias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/conta-bancarias/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private ContaBancariaMapper contaBancariaMapper;

    @Autowired
    private ContaBancariaSearchRepository contaBancariaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaBancariaMockMvc;

    private ContaBancaria contaBancaria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaBancaria createEntity(EntityManager em) {
        ContaBancaria contaBancaria = new ContaBancaria()
            .agencia(DEFAULT_AGENCIA)
            .conta(DEFAULT_CONTA)
            .observacao(DEFAULT_OBSERVACAO)
            .tipo(DEFAULT_TIPO)
            .pix(DEFAULT_PIX)
            .titular(DEFAULT_TITULAR)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataAtualizacao(DEFAULT_DATA_ATUALIZACAO);
        return contaBancaria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaBancaria createUpdatedEntity(EntityManager em) {
        ContaBancaria contaBancaria = new ContaBancaria()
            .agencia(UPDATED_AGENCIA)
            .conta(UPDATED_CONTA)
            .observacao(UPDATED_OBSERVACAO)
            .tipo(UPDATED_TIPO)
            .pix(UPDATED_PIX)
            .titular(UPDATED_TITULAR)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);
        return contaBancaria;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        contaBancariaSearchRepository.deleteAll();
        assertThat(contaBancariaSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        contaBancaria = createEntity(em);
    }

    @Test
    @Transactional
    void createContaBancaria() throws Exception {
        int databaseSizeBeforeCreate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);
        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(DEFAULT_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(DEFAULT_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(DEFAULT_TITULAR);
        assertThat(testContaBancaria.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testContaBancaria.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
    }

    @Test
    @Transactional
    void createContaBancariaWithExistingId() throws Exception {
        // Create the ContaBancaria with an existing ID
        contaBancaria.setId(1L);
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        int databaseSizeBeforeCreate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAgenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // set the field null
        contaBancaria.setAgencia(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkContaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // set the field null
        contaBancaria.setConta(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        // set the field null
        contaBancaria.setDataCadastro(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllContaBancarias() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].pix").value(hasItem(DEFAULT_PIX)))
            .andExpect(jsonPath("$.[*].titular").value(hasItem(DEFAULT_TITULAR)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))));
    }

    @Test
    @Transactional
    void getContaBancaria() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get the contaBancaria
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL_ID, contaBancaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaBancaria.getId().intValue()))
            .andExpect(jsonPath("$.agencia").value(DEFAULT_AGENCIA))
            .andExpect(jsonPath("$.conta").value(DEFAULT_CONTA))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.pix").value(DEFAULT_PIX))
            .andExpect(jsonPath("$.titular").value(DEFAULT_TITULAR))
            .andExpect(jsonPath("$.dataCadastro").value(sameInstant(DEFAULT_DATA_CADASTRO)))
            .andExpect(jsonPath("$.dataAtualizacao").value(sameInstant(DEFAULT_DATA_ATUALIZACAO)));
    }

    @Test
    @Transactional
    void getNonExistingContaBancaria() throws Exception {
        // Get the contaBancaria
        restContaBancariaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContaBancaria() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        contaBancariaSearchRepository.save(contaBancaria);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());

        // Update the contaBancaria
        ContaBancaria updatedContaBancaria = contaBancariaRepository.findById(contaBancaria.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContaBancaria are not directly saved in db
        em.detach(updatedContaBancaria);
        updatedContaBancaria
            .agencia(UPDATED_AGENCIA)
            .conta(UPDATED_CONTA)
            .observacao(UPDATED_OBSERVACAO)
            .tipo(UPDATED_TIPO)
            .pix(UPDATED_PIX)
            .titular(UPDATED_TITULAR)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(updatedContaBancaria);

        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(UPDATED_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(UPDATED_TITULAR);
        assertThat(testContaBancaria.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testContaBancaria.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ContaBancaria> contaBancariaSearchList = IterableUtils.toList(contaBancariaSearchRepository.findAll());
                ContaBancaria testContaBancariaSearch = contaBancariaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testContaBancariaSearch.getAgencia()).isEqualTo(UPDATED_AGENCIA);
                assertThat(testContaBancariaSearch.getConta()).isEqualTo(UPDATED_CONTA);
                assertThat(testContaBancariaSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testContaBancariaSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testContaBancariaSearch.getPix()).isEqualTo(UPDATED_PIX);
                assertThat(testContaBancariaSearch.getTitular()).isEqualTo(UPDATED_TITULAR);
                assertThat(testContaBancariaSearch.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
                assertThat(testContaBancariaSearch.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
            });
    }

    @Test
    @Transactional
    void putNonExistingContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateContaBancariaWithPatch() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();

        // Update the contaBancaria using partial update
        ContaBancaria partialUpdatedContaBancaria = new ContaBancaria();
        partialUpdatedContaBancaria.setId(contaBancaria.getId());

        partialUpdatedContaBancaria.agencia(UPDATED_AGENCIA).pix(UPDATED_PIX).dataCadastro(UPDATED_DATA_CADASTRO);

        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaBancaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaBancaria))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(UPDATED_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(DEFAULT_TITULAR);
        assertThat(testContaBancaria.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testContaBancaria.getDataAtualizacao()).isEqualTo(DEFAULT_DATA_ATUALIZACAO);
    }

    @Test
    @Transactional
    void fullUpdateContaBancariaWithPatch() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);

        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();

        // Update the contaBancaria using partial update
        ContaBancaria partialUpdatedContaBancaria = new ContaBancaria();
        partialUpdatedContaBancaria.setId(contaBancaria.getId());

        partialUpdatedContaBancaria
            .agencia(UPDATED_AGENCIA)
            .conta(UPDATED_CONTA)
            .observacao(UPDATED_OBSERVACAO)
            .tipo(UPDATED_TIPO)
            .pix(UPDATED_PIX)
            .titular(UPDATED_TITULAR)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataAtualizacao(UPDATED_DATA_ATUALIZACAO);

        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaBancaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaBancaria))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        ContaBancaria testContaBancaria = contaBancariaList.get(contaBancariaList.size() - 1);
        assertThat(testContaBancaria.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testContaBancaria.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testContaBancaria.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testContaBancaria.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContaBancaria.getPix()).isEqualTo(UPDATED_PIX);
        assertThat(testContaBancaria.getTitular()).isEqualTo(UPDATED_TITULAR);
        assertThat(testContaBancaria.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testContaBancaria.getDataAtualizacao()).isEqualTo(UPDATED_DATA_ATUALIZACAO);
    }

    @Test
    @Transactional
    void patchNonExistingContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaBancaria() throws Exception {
        int databaseSizeBeforeUpdate = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaBancariaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaBancaria in the database
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteContaBancaria() throws Exception {
        // Initialize the database
        contaBancariaRepository.saveAndFlush(contaBancaria);
        contaBancariaRepository.save(contaBancaria);
        contaBancariaSearchRepository.save(contaBancaria);

        int databaseSizeBeforeDelete = contaBancariaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the contaBancaria
        restContaBancariaMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaBancaria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContaBancaria> contaBancariaList = contaBancariaRepository.findAll();
        assertThat(contaBancariaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contaBancariaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchContaBancaria() throws Exception {
        // Initialize the database
        contaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);
        contaBancariaSearchRepository.save(contaBancaria);

        // Search the contaBancaria
        restContaBancariaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contaBancaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].pix").value(hasItem(DEFAULT_PIX)))
            .andExpect(jsonPath("$.[*].titular").value(hasItem(DEFAULT_TITULAR)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(sameInstant(DEFAULT_DATA_CADASTRO))))
            .andExpect(jsonPath("$.[*].dataAtualizacao").value(hasItem(sameInstant(DEFAULT_DATA_ATUALIZACAO))));
    }
}
