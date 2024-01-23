package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.repository.TipoCargaRepository;
import br.com.revenuebrasil.newcargas.repository.search.TipoCargaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.TipoCargaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TipoCargaMapper;
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
 * Service for executing complex queries for {@link TipoCarga} entities in the database.
 * The main input is a {@link TipoCargaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoCargaDTO} or a {@link Page} of {@link TipoCargaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoCargaQueryService extends QueryService<TipoCarga> {

    private final Logger log = LoggerFactory.getLogger(TipoCargaQueryService.class);

    private final TipoCargaRepository tipoCargaRepository;

    private final TipoCargaMapper tipoCargaMapper;

    private final TipoCargaSearchRepository tipoCargaSearchRepository;

    public TipoCargaQueryService(
        TipoCargaRepository tipoCargaRepository,
        TipoCargaMapper tipoCargaMapper,
        TipoCargaSearchRepository tipoCargaSearchRepository
    ) {
        this.tipoCargaRepository = tipoCargaRepository;
        this.tipoCargaMapper = tipoCargaMapper;
        this.tipoCargaSearchRepository = tipoCargaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TipoCargaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoCargaDTO> findByCriteria(TipoCargaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TipoCarga> specification = createSpecification(criteria);
        return tipoCargaMapper.toDto(tipoCargaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TipoCargaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoCargaDTO> findByCriteria(TipoCargaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoCarga> specification = createSpecification(criteria);
        return tipoCargaRepository.findAll(specification, page).map(tipoCargaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoCargaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TipoCarga> specification = createSpecification(criteria);
        return tipoCargaRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoCargaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoCarga> createSpecification(TipoCargaCriteria criteria) {
        Specification<TipoCarga> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TipoCarga_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), TipoCarga_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), TipoCarga_.descricao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), TipoCarga_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TipoCarga_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), TipoCarga_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TipoCarga_.lastModifiedDate));
            }
            if (criteria.getTabelaFreteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteId(),
                            root -> root.join(TipoCarga_.tabelaFretes, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
