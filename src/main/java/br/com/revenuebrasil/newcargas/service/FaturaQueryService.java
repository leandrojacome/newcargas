package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.repository.FaturaRepository;
import br.com.revenuebrasil.newcargas.repository.search.FaturaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.FaturaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.FaturaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.FaturaMapper;
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
 * Service for executing complex queries for {@link Fatura} entities in the database.
 * The main input is a {@link FaturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FaturaDTO} or a {@link Page} of {@link FaturaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FaturaQueryService extends QueryService<Fatura> {

    private final Logger log = LoggerFactory.getLogger(FaturaQueryService.class);

    private final FaturaRepository faturaRepository;

    private final FaturaMapper faturaMapper;

    private final FaturaSearchRepository faturaSearchRepository;

    public FaturaQueryService(FaturaRepository faturaRepository, FaturaMapper faturaMapper, FaturaSearchRepository faturaSearchRepository) {
        this.faturaRepository = faturaRepository;
        this.faturaMapper = faturaMapper;
        this.faturaSearchRepository = faturaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FaturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FaturaDTO> findByCriteria(FaturaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fatura> specification = createSpecification(criteria);
        return faturaMapper.toDto(faturaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FaturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FaturaDTO> findByCriteria(FaturaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fatura> specification = createSpecification(criteria);
        return faturaRepository.findAll(specification, page).map(faturaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FaturaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fatura> specification = createSpecification(criteria);
        return faturaRepository.count(specification);
    }

    /**
     * Function to convert {@link FaturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fatura> createSpecification(FaturaCriteria criteria) {
        Specification<Fatura> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fatura_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Fatura_.tipo));
            }
            if (criteria.getDataFatura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataFatura(), Fatura_.dataFatura));
            }
            if (criteria.getDataVencimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataVencimento(), Fatura_.dataVencimento));
            }
            if (criteria.getDataPagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPagamento(), Fatura_.dataPagamento));
            }
            if (criteria.getNumeroParcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumeroParcela(), Fatura_.numeroParcela));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), Fatura_.valorTotal));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Fatura_.observacao));
            }
            if (criteria.getCancelado() != null) {
                specification = specification.and(buildSpecification(criteria.getCancelado(), Fatura_.cancelado));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), Fatura_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Fatura_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Fatura_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Fatura_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Fatura_.lastModifiedDate));
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(Fatura_.embarcador, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(Fatura_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
            if (criteria.getContratacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContratacaoId(),
                            root -> root.join(Fatura_.contratacao, JoinType.LEFT).get(Contratacao_.id)
                        )
                    );
            }
            if (criteria.getFormaCobrancaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFormaCobrancaId(),
                            root -> root.join(Fatura_.formaCobranca, JoinType.LEFT).get(FormaCobranca_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
