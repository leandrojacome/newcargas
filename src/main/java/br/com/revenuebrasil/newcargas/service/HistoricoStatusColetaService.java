package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta}.
 */
public interface HistoricoStatusColetaService {
    /**
     * Save a historicoStatusColeta.
     *
     * @param historicoStatusColetaDTO the entity to save.
     * @return the persisted entity.
     */
    HistoricoStatusColetaDTO save(HistoricoStatusColetaDTO historicoStatusColetaDTO);

    /**
     * Updates a historicoStatusColeta.
     *
     * @param historicoStatusColetaDTO the entity to update.
     * @return the persisted entity.
     */
    HistoricoStatusColetaDTO update(HistoricoStatusColetaDTO historicoStatusColetaDTO);

    /**
     * Partially updates a historicoStatusColeta.
     *
     * @param historicoStatusColetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoricoStatusColetaDTO> partialUpdate(HistoricoStatusColetaDTO historicoStatusColetaDTO);

    /**
     * Get all the historicoStatusColetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoricoStatusColetaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historicoStatusColeta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoricoStatusColetaDTO> findOne(Long id);

    /**
     * Delete the "id" historicoStatusColeta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the historicoStatusColeta corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoricoStatusColetaDTO> search(String query, Pageable pageable);
}
