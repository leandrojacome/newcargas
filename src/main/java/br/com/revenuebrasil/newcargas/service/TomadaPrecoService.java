package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.TomadaPrecoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.TomadaPreco}.
 */
public interface TomadaPrecoService {
    /**
     * Save a tomadaPreco.
     *
     * @param tomadaPrecoDTO the entity to save.
     * @return the persisted entity.
     */
    TomadaPrecoDTO save(TomadaPrecoDTO tomadaPrecoDTO);

    /**
     * Updates a tomadaPreco.
     *
     * @param tomadaPrecoDTO the entity to update.
     * @return the persisted entity.
     */
    TomadaPrecoDTO update(TomadaPrecoDTO tomadaPrecoDTO);

    /**
     * Partially updates a tomadaPreco.
     *
     * @param tomadaPrecoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TomadaPrecoDTO> partialUpdate(TomadaPrecoDTO tomadaPrecoDTO);

    /**
     * Get all the tomadaPrecos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TomadaPrecoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tomadaPreco.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TomadaPrecoDTO> findOne(Long id);

    /**
     * Delete the "id" tomadaPreco.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tomadaPreco corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TomadaPrecoDTO> search(String query, Pageable pageable);
}
