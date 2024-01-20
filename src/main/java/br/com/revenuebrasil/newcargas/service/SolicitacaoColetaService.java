package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta}.
 */
public interface SolicitacaoColetaService {
    /**
     * Save a solicitacaoColeta.
     *
     * @param solicitacaoColetaDTO the entity to save.
     * @return the persisted entity.
     */
    SolicitacaoColetaDTO save(SolicitacaoColetaDTO solicitacaoColetaDTO);

    /**
     * Updates a solicitacaoColeta.
     *
     * @param solicitacaoColetaDTO the entity to update.
     * @return the persisted entity.
     */
    SolicitacaoColetaDTO update(SolicitacaoColetaDTO solicitacaoColetaDTO);

    /**
     * Partially updates a solicitacaoColeta.
     *
     * @param solicitacaoColetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SolicitacaoColetaDTO> partialUpdate(SolicitacaoColetaDTO solicitacaoColetaDTO);

    /**
     * Get all the solicitacaoColetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolicitacaoColetaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" solicitacaoColeta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SolicitacaoColetaDTO> findOne(Long id);

    /**
     * Delete the "id" solicitacaoColeta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the solicitacaoColeta corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolicitacaoColetaDTO> search(String query, Pageable pageable);
}
