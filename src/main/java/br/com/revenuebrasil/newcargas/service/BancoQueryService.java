package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Banco;
import br.com.revenuebrasil.newcargas.repository.BancoRepository;
import br.com.revenuebrasil.newcargas.repository.search.BancoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.BancoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.BancoMapper;
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
 * Service for executing complex queries for {@link Banco} entities in the database.
 * The main input is a {@link BancoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BancoDTO} or a {@link Page} of {@link BancoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BancoQueryService extends QueryService<Banco> {

    private final Logger log = LoggerFactory.getLogger(BancoQueryService.class);

    private final BancoRepository bancoRepository;

    private final BancoMapper bancoMapper;

    private final BancoSearchRepository bancoSearchRepository;

    public BancoQueryService(BancoRepository bancoRepository, BancoMapper bancoMapper, BancoSearchRepository bancoSearchRepository) {
        this.bancoRepository = bancoRepository;
        this.bancoMapper = bancoMapper;
        this.bancoSearchRepository = bancoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BancoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BancoDTO> findByCriteria(BancoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoMapper.toDto(bancoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BancoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BancoDTO> findByCriteria(BancoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoRepository.findAll(specification, page).map(bancoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BancoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoRepository.count(specification);
    }

    /**
     * Function to convert {@link BancoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Banco> createSpecification(BancoCriteria criteria) {
        Specification<Banco> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Banco_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Banco_.nome));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Banco_.codigo));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Banco_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Banco_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Banco_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Banco_.lastModifiedDate));
            }
            if (criteria.getContaBancariaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContaBancariaId(),
                            root -> root.join(Banco_.contaBancarias, JoinType.LEFT).get(ContaBancaria_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
