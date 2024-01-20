package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Roteirizacao}.
 */
public interface RoteirizacaoService {
    /**
     * Save a roteirizacao.
     *
     * @param roteirizacaoDTO the entity to save.
     * @return the persisted entity.
     */
    RoteirizacaoDTO save(RoteirizacaoDTO roteirizacaoDTO);

    /**
     * Updates a roteirizacao.
     *
     * @param roteirizacaoDTO the entity to update.
     * @return the persisted entity.
     */
    RoteirizacaoDTO update(RoteirizacaoDTO roteirizacaoDTO);

    /**
     * Partially updates a roteirizacao.
     *
     * @param roteirizacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoteirizacaoDTO> partialUpdate(RoteirizacaoDTO roteirizacaoDTO);

    /**
     * Get all the roteirizacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoteirizacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" roteirizacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoteirizacaoDTO> findOne(Long id);

    /**
     * Delete the "id" roteirizacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the roteirizacao corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoteirizacaoDTO> search(String query, Pageable pageable);
}
