package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.TipoCarga}.
 */
public interface TipoCargaService {
    /**
     * Save a tipoCarga.
     *
     * @param tipoCargaDTO the entity to save.
     * @return the persisted entity.
     */
    TipoCargaDTO save(TipoCargaDTO tipoCargaDTO);

    /**
     * Updates a tipoCarga.
     *
     * @param tipoCargaDTO the entity to update.
     * @return the persisted entity.
     */
    TipoCargaDTO update(TipoCargaDTO tipoCargaDTO);

    /**
     * Partially updates a tipoCarga.
     *
     * @param tipoCargaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoCargaDTO> partialUpdate(TipoCargaDTO tipoCargaDTO);

    /**
     * Get all the tipoCargas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoCargaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tipoCarga.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoCargaDTO> findOne(Long id);

    /**
     * Delete the "id" tipoCarga.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tipoCarga corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoCargaDTO> search(String query, Pageable pageable);
}
