package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.EnderecoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Endereco}.
 */
public interface EnderecoService {
    /**
     * Save a endereco.
     *
     * @param enderecoDTO the entity to save.
     * @return the persisted entity.
     */
    EnderecoDTO save(EnderecoDTO enderecoDTO);

    /**
     * Updates a endereco.
     *
     * @param enderecoDTO the entity to update.
     * @return the persisted entity.
     */
    EnderecoDTO update(EnderecoDTO enderecoDTO);

    /**
     * Partially updates a endereco.
     *
     * @param enderecoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnderecoDTO> partialUpdate(EnderecoDTO enderecoDTO);

    /**
     * Get all the enderecos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnderecoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" endereco.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnderecoDTO> findOne(Long id);

    /**
     * Delete the "id" endereco.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the endereco corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnderecoDTO> search(String query, Pageable pageable);
}
