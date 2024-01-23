package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.repository.CidadeRepository;
import br.com.revenuebrasil.newcargas.repository.search.CidadeSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.CidadeCriteria;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.mapper.CidadeMapper;
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
 * Service for executing complex queries for {@link Cidade} entities in the database.
 * The main input is a {@link CidadeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CidadeDTO} or a {@link Page} of {@link CidadeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CidadeQueryService extends QueryService<Cidade> {

    private final Logger log = LoggerFactory.getLogger(CidadeQueryService.class);

    private final CidadeRepository cidadeRepository;

    private final CidadeMapper cidadeMapper;

    private final CidadeSearchRepository cidadeSearchRepository;

    public CidadeQueryService(CidadeRepository cidadeRepository, CidadeMapper cidadeMapper, CidadeSearchRepository cidadeSearchRepository) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
        this.cidadeSearchRepository = cidadeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CidadeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CidadeDTO> findByCriteria(CidadeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cidade> specification = createSpecification(criteria);
        return cidadeMapper.toDto(cidadeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CidadeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CidadeDTO> findByCriteria(CidadeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cidade> specification = createSpecification(criteria);
        return cidadeRepository.findAll(specification, page).map(cidadeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CidadeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cidade> specification = createSpecification(criteria);
        return cidadeRepository.count(specification);
    }

    /**
     * Function to convert {@link CidadeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cidade> createSpecification(CidadeCriteria criteria) {
        Specification<Cidade> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cidade_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Cidade_.nome));
            }
            if (criteria.getCodigoIbge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodigoIbge(), Cidade_.codigoIbge));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Cidade_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Cidade_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Cidade_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Cidade_.lastModifiedDate));
            }
            if (criteria.getEnderecoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEnderecoId(), root -> root.join(Cidade_.enderecos, JoinType.LEFT).get(Endereco_.id))
                    );
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(Cidade_.embarcadors, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(Cidade_.transportadoras, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
            if (criteria.getEstadoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEstadoId(), root -> root.join(Cidade_.estado, JoinType.LEFT).get(Estado_.id))
                    );
            }
        }
        return specification;
    }
}
