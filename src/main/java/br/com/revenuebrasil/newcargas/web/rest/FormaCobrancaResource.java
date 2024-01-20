package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.FormaCobrancaRepository;
import br.com.revenuebrasil.newcargas.service.FormaCobrancaService;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.FormaCobranca}.
 */
@RestController
@RequestMapping("/api/forma-cobrancas")
public class FormaCobrancaResource {

    private final Logger log = LoggerFactory.getLogger(FormaCobrancaResource.class);

    private static final String ENTITY_NAME = "formaCobranca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormaCobrancaService formaCobrancaService;

    private final FormaCobrancaRepository formaCobrancaRepository;

    public FormaCobrancaResource(FormaCobrancaService formaCobrancaService, FormaCobrancaRepository formaCobrancaRepository) {
        this.formaCobrancaService = formaCobrancaService;
        this.formaCobrancaRepository = formaCobrancaRepository;
    }

    /**
     * {@code POST  /forma-cobrancas} : Create a new formaCobranca.
     *
     * @param formaCobrancaDTO the formaCobrancaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formaCobrancaDTO, or with status {@code 400 (Bad Request)} if the formaCobranca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FormaCobrancaDTO> createFormaCobranca(@Valid @RequestBody FormaCobrancaDTO formaCobrancaDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormaCobranca : {}", formaCobrancaDTO);
        if (formaCobrancaDTO.getId() != null) {
            throw new BadRequestAlertException("A new formaCobranca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormaCobrancaDTO result = formaCobrancaService.save(formaCobrancaDTO);
        return ResponseEntity
            .created(new URI("/api/forma-cobrancas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forma-cobrancas/:id} : Updates an existing formaCobranca.
     *
     * @param id the id of the formaCobrancaDTO to save.
     * @param formaCobrancaDTO the formaCobrancaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formaCobrancaDTO,
     * or with status {@code 400 (Bad Request)} if the formaCobrancaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formaCobrancaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormaCobrancaDTO> updateFormaCobranca(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormaCobrancaDTO formaCobrancaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormaCobranca : {}, {}", id, formaCobrancaDTO);
        if (formaCobrancaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formaCobrancaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formaCobrancaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormaCobrancaDTO result = formaCobrancaService.update(formaCobrancaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formaCobrancaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /forma-cobrancas/:id} : Partial updates given fields of an existing formaCobranca, field will ignore if it is null
     *
     * @param id the id of the formaCobrancaDTO to save.
     * @param formaCobrancaDTO the formaCobrancaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formaCobrancaDTO,
     * or with status {@code 400 (Bad Request)} if the formaCobrancaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formaCobrancaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formaCobrancaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormaCobrancaDTO> partialUpdateFormaCobranca(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormaCobrancaDTO formaCobrancaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormaCobranca partially : {}, {}", id, formaCobrancaDTO);
        if (formaCobrancaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formaCobrancaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formaCobrancaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormaCobrancaDTO> result = formaCobrancaService.partialUpdate(formaCobrancaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formaCobrancaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /forma-cobrancas} : get all the formaCobrancas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formaCobrancas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FormaCobrancaDTO>> getAllFormaCobrancas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FormaCobrancas");
        Page<FormaCobrancaDTO> page = formaCobrancaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /forma-cobrancas/:id} : get the "id" formaCobranca.
     *
     * @param id the id of the formaCobrancaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formaCobrancaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormaCobrancaDTO> getFormaCobranca(@PathVariable("id") Long id) {
        log.debug("REST request to get FormaCobranca : {}", id);
        Optional<FormaCobrancaDTO> formaCobrancaDTO = formaCobrancaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formaCobrancaDTO);
    }

    /**
     * {@code DELETE  /forma-cobrancas/:id} : delete the "id" formaCobranca.
     *
     * @param id the id of the formaCobrancaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormaCobranca(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormaCobranca : {}", id);
        formaCobrancaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /forma-cobrancas/_search?query=:query} : search for the formaCobranca corresponding
     * to the query.
     *
     * @param query the query of the formaCobranca search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FormaCobrancaDTO>> searchFormaCobrancas(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of FormaCobrancas for query {}", query);
        try {
            Page<FormaCobrancaDTO> page = formaCobrancaService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}