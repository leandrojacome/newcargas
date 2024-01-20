package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.StatusColeta}.
 */
public interface StatusColetaService {
    /**
     * Save a statusColeta.
     *
     * @param statusColetaDTO the entity to save.
     * @return the persisted entity.
     */
    StatusColetaDTO save(StatusColetaDTO statusColetaDTO);

    /**
     * Updates a statusColeta.
     *
     * @param statusColetaDTO the entity to update.
     * @return the persisted entity.
     */
    StatusColetaDTO update(StatusColetaDTO statusColetaDTO);

    /**
     * Partially updates a statusColeta.
     *
     * @param statusColetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatusColetaDTO> partialUpdate(StatusColetaDTO statusColetaDTO);

    /**
     * Get all the statusColetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatusColetaDTO> findAll(Pageable pageable);

    /**
     * Get all the statusColetas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatusColetaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" statusColeta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatusColetaDTO> findOne(Long id);

    /**
     * Delete the "id" statusColeta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the statusColeta corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatusColetaDTO> search(String query, Pageable pageable);
}
