package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.TipoVeiculoRepository;
import br.com.revenuebrasil.newcargas.service.TipoVeiculoQueryService;
import br.com.revenuebrasil.newcargas.service.TipoVeiculoService;
import br.com.revenuebrasil.newcargas.service.criteria.TipoVeiculoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.TipoVeiculo}.
 */
@RestController
@RequestMapping("/api/tipo-veiculos")
public class TipoVeiculoResource {

    private final Logger log = LoggerFactory.getLogger(TipoVeiculoResource.class);

    private static final String ENTITY_NAME = "tipoVeiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoVeiculoService tipoVeiculoService;

    private final TipoVeiculoRepository tipoVeiculoRepository;

    private final TipoVeiculoQueryService tipoVeiculoQueryService;

    public TipoVeiculoResource(
        TipoVeiculoService tipoVeiculoService,
        TipoVeiculoRepository tipoVeiculoRepository,
        TipoVeiculoQueryService tipoVeiculoQueryService
    ) {
        this.tipoVeiculoService = tipoVeiculoService;
        this.tipoVeiculoRepository = tipoVeiculoRepository;
        this.tipoVeiculoQueryService = tipoVeiculoQueryService;
    }

    /**
     * {@code POST  /tipo-veiculos} : Create a new tipoVeiculo.
     *
     * @param tipoVeiculoDTO the tipoVeiculoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoVeiculoDTO, or with status {@code 400 (Bad Request)} if the tipoVeiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoVeiculoDTO> createTipoVeiculo(@Valid @RequestBody TipoVeiculoDTO tipoVeiculoDTO) throws URISyntaxException {
        log.debug("REST request to save TipoVeiculo : {}", tipoVeiculoDTO);
        if (tipoVeiculoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoVeiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoVeiculoDTO result = tipoVeiculoService.save(tipoVeiculoDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-veiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-veiculos/:id} : Updates an existing tipoVeiculo.
     *
     * @param id the id of the tipoVeiculoDTO to save.
     * @param tipoVeiculoDTO the tipoVeiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoVeiculoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoVeiculoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoVeiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoVeiculoDTO> updateTipoVeiculo(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody TipoVeiculoDTO tipoVeiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoVeiculo : {}, {}", id, tipoVeiculoDTO);
        if (tipoVeiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoVeiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoVeiculoDTO result = tipoVeiculoService.update(tipoVeiculoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoVeiculoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-veiculos/:id} : Partial updates given fields of an existing tipoVeiculo, field will ignore if it is null
     *
     * @param id the id of the tipoVeiculoDTO to save.
     * @param tipoVeiculoDTO the tipoVeiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoVeiculoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoVeiculoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoVeiculoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoVeiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoVeiculoDTO> partialUpdateTipoVeiculo(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoVeiculoDTO tipoVeiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoVeiculo partially : {}, {}", id, tipoVeiculoDTO);
        if (tipoVeiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoVeiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoVeiculoDTO> result = tipoVeiculoService.partialUpdate(tipoVeiculoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoVeiculoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-veiculos} : get all the tipoVeiculos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoVeiculos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoVeiculoDTO>> getAllTipoVeiculos(
        TipoVeiculoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TipoVeiculos by criteria: {}", criteria);

        Page<TipoVeiculoDTO> page = tipoVeiculoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-veiculos/count} : count all the tipoVeiculos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoVeiculos(TipoVeiculoCriteria criteria) {
        log.debug("REST request to count TipoVeiculos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoVeiculoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-veiculos/:id} : get the "id" tipoVeiculo.
     *
     * @param id the id of the tipoVeiculoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoVeiculoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoVeiculoDTO> getTipoVeiculo(@PathVariable("id") Long id) {
        log.debug("REST request to get TipoVeiculo : {}", id);
        Optional<TipoVeiculoDTO> tipoVeiculoDTO = tipoVeiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoVeiculoDTO);
    }

    /**
     * {@code DELETE  /tipo-veiculos/:id} : delete the "id" tipoVeiculo.
     *
     * @param id the id of the tipoVeiculoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoVeiculo(@PathVariable("id") Long id) {
        log.debug("REST request to delete TipoVeiculo : {}", id);
        tipoVeiculoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tipo-veiculos/_search?query=:query} : search for the tipoVeiculo corresponding
     * to the query.
     *
     * @param query the query of the tipoVeiculo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TipoVeiculoDTO>> searchTipoVeiculos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TipoVeiculos for query {}", query);
        try {
            Page<TipoVeiculoDTO> page = tipoVeiculoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
