package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.RegiaoRepository;
import br.com.revenuebrasil.newcargas.service.RegiaoService;
import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Regiao}.
 */
@RestController
@RequestMapping("/api/regiaos")
public class RegiaoResource {

    private final Logger log = LoggerFactory.getLogger(RegiaoResource.class);

    private static final String ENTITY_NAME = "regiao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegiaoService regiaoService;

    private final RegiaoRepository regiaoRepository;

    public RegiaoResource(RegiaoService regiaoService, RegiaoRepository regiaoRepository) {
        this.regiaoService = regiaoService;
        this.regiaoRepository = regiaoRepository;
    }

    /**
     * {@code POST  /regiaos} : Create a new regiao.
     *
     * @param regiaoDTO the regiaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regiaoDTO, or with status {@code 400 (Bad Request)} if the regiao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RegiaoDTO> createRegiao(@Valid @RequestBody RegiaoDTO regiaoDTO) throws URISyntaxException {
        log.debug("REST request to save Regiao : {}", regiaoDTO);
        if (regiaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new regiao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegiaoDTO result = regiaoService.save(regiaoDTO);
        return ResponseEntity
            .created(new URI("/api/regiaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /regiaos/:id} : Updates an existing regiao.
     *
     * @param id the id of the regiaoDTO to save.
     * @param regiaoDTO the regiaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regiaoDTO,
     * or with status {@code 400 (Bad Request)} if the regiaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regiaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegiaoDTO> updateRegiao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RegiaoDTO regiaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Regiao : {}, {}", id, regiaoDTO);
        if (regiaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regiaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regiaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegiaoDTO result = regiaoService.update(regiaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regiaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /regiaos/:id} : Partial updates given fields of an existing regiao, field will ignore if it is null
     *
     * @param id the id of the regiaoDTO to save.
     * @param regiaoDTO the regiaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regiaoDTO,
     * or with status {@code 400 (Bad Request)} if the regiaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the regiaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the regiaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RegiaoDTO> partialUpdateRegiao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RegiaoDTO regiaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Regiao partially : {}, {}", id, regiaoDTO);
        if (regiaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regiaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regiaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegiaoDTO> result = regiaoService.partialUpdate(regiaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regiaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /regiaos} : get all the regiaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regiaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RegiaoDTO>> getAllRegiaos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Regiaos");
        Page<RegiaoDTO> page = regiaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /regiaos/:id} : get the "id" regiao.
     *
     * @param id the id of the regiaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regiaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RegiaoDTO> getRegiao(@PathVariable("id") Long id) {
        log.debug("REST request to get Regiao : {}", id);
        Optional<RegiaoDTO> regiaoDTO = regiaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regiaoDTO);
    }

    /**
     * {@code DELETE  /regiaos/:id} : delete the "id" regiao.
     *
     * @param id the id of the regiaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegiao(@PathVariable("id") Long id) {
        log.debug("REST request to delete Regiao : {}", id);
        regiaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /regiaos/_search?query=:query} : search for the regiao corresponding
     * to the query.
     *
     * @param query the query of the regiao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<RegiaoDTO>> searchRegiaos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Regiaos for query {}", query);
        try {
            Page<RegiaoDTO> page = regiaoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
