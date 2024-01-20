package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.TipoVeiculo}.
 */
public interface TipoVeiculoService {
    /**
     * Save a tipoVeiculo.
     *
     * @param tipoVeiculoDTO the entity to save.
     * @return the persisted entity.
     */
    TipoVeiculoDTO save(TipoVeiculoDTO tipoVeiculoDTO);

    /**
     * Updates a tipoVeiculo.
     *
     * @param tipoVeiculoDTO the entity to update.
     * @return the persisted entity.
     */
    TipoVeiculoDTO update(TipoVeiculoDTO tipoVeiculoDTO);

    /**
     * Partially updates a tipoVeiculo.
     *
     * @param tipoVeiculoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoVeiculoDTO> partialUpdate(TipoVeiculoDTO tipoVeiculoDTO);

    /**
     * Get all the tipoVeiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoVeiculoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tipoVeiculo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoVeiculoDTO> findOne(Long id);

    /**
     * Delete the "id" tipoVeiculo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tipoVeiculo corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoVeiculoDTO> search(String query, Pageable pageable);
}
