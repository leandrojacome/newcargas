package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.repository.FaturaRepository;
import br.com.revenuebrasil.newcargas.repository.search.FaturaSearchRepository;
import br.com.revenuebrasil.newcargas.service.FaturaService;
import br.com.revenuebrasil.newcargas.service.dto.FaturaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.FaturaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Fatura}.
 */
@Service
@Transactional
public class FaturaServiceImpl implements FaturaService {

    private final Logger log = LoggerFactory.getLogger(FaturaServiceImpl.class);

    private final FaturaRepository faturaRepository;

    private final FaturaMapper faturaMapper;

    private final FaturaSearchRepository faturaSearchRepository;

    public FaturaServiceImpl(FaturaRepository faturaRepository, FaturaMapper faturaMapper, FaturaSearchRepository faturaSearchRepository) {
        this.faturaRepository = faturaRepository;
        this.faturaMapper = faturaMapper;
        this.faturaSearchRepository = faturaSearchRepository;
    }

    @Override
    public FaturaDTO save(FaturaDTO faturaDTO) {
        log.debug("Request to save Fatura : {}", faturaDTO);
        Fatura fatura = faturaMapper.toEntity(faturaDTO);
        fatura = faturaRepository.save(fatura);
        FaturaDTO result = faturaMapper.toDto(fatura);
        faturaSearchRepository.index(fatura);
        return result;
    }

    @Override
    public FaturaDTO update(FaturaDTO faturaDTO) {
        log.debug("Request to update Fatura : {}", faturaDTO);
        Fatura fatura = faturaMapper.toEntity(faturaDTO);
        fatura = faturaRepository.save(fatura);
        FaturaDTO result = faturaMapper.toDto(fatura);
        faturaSearchRepository.index(fatura);
        return result;
    }

    @Override
    public Optional<FaturaDTO> partialUpdate(FaturaDTO faturaDTO) {
        log.debug("Request to partially update Fatura : {}", faturaDTO);

        return faturaRepository
            .findById(faturaDTO.getId())
            .map(existingFatura -> {
                faturaMapper.partialUpdate(existingFatura, faturaDTO);

                return existingFatura;
            })
            .map(faturaRepository::save)
            .map(savedFatura -> {
                faturaSearchRepository.index(savedFatura);
                return savedFatura;
            })
            .map(faturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FaturaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Faturas");
        return faturaRepository.findAll(pageable).map(faturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FaturaDTO> findOne(Long id) {
        log.debug("Request to get Fatura : {}", id);
        return faturaRepository.findById(id).map(faturaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fatura : {}", id);
        faturaRepository.deleteById(id);
        faturaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FaturaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Faturas for query {}", query);
        return faturaSearchRepository.search(query, pageable).map(faturaMapper::toDto);
    }
}
