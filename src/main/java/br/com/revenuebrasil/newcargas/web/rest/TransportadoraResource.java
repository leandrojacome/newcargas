package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.TransportadoraRepository;
import br.com.revenuebrasil.newcargas.service.TransportadoraQueryService;
import br.com.revenuebrasil.newcargas.service.TransportadoraService;
import br.com.revenuebrasil.newcargas.service.criteria.TransportadoraCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Transportadora}.
 */
@RestController
@RequestMapping("/api/transportadoras")
public class TransportadoraResource {

    private final Logger log = LoggerFactory.getLogger(TransportadoraResource.class);

    private static final String ENTITY_NAME = "transportadora";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransportadoraService transportadoraService;

    private final TransportadoraRepository transportadoraRepository;

    private final TransportadoraQueryService transportadoraQueryService;

    public TransportadoraResource(
        TransportadoraService transportadoraService,
        TransportadoraRepository transportadoraRepository,
        TransportadoraQueryService transportadoraQueryService
    ) {
        this.transportadoraService = transportadoraService;
        this.transportadoraRepository = transportadoraRepository;
        this.transportadoraQueryService = transportadoraQueryService;
    }

    /**
     * {@code POST  /transportadoras} : Create a new transportadora.
     *
     * @param transportadoraDTO the transportadoraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transportadoraDTO, or with status {@code 400 (Bad Request)} if the transportadora has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransportadoraDTO> createTransportadora(@Valid @RequestBody TransportadoraDTO transportadoraDTO)
        throws URISyntaxException {
        log.debug("REST request to save Transportadora : {}", transportadoraDTO);
        if (transportadoraDTO.getId() != null) {
            throw new BadRequestAlertException("A new transportadora cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransportadoraDTO result = transportadoraService.save(transportadoraDTO);
        return ResponseEntity
            .created(new URI("/api/transportadoras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transportadoras/:id} : Updates an existing transportadora.
     *
     * @param id the id of the transportadoraDTO to save.
     * @param transportadoraDTO the transportadoraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportadoraDTO,
     * or with status {@code 400 (Bad Request)} if the transportadoraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transportadoraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransportadoraDTO> updateTransportadora(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody TransportadoraDTO transportadoraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transportadora : {}, {}", id, transportadoraDTO);
        if (transportadoraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportadoraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportadoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransportadoraDTO result = transportadoraService.update(transportadoraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transportadoraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transportadoras/:id} : Partial updates given fields of an existing transportadora, field will ignore if it is null
     *
     * @param id the id of the transportadoraDTO to save.
     * @param transportadoraDTO the transportadoraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportadoraDTO,
     * or with status {@code 400 (Bad Request)} if the transportadoraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transportadoraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transportadoraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransportadoraDTO> partialUpdateTransportadora(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody TransportadoraDTO transportadoraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transportadora partially : {}, {}", id, transportadoraDTO);
        if (transportadoraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportadoraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportadoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransportadoraDTO> result = transportadoraService.partialUpdate(transportadoraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transportadoraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transportadoras} : get all the transportadoras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transportadoras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransportadoraDTO>> getAllTransportadoras(
        TransportadoraCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Transportadoras by criteria: {}", criteria);

        Page<TransportadoraDTO> page = transportadoraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transportadoras/count} : count all the transportadoras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransportadoras(TransportadoraCriteria criteria) {
        log.debug("REST request to count Transportadoras by criteria: {}", criteria);
        return ResponseEntity.ok().body(transportadoraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transportadoras/:id} : get the "id" transportadora.
     *
     * @param id the id of the transportadoraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transportadoraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransportadoraDTO> getTransportadora(@PathVariable("id") Long id) {
        log.debug("REST request to get Transportadora : {}", id);
        Optional<TransportadoraDTO> transportadoraDTO = transportadoraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transportadoraDTO);
    }

    /**
     * {@code DELETE  /transportadoras/:id} : delete the "id" transportadora.
     *
     * @param id the id of the transportadoraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportadora(@PathVariable("id") Long id) {
        log.debug("REST request to delete Transportadora : {}", id);
        transportadoraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /transportadoras/_search?query=:query} : search for the transportadora corresponding
     * to the query.
     *
     * @param query the query of the transportadora search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TransportadoraDTO>> searchTransportadoras(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Transportadoras for query {}", query);
        try {
            Page<TransportadoraDTO> page = transportadoraService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
