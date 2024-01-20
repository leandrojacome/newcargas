package br.com.revenuebrasil.newcargas.service.impl;

import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.repository.RegiaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.RegiaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.RegiaoService;
import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.RegiaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.revenuebrasil.newcargas.domain.Regiao}.
 */
@Service
@Transactional
public class RegiaoServiceImpl implements RegiaoService {

    private final Logger log = LoggerFactory.getLogger(RegiaoServiceImpl.class);

    private final RegiaoRepository regiaoRepository;

    private final RegiaoMapper regiaoMapper;

    private final RegiaoSearchRepository regiaoSearchRepository;

    public RegiaoServiceImpl(RegiaoRepository regiaoRepository, RegiaoMapper regiaoMapper, RegiaoSearchRepository regiaoSearchRepository) {
        this.regiaoRepository = regiaoRepository;
        this.regiaoMapper = regiaoMapper;
        this.regiaoSearchRepository = regiaoSearchRepository;
    }

    @Override
    public RegiaoDTO save(RegiaoDTO regiaoDTO) {
        log.debug("Request to save Regiao : {}", regiaoDTO);
        Regiao regiao = regiaoMapper.toEntity(regiaoDTO);
        regiao = regiaoRepository.save(regiao);
        RegiaoDTO result = regiaoMapper.toDto(regiao);
        regiaoSearchRepository.index(regiao);
        return result;
    }

    @Override
    public RegiaoDTO update(RegiaoDTO regiaoDTO) {
        log.debug("Request to update Regiao : {}", regiaoDTO);
        Regiao regiao = regiaoMapper.toEntity(regiaoDTO);
        regiao = regiaoRepository.save(regiao);
        RegiaoDTO result = regiaoMapper.toDto(regiao);
        regiaoSearchRepository.index(regiao);
        return result;
    }

    @Override
    public Optional<RegiaoDTO> partialUpdate(RegiaoDTO regiaoDTO) {
        log.debug("Request to partially update Regiao : {}", regiaoDTO);

        return regiaoRepository
            .findById(regiaoDTO.getId())
            .map(existingRegiao -> {
                regiaoMapper.partialUpdate(existingRegiao, regiaoDTO);

                return existingRegiao;
            })
            .map(regiaoRepository::save)
            .map(savedRegiao -> {
                regiaoSearchRepository.index(savedRegiao);
                return savedRegiao;
            })
            .map(regiaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegiaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regiaos");
        return regiaoRepository.findAll(pageable).map(regiaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegiaoDTO> findOne(Long id) {
        log.debug("Request to get Regiao : {}", id);
        return regiaoRepository.findById(id).map(regiaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Regiao : {}", id);
        regiaoRepository.deleteById(id);
        regiaoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegiaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Regiaos for query {}", query);
        return regiaoSearchRepository.search(query, pageable).map(regiaoMapper::toDto);
    }
}
