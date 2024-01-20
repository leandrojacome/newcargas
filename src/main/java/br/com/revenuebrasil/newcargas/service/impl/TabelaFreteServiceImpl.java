package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.repository.TabelaFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TabelaFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.TabelaFreteService;
import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TabelaFreteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.TabelaFrete}.
 */
@Service
@Transactional
public class TabelaFreteServiceImpl implements TabelaFreteService {

    private final Logger log = LoggerFactory.getLogger(TabelaFreteServiceImpl.class);

    private final TabelaFreteRepository tabelaFreteRepository;

    private final TabelaFreteMapper tabelaFreteMapper;

    private final TabelaFreteSearchRepository tabelaFreteSearchRepository;

    public TabelaFreteServiceImpl(
        TabelaFreteRepository tabelaFreteRepository,
        TabelaFreteMapper tabelaFreteMapper,
        TabelaFreteSearchRepository tabelaFreteSearchRepository
    ) {
        this.tabelaFreteRepository = tabelaFreteRepository;
        this.tabelaFreteMapper = tabelaFreteMapper;
        this.tabelaFreteSearchRepository = tabelaFreteSearchRepository;
    }

    @Override
    public TabelaFreteDTO save(TabelaFreteDTO tabelaFreteDTO) {
        log.debug("Request to save TabelaFrete : {}", tabelaFreteDTO);
        TabelaFrete tabelaFrete = tabelaFreteMapper.toEntity(tabelaFreteDTO);
        tabelaFrete = tabelaFreteRepository.save(tabelaFrete);
        TabelaFreteDTO result = tabelaFreteMapper.toDto(tabelaFrete);
        tabelaFreteSearchRepository.index(tabelaFrete);
        return result;
    }

    @Override
    public TabelaFreteDTO update(TabelaFreteDTO tabelaFreteDTO) {
        log.debug("Request to update TabelaFrete : {}", tabelaFreteDTO);
        TabelaFrete tabelaFrete = tabelaFreteMapper.toEntity(tabelaFreteDTO);
        tabelaFrete = tabelaFreteRepository.save(tabelaFrete);
        TabelaFreteDTO result = tabelaFreteMapper.toDto(tabelaFrete);
        tabelaFreteSearchRepository.index(tabelaFrete);
        return result;
    }

    @Override
    public Optional<TabelaFreteDTO> partialUpdate(TabelaFreteDTO tabelaFreteDTO) {
        log.debug("Request to partially update TabelaFrete : {}", tabelaFreteDTO);

        return tabelaFreteRepository
            .findById(tabelaFreteDTO.getId())
            .map(existingTabelaFrete -> {
                tabelaFreteMapper.partialUpdate(existingTabelaFrete, tabelaFreteDTO);

                return existingTabelaFrete;
            })
            .map(tabelaFreteRepository::save)
            .map(savedTabelaFrete -> {
                tabelaFreteSearchRepository.index(savedTabelaFrete);
                return savedTabelaFrete;
            })
            .map(tabelaFreteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TabelaFreteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TabelaFretes");
        return tabelaFreteRepository.findAll(pageable).map(tabelaFreteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TabelaFreteDTO> findOne(Long id) {
        log.debug("Request to get TabelaFrete : {}", id);
        return tabelaFreteRepository.findById(id).map(tabelaFreteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TabelaFrete : {}", id);
        tabelaFreteRepository.deleteById(id);
        tabelaFreteSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TabelaFreteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TabelaFretes for query {}", query);
        return tabelaFreteSearchRepository.search(query, pageable).map(tabelaFreteMapper::toDto);
    }
}
