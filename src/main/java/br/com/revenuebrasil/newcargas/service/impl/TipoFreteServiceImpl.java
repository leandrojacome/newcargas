package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.repository.TipoFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.TipoFreteService;
import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoFreteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.TipoFrete}.
 */
@Service
@Transactional
public class TipoFreteServiceImpl implements TipoFreteService {

    private final Logger log = LoggerFactory.getLogger(TipoFreteServiceImpl.class);

    private final TipoFreteRepository tipoFreteRepository;

    private final TipoFreteMapper tipoFreteMapper;

    private final TipoFreteSearchRepository tipoFreteSearchRepository;

    public TipoFreteServiceImpl(
        TipoFreteRepository tipoFreteRepository,
        TipoFreteMapper tipoFreteMapper,
        TipoFreteSearchRepository tipoFreteSearchRepository
    ) {
        this.tipoFreteRepository = tipoFreteRepository;
        this.tipoFreteMapper = tipoFreteMapper;
        this.tipoFreteSearchRepository = tipoFreteSearchRepository;
    }

    @Override
    public TipoFreteDTO save(TipoFreteDTO tipoFreteDTO) {
        log.debug("Request to save TipoFrete : {}", tipoFreteDTO);
        TipoFrete tipoFrete = tipoFreteMapper.toEntity(tipoFreteDTO);
        tipoFrete = tipoFreteRepository.save(tipoFrete);
        TipoFreteDTO result = tipoFreteMapper.toDto(tipoFrete);
        tipoFreteSearchRepository.index(tipoFrete);
        return result;
    }

    @Override
    public TipoFreteDTO update(TipoFreteDTO tipoFreteDTO) {
        log.debug("Request to update TipoFrete : {}", tipoFreteDTO);
        TipoFrete tipoFrete = tipoFreteMapper.toEntity(tipoFreteDTO);
        tipoFrete = tipoFreteRepository.save(tipoFrete);
        TipoFreteDTO result = tipoFreteMapper.toDto(tipoFrete);
        tipoFreteSearchRepository.index(tipoFrete);
        return result;
    }

    @Override
    public Optional<TipoFreteDTO> partialUpdate(TipoFreteDTO tipoFreteDTO) {
        log.debug("Request to partially update TipoFrete : {}", tipoFreteDTO);

        return tipoFreteRepository
            .findById(tipoFreteDTO.getId())
            .map(existingTipoFrete -> {
                tipoFreteMapper.partialUpdate(existingTipoFrete, tipoFreteDTO);

                return existingTipoFrete;
            })
            .map(tipoFreteRepository::save)
            .map(savedTipoFrete -> {
                tipoFreteSearchRepository.index(savedTipoFrete);
                return savedTipoFrete;
            })
            .map(tipoFreteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoFreteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoFretes");
        return tipoFreteRepository.findAll(pageable).map(tipoFreteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoFreteDTO> findOne(Long id) {
        log.debug("Request to get TipoFrete : {}", id);
        return tipoFreteRepository.findById(id).map(tipoFreteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoFrete : {}", id);
        tipoFreteRepository.deleteById(id);
        tipoFreteSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoFreteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoFretes for query {}", query);
        return tipoFreteSearchRepository.search(query, pageable).map(tipoFreteMapper::toDto);
    }
}
