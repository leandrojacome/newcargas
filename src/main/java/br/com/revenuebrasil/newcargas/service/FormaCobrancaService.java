package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.FormaCobranca}.
 */
public interface FormaCobrancaService {
    /**
     * Save a formaCobranca.
     *
     * @param formaCobrancaDTO the entity to save.
     * @return the persisted entity.
     */
    FormaCobrancaDTO save(FormaCobrancaDTO formaCobrancaDTO);

    /**
     * Updates a formaCobranca.
     *
     * @param formaCobrancaDTO the entity to update.
     * @return the persisted entity.
     */
    FormaCobrancaDTO update(FormaCobrancaDTO formaCobrancaDTO);

    /**
     * Partially updates a formaCobranca.
     *
     * @param formaCobrancaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormaCobrancaDTO> partialUpdate(FormaCobrancaDTO formaCobrancaDTO);

    /**
     * Get all the formaCobrancas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormaCobrancaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formaCobranca.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormaCobrancaDTO> findOne(Long id);

    /**
     * Delete the "id" formaCobranca.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formaCobranca corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormaCobrancaDTO> search(String query, Pageable pageable);
}
