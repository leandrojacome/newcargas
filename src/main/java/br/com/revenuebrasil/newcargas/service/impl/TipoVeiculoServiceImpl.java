package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.repository.TipoVeiculoRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoVeiculoSearchRepository;
import br.com.revenuebrasil.newcargas.service.TipoVeiculoService;
import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoVeiculoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.TipoVeiculo}.
 */
@Service
@Transactional
public class TipoVeiculoServiceImpl implements TipoVeiculoService {

    private final Logger log = LoggerFactory.getLogger(TipoVeiculoServiceImpl.class);

    private final TipoVeiculoRepository tipoVeiculoRepository;

    private final TipoVeiculoMapper tipoVeiculoMapper;

    private final TipoVeiculoSearchRepository tipoVeiculoSearchRepository;

    public TipoVeiculoServiceImpl(
        TipoVeiculoRepository tipoVeiculoRepository,
        TipoVeiculoMapper tipoVeiculoMapper,
        TipoVeiculoSearchRepository tipoVeiculoSearchRepository
    ) {
        this.tipoVeiculoRepository = tipoVeiculoRepository;
        this.tipoVeiculoMapper = tipoVeiculoMapper;
        this.tipoVeiculoSearchRepository = tipoVeiculoSearchRepository;
    }

    @Override
    public TipoVeiculoDTO save(TipoVeiculoDTO tipoVeiculoDTO) {
        log.debug("Request to save TipoVeiculo : {}", tipoVeiculoDTO);
        TipoVeiculo tipoVeiculo = tipoVeiculoMapper.toEntity(tipoVeiculoDTO);
        tipoVeiculo = tipoVeiculoRepository.save(tipoVeiculo);
        TipoVeiculoDTO result = tipoVeiculoMapper.toDto(tipoVeiculo);
        tipoVeiculoSearchRepository.index(tipoVeiculo);
        return result;
    }

    @Override
    public TipoVeiculoDTO update(TipoVeiculoDTO tipoVeiculoDTO) {
        log.debug("Request to update TipoVeiculo : {}", tipoVeiculoDTO);
        TipoVeiculo tipoVeiculo = tipoVeiculoMapper.toEntity(tipoVeiculoDTO);
        tipoVeiculo = tipoVeiculoRepository.save(tipoVeiculo);
        TipoVeiculoDTO result = tipoVeiculoMapper.toDto(tipoVeiculo);
        tipoVeiculoSearchRepository.index(tipoVeiculo);
        return result;
    }

    @Override
    public Optional<TipoVeiculoDTO> partialUpdate(TipoVeiculoDTO tipoVeiculoDTO) {
        log.debug("Request to partially update TipoVeiculo : {}", tipoVeiculoDTO);

        return tipoVeiculoRepository
            .findById(tipoVeiculoDTO.getId())
            .map(existingTipoVeiculo -> {
                tipoVeiculoMapper.partialUpdate(existingTipoVeiculo, tipoVeiculoDTO);

                return existingTipoVeiculo;
            })
            .map(tipoVeiculoRepository::save)
            .map(savedTipoVeiculo -> {
                tipoVeiculoSearchRepository.index(savedTipoVeiculo);
                return savedTipoVeiculo;
            })
            .map(tipoVeiculoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoVeiculoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoVeiculos");
        return tipoVeiculoRepository.findAll(pageable).map(tipoVeiculoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoVeiculoDTO> findOne(Long id) {
        log.debug("Request to get TipoVeiculo : {}", id);
        return tipoVeiculoRepository.findById(id).map(tipoVeiculoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoVeiculo : {}", id);
        tipoVeiculoRepository.deleteById(id);
        tipoVeiculoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoVeiculoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoVeiculos for query {}", query);
        return tipoVeiculoSearchRepository.search(query, pageable).map(tipoVeiculoMapper::toDto);
    }
}
