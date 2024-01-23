package br.com.revenuebrasil.newcargas.web.rest;

import br.com.revenuebrasil.newcargas.repository.BancoRepository;
import br.com.revenuebrasil.newcargas.service.BancoQueryService;
import br.com.revenuebrasil.newcargas.service.BancoService;
import br.com.revenuebrasil.newcargas.service.criteria.BancoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.revenuebrasil.newcargas.domain.Banco}.
 */
@RestController
@RequestMapping("/api/bancos")
public class BancoResource {

    private final Logger log = LoggerFactory.getLogger(BancoResource.class);

    private static final String ENTITY_NAME = "banco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BancoService bancoService;

    private final BancoRepository bancoRepository;

    private final BancoQueryService bancoQueryService;

    public BancoResource(BancoService bancoService, BancoRepository bancoRepository, BancoQueryService bancoQueryService) {
        this.bancoService = bancoService;
        this.bancoRepository = bancoRepository;
        this.bancoQueryService = bancoQueryService;
    }

    /**
     * {@code POST  /bancos} : Create a new banco.
     *
     * @param bancoDTO the bancoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bancoDTO, or with status {@code 400 (Bad Request)} if the banco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("permitAll()")
    @PostMapping("")
    public ResponseEntity<BancoDTO> createBanco(@Valid @RequestBody BancoDTO bancoDTO) throws URISyntaxException {
        log.debug("REST request to save Banco : {}", bancoDTO);
        if (bancoDTO.getId() != null) {
            throw new BadRequestAlertException("A new banco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BancoDTO result = bancoService.save(bancoDTO);
        return ResponseEntity
            .created(new URI("/api/bancos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bancos/:id} : Updates an existing banco.
     *
     * @param id the id of the bancoDTO to save.
     * @param bancoDTO the bancoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bancoDTO,
     * or with status {@code 400 (Bad Request)} if the bancoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bancoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BancoDTO> updateBanco(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @Valid @RequestBody BancoDTO bancoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Banco : {}, {}", id, bancoDTO);
        if (bancoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bancoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bancoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BancoDTO result = bancoService.update(bancoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bancoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bancos/:id} : Partial updates given fields of an existing banco, field will ignore if it is null
     *
     * @param id the id of the bancoDTO to save.
     * @param bancoDTO the bancoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bancoDTO,
     * or with status {@code 400 (Bad Request)} if the bancoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bancoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bancoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BancoDTO> partialUpdateBanco(
        @PathVariable(name = "id", value = "id", required = false) final Long id,
        @NotNull @RequestBody BancoDTO bancoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Banco partially : {}, {}", id, bancoDTO);
        if (bancoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bancoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bancoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BancoDTO> result = bancoService.partialUpdate(bancoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bancoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bancos} : get all the bancos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bancos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BancoDTO>> getAllBancos(
        BancoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Bancos by criteria: {}", criteria);

        Page<BancoDTO> page = bancoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bancos/count} : count all the bancos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBancos(BancoCriteria criteria) {
        log.debug("REST request to count Bancos by criteria: {}", criteria);
        return ResponseEntity.ok().body(bancoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bancos/:id} : get the "id" banco.
     *
     * @param id the id of the bancoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bancoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BancoDTO> getBanco(@PathVariable("id") Long id) {
        log.debug("REST request to get Banco : {}", id);
        Optional<BancoDTO> bancoDTO = bancoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bancoDTO);
    }

    /**
     * {@code DELETE  /bancos/:id} : delete the "id" banco.
     *
     * @param id the id of the bancoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanco(@PathVariable("id") Long id) {
        log.debug("REST request to delete Banco : {}", id);
        bancoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /bancos/_search?query=:query} : search for the banco corresponding
     * to the query.
     *
     * @param query the query of the banco search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<BancoDTO>> searchBancos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Bancos for query {}", query);
        try {
            Page<BancoDTO> page = bancoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
