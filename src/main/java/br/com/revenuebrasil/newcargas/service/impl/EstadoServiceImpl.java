package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Estado;
import br.com.revenuebrasil.newcargas.repository.EstadoRepository;
import br.com.revenuebrasil.newcargas.repository.search.EstadoSearchRepository;
import br.com.revenuebrasil.newcargas.service.EstadoService;
import br.com.revenuebrasil.newcargas.service.dto.EstadoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EstadoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Estado}.
 */
@Service
@Transactional
public class EstadoServiceImpl implements EstadoService {

    private final Logger log = LoggerFactory.getLogger(EstadoServiceImpl.class);

    private final EstadoRepository estadoRepository;

    private final EstadoMapper estadoMapper;

    private final EstadoSearchRepository estadoSearchRepository;

    public EstadoServiceImpl(EstadoRepository estadoRepository, EstadoMapper estadoMapper, EstadoSearchRepository estadoSearchRepository) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
        this.estadoSearchRepository = estadoSearchRepository;
    }

    @Override
    public EstadoDTO save(EstadoDTO estadoDTO) {
        log.debug("Request to save Estado : {}", estadoDTO);
        Estado estado = estadoMapper.toEntity(estadoDTO);
        estado = estadoRepository.save(estado);
        EstadoDTO result = estadoMapper.toDto(estado);
        estadoSearchRepository.index(estado);
        return result;
    }

    @Override
    public EstadoDTO update(EstadoDTO estadoDTO) {
        log.debug("Request to update Estado : {}", estadoDTO);
        Estado estado = estadoMapper.toEntity(estadoDTO);
        estado = estadoRepository.save(estado);
        EstadoDTO result = estadoMapper.toDto(estado);
        estadoSearchRepository.index(estado);
        return result;
    }

    @Override
    public Optional<EstadoDTO> partialUpdate(EstadoDTO estadoDTO) {
        log.debug("Request to partially update Estado : {}", estadoDTO);

        return estadoRepository
            .findById(estadoDTO.getId())
            .map(existingEstado -> {
                estadoMapper.partialUpdate(existingEstado, estadoDTO);

                return existingEstado;
            })
            .map(estadoRepository::save)
            .map(savedEstado -> {
                estadoSearchRepository.index(savedEstado);
                return savedEstado;
            })
            .map(estadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstadoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estados");
        return estadoRepository.findAll(pageable).map(estadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoDTO> findOne(Long id) {
        log.debug("Request to get Estado : {}", id);
        return estadoRepository.findById(id).map(estadoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estado : {}", id);
        estadoRepository.deleteById(id);
        estadoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstadoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Estados for query {}", query);
        return estadoSearchRepository.search(query, pageable).map(estadoMapper::toDto);
    }
}
