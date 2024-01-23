package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.repository.TomadaPrecoRepository;
import br.com.revenuebrasil.newcargas.repository.search.TomadaPrecoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.TomadaPrecoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TomadaPrecoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TomadaPrecoMapper;
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
 * Service for executing complex queries for {@link TomadaPreco} entities in the database.
 * The main input is a {@link TomadaPrecoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TomadaPrecoDTO} or a {@link Page} of {@link TomadaPrecoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TomadaPrecoQueryService extends QueryService<TomadaPreco> {

    private final Logger log = LoggerFactory.getLogger(TomadaPrecoQueryService.class);

    private final TomadaPrecoRepository tomadaPrecoRepository;

    private final TomadaPrecoMapper tomadaPrecoMapper;

    private final TomadaPrecoSearchRepository tomadaPrecoSearchRepository;

    public TomadaPrecoQueryService(
        TomadaPrecoRepository tomadaPrecoRepository,
        TomadaPrecoMapper tomadaPrecoMapper,
        TomadaPrecoSearchRepository tomadaPrecoSearchRepository
    ) {
        this.tomadaPrecoRepository = tomadaPrecoRepository;
        this.tomadaPrecoMapper = tomadaPrecoMapper;
        this.tomadaPrecoSearchRepository = tomadaPrecoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TomadaPrecoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TomadaPrecoDTO> findByCriteria(TomadaPrecoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TomadaPreco> specification = createSpecification(criteria);
        return tomadaPrecoMapper.toDto(tomadaPrecoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TomadaPrecoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TomadaPrecoDTO> findByCriteria(TomadaPrecoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TomadaPreco> specification = createSpecification(criteria);
        return tomadaPrecoRepository.findAll(specification, page).map(tomadaPrecoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TomadaPrecoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TomadaPreco> specification = createSpecification(criteria);
        return tomadaPrecoRepository.count(specification);
    }

    /**
     * Function to convert {@link TomadaPrecoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TomadaPreco> createSpecification(TomadaPrecoCriteria criteria) {
        Specification<TomadaPreco> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TomadaPreco_.id));
            }
            if (criteria.getDataHoraEnvio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHoraEnvio(), TomadaPreco_.dataHoraEnvio));
            }
            if (criteria.getPrazoResposta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrazoResposta(), TomadaPreco_.prazoResposta));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), TomadaPreco_.valorTotal));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), TomadaPreco_.observacao));
            }
            if (criteria.getAprovado() != null) {
                specification = specification.and(buildSpecification(criteria.getAprovado(), TomadaPreco_.aprovado));
            }
            if (criteria.getCancelado() != null) {
                specification = specification.and(buildSpecification(criteria.getCancelado(), TomadaPreco_.cancelado));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), TomadaPreco_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), TomadaPreco_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TomadaPreco_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), TomadaPreco_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TomadaPreco_.lastModifiedDate));
            }
            if (criteria.getContratacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContratacaoId(),
                            root -> root.join(TomadaPreco_.contratacao, JoinType.LEFT).get(Contratacao_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(TomadaPreco_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
            if (criteria.getRoteirizacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoteirizacaoId(),
                            root -> root.join(TomadaPreco_.roteirizacao, JoinType.LEFT).get(Roteirizacao_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
