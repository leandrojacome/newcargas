package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.EstadoRepository;
import br.com.revenuebrasil.newcargas.service.EstadoService;
import br.com.revenuebrasil.newcargas.service.dto.EstadoDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Estado}.
 */
@RestController
@RequestMapping("/api/estados")
public class EstadoResource {

    private final Logger log = LoggerFactory.getLogger(EstadoResource.class);

    private static final String ENTITY_NAME = "estado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstadoService estadoService;

    private final EstadoRepository estadoRepository;

    public EstadoResource(EstadoService estadoService, EstadoRepository estadoRepository) {
        this.estadoService = estadoService;
        this.estadoRepository = estadoRepository;
    }

    /**
     * {@code POST  /estados} : Create a new estado.
     *
     * @param estadoDTO the estadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadoDTO, or with status {@code 400 (Bad Request)} if the estado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EstadoDTO> createEstado(@Valid @RequestBody EstadoDTO estadoDTO) throws URISyntaxException {
        log.debug("REST request to save Estado : {}", estadoDTO);
        if (estadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new estado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstadoDTO result = estadoService.save(estadoDTO);
        return ResponseEntity
            .created(new URI("/api/estados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /estados/:id} : Updates an existing estado.
     *
     * @param id the id of the estadoDTO to save.
     * @param estadoDTO the estadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> updateEstado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstadoDTO estadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Estado : {}, {}", id, estadoDTO);
        if (estadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EstadoDTO result = estadoService.update(estadoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /estados/:id} : Partial updates given fields of an existing estado, field will ignore if it is null
     *
     * @param id the id of the estadoDTO to save.
     * @param estadoDTO the estadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstadoDTO> partialUpdateEstado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstadoDTO estadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estado partially : {}, {}", id, estadoDTO);
        if (estadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstadoDTO> result = estadoService.partialUpdate(estadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estados} : get all the estados.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estados in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EstadoDTO>> getAllEstados(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Estados");
        Page<EstadoDTO> page = estadoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estados/:id} : get the "id" estado.
     *
     * @param id the id of the estadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getEstado(@PathVariable("id") Long id) {
        log.debug("REST request to get Estado : {}", id);
        Optional<EstadoDTO> estadoDTO = estadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadoDTO);
    }

    /**
     * {@code DELETE  /estados/:id} : delete the "id" estado.
     *
     * @param id the id of the estadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable("id") Long id) {
        log.debug("REST request to delete Estado : {}", id);
        estadoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /estados/_search?query=:query} : search for the estado corresponding
     * to the query.
     *
     * @param query the query of the estado search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<EstadoDTO>> searchEstados(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Estados for query {}", query);
        try {
            Page<EstadoDTO> page = estadoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
