package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.service.dto.NotificacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.revenuebrasil.newcargas.domain.Notificacao}.
 */
public interface NotificacaoService {
    /**
     * Save a notificacao.
     *
     * @param notificacaoDTO the entity to save.
     * @return the persisted entity.
     */
    NotificacaoDTO save(NotificacaoDTO notificacaoDTO);

    /**
     * Updates a notificacao.
     *
     * @param notificacaoDTO the entity to update.
     * @return the persisted entity.
     */
    NotificacaoDTO update(NotificacaoDTO notificacaoDTO);

    /**
     * Partially updates a notificacao.
     *
     * @param notificacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificacaoDTO> partialUpdate(NotificacaoDTO notificacaoDTO);

    /**
     * Get all the notificacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notificacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificacaoDTO> findOne(Long id);

    /**
     * Delete the "id" notificacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the notificacao corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificacaoDTO> search(String query, Pageable pageable);
}
