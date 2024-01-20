package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Regiao}.
 */
public interface RegiaoService {
    /**
     * Save a regiao.
     *
     * @param regiaoDTO the entity to save.
     * @return the persisted entity.
     */
    RegiaoDTO save(RegiaoDTO regiaoDTO);

    /**
     * Updates a regiao.
     *
     * @param regiaoDTO the entity to update.
     * @return the persisted entity.
     */
    RegiaoDTO update(RegiaoDTO regiaoDTO);

    /**
     * Partially updates a regiao.
     *
     * @param regiaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RegiaoDTO> partialUpdate(RegiaoDTO regiaoDTO);

    /**
     * Get all the regiaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegiaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" regiao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RegiaoDTO> findOne(Long id);

    /**
     * Delete the "id" regiao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the regiao corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegiaoDTO> search(String query, Pageable pageable);
}
