package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.repository.RegiaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.RegiaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.RegiaoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.RegiaoMapper;
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
 * Service for executing complex queries for {@link Regiao} entities in the database.
 * The main input is a {@link RegiaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegiaoDTO} or a {@link Page} of {@link RegiaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegiaoQueryService extends QueryService<Regiao> {

    private final Logger log = LoggerFactory.getLogger(RegiaoQueryService.class);

    private final RegiaoRepository regiaoRepository;

    private final RegiaoMapper regiaoMapper;

    private final RegiaoSearchRepository regiaoSearchRepository;

    public RegiaoQueryService(RegiaoRepository regiaoRepository, RegiaoMapper regiaoMapper, RegiaoSearchRepository regiaoSearchRepository) {
        this.regiaoRepository = regiaoRepository;
        this.regiaoMapper = regiaoMapper;
        this.regiaoSearchRepository = regiaoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RegiaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegiaoDTO> findByCriteria(RegiaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Regiao> specification = createSpecification(criteria);
        return regiaoMapper.toDto(regiaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RegiaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegiaoDTO> findByCriteria(RegiaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Regiao> specification = createSpecification(criteria);
        return regiaoRepository.findAll(specification, page).map(regiaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegiaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Regiao> specification = createSpecification(criteria);
        return regiaoRepository.count(specification);
    }

    /**
     * Function to convert {@link RegiaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Regiao> createSpecification(RegiaoCriteria criteria) {
        Specification<Regiao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Regiao_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Regiao_.nome));
            }
            if (criteria.getSigla() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSigla(), Regiao_.sigla));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Regiao_.descricao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Regiao_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Regiao_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Regiao_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Regiao_.lastModifiedDate));
            }
            if (criteria.getTabelaFreteOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteOrigemId(),
                            root -> root.join(Regiao_.tabelaFreteOrigems, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
            if (criteria.getTabelaFreteDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteDestinoId(),
                            root -> root.join(Regiao_.tabelaFreteDestinos, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
