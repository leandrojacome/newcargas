package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.repository.EmbarcadorRepository;
import br.com.revenuebrasil.newcargas.repository.search.EmbarcadorSearchRepository;
import br.com.revenuebrasil.newcargas.service.EmbarcadorService;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EmbarcadorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Embarcador}.
 */
@Service
@Transactional
public class EmbarcadorServiceImpl implements EmbarcadorService {

    private final Logger log = LoggerFactory.getLogger(EmbarcadorServiceImpl.class);

    private final EmbarcadorRepository embarcadorRepository;

    private final EmbarcadorMapper embarcadorMapper;

    private final EmbarcadorSearchRepository embarcadorSearchRepository;

    public EmbarcadorServiceImpl(
        EmbarcadorRepository embarcadorRepository,
        EmbarcadorMapper embarcadorMapper,
        EmbarcadorSearchRepository embarcadorSearchRepository
    ) {
        this.embarcadorRepository = embarcadorRepository;
        this.embarcadorMapper = embarcadorMapper;
        this.embarcadorSearchRepository = embarcadorSearchRepository;
    }

    @Override
    public EmbarcadorDTO save(EmbarcadorDTO embarcadorDTO) {
        log.debug("Request to save Embarcador : {}", embarcadorDTO);
        Embarcador embarcador = embarcadorMapper.toEntity(embarcadorDTO);
        embarcador = embarcadorRepository.save(embarcador);
        EmbarcadorDTO result = embarcadorMapper.toDto(embarcador);
        embarcadorSearchRepository.index(embarcador);
        return result;
    }

    @Override
    public EmbarcadorDTO update(EmbarcadorDTO embarcadorDTO) {
        log.debug("Request to update Embarcador : {}", embarcadorDTO);
        Embarcador embarcador = embarcadorMapper.toEntity(embarcadorDTO);
        embarcador = embarcadorRepository.save(embarcador);
        EmbarcadorDTO result = embarcadorMapper.toDto(embarcador);
        embarcadorSearchRepository.index(embarcador);
        return result;
    }

    @Override
    public Optional<EmbarcadorDTO> partialUpdate(EmbarcadorDTO embarcadorDTO) {
        log.debug("Request to partially update Embarcador : {}", embarcadorDTO);

        return embarcadorRepository
            .findById(embarcadorDTO.getId())
            .map(existingEmbarcador -> {
                embarcadorMapper.partialUpdate(existingEmbarcador, embarcadorDTO);

                return existingEmbarcador;
            })
            .map(embarcadorRepository::save)
            .map(savedEmbarcador -> {
                embarcadorSearchRepository.index(savedEmbarcador);
                return savedEmbarcador;
            })
            .map(embarcadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmbarcadorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Embarcadors");
        return embarcadorRepository.findAll(pageable).map(embarcadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmbarcadorDTO> findOne(Long id) {
        log.debug("Request to get Embarcador : {}", id);
        return embarcadorRepository.findById(id).map(embarcadorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Embarcador : {}", id);
        embarcadorRepository.deleteById(id);
        embarcadorSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmbarcadorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Embarcadors for query {}", query);
        return embarcadorSearchRepository.search(query, pageable).map(embarcadorMapper::toDto);
    }
}
