package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.repository.NotaFiscalColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.NotaFiscalColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.NotaFiscalColetaService;
import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.NotaFiscalColetaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta}.
 */
@Service
@Transactional
public class NotaFiscalColetaServiceImpl implements NotaFiscalColetaService {

    private final Logger log = LoggerFactory.getLogger(NotaFiscalColetaServiceImpl.class);

    private final NotaFiscalColetaRepository notaFiscalColetaRepository;

    private final NotaFiscalColetaMapper notaFiscalColetaMapper;

    private final NotaFiscalColetaSearchRepository notaFiscalColetaSearchRepository;

    public NotaFiscalColetaServiceImpl(
        NotaFiscalColetaRepository notaFiscalColetaRepository,
        NotaFiscalColetaMapper notaFiscalColetaMapper,
        NotaFiscalColetaSearchRepository notaFiscalColetaSearchRepository
    ) {
        this.notaFiscalColetaRepository = notaFiscalColetaRepository;
        this.notaFiscalColetaMapper = notaFiscalColetaMapper;
        this.notaFiscalColetaSearchRepository = notaFiscalColetaSearchRepository;
    }

    @Override
    public NotaFiscalColetaDTO save(NotaFiscalColetaDTO notaFiscalColetaDTO) {
        log.debug("Request to save NotaFiscalColeta : {}", notaFiscalColetaDTO);
        NotaFiscalColeta notaFiscalColeta = notaFiscalColetaMapper.toEntity(notaFiscalColetaDTO);
        notaFiscalColeta = notaFiscalColetaRepository.save(notaFiscalColeta);
        NotaFiscalColetaDTO result = notaFiscalColetaMapper.toDto(notaFiscalColeta);
        notaFiscalColetaSearchRepository.index(notaFiscalColeta);
        return result;
    }

    @Override
    public NotaFiscalColetaDTO update(NotaFiscalColetaDTO notaFiscalColetaDTO) {
        log.debug("Request to update NotaFiscalColeta : {}", notaFiscalColetaDTO);
        NotaFiscalColeta notaFiscalColeta = notaFiscalColetaMapper.toEntity(notaFiscalColetaDTO);
        notaFiscalColeta = notaFiscalColetaRepository.save(notaFiscalColeta);
        NotaFiscalColetaDTO result = notaFiscalColetaMapper.toDto(notaFiscalColeta);
        notaFiscalColetaSearchRepository.index(notaFiscalColeta);
        return result;
    }

    @Override
    public Optional<NotaFiscalColetaDTO> partialUpdate(NotaFiscalColetaDTO notaFiscalColetaDTO) {
        log.debug("Request to partially update NotaFiscalColeta : {}", notaFiscalColetaDTO);

        return notaFiscalColetaRepository
            .findById(notaFiscalColetaDTO.getId())
            .map(existingNotaFiscalColeta -> {
                notaFiscalColetaMapper.partialUpdate(existingNotaFiscalColeta, notaFiscalColetaDTO);

                return existingNotaFiscalColeta;
            })
            .map(notaFiscalColetaRepository::save)
            .map(savedNotaFiscalColeta -> {
                notaFiscalColetaSearchRepository.index(savedNotaFiscalColeta);
                return savedNotaFiscalColeta;
            })
            .map(notaFiscalColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotaFiscalColetaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NotaFiscalColetas");
        return notaFiscalColetaRepository.findAll(pageable).map(notaFiscalColetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotaFiscalColetaDTO> findOne(Long id) {
        log.debug("Request to get NotaFiscalColeta : {}", id);
        return notaFiscalColetaRepository.findById(id).map(notaFiscalColetaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NotaFiscalColeta : {}", id);
        notaFiscalColetaRepository.deleteById(id);
        notaFiscalColetaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotaFiscalColetaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NotaFiscalColetas for query {}", query);
        return notaFiscalColetaSearchRepository.search(query, pageable).map(notaFiscalColetaMapper::toDto);
    }
}
