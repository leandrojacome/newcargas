package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.StatusColetaRepository;
import br.com.revenuebrasil.newcargas.service.StatusColetaService;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.StatusColeta}.
 */
@RestController
@RequestMapping("/api/status-coletas")
public class StatusColetaResource {

    private final Logger log = LoggerFactory.getLogger(StatusColetaResource.class);

    private static final String ENTITY_NAME = "statusColeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatusColetaService statusColetaService;

    private final StatusColetaRepository statusColetaRepository;

    public StatusColetaResource(StatusColetaService statusColetaService, StatusColetaRepository statusColetaRepository) {
        this.statusColetaService = statusColetaService;
        this.statusColetaRepository = statusColetaRepository;
    }

    /**
     * {@code POST  /status-coletas} : Create a new statusColeta.
     *
     * @param statusColetaDTO the statusColetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusColetaDTO, or with status {@code 400 (Bad Request)} if the statusColeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatusColetaDTO> createStatusColeta(@Valid @RequestBody StatusColetaDTO statusColetaDTO)
        throws URISyntaxException {
        log.debug("REST request to save StatusColeta : {}", statusColetaDTO);
        if (statusColetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new statusColeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatusColetaDTO result = statusColetaService.save(statusColetaDTO);
        return ResponseEntity
            .created(new URI("/api/status-coletas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /status-coletas/:id} : Updates an existing statusColeta.
     *
     * @param id the id of the statusColetaDTO to save.
     * @param statusColetaDTO the statusColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusColetaDTO,
     * or with status {@code 400 (Bad Request)} if the statusColetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatusColetaDTO> updateStatusColeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StatusColetaDTO statusColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StatusColeta : {}, {}", id, statusColetaDTO);
        if (statusColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StatusColetaDTO result = statusColetaService.update(statusColetaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, statusColetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /status-coletas/:id} : Partial updates given fields of an existing statusColeta, field will ignore if it is null
     *
     * @param id the id of the statusColetaDTO to save.
     * @param statusColetaDTO the statusColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusColetaDTO,
     * or with status {@code 400 (Bad Request)} if the statusColetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the statusColetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the statusColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatusColetaDTO> partialUpdateStatusColeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StatusColetaDTO statusColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StatusColeta partially : {}, {}", id, statusColetaDTO);
        if (statusColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatusColetaDTO> result = statusColetaService.partialUpdate(statusColetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, statusColetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /status-coletas} : get all the statusColetas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusColetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StatusColetaDTO>> getAllStatusColetas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of StatusColetas");
        Page<StatusColetaDTO> page;
        if (eagerload) {
            page = statusColetaService.findAllWithEagerRelationships(pageable);
        } else {
            page = statusColetaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /status-coletas/:id} : get the "id" statusColeta.
     *
     * @param id the id of the statusColetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusColetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusColetaDTO> getStatusColeta(@PathVariable("id") Long id) {
        log.debug("REST request to get StatusColeta : {}", id);
        Optional<StatusColetaDTO> statusColetaDTO = statusColetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusColetaDTO);
    }

    /**
     * {@code DELETE  /status-coletas/:id} : delete the "id" statusColeta.
     *
     * @param id the id of the statusColetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatusColeta(@PathVariable("id") Long id) {
        log.debug("REST request to delete StatusColeta : {}", id);
        statusColetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /status-coletas/_search?query=:query} : search for the statusColeta corresponding
     * to the query.
     *
     * @param query the query of the statusColeta search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<StatusColetaDTO>> searchStatusColetas(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of StatusColetas for query {}", query);
        try {
            Page<StatusColetaDTO> page = statusColetaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
