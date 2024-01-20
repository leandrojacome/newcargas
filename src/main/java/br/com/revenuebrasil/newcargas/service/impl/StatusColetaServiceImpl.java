package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.repository.StatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.StatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.StatusColetaService;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.StatusColetaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.StatusColeta}.
 */
@Service
@Transactional
public class StatusColetaServiceImpl implements StatusColetaService {

    private final Logger log = LoggerFactory.getLogger(StatusColetaServiceImpl.class);

    private final StatusColetaRepository statusColetaRepository;

    private final StatusColetaMapper statusColetaMapper;

    private final StatusColetaSearchRepository statusColetaSearchRepository;

    public StatusColetaServiceImpl(
        StatusColetaRepository statusColetaRepository,
        StatusColetaMapper statusColetaMapper,
        StatusColetaSearchRepository statusColetaSearchRepository
    ) {
        this.statusColetaRepository = statusColetaRepository;
        this.statusColetaMapper = statusColetaMapper;
        this.statusColetaSearchRepository = statusColetaSearchRepository;
    }

    @Override
    public StatusColetaDTO save(StatusColetaDTO statusColetaDTO) {
        log.debug("Request to save StatusColeta : {}", statusColetaDTO);
        StatusColeta statusColeta = statusColetaMapper.toEntity(statusColetaDTO);
        statusColeta = statusColetaRepository.save(statusColeta);
        StatusColetaDTO result = statusColetaMapper.toDto(statusColeta);
        statusColetaSearchRepository.index(statusColeta);
        return result;
    }

    @Override
    public StatusColetaDTO update(StatusColetaDTO statusColetaDTO) {
        log.debug("Request to update StatusColeta : {}", statusColetaDTO);
        StatusColeta statusColeta = statusColetaMapper.toEntity(statusColetaDTO);
        statusColeta = statusColetaRepository.save(statusColeta);
        StatusColetaDTO result = statusColetaMapper.toDto(statusColeta);
        statusColetaSearchRepository.index(statusColeta);
        return result;
    }

    @Override
    public Optional<StatusColetaDTO> partialUpdate(StatusColetaDTO statusColetaDTO) {
        log.debug("Request to partially update StatusColeta : {}", statusColetaDTO);

        return statusColetaRepository
            .findById(statusColetaDTO.getId())
            .map(existingStatusColeta -> {
                statusColetaMapper.partialUpdate(existingStatusColeta, statusColetaDTO);

                return existingStatusColeta;
            })
            .map(statusColetaRepository::save)
            .map(savedStatusColeta -> {
                statusColetaSearchRepository.index(savedStatusColeta);
                return savedStatusColeta;
            })
            .map(statusColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatusColetaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StatusColetas");
        return statusColetaRepository.findAll(pageable).map(statusColetaMapper::toDto);
    }

    public Page<StatusColetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return statusColetaRepository.findAllWithEagerRelationships(pageable).map(statusColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusColetaDTO> findOne(Long id) {
        log.debug("Request to get StatusColeta : {}", id);
        return statusColetaRepository.findOneWithEagerRelationships(id).map(statusColetaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StatusColeta : {}", id);
        statusColetaRepository.deleteById(id);
        statusColetaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatusColetaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StatusColetas for query {}", query);
        return statusColetaSearchRepository.search(query, pageable).map(statusColetaMapper::toDto);
    }
}
