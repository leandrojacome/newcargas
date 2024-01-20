package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.repository.TipoCargaRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoCargaSearchRepository;
import br.com.revenuebrasil.newcargas.service.TipoCargaService;
import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoCargaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.TipoCarga}.
 */
@Service
@Transactional
public class TipoCargaServiceImpl implements TipoCargaService {

    private final Logger log = LoggerFactory.getLogger(TipoCargaServiceImpl.class);

    private final TipoCargaRepository tipoCargaRepository;

    private final TipoCargaMapper tipoCargaMapper;

    private final TipoCargaSearchRepository tipoCargaSearchRepository;

    public TipoCargaServiceImpl(
        TipoCargaRepository tipoCargaRepository,
        TipoCargaMapper tipoCargaMapper,
        TipoCargaSearchRepository tipoCargaSearchRepository
    ) {
        this.tipoCargaRepository = tipoCargaRepository;
        this.tipoCargaMapper = tipoCargaMapper;
        this.tipoCargaSearchRepository = tipoCargaSearchRepository;
    }

    @Override
    public TipoCargaDTO save(TipoCargaDTO tipoCargaDTO) {
        log.debug("Request to save TipoCarga : {}", tipoCargaDTO);
        TipoCarga tipoCarga = tipoCargaMapper.toEntity(tipoCargaDTO);
        tipoCarga = tipoCargaRepository.save(tipoCarga);
        TipoCargaDTO result = tipoCargaMapper.toDto(tipoCarga);
        tipoCargaSearchRepository.index(tipoCarga);
        return result;
    }

    @Override
    public TipoCargaDTO update(TipoCargaDTO tipoCargaDTO) {
        log.debug("Request to update TipoCarga : {}", tipoCargaDTO);
        TipoCarga tipoCarga = tipoCargaMapper.toEntity(tipoCargaDTO);
        tipoCarga = tipoCargaRepository.save(tipoCarga);
        TipoCargaDTO result = tipoCargaMapper.toDto(tipoCarga);
        tipoCargaSearchRepository.index(tipoCarga);
        return result;
    }

    @Override
    public Optional<TipoCargaDTO> partialUpdate(TipoCargaDTO tipoCargaDTO) {
        log.debug("Request to partially update TipoCarga : {}", tipoCargaDTO);

        return tipoCargaRepository
            .findById(tipoCargaDTO.getId())
            .map(existingTipoCarga -> {
                tipoCargaMapper.partialUpdate(existingTipoCarga, tipoCargaDTO);

                return existingTipoCarga;
            })
            .map(tipoCargaRepository::save)
            .map(savedTipoCarga -> {
                tipoCargaSearchRepository.index(savedTipoCarga);
                return savedTipoCarga;
            })
            .map(tipoCargaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoCargaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoCargas");
        return tipoCargaRepository.findAll(pageable).map(tipoCargaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoCargaDTO> findOne(Long id) {
        log.debug("Request to get TipoCarga : {}", id);
        return tipoCargaRepository.findById(id).map(tipoCargaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoCarga : {}", id);
        tipoCargaRepository.deleteById(id);
        tipoCargaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoCargaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoCargas for query {}", query);
        return tipoCargaSearchRepository.search(query, pageable).map(tipoCargaMapper::toDto);
    }
}
