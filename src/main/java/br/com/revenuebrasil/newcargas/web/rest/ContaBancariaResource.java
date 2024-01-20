package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.ContaBancariaRepository;
import br.com.revenuebrasil.newcargas.service.ContaBancariaService;
import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import br.com.revenuebrasil.newcargas.web.rest.errors.BadRequestAlertException;
import br.com.revenuebrasil.newcargas.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.ContaBancaria}.
 */
@RestController
@RequestMapping("/api/conta-bancarias")
public class ContaBancariaResource {

    private final Logger log = LoggerFactory.getLogger(ContaBancariaResource.class);

    private static final String ENTITY_NAME = "contaBancaria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaBancariaService contaBancariaService;

    private final ContaBancariaRepository contaBancariaRepository;

    public ContaBancariaResource(ContaBancariaService contaBancariaService, ContaBancariaRepository contaBancariaRepository) {
        this.contaBancariaService = contaBancariaService;
        this.contaBancariaRepository = contaBancariaRepository;
    }

    /**
     * {@code POST  /conta-bancarias} : Create a new contaBancaria.
     *
     * @param contaBancariaDTO the contaBancariaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contaBancariaDTO, or with status {@code 400 (Bad Request)} if the contaBancaria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContaBancariaDTO> createContaBancaria(@Valid @RequestBody ContaBancariaDTO contaBancariaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContaBancaria : {}", contaBancariaDTO);
        if (contaBancariaDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaBancaria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContaBancariaDTO result = contaBancariaService.save(contaBancariaDTO);
        return ResponseEntity
            .created(new URI("/api/conta-bancarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conta-bancarias/:id} : Updates an existing contaBancaria.
     *
     * @param id the id of the contaBancariaDTO to save.
     * @param contaBancariaDTO the contaBancariaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaBancariaDTO,
     * or with status {@code 400 (Bad Request)} if the contaBancariaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contaBancariaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContaBancariaDTO> updateContaBancaria(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContaBancariaDTO contaBancariaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContaBancaria : {}, {}", id, contaBancariaDTO);
        if (contaBancariaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaBancariaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaBancariaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContaBancariaDTO result = contaBancariaService.update(contaBancariaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contaBancariaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conta-bancarias/:id} : Partial updates given fields of an existing contaBancaria, field will ignore if it is null
     *
     * @param id the id of the contaBancariaDTO to save.
     * @param contaBancariaDTO the contaBancariaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaBancariaDTO,
     * or with status {@code 400 (Bad Request)} if the contaBancariaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contaBancariaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contaBancariaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContaBancariaDTO> partialUpdateContaBancaria(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContaBancariaDTO contaBancariaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContaBancaria partially : {}, {}", id, contaBancariaDTO);
        if (contaBancariaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaBancariaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaBancariaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContaBancariaDTO> result = contaBancariaService.partialUpdate(contaBancariaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contaBancariaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conta-bancarias} : get all the contaBancarias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contaBancarias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContaBancariaDTO>> getAllContaBancarias(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ContaBancarias");
        Page<ContaBancariaDTO> page = contaBancariaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conta-bancarias/:id} : get the "id" contaBancaria.
     *
     * @param id the id of the contaBancariaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contaBancariaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaDTO> getContaBancaria(@PathVariable("id") Long id) {
        log.debug("REST request to get ContaBancaria : {}", id);
        Optional<ContaBancariaDTO> contaBancariaDTO = contaBancariaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contaBancariaDTO);
    }

    /**
     * {@code DELETE  /conta-bancarias/:id} : delete the "id" contaBancaria.
     *
     * @param id the id of the contaBancariaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContaBancaria(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContaBancaria : {}", id);
        contaBancariaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /conta-bancarias/_search?query=:query} : search for the contaBancaria corresponding
     * to the query.
     *
     * @param query the query of the contaBancaria search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ContaBancariaDTO>> searchContaBancarias(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of ContaBancarias for query {}", query);
        try {
            Page<ContaBancariaDTO> page = contaBancariaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
