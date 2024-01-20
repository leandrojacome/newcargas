package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Banco}.
 */
public interface BancoService {
    /**
     * Save a banco.
     *
     * @param bancoDTO the entity to save.
     * @return the persisted entity.
     */
    BancoDTO save(BancoDTO bancoDTO);

    /**
     * Updates a banco.
     *
     * @param bancoDTO the entity to update.
     * @return the persisted entity.
     */
    BancoDTO update(BancoDTO bancoDTO);

    /**
     * Partially updates a banco.
     *
     * @param bancoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BancoDTO> partialUpdate(BancoDTO bancoDTO);

    /**
     * Get all the bancos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BancoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" banco.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BancoDTO> findOne(Long id);

    /**
     * Delete the "id" banco.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the banco corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BancoDTO> search(String query, Pageable pageable);
}
