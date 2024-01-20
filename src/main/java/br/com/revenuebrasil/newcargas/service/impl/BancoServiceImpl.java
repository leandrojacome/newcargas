package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Banco;
import br.com.revenuebrasil.newcargas.repository.BancoRepository;
import br.com.revenuebrasil.newcargas.repository.search.BancoSearchRepository;
import br.com.revenuebrasil.newcargas.service.BancoService;
import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.BancoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Banco}.
 */
@Service
@Transactional
public class BancoServiceImpl implements BancoService {

    private final Logger log = LoggerFactory.getLogger(BancoServiceImpl.class);

    private final BancoRepository bancoRepository;

    private final BancoMapper bancoMapper;

    private final BancoSearchRepository bancoSearchRepository;

    public BancoServiceImpl(BancoRepository bancoRepository, BancoMapper bancoMapper, BancoSearchRepository bancoSearchRepository) {
        this.bancoRepository = bancoRepository;
        this.bancoMapper = bancoMapper;
        this.bancoSearchRepository = bancoSearchRepository;
    }

    @Override
    public BancoDTO save(BancoDTO bancoDTO) {
        log.debug("Request to save Banco : {}", bancoDTO);
        Banco banco = bancoMapper.toEntity(bancoDTO);
        banco = bancoRepository.save(banco);
        BancoDTO result = bancoMapper.toDto(banco);
        bancoSearchRepository.index(banco);
        return result;
    }

    @Override
    public BancoDTO update(BancoDTO bancoDTO) {
        log.debug("Request to update Banco : {}", bancoDTO);
        Banco banco = bancoMapper.toEntity(bancoDTO);
        banco = bancoRepository.save(banco);
        BancoDTO result = bancoMapper.toDto(banco);
        bancoSearchRepository.index(banco);
        return result;
    }

    @Override
    public Optional<BancoDTO> partialUpdate(BancoDTO bancoDTO) {
        log.debug("Request to partially update Banco : {}", bancoDTO);

        return bancoRepository
            .findById(bancoDTO.getId())
            .map(existingBanco -> {
                bancoMapper.partialUpdate(existingBanco, bancoDTO);

                return existingBanco;
            })
            .map(bancoRepository::save)
            .map(savedBanco -> {
                bancoSearchRepository.index(savedBanco);
                return savedBanco;
            })
            .map(bancoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BancoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll(pageable).map(bancoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BancoDTO> findOne(Long id) {
        log.debug("Request to get Banco : {}", id);
        return bancoRepository.findById(id).map(bancoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Banco : {}", id);
        bancoRepository.deleteById(id);
        bancoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BancoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bancos for query {}", query);
        return bancoSearchRepository.search(query, pageable).map(bancoMapper::toDto);
    }
}
