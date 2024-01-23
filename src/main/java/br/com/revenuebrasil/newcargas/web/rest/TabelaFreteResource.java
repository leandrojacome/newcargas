package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.TabelaFreteRepository;
import br.com.revenuebrasil.newcargas.service.TabelaFreteQueryService;
import br.com.revenuebrasil.newcargas.service.TabelaFreteService;
import br.com.revenuebrasil.newcargas.service.criteria.TabelaFreteCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.TabelaFrete}.
 */
@RestController
@RequestMapping("/api/tabela-fretes")
public class TabelaFreteResource {

    private final Logger log = LoggerFactory.getLogger(TabelaFreteResource.class);

    private static final String ENTITY_NAME = "tabelaFrete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TabelaFreteService tabelaFreteService;

    private final TabelaFreteRepository tabelaFreteRepository;

    private final TabelaFreteQueryService tabelaFreteQueryService;

    public TabelaFreteResource(
        TabelaFreteService tabelaFreteService,
        TabelaFreteRepository tabelaFreteRepository,
        TabelaFreteQueryService tabelaFreteQueryService
    ) {
        this.tabelaFreteService = tabelaFreteService;
        this.tabelaFreteRepository = tabelaFreteRepository;
        this.tabelaFreteQueryService = tabelaFreteQueryService;
    }

    /**
     * {@code POST  /tabela-fretes} : Create a new tabelaFrete.
     *
     * @param tabelaFreteDTO the tabelaFreteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tabelaFreteDTO, or with status {@code 400 (Bad Request)} if the tabelaFrete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TabelaFreteDTO> createTabelaFrete(@Valid @RequestBody TabelaFreteDTO tabelaFreteDTO) throws URISyntaxException {
        log.debug("REST request to save TabelaFrete : {}", tabelaFreteDTO);
        if (tabelaFreteDTO.getId() != null) {
            throw new BadRequestAlertException("A new tabelaFrete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TabelaFreteDTO result = tabelaFreteService.save(tabelaFreteDTO);
        return ResponseEntity
            .created(new URI("/api/tabela-fretes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tabela-fretes/:id} : Updates an existing tabelaFrete.
     *
     * @param id the id of the tabelaFreteDTO to save.
     * @param tabelaFreteDTO the tabelaFreteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabelaFreteDTO,
     * or with status {@code 400 (Bad Request)} if the tabelaFreteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tabelaFreteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TabelaFreteDTO> updateTabelaFrete(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody TabelaFreteDTO tabelaFreteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TabelaFrete : {}, {}", id, tabelaFreteDTO);
        if (tabelaFreteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tabelaFreteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tabelaFreteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TabelaFreteDTO result = tabelaFreteService.update(tabelaFreteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tabelaFreteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tabela-fretes/:id} : Partial updates given fields of an existing tabelaFrete, field will ignore if it is null
     *
     * @param id the id of the tabelaFreteDTO to save.
     * @param tabelaFreteDTO the tabelaFreteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabelaFreteDTO,
     * or with status {@code 400 (Bad Request)} if the tabelaFreteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabelaFreteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabelaFreteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TabelaFreteDTO> partialUpdateTabelaFrete(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody TabelaFreteDTO tabelaFreteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TabelaFrete partially : {}, {}", id, tabelaFreteDTO);
        if (tabelaFreteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tabelaFreteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tabelaFreteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TabelaFreteDTO> result = tabelaFreteService.partialUpdate(tabelaFreteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tabelaFreteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tabela-fretes} : get all the tabelaFretes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tabelaFretes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TabelaFreteDTO>> getAllTabelaFretes(
        TabelaFreteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TabelaFretes by criteria: {}", criteria);

        Page<TabelaFreteDTO> page = tabelaFreteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tabela-fretes/count} : count all the tabelaFretes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTabelaFretes(TabelaFreteCriteria criteria) {
        log.debug("REST request to count TabelaFretes by criteria: {}", criteria);
        return ResponseEntity.ok().body(tabelaFreteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tabela-fretes/:id} : get the "id" tabelaFrete.
     *
     * @param id the id of the tabelaFreteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tabelaFreteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TabelaFreteDTO> getTabelaFrete(@PathVariable("id") Long id) {
        log.debug("REST request to get TabelaFrete : {}", id);
        Optional<TabelaFreteDTO> tabelaFreteDTO = tabelaFreteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tabelaFreteDTO);
    }

    /**
     * {@code DELETE  /tabela-fretes/:id} : delete the "id" tabelaFrete.
     *
     * @param id the id of the tabelaFreteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTabelaFrete(@PathVariable("id") Long id) {
        log.debug("REST request to delete TabelaFrete : {}", id);
        tabelaFreteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tabela-fretes/_search?query=:query} : search for the tabelaFrete corresponding
     * to the query.
     *
     * @param query the query of the tabelaFrete search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TabelaFreteDTO>> searchTabelaFretes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TabelaFretes for query {}", query);
        try {
            Page<TabelaFreteDTO> page = tabelaFreteService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
