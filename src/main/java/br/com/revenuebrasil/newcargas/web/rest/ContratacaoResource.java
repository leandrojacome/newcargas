package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.ContratacaoRepository;
import br.com.revenuebrasil.newcargas.service.ContratacaoService;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Contratacao}.
 */
@RestController
@RequestMapping("/api/contratacaos")
public class ContratacaoResource {

    private final Logger log = LoggerFactory.getLogger(ContratacaoResource.class);

    private static final String ENTITY_NAME = "contratacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContratacaoService contratacaoService;

    private final ContratacaoRepository contratacaoRepository;

    public ContratacaoResource(ContratacaoService contratacaoService, ContratacaoRepository contratacaoRepository) {
        this.contratacaoService = contratacaoService;
        this.contratacaoRepository = contratacaoRepository;
    }

    /**
     * {@code POST  /contratacaos} : Create a new contratacao.
     *
     * @param contratacaoDTO the contratacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contratacaoDTO, or with status {@code 400 (Bad Request)} if the contratacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContratacaoDTO> createContratacao(@Valid @RequestBody ContratacaoDTO contratacaoDTO) throws URISyntaxException {
        log.debug("REST request to save Contratacao : {}", contratacaoDTO);
        if (contratacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new contratacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContratacaoDTO result = contratacaoService.save(contratacaoDTO);
        return ResponseEntity
            .created(new URI("/api/contratacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contratacaos/:id} : Updates an existing contratacao.
     *
     * @param id the id of the contratacaoDTO to save.
     * @param contratacaoDTO the contratacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratacaoDTO,
     * or with status {@code 400 (Bad Request)} if the contratacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contratacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContratacaoDTO> updateContratacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContratacaoDTO contratacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contratacao : {}, {}", id, contratacaoDTO);
        if (contratacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contratacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contratacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContratacaoDTO result = contratacaoService.update(contratacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contratacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contratacaos/:id} : Partial updates given fields of an existing contratacao, field will ignore if it is null
     *
     * @param id the id of the contratacaoDTO to save.
     * @param contratacaoDTO the contratacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratacaoDTO,
     * or with status {@code 400 (Bad Request)} if the contratacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contratacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contratacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContratacaoDTO> partialUpdateContratacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContratacaoDTO contratacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contratacao partially : {}, {}", id, contratacaoDTO);
        if (contratacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contratacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contratacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContratacaoDTO> result = contratacaoService.partialUpdate(contratacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contratacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contratacaos} : get all the contratacaos.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contratacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContratacaoDTO>> getAllContratacaos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("solicitacaocoleta-is-null".equals(filter)) {
            log.debug("REST request to get all Contratacaos where solicitacaoColeta is null");
            return new ResponseEntity<>(contratacaoService.findAllWhereSolicitacaoColetaIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Contratacaos");
        Page<ContratacaoDTO> page = contratacaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contratacaos/:id} : get the "id" contratacao.
     *
     * @param id the id of the contratacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contratacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContratacaoDTO> getContratacao(@PathVariable("id") Long id) {
        log.debug("REST request to get Contratacao : {}", id);
        Optional<ContratacaoDTO> contratacaoDTO = contratacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contratacaoDTO);
    }

    /**
     * {@code DELETE  /contratacaos/:id} : delete the "id" contratacao.
     *
     * @param id the id of the contratacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContratacao(@PathVariable("id") Long id) {
        log.debug("REST request to delete Contratacao : {}", id);
        contratacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /contratacaos/_search?query=:query} : search for the contratacao corresponding
     * to the query.
     *
     * @param query the query of the contratacao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ContratacaoDTO>> searchContratacaos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Contratacaos for query {}", query);
        try {
            Page<ContratacaoDTO> page = contratacaoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
