package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.repository.TomadaPrecoRepository;
import br.com.revenuebrasil.newcargas.repository.search.TomadaPrecoSearchRepository;
import br.com.revenuebrasil.newcargas.service.TomadaPrecoService;
import br.com.revenuebrasil.newcargas.service.dto.TomadaPrecoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TomadaPrecoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.TomadaPreco}.
 */
@Service
@Transactional
public class TomadaPrecoServiceImpl implements TomadaPrecoService {

    private final Logger log = LoggerFactory.getLogger(TomadaPrecoServiceImpl.class);

    private final TomadaPrecoRepository tomadaPrecoRepository;

    private final TomadaPrecoMapper tomadaPrecoMapper;

    private final TomadaPrecoSearchRepository tomadaPrecoSearchRepository;

    public TomadaPrecoServiceImpl(
        TomadaPrecoRepository tomadaPrecoRepository,
        TomadaPrecoMapper tomadaPrecoMapper,
        TomadaPrecoSearchRepository tomadaPrecoSearchRepository
    ) {
        this.tomadaPrecoRepository = tomadaPrecoRepository;
        this.tomadaPrecoMapper = tomadaPrecoMapper;
        this.tomadaPrecoSearchRepository = tomadaPrecoSearchRepository;
    }

    @Override
    public TomadaPrecoDTO save(TomadaPrecoDTO tomadaPrecoDTO) {
        log.debug("Request to save TomadaPreco : {}", tomadaPrecoDTO);
        TomadaPreco tomadaPreco = tomadaPrecoMapper.toEntity(tomadaPrecoDTO);
        tomadaPreco = tomadaPrecoRepository.save(tomadaPreco);
        TomadaPrecoDTO result = tomadaPrecoMapper.toDto(tomadaPreco);
        tomadaPrecoSearchRepository.index(tomadaPreco);
        return result;
    }

    @Override
    public TomadaPrecoDTO update(TomadaPrecoDTO tomadaPrecoDTO) {
        log.debug("Request to update TomadaPreco : {}", tomadaPrecoDTO);
        TomadaPreco tomadaPreco = tomadaPrecoMapper.toEntity(tomadaPrecoDTO);
        tomadaPreco = tomadaPrecoRepository.save(tomadaPreco);
        TomadaPrecoDTO result = tomadaPrecoMapper.toDto(tomadaPreco);
        tomadaPrecoSearchRepository.index(tomadaPreco);
        return result;
    }

    @Override
    public Optional<TomadaPrecoDTO> partialUpdate(TomadaPrecoDTO tomadaPrecoDTO) {
        log.debug("Request to partially update TomadaPreco : {}", tomadaPrecoDTO);

        return tomadaPrecoRepository
            .findById(tomadaPrecoDTO.getId())
            .map(existingTomadaPreco -> {
                tomadaPrecoMapper.partialUpdate(existingTomadaPreco, tomadaPrecoDTO);

                return existingTomadaPreco;
            })
            .map(tomadaPrecoRepository::save)
            .map(savedTomadaPreco -> {
                tomadaPrecoSearchRepository.index(savedTomadaPreco);
                return savedTomadaPreco;
            })
            .map(tomadaPrecoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TomadaPrecoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TomadaPrecos");
        return tomadaPrecoRepository.findAll(pageable).map(tomadaPrecoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TomadaPrecoDTO> findOne(Long id) {
        log.debug("Request to get TomadaPreco : {}", id);
        return tomadaPrecoRepository.findById(id).map(tomadaPrecoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TomadaPreco : {}", id);
        tomadaPrecoRepository.deleteById(id);
        tomadaPrecoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TomadaPrecoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TomadaPrecos for query {}", query);
        return tomadaPrecoSearchRepository.search(query, pageable).map(tomadaPrecoMapper::toDto);
    }
}
