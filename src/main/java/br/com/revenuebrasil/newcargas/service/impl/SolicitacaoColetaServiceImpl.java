package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.repository.SolicitacaoColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.SolicitacaoColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.SolicitacaoColetaService;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.SolicitacaoColetaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta}.
 */
@Service
@Transactional
public class SolicitacaoColetaServiceImpl implements SolicitacaoColetaService {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoColetaServiceImpl.class);

    private final SolicitacaoColetaRepository solicitacaoColetaRepository;

    private final SolicitacaoColetaMapper solicitacaoColetaMapper;

    private final SolicitacaoColetaSearchRepository solicitacaoColetaSearchRepository;

    public SolicitacaoColetaServiceImpl(
        SolicitacaoColetaRepository solicitacaoColetaRepository,
        SolicitacaoColetaMapper solicitacaoColetaMapper,
        SolicitacaoColetaSearchRepository solicitacaoColetaSearchRepository
    ) {
        this.solicitacaoColetaRepository = solicitacaoColetaRepository;
        this.solicitacaoColetaMapper = solicitacaoColetaMapper;
        this.solicitacaoColetaSearchRepository = solicitacaoColetaSearchRepository;
    }

    @Override
    public SolicitacaoColetaDTO save(SolicitacaoColetaDTO solicitacaoColetaDTO) {
        log.debug("Request to save SolicitacaoColeta : {}", solicitacaoColetaDTO);
        SolicitacaoColeta solicitacaoColeta = solicitacaoColetaMapper.toEntity(solicitacaoColetaDTO);
        solicitacaoColeta = solicitacaoColetaRepository.save(solicitacaoColeta);
        SolicitacaoColetaDTO result = solicitacaoColetaMapper.toDto(solicitacaoColeta);
        solicitacaoColetaSearchRepository.index(solicitacaoColeta);
        return result;
    }

    @Override
    public SolicitacaoColetaDTO update(SolicitacaoColetaDTO solicitacaoColetaDTO) {
        log.debug("Request to update SolicitacaoColeta : {}", solicitacaoColetaDTO);
        SolicitacaoColeta solicitacaoColeta = solicitacaoColetaMapper.toEntity(solicitacaoColetaDTO);
        solicitacaoColeta = solicitacaoColetaRepository.save(solicitacaoColeta);
        SolicitacaoColetaDTO result = solicitacaoColetaMapper.toDto(solicitacaoColeta);
        solicitacaoColetaSearchRepository.index(solicitacaoColeta);
        return result;
    }

    @Override
    public Optional<SolicitacaoColetaDTO> partialUpdate(SolicitacaoColetaDTO solicitacaoColetaDTO) {
        log.debug("Request to partially update SolicitacaoColeta : {}", solicitacaoColetaDTO);

        return solicitacaoColetaRepository
            .findById(solicitacaoColetaDTO.getId())
            .map(existingSolicitacaoColeta -> {
                solicitacaoColetaMapper.partialUpdate(existingSolicitacaoColeta, solicitacaoColetaDTO);

                return existingSolicitacaoColeta;
            })
            .map(solicitacaoColetaRepository::save)
            .map(savedSolicitacaoColeta -> {
                solicitacaoColetaSearchRepository.index(savedSolicitacaoColeta);
                return savedSolicitacaoColeta;
            })
            .map(solicitacaoColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoColetaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SolicitacaoColetas");
        return solicitacaoColetaRepository.findAll(pageable).map(solicitacaoColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitacaoColetaDTO> findOne(Long id) {
        log.debug("Request to get SolicitacaoColeta : {}", id);
        return solicitacaoColetaRepository.findById(id).map(solicitacaoColetaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SolicitacaoColeta : {}", id);
        solicitacaoColetaRepository.deleteById(id);
        solicitacaoColetaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoColetaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SolicitacaoColetas for query {}", query);
        return solicitacaoColetaSearchRepository.search(query, pageable).map(solicitacaoColetaMapper::toDto);
    }
}
