package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.repository.ContaBancariaRepository;
import br.com.revenuebrasil.newcargas.repository.search.ContaBancariaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.ContaBancariaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.ContaBancariaMapper;
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
 * Service for executing complex queries for {@link ContaBancaria} entities in the database.
 * The main input is a {@link ContaBancariaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContaBancariaDTO} or a {@link Page} of {@link ContaBancariaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaBancariaQueryService extends QueryService<ContaBancaria> {

    private final Logger log = LoggerFactory.getLogger(ContaBancariaQueryService.class);

    private final ContaBancariaRepository contaBancariaRepository;

    private final ContaBancariaMapper contaBancariaMapper;

    private final ContaBancariaSearchRepository contaBancariaSearchRepository;

    public ContaBancariaQueryService(
        ContaBancariaRepository contaBancariaRepository,
        ContaBancariaMapper contaBancariaMapper,
        ContaBancariaSearchRepository contaBancariaSearchRepository
    ) {
        this.contaBancariaRepository = contaBancariaRepository;
        this.contaBancariaMapper = contaBancariaMapper;
        this.contaBancariaSearchRepository = contaBancariaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContaBancariaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContaBancariaDTO> findByCriteria(ContaBancariaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContaBancaria> specification = createSpecification(criteria);
        return contaBancariaMapper.toDto(contaBancariaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContaBancariaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaBancariaDTO> findByCriteria(ContaBancariaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContaBancaria> specification = createSpecification(criteria);
        return contaBancariaRepository.findAll(specification, page).map(contaBancariaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaBancariaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContaBancaria> specification = createSpecification(criteria);
        return contaBancariaRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaBancariaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContaBancaria> createSpecification(ContaBancariaCriteria criteria) {
        Specification<ContaBancaria> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContaBancaria_.id));
            }
            if (criteria.getAgencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgencia(), ContaBancaria_.agencia));
            }
            if (criteria.getConta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConta(), ContaBancaria_.conta));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), ContaBancaria_.observacao));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), ContaBancaria_.tipo));
            }
            if (criteria.getPix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPix(), ContaBancaria_.pix));
            }
            if (criteria.getTitular() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitular(), ContaBancaria_.titular));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ContaBancaria_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ContaBancaria_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ContaBancaria_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ContaBancaria_.lastModifiedDate));
            }
            if (criteria.getBancoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBancoId(), root -> root.join(ContaBancaria_.banco, JoinType.LEFT).get(Banco_.id))
                    );
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(ContaBancaria_.embarcador, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(ContaBancaria_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
