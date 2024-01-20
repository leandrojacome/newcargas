package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Transportadora}.
 */
public interface TransportadoraService {
    /**
     * Save a transportadora.
     *
     * @param transportadoraDTO the entity to save.
     * @return the persisted entity.
     */
    TransportadoraDTO save(TransportadoraDTO transportadoraDTO);

    /**
     * Updates a transportadora.
     *
     * @param transportadoraDTO the entity to update.
     * @return the persisted entity.
     */
    TransportadoraDTO update(TransportadoraDTO transportadoraDTO);

    /**
     * Partially updates a transportadora.
     *
     * @param transportadoraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransportadoraDTO> partialUpdate(TransportadoraDTO transportadoraDTO);

    /**
     * Get all the transportadoras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransportadoraDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transportadora.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransportadoraDTO> findOne(Long id);

    /**
     * Delete the "id" transportadora.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the transportadora corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransportadoraDTO> search(String query, Pageable pageable);
}
