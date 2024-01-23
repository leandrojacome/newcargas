package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.repository.TipoVeiculoRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoVeiculoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.TipoVeiculoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoVeiculoMapper;
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
 * Service for executing complex queries for {@link TipoVeiculo} entities in the database.
 * The main input is a {@link TipoVeiculoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoVeiculoDTO} or a {@link Page} of {@link TipoVeiculoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoVeiculoQueryService extends QueryService<TipoVeiculo> {

    private final Logger log = LoggerFactory.getLogger(TipoVeiculoQueryService.class);

    private final TipoVeiculoRepository tipoVeiculoRepository;

    private final TipoVeiculoMapper tipoVeiculoMapper;

    private final TipoVeiculoSearchRepository tipoVeiculoSearchRepository;

    public TipoVeiculoQueryService(
        TipoVeiculoRepository tipoVeiculoRepository,
        TipoVeiculoMapper tipoVeiculoMapper,
        TipoVeiculoSearchRepository tipoVeiculoSearchRepository
    ) {
        this.tipoVeiculoRepository = tipoVeiculoRepository;
        this.tipoVeiculoMapper = tipoVeiculoMapper;
        this.tipoVeiculoSearchRepository = tipoVeiculoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TipoVeiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoVeiculoDTO> findByCriteria(TipoVeiculoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TipoVeiculo> specification = createSpecification(criteria);
        return tipoVeiculoMapper.toDto(tipoVeiculoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TipoVeiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoVeiculoDTO> findByCriteria(TipoVeiculoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoVeiculo> specification = createSpecification(criteria);
        return tipoVeiculoRepository.findAll(specification, page).map(tipoVeiculoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoVeiculoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TipoVeiculo> specification = createSpecification(criteria);
        return tipoVeiculoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoVeiculoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoVeiculo> createSpecification(TipoVeiculoCriteria criteria) {
        Specification<TipoVeiculo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TipoVeiculo_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), TipoVeiculo_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), TipoVeiculo_.descricao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), TipoVeiculo_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TipoVeiculo_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), TipoVeiculo_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TipoVeiculo_.lastModifiedDate));
            }
            if (criteria.getSolitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolitacaoColetaId(),
                            root -> root.join(TipoVeiculo_.solitacaoColetas, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
