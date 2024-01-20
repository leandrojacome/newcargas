package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.TransportadoraRepository;
import br.com.revenuebrasil.newcargas.repository.search.TransportadoraSearchRepository;
import br.com.revenuebrasil.newcargas.service.TransportadoraService;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TransportadoraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Transportadora}.
 */
@Service
@Transactional
public class TransportadoraServiceImpl implements TransportadoraService {

    private final Logger log = LoggerFactory.getLogger(TransportadoraServiceImpl.class);

    private final TransportadoraRepository transportadoraRepository;

    private final TransportadoraMapper transportadoraMapper;

    private final TransportadoraSearchRepository transportadoraSearchRepository;

    public TransportadoraServiceImpl(
        TransportadoraRepository transportadoraRepository,
        TransportadoraMapper transportadoraMapper,
        TransportadoraSearchRepository transportadoraSearchRepository
    ) {
        this.transportadoraRepository = transportadoraRepository;
        this.transportadoraMapper = transportadoraMapper;
        this.transportadoraSearchRepository = transportadoraSearchRepository;
    }

    @Override
    public TransportadoraDTO save(TransportadoraDTO transportadoraDTO) {
        log.debug("Request to save Transportadora : {}", transportadoraDTO);
        Transportadora transportadora = transportadoraMapper.toEntity(transportadoraDTO);
        transportadora = transportadoraRepository.save(transportadora);
        TransportadoraDTO result = transportadoraMapper.toDto(transportadora);
        transportadoraSearchRepository.index(transportadora);
        return result;
    }

    @Override
    public TransportadoraDTO update(TransportadoraDTO transportadoraDTO) {
        log.debug("Request to update Transportadora : {}", transportadoraDTO);
        Transportadora transportadora = transportadoraMapper.toEntity(transportadoraDTO);
        transportadora = transportadoraRepository.save(transportadora);
        TransportadoraDTO result = transportadoraMapper.toDto(transportadora);
        transportadoraSearchRepository.index(transportadora);
        return result;
    }

    @Override
    public Optional<TransportadoraDTO> partialUpdate(TransportadoraDTO transportadoraDTO) {
        log.debug("Request to partially update Transportadora : {}", transportadoraDTO);

        return transportadoraRepository
            .findById(transportadoraDTO.getId())
            .map(existingTransportadora -> {
                transportadoraMapper.partialUpdate(existingTransportadora, transportadoraDTO);

                return existingTransportadora;
            })
            .map(transportadoraRepository::save)
            .map(savedTransportadora -> {
                transportadoraSearchRepository.index(savedTransportadora);
                return savedTransportadora;
            })
            .map(transportadoraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportadoraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transportadoras");
        return transportadoraRepository.findAll(pageable).map(transportadoraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransportadoraDTO> findOne(Long id) {
        log.debug("Request to get Transportadora : {}", id);
        return transportadoraRepository.findById(id).map(transportadoraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transportadora : {}", id);
        transportadoraRepository.deleteById(id);
        transportadoraSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportadoraDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Transportadoras for query {}", query);
        return transportadoraSearchRepository.search(query, pageable).map(transportadoraMapper::toDto);
    }
}
