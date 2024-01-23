package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.repository.HistoricoStatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.HistoricoStatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.HistoricoStatusColetaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.HistoricoStatusColetaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HistoricoStatusColeta} entities in the database.
 * The main input is a {@link HistoricoStatusColetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoricoStatusColetaDTO} or a {@link Page} of {@link HistoricoStatusColetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoricoStatusColetaQueryService extends QueryService<HistoricoStatusColeta> {

    private final Logger log = LoggerFactory.getLogger(HistoricoStatusColetaQueryService.class);

    private final HistoricoStatusColetaRepository historicoStatusColetaRepository;

    private final HistoricoStatusColetaMapper historicoStatusColetaMapper;

    private final HistoricoStatusColetaSearchRepository historicoStatusColetaSearchRepository;

    public HistoricoStatusColetaQueryService(
        HistoricoStatusColetaRepository historicoStatusColetaRepository,
        HistoricoStatusColetaMapper historicoStatusColetaMapper,
        HistoricoStatusColetaSearchRepository historicoStatusColetaSearchRepository
    ) {
        this.historicoStatusColetaRepository = historicoStatusColetaRepository;
        this.historicoStatusColetaMapper = historicoStatusColetaMapper;
        this.historicoStatusColetaSearchRepository = historicoStatusColetaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HistoricoStatusColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoricoStatusColetaDTO> findByCriteria(HistoricoStatusColetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoricoStatusColeta> specification = createSpecification(criteria);
        return historicoStatusColetaMapper.toDto(historicoStatusColetaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HistoricoStatusColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoricoStatusColetaDTO> findByCriteria(HistoricoStatusColetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoricoStatusColeta> specification = createSpecification(criteria);
        return historicoStatusColetaRepository.findAll(specification, page).map(historicoStatusColetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoricoStatusColetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoricoStatusColeta> specification = createSpecification(criteria);
        return historicoStatusColetaRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoricoStatusColetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoricoStatusColeta> createSpecification(HistoricoStatusColetaCriteria criteria) {
        Specification<HistoricoStatusColeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoricoStatusColeta_.id));
            }
            if (criteria.getDataCriacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCriacao(), HistoricoStatusColeta_.dataCriacao));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), HistoricoStatusColeta_.observacao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HistoricoStatusColeta_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HistoricoStatusColeta_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HistoricoStatusColeta_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HistoricoStatusColeta_.lastModifiedDate));
            }
            if (criteria.getSolicitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoColetaId(),
                            root -> root.join(HistoricoStatusColeta_.solicitacaoColeta, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
            if (criteria.getRoteirizacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoteirizacaoId(),
                            root -> root.join(HistoricoStatusColeta_.roteirizacao, JoinType.LEFT).get(Roteirizacao_.id)
                        )
                    );
            }
            if (criteria.getStatusColetaOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusColetaOrigemId(),
                            root -> root.join(HistoricoStatusColeta_.statusColetaOrigem, JoinType.LEFT).get(StatusColeta_.id)
                        )
                    );
            }
            if (criteria.getStatusColetaDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusColetaDestinoId(),
                            root -> root.join(HistoricoStatusColeta_.statusColetaDestino, JoinType.LEFT).get(StatusColeta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
