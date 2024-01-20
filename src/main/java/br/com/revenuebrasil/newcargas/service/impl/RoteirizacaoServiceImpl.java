package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.repository.RoteirizacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.RoteirizacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.RoteirizacaoService;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.RoteirizacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Roteirizacao}.
 */
@Service
@Transactional
public class RoteirizacaoServiceImpl implements RoteirizacaoService {

    private final Logger log = LoggerFactory.getLogger(RoteirizacaoServiceImpl.class);

    private final RoteirizacaoRepository roteirizacaoRepository;

    private final RoteirizacaoMapper roteirizacaoMapper;

    private final RoteirizacaoSearchRepository roteirizacaoSearchRepository;

    public RoteirizacaoServiceImpl(
        RoteirizacaoRepository roteirizacaoRepository,
        RoteirizacaoMapper roteirizacaoMapper,
        RoteirizacaoSearchRepository roteirizacaoSearchRepository
    ) {
        this.roteirizacaoRepository = roteirizacaoRepository;
        this.roteirizacaoMapper = roteirizacaoMapper;
        this.roteirizacaoSearchRepository = roteirizacaoSearchRepository;
    }

    @Override
    public RoteirizacaoDTO save(RoteirizacaoDTO roteirizacaoDTO) {
        log.debug("Request to save Roteirizacao : {}", roteirizacaoDTO);
        Roteirizacao roteirizacao = roteirizacaoMapper.toEntity(roteirizacaoDTO);
        roteirizacao = roteirizacaoRepository.save(roteirizacao);
        RoteirizacaoDTO result = roteirizacaoMapper.toDto(roteirizacao);
        roteirizacaoSearchRepository.index(roteirizacao);
        return result;
    }

    @Override
    public RoteirizacaoDTO update(RoteirizacaoDTO roteirizacaoDTO) {
        log.debug("Request to update Roteirizacao : {}", roteirizacaoDTO);
        Roteirizacao roteirizacao = roteirizacaoMapper.toEntity(roteirizacaoDTO);
        roteirizacao = roteirizacaoRepository.save(roteirizacao);
        RoteirizacaoDTO result = roteirizacaoMapper.toDto(roteirizacao);
        roteirizacaoSearchRepository.index(roteirizacao);
        return result;
    }

    @Override
    public Optional<RoteirizacaoDTO> partialUpdate(RoteirizacaoDTO roteirizacaoDTO) {
        log.debug("Request to partially update Roteirizacao : {}", roteirizacaoDTO);

        return roteirizacaoRepository
            .findById(roteirizacaoDTO.getId())
            .map(existingRoteirizacao -> {
                roteirizacaoMapper.partialUpdate(existingRoteirizacao, roteirizacaoDTO);

                return existingRoteirizacao;
            })
            .map(roteirizacaoRepository::save)
            .map(savedRoteirizacao -> {
                roteirizacaoSearchRepository.index(savedRoteirizacao);
                return savedRoteirizacao;
            })
            .map(roteirizacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoteirizacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Roteirizacaos");
        return roteirizacaoRepository.findAll(pageable).map(roteirizacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoteirizacaoDTO> findOne(Long id) {
        log.debug("Request to get Roteirizacao : {}", id);
        return roteirizacaoRepository.findById(id).map(roteirizacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Roteirizacao : {}", id);
        roteirizacaoRepository.deleteById(id);
        roteirizacaoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoteirizacaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Roteirizacaos for query {}", query);
        return roteirizacaoSearchRepository.search(query, pageable).map(roteirizacaoMapper::toDto);
    }
}
