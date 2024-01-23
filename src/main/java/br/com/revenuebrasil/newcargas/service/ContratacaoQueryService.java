package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.repository.ContratacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContratacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.ContratacaoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContratacaoMapper;
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
 * Service for executing complex queries for {@link Contratacao} entities in the database.
 * The main input is a {@link ContratacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContratacaoDTO} or a {@link Page} of {@link ContratacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContratacaoQueryService extends QueryService<Contratacao> {

    private final Logger log = LoggerFactory.getLogger(ContratacaoQueryService.class);

    private final ContratacaoRepository contratacaoRepository;

    private final ContratacaoMapper contratacaoMapper;

    private final ContratacaoSearchRepository contratacaoSearchRepository;

    public ContratacaoQueryService(
        ContratacaoRepository contratacaoRepository,
        ContratacaoMapper contratacaoMapper,
        ContratacaoSearchRepository contratacaoSearchRepository
    ) {
        this.contratacaoRepository = contratacaoRepository;
        this.contratacaoMapper = contratacaoMapper;
        this.contratacaoSearchRepository = contratacaoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContratacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContratacaoDTO> findByCriteria(ContratacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contratacao> specification = createSpecification(criteria);
        return contratacaoMapper.toDto(contratacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContratacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContratacaoDTO> findByCriteria(ContratacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contratacao> specification = createSpecification(criteria);
        return contratacaoRepository.findAll(specification, page).map(contratacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContratacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Contratacao> specification = createSpecification(criteria);
        return contratacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link ContratacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contratacao> createSpecification(ContratacaoCriteria criteria) {
        Specification<Contratacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Contratacao_.id));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), Contratacao_.valorTotal));
            }
            if (criteria.getValidadeEmDias() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidadeEmDias(), Contratacao_.validadeEmDias));
            }
            if (criteria.getDataValidade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataValidade(), Contratacao_.dataValidade));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Contratacao_.observacao));
            }
            if (criteria.getCancelado() != null) {
                specification = specification.and(buildSpecification(criteria.getCancelado(), Contratacao_.cancelado));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), Contratacao_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Contratacao_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Contratacao_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Contratacao_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Contratacao_.lastModifiedDate));
            }
            if (criteria.getFaturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFaturaId(), root -> root.join(Contratacao_.faturas, JoinType.LEFT).get(Fatura_.id))
                    );
            }
            if (criteria.getSolicitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoColetaId(),
                            root -> root.join(Contratacao_.solicitacaoColeta, JoinType.LEFT).get(TomadaPreco_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(Contratacao_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
