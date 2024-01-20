package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.TipoFrete}.
 */
public interface TipoFreteService {
    /**
     * Save a tipoFrete.
     *
     * @param tipoFreteDTO the entity to save.
     * @return the persisted entity.
     */
    TipoFreteDTO save(TipoFreteDTO tipoFreteDTO);

    /**
     * Updates a tipoFrete.
     *
     * @param tipoFreteDTO the entity to update.
     * @return the persisted entity.
     */
    TipoFreteDTO update(TipoFreteDTO tipoFreteDTO);

    /**
     * Partially updates a tipoFrete.
     *
     * @param tipoFreteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoFreteDTO> partialUpdate(TipoFreteDTO tipoFreteDTO);

    /**
     * Get all the tipoFretes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoFreteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tipoFrete.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoFreteDTO> findOne(Long id);

    /**
     * Delete the "id" tipoFrete.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tipoFrete corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoFreteDTO> search(String query, Pageable pageable);
}
