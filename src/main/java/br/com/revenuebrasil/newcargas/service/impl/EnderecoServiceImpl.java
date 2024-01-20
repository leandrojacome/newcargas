package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.repository.EnderecoRepository;
import br.com.revenuebrasil.newcargas.repository.search.EnderecoSearchRepository;
import br.com.revenuebrasil.newcargas.service.EnderecoService;
import br.com.revenuebrasil.newcargas.service.dto.EnderecoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EnderecoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Endereco}.
 */
@Service
@Transactional
public class EnderecoServiceImpl implements EnderecoService {

    private final Logger log = LoggerFactory.getLogger(EnderecoServiceImpl.class);

    private final EnderecoRepository enderecoRepository;

    private final EnderecoMapper enderecoMapper;

    private final EnderecoSearchRepository enderecoSearchRepository;

    public EnderecoServiceImpl(
        EnderecoRepository enderecoRepository,
        EnderecoMapper enderecoMapper,
        EnderecoSearchRepository enderecoSearchRepository
    ) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.enderecoSearchRepository = enderecoSearchRepository;
    }

    @Override
    public EnderecoDTO save(EnderecoDTO enderecoDTO) {
        log.debug("Request to save Endereco : {}", enderecoDTO);
        Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        EnderecoDTO result = enderecoMapper.toDto(endereco);
        enderecoSearchRepository.index(endereco);
        return result;
    }

    @Override
    public EnderecoDTO update(EnderecoDTO enderecoDTO) {
        log.debug("Request to update Endereco : {}", enderecoDTO);
        Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        EnderecoDTO result = enderecoMapper.toDto(endereco);
        enderecoSearchRepository.index(endereco);
        return result;
    }

    @Override
    public Optional<EnderecoDTO> partialUpdate(EnderecoDTO enderecoDTO) {
        log.debug("Request to partially update Endereco : {}", enderecoDTO);

        return enderecoRepository
            .findById(enderecoDTO.getId())
            .map(existingEndereco -> {
                enderecoMapper.partialUpdate(existingEndereco, enderecoDTO);

                return existingEndereco;
            })
            .map(enderecoRepository::save)
            .map(savedEndereco -> {
                enderecoSearchRepository.index(savedEndereco);
                return savedEndereco;
            })
            .map(enderecoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnderecoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enderecos");
        return enderecoRepository.findAll(pageable).map(enderecoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoDTO> findOne(Long id) {
        log.debug("Request to get Endereco : {}", id);
        return enderecoRepository.findById(id).map(enderecoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Endereco : {}", id);
        enderecoRepository.deleteById(id);
        enderecoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnderecoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Enderecos for query {}", query);
        return enderecoSearchRepository.search(query, pageable).map(enderecoMapper::toDto);
    }
}
