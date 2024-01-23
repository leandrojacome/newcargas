package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.NotaFiscalColetaRepository;
import br.com.revenuebrasil.newcargas.service.NotaFiscalColetaQueryService;
import br.com.revenuebrasil.newcargas.service.NotaFiscalColetaService;
import br.com.revenuebrasil.newcargas.service.criteria.NotaFiscalColetaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta}.
 */
@RestController
@RequestMapping("/api/nota-fiscal-coletas")
public class NotaFiscalColetaResource {

    private final Logger log = LoggerFactory.getLogger(NotaFiscalColetaResource.class);

    private static final String ENTITY_NAME = "notaFiscalColeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotaFiscalColetaService notaFiscalColetaService;

    private final NotaFiscalColetaRepository notaFiscalColetaRepository;

    private final NotaFiscalColetaQueryService notaFiscalColetaQueryService;

    public NotaFiscalColetaResource(
        NotaFiscalColetaService notaFiscalColetaService,
        NotaFiscalColetaRepository notaFiscalColetaRepository,
        NotaFiscalColetaQueryService notaFiscalColetaQueryService
    ) {
        this.notaFiscalColetaService = notaFiscalColetaService;
        this.notaFiscalColetaRepository = notaFiscalColetaRepository;
        this.notaFiscalColetaQueryService = notaFiscalColetaQueryService;
    }

    /**
     * {@code POST  /nota-fiscal-coletas} : Create a new notaFiscalColeta.
     *
     * @param notaFiscalColetaDTO the notaFiscalColetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notaFiscalColetaDTO, or with status {@code 400 (Bad Request)} if the notaFiscalColeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotaFiscalColetaDTO> createNotaFiscalColeta(@Valid @RequestBody NotaFiscalColetaDTO notaFiscalColetaDTO)
        throws URISyntaxException {
        log.debug("REST request to save NotaFiscalColeta : {}", notaFiscalColetaDTO);
        if (notaFiscalColetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new notaFiscalColeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotaFiscalColetaDTO result = notaFiscalColetaService.save(notaFiscalColetaDTO);
        return ResponseEntity
            .created(new URI("/api/nota-fiscal-coletas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nota-fiscal-coletas/:id} : Updates an existing notaFiscalColeta.
     *
     * @param id the id of the notaFiscalColetaDTO to save.
     * @param notaFiscalColetaDTO the notaFiscalColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaFiscalColetaDTO,
     * or with status {@code 400 (Bad Request)} if the notaFiscalColetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notaFiscalColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscalColetaDTO> updateNotaFiscalColeta(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody NotaFiscalColetaDTO notaFiscalColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NotaFiscalColeta : {}, {}", id, notaFiscalColetaDTO);
        if (notaFiscalColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notaFiscalColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notaFiscalColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotaFiscalColetaDTO result = notaFiscalColetaService.update(notaFiscalColetaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notaFiscalColetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nota-fiscal-coletas/:id} : Partial updates given fields of an existing notaFiscalColeta, field will ignore if it is null
     *
     * @param id the id of the notaFiscalColetaDTO to save.
     * @param notaFiscalColetaDTO the notaFiscalColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaFiscalColetaDTO,
     * or with status {@code 400 (Bad Request)} if the notaFiscalColetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notaFiscalColetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notaFiscalColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotaFiscalColetaDTO> partialUpdateNotaFiscalColeta(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody NotaFiscalColetaDTO notaFiscalColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotaFiscalColeta partially : {}, {}", id, notaFiscalColetaDTO);
        if (notaFiscalColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notaFiscalColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notaFiscalColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotaFiscalColetaDTO> result = notaFiscalColetaService.partialUpdate(notaFiscalColetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notaFiscalColetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nota-fiscal-coletas} : get all the notaFiscalColetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notaFiscalColetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotaFiscalColetaDTO>> getAllNotaFiscalColetas(
        NotaFiscalColetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get NotaFiscalColetas by criteria: {}", criteria);

        Page<NotaFiscalColetaDTO> page = notaFiscalColetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nota-fiscal-coletas/count} : count all the notaFiscalColetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotaFiscalColetas(NotaFiscalColetaCriteria criteria) {
        log.debug("REST request to count NotaFiscalColetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(notaFiscalColetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nota-fiscal-coletas/:id} : get the "id" notaFiscalColeta.
     *
     * @param id the id of the notaFiscalColetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notaFiscalColetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalColetaDTO> getNotaFiscalColeta(@PathVariable("id") Long id) {
        log.debug("REST request to get NotaFiscalColeta : {}", id);
        Optional<NotaFiscalColetaDTO> notaFiscalColetaDTO = notaFiscalColetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notaFiscalColetaDTO);
    }

    /**
     * {@code DELETE  /nota-fiscal-coletas/:id} : delete the "id" notaFiscalColeta.
     *
     * @param id the id of the notaFiscalColetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotaFiscalColeta(@PathVariable("id") Long id) {
        log.debug("REST request to delete NotaFiscalColeta : {}", id);
        notaFiscalColetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /nota-fiscal-coletas/_search?query=:query} : search for the notaFiscalColeta corresponding
     * to the query.
     *
     * @param query the query of the notaFiscalColeta search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<NotaFiscalColetaDTO>> searchNotaFiscalColetas(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of NotaFiscalColetas for query {}", query);
        try {
            Page<NotaFiscalColetaDTO> page = notaFiscalColetaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
