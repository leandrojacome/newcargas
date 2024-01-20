package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.repository.FormaCobrancaRepository;
import br.com.revenuebrasil.newcargas.repository.search.FormaCobrancaSearchRepository;
import br.com.revenuebrasil.newcargas.service.FormaCobrancaService;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.FormaCobrancaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.FormaCobranca}.
 */
@Service
@Transactional
public class FormaCobrancaServiceImpl implements FormaCobrancaService {

    private final Logger log = LoggerFactory.getLogger(FormaCobrancaServiceImpl.class);

    private final FormaCobrancaRepository formaCobrancaRepository;

    private final FormaCobrancaMapper formaCobrancaMapper;

    private final FormaCobrancaSearchRepository formaCobrancaSearchRepository;

    public FormaCobrancaServiceImpl(
        FormaCobrancaRepository formaCobrancaRepository,
        FormaCobrancaMapper formaCobrancaMapper,
        FormaCobrancaSearchRepository formaCobrancaSearchRepository
    ) {
        this.formaCobrancaRepository = formaCobrancaRepository;
        this.formaCobrancaMapper = formaCobrancaMapper;
        this.formaCobrancaSearchRepository = formaCobrancaSearchRepository;
    }

    @Override
    public FormaCobrancaDTO save(FormaCobrancaDTO formaCobrancaDTO) {
        log.debug("Request to save FormaCobranca : {}", formaCobrancaDTO);
        FormaCobranca formaCobranca = formaCobrancaMapper.toEntity(formaCobrancaDTO);
        formaCobranca = formaCobrancaRepository.save(formaCobranca);
        FormaCobrancaDTO result = formaCobrancaMapper.toDto(formaCobranca);
        formaCobrancaSearchRepository.index(formaCobranca);
        return result;
    }

    @Override
    public FormaCobrancaDTO update(FormaCobrancaDTO formaCobrancaDTO) {
        log.debug("Request to update FormaCobranca : {}", formaCobrancaDTO);
        FormaCobranca formaCobranca = formaCobrancaMapper.toEntity(formaCobrancaDTO);
        formaCobranca = formaCobrancaRepository.save(formaCobranca);
        FormaCobrancaDTO result = formaCobrancaMapper.toDto(formaCobranca);
        formaCobrancaSearchRepository.index(formaCobranca);
        return result;
    }

    @Override
    public Optional<FormaCobrancaDTO> partialUpdate(FormaCobrancaDTO formaCobrancaDTO) {
        log.debug("Request to partially update FormaCobranca : {}", formaCobrancaDTO);

        return formaCobrancaRepository
            .findById(formaCobrancaDTO.getId())
            .map(existingFormaCobranca -> {
                formaCobrancaMapper.partialUpdate(existingFormaCobranca, formaCobrancaDTO);

                return existingFormaCobranca;
            })
            .map(formaCobrancaRepository::save)
            .map(savedFormaCobranca -> {
                formaCobrancaSearchRepository.index(savedFormaCobranca);
                return savedFormaCobranca;
            })
            .map(formaCobrancaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormaCobrancaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormaCobrancas");
        return formaCobrancaRepository.findAll(pageable).map(formaCobrancaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormaCobrancaDTO> findOne(Long id) {
        log.debug("Request to get FormaCobranca : {}", id);
        return formaCobrancaRepository.findById(id).map(formaCobrancaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormaCobranca : {}", id);
        formaCobrancaRepository.deleteById(id);
        formaCobrancaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormaCobrancaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FormaCobrancas for query {}", query);
        return formaCobrancaSearchRepository.search(query, pageable).map(formaCobrancaMapper::toDto);
    }
}
