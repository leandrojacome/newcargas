package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta}.
 */
public interface NotaFiscalColetaService {
    /**
     * Save a notaFiscalColeta.
     *
     * @param notaFiscalColetaDTO the entity to save.
     * @return the persisted entity.
     */
    NotaFiscalColetaDTO save(NotaFiscalColetaDTO notaFiscalColetaDTO);

    /**
     * Updates a notaFiscalColeta.
     *
     * @param notaFiscalColetaDTO the entity to update.
     * @return the persisted entity.
     */
    NotaFiscalColetaDTO update(NotaFiscalColetaDTO notaFiscalColetaDTO);

    /**
     * Partially updates a notaFiscalColeta.
     *
     * @param notaFiscalColetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotaFiscalColetaDTO> partialUpdate(NotaFiscalColetaDTO notaFiscalColetaDTO);

    /**
     * Get all the notaFiscalColetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotaFiscalColetaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notaFiscalColeta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotaFiscalColetaDTO> findOne(Long id);

    /**
     * Delete the "id" notaFiscalColeta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the notaFiscalColeta corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotaFiscalColetaDTO> search(String query, Pageable pageable);
}
