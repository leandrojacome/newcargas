package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.ContaBancaria}.
 */
public interface ContaBancariaService {
    /**
     * Save a contaBancaria.
     *
     * @param contaBancariaDTO the entity to save.
     * @return the persisted entity.
     */
    ContaBancariaDTO save(ContaBancariaDTO contaBancariaDTO);

    /**
     * Updates a contaBancaria.
     *
     * @param contaBancariaDTO the entity to update.
     * @return the persisted entity.
     */
    ContaBancariaDTO update(ContaBancariaDTO contaBancariaDTO);

    /**
     * Partially updates a contaBancaria.
     *
     * @param contaBancariaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContaBancariaDTO> partialUpdate(ContaBancariaDTO contaBancariaDTO);

    /**
     * Get all the contaBancarias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaBancariaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contaBancaria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContaBancariaDTO> findOne(Long id);

    /**
     * Delete the "id" contaBancaria.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contaBancaria corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaBancariaDTO> search(String query, Pageable pageable);
}
