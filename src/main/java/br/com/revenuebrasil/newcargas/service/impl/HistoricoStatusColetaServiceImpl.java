package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.repository.HistoricoStatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.HistoricoStatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.HistoricoStatusColetaService;
import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.HistoricoStatusColetaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta}.
 */
@Service
@Transactional
public class HistoricoStatusColetaServiceImpl implements HistoricoStatusColetaService {

    private final Logger log = LoggerFactory.getLogger(HistoricoStatusColetaServiceImpl.class);

    private final HistoricoStatusColetaRepository historicoStatusColetaRepository;

    private final HistoricoStatusColetaMapper historicoStatusColetaMapper;

    private final HistoricoStatusColetaSearchRepository historicoStatusColetaSearchRepository;

    public HistoricoStatusColetaServiceImpl(
        HistoricoStatusColetaRepository historicoStatusColetaRepository,
        HistoricoStatusColetaMapper historicoStatusColetaMapper,
        HistoricoStatusColetaSearchRepository historicoStatusColetaSearchRepository
    ) {
        this.historicoStatusColetaRepository = historicoStatusColetaRepository;
        this.historicoStatusColetaMapper = historicoStatusColetaMapper;
        this.historicoStatusColetaSearchRepository = historicoStatusColetaSearchRepository;
    }

    @Override
    public HistoricoStatusColetaDTO save(HistoricoStatusColetaDTO historicoStatusColetaDTO) {
        log.debug("Request to save HistoricoStatusColeta : {}", historicoStatusColetaDTO);
        HistoricoStatusColeta historicoStatusColeta = historicoStatusColetaMapper.toEntity(historicoStatusColetaDTO);
        historicoStatusColeta = historicoStatusColetaRepository.save(historicoStatusColeta);
        HistoricoStatusColetaDTO result = historicoStatusColetaMapper.toDto(historicoStatusColeta);
        historicoStatusColetaSearchRepository.index(historicoStatusColeta);
        return result;
    }

    @Override
    public HistoricoStatusColetaDTO update(HistoricoStatusColetaDTO historicoStatusColetaDTO) {
        log.debug("Request to update HistoricoStatusColeta : {}", historicoStatusColetaDTO);
        HistoricoStatusColeta historicoStatusColeta = historicoStatusColetaMapper.toEntity(historicoStatusColetaDTO);
        historicoStatusColeta = historicoStatusColetaRepository.save(historicoStatusColeta);
        HistoricoStatusColetaDTO result = historicoStatusColetaMapper.toDto(historicoStatusColeta);
        historicoStatusColetaSearchRepository.index(historicoStatusColeta);
        return result;
    }

    @Override
    public Optional<HistoricoStatusColetaDTO> partialUpdate(HistoricoStatusColetaDTO historicoStatusColetaDTO) {
        log.debug("Request to partially update HistoricoStatusColeta : {}", historicoStatusColetaDTO);

        return historicoStatusColetaRepository
            .findById(historicoStatusColetaDTO.getId())
            .map(existingHistoricoStatusColeta -> {
                historicoStatusColetaMapper.partialUpdate(existingHistoricoStatusColeta, historicoStatusColetaDTO);

                return existingHistoricoStatusColeta;
            })
            .map(historicoStatusColetaRepository::save)
            .map(savedHistoricoStatusColeta -> {
                historicoStatusColetaSearchRepository.index(savedHistoricoStatusColeta);
                return savedHistoricoStatusColeta;
            })
            .map(historicoStatusColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoricoStatusColetaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoricoStatusColetas");
        return historicoStatusColetaRepository.findAll(pageable).map(historicoStatusColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoricoStatusColetaDTO> findOne(Long id) {
        log.debug("Request to get HistoricoStatusColeta : {}", id);
        return historicoStatusColetaRepository.findById(id).map(historicoStatusColetaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HistoricoStatusColeta : {}", id);
        historicoStatusColetaRepository.deleteById(id);
        historicoStatusColetaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoricoStatusColetaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoricoStatusColetas for query {}", query);
        return historicoStatusColetaSearchRepository.search(query, pageable).map(historicoStatusColetaMapper::toDto);
    }
}
