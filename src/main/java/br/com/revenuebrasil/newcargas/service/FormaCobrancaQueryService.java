package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.repository.FormaCobrancaRepository;
import br.com.revenuebrasil.newcargas.repository.search.FormaCobrancaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.FormaCobrancaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.FormaCobrancaMapper;
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
 * Service for executing complex queries for {@link FormaCobranca} entities in the database.
 * The main input is a {@link FormaCobrancaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormaCobrancaDTO} or a {@link Page} of {@link FormaCobrancaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormaCobrancaQueryService extends QueryService<FormaCobranca> {

    private final Logger log = LoggerFactory.getLogger(FormaCobrancaQueryService.class);

    private final FormaCobrancaRepository formaCobrancaRepository;

    private final FormaCobrancaMapper formaCobrancaMapper;

    private final FormaCobrancaSearchRepository formaCobrancaSearchRepository;

    public FormaCobrancaQueryService(
        FormaCobrancaRepository formaCobrancaRepository,
        FormaCobrancaMapper formaCobrancaMapper,
        FormaCobrancaSearchRepository formaCobrancaSearchRepository
    ) {
        this.formaCobrancaRepository = formaCobrancaRepository;
        this.formaCobrancaMapper = formaCobrancaMapper;
        this.formaCobrancaSearchRepository = formaCobrancaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FormaCobrancaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormaCobrancaDTO> findByCriteria(FormaCobrancaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FormaCobranca> specification = createSpecification(criteria);
        return formaCobrancaMapper.toDto(formaCobrancaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormaCobrancaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormaCobrancaDTO> findByCriteria(FormaCobrancaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FormaCobranca> specification = createSpecification(criteria);
        return formaCobrancaRepository.findAll(specification, page).map(formaCobrancaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormaCobrancaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FormaCobranca> specification = createSpecification(criteria);
        return formaCobrancaRepository.count(specification);
    }

    /**
     * Function to convert {@link FormaCobrancaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FormaCobranca> createSpecification(FormaCobrancaCriteria criteria) {
        Specification<FormaCobranca> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FormaCobranca_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), FormaCobranca_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), FormaCobranca_.descricao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), FormaCobranca_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), FormaCobranca_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), FormaCobranca_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), FormaCobranca_.lastModifiedDate));
            }
            if (criteria.getTabelaFreteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteId(),
                            root -> root.join(FormaCobranca_.tabelaFretes, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
            if (criteria.getFatutaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFatutaId(), root -> root.join(FormaCobranca_.fatutas, JoinType.LEFT).get(Fatura_.id))
                    );
            }
        }
        return specification;
    }
}
