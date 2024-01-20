package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.FaturaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Fatura}.
 */
public interface FaturaService {
    /**
     * Save a fatura.
     *
     * @param faturaDTO the entity to save.
     * @return the persisted entity.
     */
    FaturaDTO save(FaturaDTO faturaDTO);

    /**
     * Updates a fatura.
     *
     * @param faturaDTO the entity to update.
     * @return the persisted entity.
     */
    FaturaDTO update(FaturaDTO faturaDTO);

    /**
     * Partially updates a fatura.
     *
     * @param faturaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FaturaDTO> partialUpdate(FaturaDTO faturaDTO);

    /**
     * Get all the faturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FaturaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fatura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FaturaDTO> findOne(Long id);

    /**
     * Delete the "id" fatura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fatura corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FaturaDTO> search(String query, Pageable pageable);
}
