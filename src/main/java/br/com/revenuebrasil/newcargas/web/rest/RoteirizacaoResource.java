package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.RoteirizacaoRepository;
import br.com.revenuebrasil.newcargas.service.RoteirizacaoQueryService;
import br.com.revenuebrasil.newcargas.service.RoteirizacaoService;
import br.com.revenuebrasil.newcargas.service.criteria.RoteirizacaoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Roteirizacao}.
 */
@RestController
@RequestMapping("/api/roteirizacaos")
public class RoteirizacaoResource {

    private final Logger log = LoggerFactory.getLogger(RoteirizacaoResource.class);

    private static final String ENTITY_NAME = "roteirizacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoteirizacaoService roteirizacaoService;

    private final RoteirizacaoRepository roteirizacaoRepository;

    private final RoteirizacaoQueryService roteirizacaoQueryService;

    public RoteirizacaoResource(
        RoteirizacaoService roteirizacaoService,
        RoteirizacaoRepository roteirizacaoRepository,
        RoteirizacaoQueryService roteirizacaoQueryService
    ) {
        this.roteirizacaoService = roteirizacaoService;
        this.roteirizacaoRepository = roteirizacaoRepository;
        this.roteirizacaoQueryService = roteirizacaoQueryService;
    }

    /**
     * {@code POST  /roteirizacaos} : Create a new roteirizacao.
     *
     * @param roteirizacaoDTO the roteirizacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roteirizacaoDTO, or with status {@code 400 (Bad Request)} if the roteirizacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoteirizacaoDTO> createRoteirizacao(@Valid @RequestBody RoteirizacaoDTO roteirizacaoDTO)
        throws URISyntaxException {
        log.debug("REST request to save Roteirizacao : {}", roteirizacaoDTO);
        if (roteirizacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new roteirizacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoteirizacaoDTO result = roteirizacaoService.save(roteirizacaoDTO);
        return ResponseEntity
            .created(new URI("/api/roteirizacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roteirizacaos/:id} : Updates an existing roteirizacao.
     *
     * @param id the id of the roteirizacaoDTO to save.
     * @param roteirizacaoDTO the roteirizacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roteirizacaoDTO,
     * or with status {@code 400 (Bad Request)} if the roteirizacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roteirizacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoteirizacaoDTO> updateRoteirizacao(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody RoteirizacaoDTO roteirizacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Roteirizacao : {}, {}", id, roteirizacaoDTO);
        if (roteirizacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roteirizacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roteirizacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoteirizacaoDTO result = roteirizacaoService.update(roteirizacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roteirizacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roteirizacaos/:id} : Partial updates given fields of an existing roteirizacao, field will ignore if it is null
     *
     * @param id the id of the roteirizacaoDTO to save.
     * @param roteirizacaoDTO the roteirizacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roteirizacaoDTO,
     * or with status {@code 400 (Bad Request)} if the roteirizacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roteirizacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roteirizacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoteirizacaoDTO> partialUpdateRoteirizacao(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody RoteirizacaoDTO roteirizacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Roteirizacao partially : {}, {}", id, roteirizacaoDTO);
        if (roteirizacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roteirizacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roteirizacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoteirizacaoDTO> result = roteirizacaoService.partialUpdate(roteirizacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roteirizacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /roteirizacaos} : get all the roteirizacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roteirizacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoteirizacaoDTO>> getAllRoteirizacaos(
        RoteirizacaoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Roteirizacaos by criteria: {}", criteria);

        Page<RoteirizacaoDTO> page = roteirizacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roteirizacaos/count} : count all the roteirizacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRoteirizacaos(RoteirizacaoCriteria criteria) {
        log.debug("REST request to count Roteirizacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(roteirizacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /roteirizacaos/:id} : get the "id" roteirizacao.
     *
     * @param id the id of the roteirizacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roteirizacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoteirizacaoDTO> getRoteirizacao(@PathVariable("id") Long id) {
        log.debug("REST request to get Roteirizacao : {}", id);
        Optional<RoteirizacaoDTO> roteirizacaoDTO = roteirizacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roteirizacaoDTO);
    }

    /**
     * {@code DELETE  /roteirizacaos/:id} : delete the "id" roteirizacao.
     *
     * @param id the id of the roteirizacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoteirizacao(@PathVariable("id") Long id) {
        log.debug("REST request to delete Roteirizacao : {}", id);
        roteirizacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /roteirizacaos/_search?query=:query} : search for the roteirizacao corresponding
     * to the query.
     *
     * @param query the query of the roteirizacao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<RoteirizacaoDTO>> searchRoteirizacaos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Roteirizacaos for query {}", query);
        try {
            Page<RoteirizacaoDTO> page = roteirizacaoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
