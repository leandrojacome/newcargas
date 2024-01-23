package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.TomadaPrecoRepository;
import br.com.revenuebrasil.newcargas.service.TomadaPrecoQueryService;
import br.com.revenuebrasil.newcargas.service.TomadaPrecoService;
import br.com.revenuebrasil.newcargas.service.criteria.TomadaPrecoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TomadaPrecoDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.TomadaPreco}.
 */
@RestController
@RequestMapping("/api/tomada-precos")
public class TomadaPrecoResource {

    private final Logger log = LoggerFactory.getLogger(TomadaPrecoResource.class);

    private static final String ENTITY_NAME = "tomadaPreco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TomadaPrecoService tomadaPrecoService;

    private final TomadaPrecoRepository tomadaPrecoRepository;

    private final TomadaPrecoQueryService tomadaPrecoQueryService;

    public TomadaPrecoResource(
        TomadaPrecoService tomadaPrecoService,
        TomadaPrecoRepository tomadaPrecoRepository,
        TomadaPrecoQueryService tomadaPrecoQueryService
    ) {
        this.tomadaPrecoService = tomadaPrecoService;
        this.tomadaPrecoRepository = tomadaPrecoRepository;
        this.tomadaPrecoQueryService = tomadaPrecoQueryService;
    }

    /**
     * {@code POST  /tomada-precos} : Create a new tomadaPreco.
     *
     * @param tomadaPrecoDTO the tomadaPrecoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tomadaPrecoDTO, or with status {@code 400 (Bad Request)} if the tomadaPreco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TomadaPrecoDTO> createTomadaPreco(@Valid @RequestBody TomadaPrecoDTO tomadaPrecoDTO) throws URISyntaxException {
        log.debug("REST request to save TomadaPreco : {}", tomadaPrecoDTO);
        if (tomadaPrecoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tomadaPreco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TomadaPrecoDTO result = tomadaPrecoService.save(tomadaPrecoDTO);
        return ResponseEntity
            .created(new URI("/api/tomada-precos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tomada-precos/:id} : Updates an existing tomadaPreco.
     *
     * @param id the id of the tomadaPrecoDTO to save.
     * @param tomadaPrecoDTO the tomadaPrecoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tomadaPrecoDTO,
     * or with status {@code 400 (Bad Request)} if the tomadaPrecoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tomadaPrecoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TomadaPrecoDTO> updateTomadaPreco(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody TomadaPrecoDTO tomadaPrecoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TomadaPreco : {}, {}", id, tomadaPrecoDTO);
        if (tomadaPrecoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tomadaPrecoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tomadaPrecoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TomadaPrecoDTO result = tomadaPrecoService.update(tomadaPrecoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tomadaPrecoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tomada-precos/:id} : Partial updates given fields of an existing tomadaPreco, field will ignore if it is null
     *
     * @param id the id of the tomadaPrecoDTO to save.
     * @param tomadaPrecoDTO the tomadaPrecoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tomadaPrecoDTO,
     * or with status {@code 400 (Bad Request)} if the tomadaPrecoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tomadaPrecoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tomadaPrecoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TomadaPrecoDTO> partialUpdateTomadaPreco(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody TomadaPrecoDTO tomadaPrecoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TomadaPreco partially : {}, {}", id, tomadaPrecoDTO);
        if (tomadaPrecoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tomadaPrecoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tomadaPrecoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TomadaPrecoDTO> result = tomadaPrecoService.partialUpdate(tomadaPrecoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tomadaPrecoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tomada-precos} : get all the tomadaPrecos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tomadaPrecos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TomadaPrecoDTO>> getAllTomadaPrecos(
        TomadaPrecoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TomadaPrecos by criteria: {}", criteria);

        Page<TomadaPrecoDTO> page = tomadaPrecoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tomada-precos/count} : count all the tomadaPrecos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTomadaPrecos(TomadaPrecoCriteria criteria) {
        log.debug("REST request to count TomadaPrecos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tomadaPrecoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tomada-precos/:id} : get the "id" tomadaPreco.
     *
     * @param id the id of the tomadaPrecoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tomadaPrecoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TomadaPrecoDTO> getTomadaPreco(@PathVariable("id") Long id) {
        log.debug("REST request to get TomadaPreco : {}", id);
        Optional<TomadaPrecoDTO> tomadaPrecoDTO = tomadaPrecoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tomadaPrecoDTO);
    }

    /**
     * {@code DELETE  /tomada-precos/:id} : delete the "id" tomadaPreco.
     *
     * @param id the id of the tomadaPrecoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTomadaPreco(@PathVariable("id") Long id) {
        log.debug("REST request to delete TomadaPreco : {}", id);
        tomadaPrecoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tomada-precos/_search?query=:query} : search for the tomadaPreco corresponding
     * to the query.
     *
     * @param query the query of the tomadaPreco search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TomadaPrecoDTO>> searchTomadaPrecos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TomadaPrecos for query {}", query);
        try {
            Page<TomadaPrecoDTO> page = tomadaPrecoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
