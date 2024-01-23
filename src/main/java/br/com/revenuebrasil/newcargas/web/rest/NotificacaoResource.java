package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.NotificacaoRepository;
import br.com.revenuebrasil.newcargas.service.NotificacaoQueryService;
import br.com.revenuebrasil.newcargas.service.NotificacaoService;
import br.com.revenuebrasil.newcargas.service.criteria.NotificacaoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.NotificacaoDTO;
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
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Notificacao}.
 */
@RestController
@RequestMapping("/api/notificacaos")
public class NotificacaoResource {

    private final Logger log = LoggerFactory.getLogger(NotificacaoResource.class);

    private static final String ENTITY_NAME = "notificacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacaoService notificacaoService;

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoQueryService notificacaoQueryService;

    public NotificacaoResource(
        NotificacaoService notificacaoService,
        NotificacaoRepository notificacaoRepository,
        NotificacaoQueryService notificacaoQueryService
    ) {
        this.notificacaoService = notificacaoService;
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoQueryService = notificacaoQueryService;
    }

    /**
     * {@code POST  /notificacaos} : Create a new notificacao.
     *
     * @param notificacaoDTO the notificacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacaoDTO, or with status {@code 400 (Bad Request)} if the notificacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificacaoDTO> createNotificacao(@Valid @RequestBody NotificacaoDTO notificacaoDTO) throws URISyntaxException {
        log.debug("REST request to save Notificacao : {}", notificacaoDTO);
        if (notificacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificacaoDTO result = notificacaoService.save(notificacaoDTO);
        return ResponseEntity
            .created(new URI("/api/notificacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notificacaos/:id} : Updates an existing notificacao.
     *
     * @param id the id of the notificacaoDTO to save.
     * @param notificacaoDTO the notificacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacaoDTO,
     * or with status {@code 400 (Bad Request)} if the notificacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificacaoDTO> updateNotificacao(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody NotificacaoDTO notificacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Notificacao : {}, {}", id, notificacaoDTO);
        if (notificacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotificacaoDTO result = notificacaoService.update(notificacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notificacaos/:id} : Partial updates given fields of an existing notificacao, field will ignore if it is null
     *
     * @param id the id of the notificacaoDTO to save.
     * @param notificacaoDTO the notificacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacaoDTO,
     * or with status {@code 400 (Bad Request)} if the notificacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificacaoDTO> partialUpdateNotificacao(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificacaoDTO notificacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notificacao partially : {}, {}", id, notificacaoDTO);
        if (notificacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificacaoDTO> result = notificacaoService.partialUpdate(notificacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notificacaos} : get all the notificacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificacaoDTO>> getAllNotificacaos(
        NotificacaoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Notificacaos by criteria: {}", criteria);

        Page<NotificacaoDTO> page = notificacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notificacaos/count} : count all the notificacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotificacaos(NotificacaoCriteria criteria) {
        log.debug("REST request to count Notificacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notificacaos/:id} : get the "id" notificacao.
     *
     * @param id the id of the notificacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoDTO> getNotificacao(@PathVariable("id") Long id) {
        log.debug("REST request to get Notificacao : {}", id);
        Optional<NotificacaoDTO> notificacaoDTO = notificacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificacaoDTO);
    }

    /**
     * {@code DELETE  /notificacaos/:id} : delete the "id" notificacao.
     *
     * @param id the id of the notificacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificacao(@PathVariable("id") Long id) {
        log.debug("REST request to delete Notificacao : {}", id);
        notificacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /notificacaos/_search?query=:query} : search for the notificacao corresponding
     * to the query.
     *
     * @param query the query of the notificacao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<NotificacaoDTO>> searchNotificacaos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Notificacaos for query {}", query);
        try {
            Page<NotificacaoDTO> page = notificacaoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
