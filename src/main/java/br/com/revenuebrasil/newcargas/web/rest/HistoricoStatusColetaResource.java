package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.HistoricoStatusColetaRepository;
import br.com.revenuebrasil.newcargas.service.HistoricoStatusColetaService;
import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta}.
 */
@RestController
@RequestMapping("/api/historico-status-coletas")
public class HistoricoStatusColetaResource {

    private final Logger log = LoggerFactory.getLogger(HistoricoStatusColetaResource.class);

    private static final String ENTITY_NAME = "historicoStatusColeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoricoStatusColetaService historicoStatusColetaService;

    private final HistoricoStatusColetaRepository historicoStatusColetaRepository;

    public HistoricoStatusColetaResource(
        HistoricoStatusColetaService historicoStatusColetaService,
        HistoricoStatusColetaRepository historicoStatusColetaRepository
    ) {
        this.historicoStatusColetaService = historicoStatusColetaService;
        this.historicoStatusColetaRepository = historicoStatusColetaRepository;
    }

    /**
     * {@code POST  /historico-status-coletas} : Create a new historicoStatusColeta.
     *
     * @param historicoStatusColetaDTO the historicoStatusColetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historicoStatusColetaDTO, or with status {@code 400 (Bad Request)} if the historicoStatusColeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoricoStatusColetaDTO> createHistoricoStatusColeta(
        @Valid @RequestBody HistoricoStatusColetaDTO historicoStatusColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to save HistoricoStatusColeta : {}", historicoStatusColetaDTO);
        if (historicoStatusColetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new historicoStatusColeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoricoStatusColetaDTO result = historicoStatusColetaService.save(historicoStatusColetaDTO);
        return ResponseEntity
            .created(new URI("/api/historico-status-coletas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historico-status-coletas/:id} : Updates an existing historicoStatusColeta.
     *
     * @param id the id of the historicoStatusColetaDTO to save.
     * @param historicoStatusColetaDTO the historicoStatusColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historicoStatusColetaDTO,
     * or with status {@code 400 (Bad Request)} if the historicoStatusColetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historicoStatusColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoricoStatusColetaDTO> updateHistoricoStatusColeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoricoStatusColetaDTO historicoStatusColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoricoStatusColeta : {}, {}", id, historicoStatusColetaDTO);
        if (historicoStatusColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historicoStatusColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historicoStatusColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoricoStatusColetaDTO result = historicoStatusColetaService.update(historicoStatusColetaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, historicoStatusColetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historico-status-coletas/:id} : Partial updates given fields of an existing historicoStatusColeta, field will ignore if it is null
     *
     * @param id the id of the historicoStatusColetaDTO to save.
     * @param historicoStatusColetaDTO the historicoStatusColetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historicoStatusColetaDTO,
     * or with status {@code 400 (Bad Request)} if the historicoStatusColetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historicoStatusColetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historicoStatusColetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoricoStatusColetaDTO> partialUpdateHistoricoStatusColeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoricoStatusColetaDTO historicoStatusColetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoricoStatusColeta partially : {}, {}", id, historicoStatusColetaDTO);
        if (historicoStatusColetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historicoStatusColetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historicoStatusColetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoricoStatusColetaDTO> result = historicoStatusColetaService.partialUpdate(historicoStatusColetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, historicoStatusColetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historico-status-coletas} : get all the historicoStatusColetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historicoStatusColetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoricoStatusColetaDTO>> getAllHistoricoStatusColetas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of HistoricoStatusColetas");
        Page<HistoricoStatusColetaDTO> page = historicoStatusColetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historico-status-coletas/:id} : get the "id" historicoStatusColeta.
     *
     * @param id the id of the historicoStatusColetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historicoStatusColetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoStatusColetaDTO> getHistoricoStatusColeta(@PathVariable("id") Long id) {
        log.debug("REST request to get HistoricoStatusColeta : {}", id);
        Optional<HistoricoStatusColetaDTO> historicoStatusColetaDTO = historicoStatusColetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historicoStatusColetaDTO);
    }

    /**
     * {@code DELETE  /historico-status-coletas/:id} : delete the "id" historicoStatusColeta.
     *
     * @param id the id of the historicoStatusColetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoricoStatusColeta(@PathVariable("id") Long id) {
        log.debug("REST request to delete HistoricoStatusColeta : {}", id);
        historicoStatusColetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /historico-status-coletas/_search?query=:query} : search for the historicoStatusColeta corresponding
     * to the query.
     *
     * @param query the query of the historicoStatusColeta search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<HistoricoStatusColetaDTO>> searchHistoricoStatusColetas(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of HistoricoStatusColetas for query {}", query);
        try {
            Page<HistoricoStatusColetaDTO> page = historicoStatusColetaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
