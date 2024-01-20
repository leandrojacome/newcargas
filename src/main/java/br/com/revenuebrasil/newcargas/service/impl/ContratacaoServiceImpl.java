package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.repository.ContratacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContratacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.ContratacaoService;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContratacaoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Contratacao}.
 */
@Service
@Transactional
public class ContratacaoServiceImpl implements ContratacaoService {

    private final Logger log = LoggerFactory.getLogger(ContratacaoServiceImpl.class);

    private final ContratacaoRepository contratacaoRepository;

    private final ContratacaoMapper contratacaoMapper;

    private final ContratacaoSearchRepository contratacaoSearchRepository;

    public ContratacaoServiceImpl(
        ContratacaoRepository contratacaoRepository,
        ContratacaoMapper contratacaoMapper,
        ContratacaoSearchRepository contratacaoSearchRepository
    ) {
        this.contratacaoRepository = contratacaoRepository;
        this.contratacaoMapper = contratacaoMapper;
        this.contratacaoSearchRepository = contratacaoSearchRepository;
    }

    @Override
    public ContratacaoDTO save(ContratacaoDTO contratacaoDTO) {
        log.debug("Request to save Contratacao : {}", contratacaoDTO);
        Contratacao contratacao = contratacaoMapper.toEntity(contratacaoDTO);
        contratacao = contratacaoRepository.save(contratacao);
        ContratacaoDTO result = contratacaoMapper.toDto(contratacao);
        contratacaoSearchRepository.index(contratacao);
        return result;
    }

    @Override
    public ContratacaoDTO update(ContratacaoDTO contratacaoDTO) {
        log.debug("Request to update Contratacao : {}", contratacaoDTO);
        Contratacao contratacao = contratacaoMapper.toEntity(contratacaoDTO);
        contratacao = contratacaoRepository.save(contratacao);
        ContratacaoDTO result = contratacaoMapper.toDto(contratacao);
        contratacaoSearchRepository.index(contratacao);
        return result;
    }

    @Override
    public Optional<ContratacaoDTO> partialUpdate(ContratacaoDTO contratacaoDTO) {
        log.debug("Request to partially update Contratacao : {}", contratacaoDTO);

        return contratacaoRepository
            .findById(contratacaoDTO.getId())
            .map(existingContratacao -> {
                contratacaoMapper.partialUpdate(existingContratacao, contratacaoDTO);

                return existingContratacao;
            })
            .map(contratacaoRepository::save)
            .map(savedContratacao -> {
                contratacaoSearchRepository.index(savedContratacao);
                return savedContratacao;
            })
            .map(contratacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contratacaos");
        return contratacaoRepository.findAll(pageable).map(contratacaoMapper::toDto);
    }

    /**
     *  Get all the contratacaos where SolicitacaoColeta is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContratacaoDTO> findAllWhereSolicitacaoColetaIsNull() {
        log.debug("Request to get all contratacaos where SolicitacaoColeta is null");
        return StreamSupport
            .stream(contratacaoRepository.findAll().spliterator(), false)
            .filter(contratacao -> contratacao.getSolicitacaoColeta() == null)
            .map(contratacaoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContratacaoDTO> findOne(Long id) {
        log.debug("Request to get Contratacao : {}", id);
        return contratacaoRepository.findById(id).map(contratacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contratacao : {}", id);
        contratacaoRepository.deleteById(id);
        contratacaoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratacaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Contratacaos for query {}", query);
        return contratacaoSearchRepository.search(query, pageable).map(contratacaoMapper::toDto);
    }
}
