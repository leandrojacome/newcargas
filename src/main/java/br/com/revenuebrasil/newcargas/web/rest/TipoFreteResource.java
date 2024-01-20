package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.TipoFreteRepository;
import br.com.revenuebrasil.newcargas.service.TipoFreteService;
import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.TipoFrete}.
 */
@RestController
@RequestMapping("/api/tipo-fretes")
public class TipoFreteResource {

    private final Logger log = LoggerFactory.getLogger(TipoFreteResource.class);

    private static final String ENTITY_NAME = "tipoFrete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoFreteService tipoFreteService;

    private final TipoFreteRepository tipoFreteRepository;

    public TipoFreteResource(TipoFreteService tipoFreteService, TipoFreteRepository tipoFreteRepository) {
        this.tipoFreteService = tipoFreteService;
        this.tipoFreteRepository = tipoFreteRepository;
    }

    /**
     * {@code POST  /tipo-fretes} : Create a new tipoFrete.
     *
     * @param tipoFreteDTO the tipoFreteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoFreteDTO, or with status {@code 400 (Bad Request)} if the tipoFrete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoFreteDTO> createTipoFrete(@Valid @RequestBody TipoFreteDTO tipoFreteDTO) throws URISyntaxException {
        log.debug("REST request to save TipoFrete : {}", tipoFreteDTO);
        if (tipoFreteDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoFrete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoFreteDTO result = tipoFreteService.save(tipoFreteDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-fretes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-fretes/:id} : Updates an existing tipoFrete.
     *
     * @param id the id of the tipoFreteDTO to save.
     * @param tipoFreteDTO the tipoFreteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoFreteDTO,
     * or with status {@code 400 (Bad Request)} if the tipoFreteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoFreteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoFreteDTO> updateTipoFrete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoFreteDTO tipoFreteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoFrete : {}, {}", id, tipoFreteDTO);
        if (tipoFreteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoFreteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoFreteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoFreteDTO result = tipoFreteService.update(tipoFreteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoFreteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-fretes/:id} : Partial updates given fields of an existing tipoFrete, field will ignore if it is null
     *
     * @param id the id of the tipoFreteDTO to save.
     * @param tipoFreteDTO the tipoFreteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoFreteDTO,
     * or with status {@code 400 (Bad Request)} if the tipoFreteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoFreteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoFreteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoFreteDTO> partialUpdateTipoFrete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoFreteDTO tipoFreteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoFrete partially : {}, {}", id, tipoFreteDTO);
        if (tipoFreteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoFreteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoFreteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoFreteDTO> result = tipoFreteService.partialUpdate(tipoFreteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoFreteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-fretes} : get all the tipoFretes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoFretes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoFreteDTO>> getAllTipoFretes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TipoFretes");
        Page<TipoFreteDTO> page = tipoFreteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-fretes/:id} : get the "id" tipoFrete.
     *
     * @param id the id of the tipoFreteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoFreteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoFreteDTO> getTipoFrete(@PathVariable("id") Long id) {
        log.debug("REST request to get TipoFrete : {}", id);
        Optional<TipoFreteDTO> tipoFreteDTO = tipoFreteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoFreteDTO);
    }

    /**
     * {@code DELETE  /tipo-fretes/:id} : delete the "id" tipoFrete.
     *
     * @param id the id of the tipoFreteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoFrete(@PathVariable("id") Long id) {
        log.debug("REST request to delete TipoFrete : {}", id);
        tipoFreteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tipo-fretes/_search?query=:query} : search for the tipoFrete corresponding
     * to the query.
     *
     * @param query the query of the tipoFrete search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TipoFreteDTO>> searchTipoFretes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TipoFretes for query {}", query);
        try {
            Page<TipoFreteDTO> page = tipoFreteService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
