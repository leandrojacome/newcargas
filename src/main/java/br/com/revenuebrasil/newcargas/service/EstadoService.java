package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.EstadoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Estado}.
 */
public interface EstadoService {
    /**
     * Save a estado.
     *
     * @param estadoDTO the entity to save.
     * @return the persisted entity.
     */
    EstadoDTO save(EstadoDTO estadoDTO);

    /**
     * Updates a estado.
     *
     * @param estadoDTO the entity to update.
     * @return the persisted entity.
     */
    EstadoDTO update(EstadoDTO estadoDTO);

    /**
     * Partially updates a estado.
     *
     * @param estadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EstadoDTO> partialUpdate(EstadoDTO estadoDTO);

    /**
     * Get all the estados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstadoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" estado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EstadoDTO> findOne(Long id);

    /**
     * Delete the "id" estado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the estado corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstadoDTO> search(String query, Pageable pageable);
}
