package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.repository.NotificacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.NotificacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.NotificacaoService;
import br.com.revenuebrasil.newcargas.service.dto.NotificacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.NotificacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Notificacao}.
 */
@Service
@Transactional
public class NotificacaoServiceImpl implements NotificacaoService {

    private final Logger log = LoggerFactory.getLogger(NotificacaoServiceImpl.class);

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoMapper notificacaoMapper;

    private final NotificacaoSearchRepository notificacaoSearchRepository;

    public NotificacaoServiceImpl(
        NotificacaoRepository notificacaoRepository,
        NotificacaoMapper notificacaoMapper,
        NotificacaoSearchRepository notificacaoSearchRepository
    ) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoMapper = notificacaoMapper;
        this.notificacaoSearchRepository = notificacaoSearchRepository;
    }

    @Override
    public NotificacaoDTO save(NotificacaoDTO notificacaoDTO) {
        log.debug("Request to save Notificacao : {}", notificacaoDTO);
        Notificacao notificacao = notificacaoMapper.toEntity(notificacaoDTO);
        notificacao = notificacaoRepository.save(notificacao);
        NotificacaoDTO result = notificacaoMapper.toDto(notificacao);
        notificacaoSearchRepository.index(notificacao);
        return result;
    }

    @Override
    public NotificacaoDTO update(NotificacaoDTO notificacaoDTO) {
        log.debug("Request to update Notificacao : {}", notificacaoDTO);
        Notificacao notificacao = notificacaoMapper.toEntity(notificacaoDTO);
        notificacao = notificacaoRepository.save(notificacao);
        NotificacaoDTO result = notificacaoMapper.toDto(notificacao);
        notificacaoSearchRepository.index(notificacao);
        return result;
    }

    @Override
    public Optional<NotificacaoDTO> partialUpdate(NotificacaoDTO notificacaoDTO) {
        log.debug("Request to partially update Notificacao : {}", notificacaoDTO);

        return notificacaoRepository
            .findById(notificacaoDTO.getId())
            .map(existingNotificacao -> {
                notificacaoMapper.partialUpdate(existingNotificacao, notificacaoDTO);

                return existingNotificacao;
            })
            .map(notificacaoRepository::save)
            .map(savedNotificacao -> {
                notificacaoSearchRepository.index(savedNotificacao);
                return savedNotificacao;
            })
            .map(notificacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notificacaos");
        return notificacaoRepository.findAll(pageable).map(notificacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificacaoDTO> findOne(Long id) {
        log.debug("Request to get Notificacao : {}", id);
        return notificacaoRepository.findById(id).map(notificacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notificacao : {}", id);
        notificacaoRepository.deleteById(id);
        notificacaoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificacaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notificacaos for query {}", query);
        return notificacaoSearchRepository.search(query, pageable).map(notificacaoMapper::toDto);
    }
}
