package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Contratacao}.
 */
public interface ContratacaoService {
    /**
     * Save a contratacao.
     *
     * @param contratacaoDTO the entity to save.
     * @return the persisted entity.
     */
    ContratacaoDTO save(ContratacaoDTO contratacaoDTO);

    /**
     * Updates a contratacao.
     *
     * @param contratacaoDTO the entity to update.
     * @return the persisted entity.
     */
    ContratacaoDTO update(ContratacaoDTO contratacaoDTO);

    /**
     * Partially updates a contratacao.
     *
     * @param contratacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContratacaoDTO> partialUpdate(ContratacaoDTO contratacaoDTO);

    /**
     * Get all the contratacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContratacaoDTO> findAll(Pageable pageable);

    /**
     * Get all the ContratacaoDTO where SolicitacaoColeta is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ContratacaoDTO> findAllWhereSolicitacaoColetaIsNull();

    /**
     * Get the "id" contratacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContratacaoDTO> findOne(Long id);

    /**
     * Delete the "id" contratacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contratacao corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContratacaoDTO> search(String query, Pageable pageable);
}
