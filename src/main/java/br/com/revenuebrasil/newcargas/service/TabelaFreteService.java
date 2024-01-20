package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.TabelaFrete}.
 */
public interface TabelaFreteService {
    /**
     * Save a tabelaFrete.
     *
     * @param tabelaFreteDTO the entity to save.
     * @return the persisted entity.
     */
    TabelaFreteDTO save(TabelaFreteDTO tabelaFreteDTO);

    /**
     * Updates a tabelaFrete.
     *
     * @param tabelaFreteDTO the entity to update.
     * @return the persisted entity.
     */
    TabelaFreteDTO update(TabelaFreteDTO tabelaFreteDTO);

    /**
     * Partially updates a tabelaFrete.
     *
     * @param tabelaFreteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TabelaFreteDTO> partialUpdate(TabelaFreteDTO tabelaFreteDTO);

    /**
     * Get all the tabelaFretes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TabelaFreteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tabelaFrete.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TabelaFreteDTO> findOne(Long id);

    /**
     * Delete the "id" tabelaFrete.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tabelaFrete corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TabelaFreteDTO> search(String query, Pageable pageable);
}
