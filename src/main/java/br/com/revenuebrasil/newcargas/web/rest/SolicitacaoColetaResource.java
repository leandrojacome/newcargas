package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.SolicitacaoColetaRepository;
import br.com.revenuebrasil.newcargas.service.SolicitacaoColetaQueryService;
import br.com.revenuebrasil.newcargas.service.SolicitacaoColetaService;
import br.com.revenuebrasil.newcargas.service.criteria.SolicitacaoColetaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta}.
 */
@RestController
@RequestMapping("/api/solicitacao-coletas")
public class SolicitacaoColetaResource {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoColetaResource.class);

    private static final String ENTITY_NAME = "solicitacaoColeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolicitacaoColetaService solicitacaoColetaService;

    private final SolicitacaoColetaRepository solicitacaoColetaRepository;

    private final SolicitacaoColetaQueryService solicitacaoColetaQueryService;

    public SolicitacaoColetaResource(
        SolicitacaoColetaService solicitacaoColetaService,
        SolicitacaoColetaRepository solicitacaoColetaRepository,
        SolicitacaoColetaQueryService solicitacaoColetaQueryService
    ) {
        this.solicitacaoColetaService = solicitacaoColetaService;
        this.solicitacaoColetaRepository = solicitacaoColetaRepository;
        this.solicitacaoColetaQueryService = solicitacaoColetaQueryService;
    }

    /**
     * {@code POST  /solicitacao-coletas} : Create a new solicitacaoColeta.
     *
     * @param solicitacaoColetaDTO the solicitacaoColetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solicitacaoColetaDTO, or with status {@code 400 (Bad Request)} if the solicitacaoColeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SolicitacaoColetaDTO> createSolicitacaoColeta(@Valid @RequestBody SolicitacaoColetaDTO solicitacaoColetaDTO)
        throws URISyntaxException {
        log.debug("REST request to save SolicitacaoColeta : {}", solicitacaoColetaDTO);
        if (solicitacaoColetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new solicitacaoColeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolicitacaoColetaDTO result = solicitacaoColetaService.save(solicitacaoColetaDTO);
        return ResponseEntity
            .created(new URI("/api/solicitacao-coletas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solicitacao-coletas/:id} : Updates an existing solicitacaoColeta.
     *
     * @param id the id of the solicitacaoColetaDTO to save.
     * @param solicitacaoColetaDTO the solicitacaoColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitacaoColetaDTO,
     * or with status {@code 400 (Bad Request)} if the solicitacaoColetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solicitacaoColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoColetaDTO> updateSolicitacaoColeta(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody SolicitacaoColetaDTO solicitacaoColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SolicitacaoColeta : {}, {}", id, solicitacaoColetaDTO);
        if (solicitacaoColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solicitacaoColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solicitacaoColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SolicitacaoColetaDTO result = solicitacaoColetaService.update(solicitacaoColetaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solicitacaoColetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /solicitacao-coletas/:id} : Partial updates given fields of an existing solicitacaoColeta, field will ignore if it is null
     *
     * @param id the id of the solicitacaoColetaDTO to save.
     * @param solicitacaoColetaDTO the solicitacaoColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitacaoColetaDTO,
     * or with status {@code 400 (Bad Request)} if the solicitacaoColetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the solicitacaoColetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the solicitacaoColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SolicitacaoColetaDTO> partialUpdateSolicitacaoColeta(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody SolicitacaoColetaDTO solicitacaoColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SolicitacaoColeta partially : {}, {}", id, solicitacaoColetaDTO);
        if (solicitacaoColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solicitacaoColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solicitacaoColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SolicitacaoColetaDTO> result = solicitacaoColetaService.partialUpdate(solicitacaoColetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solicitacaoColetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /solicitacao-coletas} : get all the solicitacaoColetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solicitacaoColetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SolicitacaoColetaDTO>> getAllSolicitacaoColetas(
        SolicitacaoColetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SolicitacaoColetas by criteria: {}", criteria);

        Page<SolicitacaoColetaDTO> page = solicitacaoColetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /solicitacao-coletas/count} : count all the solicitacaoColetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSolicitacaoColetas(SolicitacaoColetaCriteria criteria) {
        log.debug("REST request to count SolicitacaoColetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(solicitacaoColetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /solicitacao-coletas/:id} : get the "id" solicitacaoColeta.
     *
     * @param id the id of the solicitacaoColetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solicitacaoColetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoColetaDTO> getSolicitacaoColeta(@PathVariable("id") Long id) {
        log.debug("REST request to get SolicitacaoColeta : {}", id);
        Optional<SolicitacaoColetaDTO> solicitacaoColetaDTO = solicitacaoColetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solicitacaoColetaDTO);
    }

    /**
     * {@code DELETE  /solicitacao-coletas/:id} : delete the "id" solicitacaoColeta.
     *
     * @param id the id of the solicitacaoColetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitacaoColeta(@PathVariable("id") Long id) {
        log.debug("REST request to delete SolicitacaoColeta : {}", id);
        solicitacaoColetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /solicitacao-coletas/_search?query=:query} : search for the solicitacaoColeta corresponding
     * to the query.
     *
     * @param query the query of the solicitacaoColeta search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SolicitacaoColetaDTO>> searchSolicitacaoColetas(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of SolicitacaoColetas for query {}", query);
        try {
            Page<SolicitacaoColetaDTO> page = solicitacaoColetaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
