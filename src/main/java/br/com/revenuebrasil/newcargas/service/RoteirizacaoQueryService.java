package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.repository.RoteirizacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.RoteirizacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.RoteirizacaoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.RoteirizacaoMapper;
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
 * Service for executing complex queries for {@link Roteirizacao} entities in the database.
 * The main input is a {@link RoteirizacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RoteirizacaoDTO} or a {@link Page} of {@link RoteirizacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoteirizacaoQueryService extends QueryService<Roteirizacao> {

    private final Logger log = LoggerFactory.getLogger(RoteirizacaoQueryService.class);

    private final RoteirizacaoRepository roteirizacaoRepository;

    private final RoteirizacaoMapper roteirizacaoMapper;

    private final RoteirizacaoSearchRepository roteirizacaoSearchRepository;

    public RoteirizacaoQueryService(
        RoteirizacaoRepository roteirizacaoRepository,
        RoteirizacaoMapper roteirizacaoMapper,
        RoteirizacaoSearchRepository roteirizacaoSearchRepository
    ) {
        this.roteirizacaoRepository = roteirizacaoRepository;
        this.roteirizacaoMapper = roteirizacaoMapper;
        this.roteirizacaoSearchRepository = roteirizacaoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RoteirizacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RoteirizacaoDTO> findByCriteria(RoteirizacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Roteirizacao> specification = createSpecification(criteria);
        return roteirizacaoMapper.toDto(roteirizacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RoteirizacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoteirizacaoDTO> findByCriteria(RoteirizacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Roteirizacao> specification = createSpecification(criteria);
        return roteirizacaoRepository.findAll(specification, page).map(roteirizacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoteirizacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Roteirizacao> specification = createSpecification(criteria);
        return roteirizacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link RoteirizacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Roteirizacao> createSpecification(RoteirizacaoCriteria criteria) {
        Specification<Roteirizacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Roteirizacao_.id));
            }
            if (criteria.getDataHoraPrimeiraColeta() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataHoraPrimeiraColeta(), Roteirizacao_.dataHoraPrimeiraColeta));
            }
            if (criteria.getDataHoraUltimaColeta() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataHoraUltimaColeta(), Roteirizacao_.dataHoraUltimaColeta));
            }
            if (criteria.getDataHoraPrimeiraEntrega() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDataHoraPrimeiraEntrega(), Roteirizacao_.dataHoraPrimeiraEntrega)
                    );
            }
            if (criteria.getDataHoraUltimaEntrega() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataHoraUltimaEntrega(), Roteirizacao_.dataHoraUltimaEntrega));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), Roteirizacao_.valorTotal));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Roteirizacao_.observacao));
            }
            if (criteria.getCancelado() != null) {
                specification = specification.and(buildSpecification(criteria.getCancelado(), Roteirizacao_.cancelado));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), Roteirizacao_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Roteirizacao_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Roteirizacao_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Roteirizacao_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Roteirizacao_.lastModifiedDate));
            }
            if (criteria.getHistoricoStatusColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoricoStatusColetaId(),
                            root -> root.join(Roteirizacao_.historicoStatusColetas, JoinType.LEFT).get(HistoricoStatusColeta_.id)
                        )
                    );
            }
            if (criteria.getSolitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolitacaoColetaId(),
                            root -> root.join(Roteirizacao_.solitacaoColetas, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
            if (criteria.getTomadaPrecoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTomadaPrecoId(),
                            root -> root.join(Roteirizacao_.tomadaPrecos, JoinType.LEFT).get(TomadaPreco_.id)
                        )
                    );
            }
            if (criteria.getStatusColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusColetaId(),
                            root -> root.join(Roteirizacao_.statusColeta, JoinType.LEFT).get(StatusColeta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
