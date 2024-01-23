package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.EmbarcadorRepository;
import br.com.revenuebrasil.newcargas.service.EmbarcadorQueryService;
import br.com.revenuebrasil.newcargas.service.EmbarcadorService;
import br.com.revenuebrasil.newcargas.service.criteria.EmbarcadorCriteria;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Embarcador}.
 */
@RestController
@RequestMapping("/api/embarcadors")
public class EmbarcadorResource {

    private final Logger log = LoggerFactory.getLogger(EmbarcadorResource.class);

    private static final String ENTITY_NAME = "embarcador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmbarcadorService embarcadorService;

    private final EmbarcadorRepository embarcadorRepository;

    private final EmbarcadorQueryService embarcadorQueryService;

    public EmbarcadorResource(
        EmbarcadorService embarcadorService,
        EmbarcadorRepository embarcadorRepository,
        EmbarcadorQueryService embarcadorQueryService
    ) {
        this.embarcadorService = embarcadorService;
        this.embarcadorRepository = embarcadorRepository;
        this.embarcadorQueryService = embarcadorQueryService;
    }

    /**
     * {@code POST  /embarcadors} : Create a new embarcador.
     *
     * @param embarcadorDTO the embarcadorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new embarcadorDTO, or with status {@code 400 (Bad Request)} if the embarcador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmbarcadorDTO> createEmbarcador(@Valid @RequestBody EmbarcadorDTO embarcadorDTO) throws URISyntaxException {
        log.debug("REST request to save Embarcador : {}", embarcadorDTO);
        if (embarcadorDTO.getId() != null) {
            throw new BadRequestAlertException("A new embarcador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmbarcadorDTO result = embarcadorService.save(embarcadorDTO);
        return ResponseEntity
            .created(new URI("/api/embarcadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /embarcadors/:id} : Updates an existing embarcador.
     *
     * @param id the id of the embarcadorDTO to save.
     * @param embarcadorDTO the embarcadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated embarcadorDTO,
     * or with status {@code 400 (Bad Request)} if the embarcadorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the embarcadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmbarcadorDTO> updateEmbarcador(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody EmbarcadorDTO embarcadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Embarcador : {}, {}", id, embarcadorDTO);
        if (embarcadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, embarcadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!embarcadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmbarcadorDTO result = embarcadorService.update(embarcadorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, embarcadorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /embarcadors/:id} : Partial updates given fields of an existing embarcador, field will ignore if it is null
     *
     * @param id the id of the embarcadorDTO to save.
     * @param embarcadorDTO the embarcadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated embarcadorDTO,
     * or with status {@code 400 (Bad Request)} if the embarcadorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the embarcadorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the embarcadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmbarcadorDTO> partialUpdateEmbarcador(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody EmbarcadorDTO embarcadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Embarcador partially : {}, {}", id, embarcadorDTO);
        if (embarcadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, embarcadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!embarcadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmbarcadorDTO> result = embarcadorService.partialUpdate(embarcadorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, embarcadorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /embarcadors} : get all the embarcadors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of embarcadors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmbarcadorDTO>> getAllEmbarcadors(
        EmbarcadorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Embarcadors by criteria: {}", criteria);

        Page<EmbarcadorDTO> page = embarcadorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /embarcadors/count} : count all the embarcadors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmbarcadors(EmbarcadorCriteria criteria) {
        log.debug("REST request to count Embarcadors by criteria: {}", criteria);
        return ResponseEntity.ok().body(embarcadorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /embarcadors/:id} : get the "id" embarcador.
     *
     * @param id the id of the embarcadorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the embarcadorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmbarcadorDTO> getEmbarcador(@PathVariable("id") Long id) {
        log.debug("REST request to get Embarcador : {}", id);
        Optional<EmbarcadorDTO> embarcadorDTO = embarcadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(embarcadorDTO);
    }

    /**
     * {@code DELETE  /embarcadors/:id} : delete the "id" embarcador.
     *
     * @param id the id of the embarcadorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmbarcador(@PathVariable("id") Long id) {
        log.debug("REST request to delete Embarcador : {}", id);
        embarcadorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /embarcadors/_search?query=:query} : search for the embarcador corresponding
     * to the query.
     *
     * @param query the query of the embarcador search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<EmbarcadorDTO>> searchEmbarcadors(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Embarcadors for query {}", query);
        try {
            Page<EmbarcadorDTO> page = embarcadorService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
