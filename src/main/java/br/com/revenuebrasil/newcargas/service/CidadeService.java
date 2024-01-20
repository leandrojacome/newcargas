package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Cidade}.
 */
public interface CidadeService {
    /**
     * Save a cidade.
     *
     * @param cidadeDTO the entity to save.
     * @return the persisted entity.
     */
    CidadeDTO save(CidadeDTO cidadeDTO);

    /**
     * Updates a cidade.
     *
     * @param cidadeDTO the entity to update.
     * @return the persisted entity.
     */
    CidadeDTO update(CidadeDTO cidadeDTO);

    /**
     * Partially updates a cidade.
     *
     * @param cidadeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CidadeDTO> partialUpdate(CidadeDTO cidadeDTO);

    /**
     * Get all the cidades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CidadeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cidade.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CidadeDTO> findOne(Long id);

    /**
     * Delete the "id" cidade.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cidade corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CidadeDTO> search(String query, Pageable pageable);
}
