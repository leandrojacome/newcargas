package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.repository.CidadeRepository;
import br.com.revenuebrasil.newcargas.repository.search.CidadeSearchRepository;
import br.com.revenuebrasil.newcargas.service.CidadeService;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.mapper.CidadeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Cidade}.
 */
@Service
@Transactional
public class CidadeServiceImpl implements CidadeService {

    private final Logger log = LoggerFactory.getLogger(CidadeServiceImpl.class);

    private final CidadeRepository cidadeRepository;

    private final CidadeMapper cidadeMapper;

    private final CidadeSearchRepository cidadeSearchRepository;

    public CidadeServiceImpl(CidadeRepository cidadeRepository, CidadeMapper cidadeMapper, CidadeSearchRepository cidadeSearchRepository) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
        this.cidadeSearchRepository = cidadeSearchRepository;
    }

    @Override
    public CidadeDTO save(CidadeDTO cidadeDTO) {
        log.debug("Request to save Cidade : {}", cidadeDTO);
        Cidade cidade = cidadeMapper.toEntity(cidadeDTO);
        cidade = cidadeRepository.save(cidade);
        CidadeDTO result = cidadeMapper.toDto(cidade);
        cidadeSearchRepository.index(cidade);
        return result;
    }

    @Override
    public CidadeDTO update(CidadeDTO cidadeDTO) {
        log.debug("Request to update Cidade : {}", cidadeDTO);
        Cidade cidade = cidadeMapper.toEntity(cidadeDTO);
        cidade = cidadeRepository.save(cidade);
        CidadeDTO result = cidadeMapper.toDto(cidade);
        cidadeSearchRepository.index(cidade);
        return result;
    }

    @Override
    public Optional<CidadeDTO> partialUpdate(CidadeDTO cidadeDTO) {
        log.debug("Request to partially update Cidade : {}", cidadeDTO);

        return cidadeRepository
            .findById(cidadeDTO.getId())
            .map(existingCidade -> {
                cidadeMapper.partialUpdate(existingCidade, cidadeDTO);

                return existingCidade;
            })
            .map(cidadeRepository::save)
            .map(savedCidade -> {
                cidadeSearchRepository.index(savedCidade);
                return savedCidade;
            })
            .map(cidadeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CidadeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cidades");
        return cidadeRepository.findAll(pageable).map(cidadeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CidadeDTO> findOne(Long id) {
        log.debug("Request to get Cidade : {}", id);
        return cidadeRepository.findById(id).map(cidadeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cidade : {}", id);
        cidadeRepository.deleteById(id);
        cidadeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CidadeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cidades for query {}", query);
        return cidadeSearchRepository.search(query, pageable).map(cidadeMapper::toDto);
    }
}
