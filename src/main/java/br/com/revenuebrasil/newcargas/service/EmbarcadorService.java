package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Embarcador}.
 */
public interface EmbarcadorService {
    /**
     * Save a embarcador.
     *
     * @param embarcadorDTO the entity to save.
     * @return the persisted entity.
     */
    EmbarcadorDTO save(EmbarcadorDTO embarcadorDTO);

    /**
     * Updates a embarcador.
     *
     * @param embarcadorDTO the entity to update.
     * @return the persisted entity.
     */
    EmbarcadorDTO update(EmbarcadorDTO embarcadorDTO);

    /**
     * Partially updates a embarcador.
     *
     * @param embarcadorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmbarcadorDTO> partialUpdate(EmbarcadorDTO embarcadorDTO);

    /**
     * Get all the embarcadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmbarcadorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" embarcador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmbarcadorDTO> findOne(Long id);

    /**
     * Delete the "id" embarcador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the embarcador corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmbarcadorDTO> search(String query, Pageable pageable);
}
