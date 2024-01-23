package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.repository.TipoFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.TipoFreteCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoFreteMapper;
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
 * Service for executing complex queries for {@link TipoFrete} entities in the database.
 * The main input is a {@link TipoFreteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoFreteDTO} or a {@link Page} of {@link TipoFreteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoFreteQueryService extends QueryService<TipoFrete> {

    private final Logger log = LoggerFactory.getLogger(TipoFreteQueryService.class);

    private final TipoFreteRepository tipoFreteRepository;

    private final TipoFreteMapper tipoFreteMapper;

    private final TipoFreteSearchRepository tipoFreteSearchRepository;

    public TipoFreteQueryService(
        TipoFreteRepository tipoFreteRepository,
        TipoFreteMapper tipoFreteMapper,
        TipoFreteSearchRepository tipoFreteSearchRepository
    ) {
        this.tipoFreteRepository = tipoFreteRepository;
        this.tipoFreteMapper = tipoFreteMapper;
        this.tipoFreteSearchRepository = tipoFreteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TipoFreteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoFreteDTO> findByCriteria(TipoFreteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TipoFrete> specification = createSpecification(criteria);
        return tipoFreteMapper.toDto(tipoFreteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TipoFreteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoFreteDTO> findByCriteria(TipoFreteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoFrete> specification = createSpecification(criteria);
        return tipoFreteRepository.findAll(specification, page).map(tipoFreteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoFreteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TipoFrete> specification = createSpecification(criteria);
        return tipoFreteRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoFreteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoFrete> createSpecification(TipoFreteCriteria criteria) {
        Specification<TipoFrete> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TipoFrete_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), TipoFrete_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), TipoFrete_.descricao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), TipoFrete_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TipoFrete_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), TipoFrete_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TipoFrete_.lastModifiedDate));
            }
            if (criteria.getTabelaFreteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteId(),
                            root -> root.join(TipoFrete_.tabelaFretes, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
