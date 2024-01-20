package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.repository.ContaBancariaRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContaBancariaSearchRepository;
import br.com.revenuebrasil.newcargas.service.ContaBancariaService;
import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContaBancariaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.ContaBancaria}.
 */
@Service
@Transactional
public class ContaBancariaServiceImpl implements ContaBancariaService {

    private final Logger log = LoggerFactory.getLogger(ContaBancariaServiceImpl.class);

    private final ContaBancariaRepository contaBancariaRepository;

    private final ContaBancariaMapper contaBancariaMapper;

    private final ContaBancariaSearchRepository contaBancariaSearchRepository;

    public ContaBancariaServiceImpl(
        ContaBancariaRepository contaBancariaRepository,
        ContaBancariaMapper contaBancariaMapper,
        ContaBancariaSearchRepository contaBancariaSearchRepository
    ) {
        this.contaBancariaRepository = contaBancariaRepository;
        this.contaBancariaMapper = contaBancariaMapper;
        this.contaBancariaSearchRepository = contaBancariaSearchRepository;
    }

    @Override
    public ContaBancariaDTO save(ContaBancariaDTO contaBancariaDTO) {
        log.debug("Request to save ContaBancaria : {}", contaBancariaDTO);
        ContaBancaria contaBancaria = contaBancariaMapper.toEntity(contaBancariaDTO);
        contaBancaria = contaBancariaRepository.save(contaBancaria);
        ContaBancariaDTO result = contaBancariaMapper.toDto(contaBancaria);
        contaBancariaSearchRepository.index(contaBancaria);
        return result;
    }

    @Override
    public ContaBancariaDTO update(ContaBancariaDTO contaBancariaDTO) {
        log.debug("Request to update ContaBancaria : {}", contaBancariaDTO);
        ContaBancaria contaBancaria = contaBancariaMapper.toEntity(contaBancariaDTO);
        contaBancaria = contaBancariaRepository.save(contaBancaria);
        ContaBancariaDTO result = contaBancariaMapper.toDto(contaBancaria);
        contaBancariaSearchRepository.index(contaBancaria);
        return result;
    }

    @Override
    public Optional<ContaBancariaDTO> partialUpdate(ContaBancariaDTO contaBancariaDTO) {
        log.debug("Request to partially update ContaBancaria : {}", contaBancariaDTO);

        return contaBancariaRepository
            .findById(contaBancariaDTO.getId())
            .map(existingContaBancaria -> {
                contaBancariaMapper.partialUpdate(existingContaBancaria, contaBancariaDTO);

                return existingContaBancaria;
            })
            .map(contaBancariaRepository::save)
            .map(savedContaBancaria -> {
                contaBancariaSearchRepository.index(savedContaBancaria);
                return savedContaBancaria;
            })
            .map(contaBancariaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContaBancariaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContaBancarias");
        return contaBancariaRepository.findAll(pageable).map(contaBancariaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContaBancariaDTO> findOne(Long id) {
        log.debug("Request to get ContaBancaria : {}", id);
        return contaBancariaRepository.findById(id).map(contaBancariaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContaBancaria : {}", id);
        contaBancariaRepository.deleteById(id);
        contaBancariaSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContaBancariaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContaBancarias for query {}", query);
        return contaBancariaSearchRepository.search(query, pageable).map(contaBancariaMapper::toDto);
    }
}
