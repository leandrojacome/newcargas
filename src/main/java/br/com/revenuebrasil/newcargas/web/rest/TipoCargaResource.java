package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.TipoCargaRepository;
import br.com.revenuebrasil.newcargas.service.TipoCargaService;
import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.TipoCarga}.
 */
@RestController
@RequestMapping("/api/tipo-cargas")
public class TipoCargaResource {

    private final Logger log = LoggerFactory.getLogger(TipoCargaResource.class);

    private static final String ENTITY_NAME = "tipoCarga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoCargaService tipoCargaService;

    private final TipoCargaRepository tipoCargaRepository;

    public TipoCargaResource(TipoCargaService tipoCargaService, TipoCargaRepository tipoCargaRepository) {
        this.tipoCargaService = tipoCargaService;
        this.tipoCargaRepository = tipoCargaRepository;
    }

    /**
     * {@code POST  /tipo-cargas} : Create a new tipoCarga.
     *
     * @param tipoCargaDTO the tipoCargaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoCargaDTO, or with status {@code 400 (Bad Request)} if the tipoCarga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoCargaDTO> createTipoCarga(@Valid @RequestBody TipoCargaDTO tipoCargaDTO) throws URISyntaxException {
        log.debug("REST request to save TipoCarga : {}", tipoCargaDTO);
        if (tipoCargaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoCarga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoCargaDTO result = tipoCargaService.save(tipoCargaDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-cargas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-cargas/:id} : Updates an existing tipoCarga.
     *
     * @param id the id of the tipoCargaDTO to save.
     * @param tipoCargaDTO the tipoCargaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoCargaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoCargaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoCargaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoCargaDTO> updateTipoCarga(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoCargaDTO tipoCargaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoCarga : {}, {}", id, tipoCargaDTO);
        if (tipoCargaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoCargaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoCargaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoCargaDTO result = tipoCargaService.update(tipoCargaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoCargaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-cargas/:id} : Partial updates given fields of an existing tipoCarga, field will ignore if it is null
     *
     * @param id the id of the tipoCargaDTO to save.
     * @param tipoCargaDTO the tipoCargaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoCargaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoCargaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoCargaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoCargaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoCargaDTO> partialUpdateTipoCarga(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoCargaDTO tipoCargaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoCarga partially : {}, {}", id, tipoCargaDTO);
        if (tipoCargaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoCargaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoCargaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoCargaDTO> result = tipoCargaService.partialUpdate(tipoCargaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoCargaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-cargas} : get all the tipoCargas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoCargas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoCargaDTO>> getAllTipoCargas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TipoCargas");
        Page<TipoCargaDTO> page = tipoCargaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-cargas/:id} : get the "id" tipoCarga.
     *
     * @param id the id of the tipoCargaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoCargaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoCargaDTO> getTipoCarga(@PathVariable("id") Long id) {
        log.debug("REST request to get TipoCarga : {}", id);
        Optional<TipoCargaDTO> tipoCargaDTO = tipoCargaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoCargaDTO);
    }

    /**
     * {@code DELETE  /tipo-cargas/:id} : delete the "id" tipoCarga.
     *
     * @param id the id of the tipoCargaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoCarga(@PathVariable("id") Long id) {
        log.debug("REST request to delete TipoCarga : {}", id);
        tipoCargaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tipo-cargas/_search?query=:query} : search for the tipoCarga corresponding
     * to the query.
     *
     * @param query the query of the tipoCarga search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TipoCargaDTO>> searchTipoCargas(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TipoCargas for query {}", query);
        try {
            Page<TipoCargaDTO> page = tipoCargaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
