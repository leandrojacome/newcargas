package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.repository.StatusColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.StatusColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.StatusColetaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.StatusColetaMapper;
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
 * Service for executing complex queries for {@link StatusColeta} entities in the database.
 * The main input is a {@link StatusColetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StatusColetaDTO} or a {@link Page} of {@link StatusColetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StatusColetaQueryService extends QueryService<StatusColeta> {

    private final Logger log = LoggerFactory.getLogger(StatusColetaQueryService.class);

    private final StatusColetaRepository statusColetaRepository;

    private final StatusColetaMapper statusColetaMapper;

    private final StatusColetaSearchRepository statusColetaSearchRepository;

    public StatusColetaQueryService(
        StatusColetaRepository statusColetaRepository,
        StatusColetaMapper statusColetaMapper,
        StatusColetaSearchRepository statusColetaSearchRepository
    ) {
        this.statusColetaRepository = statusColetaRepository;
        this.statusColetaMapper = statusColetaMapper;
        this.statusColetaSearchRepository = statusColetaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StatusColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StatusColetaDTO> findByCriteria(StatusColetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StatusColeta> specification = createSpecification(criteria);
        return statusColetaMapper.toDto(statusColetaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StatusColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StatusColetaDTO> findByCriteria(StatusColetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StatusColeta> specification = createSpecification(criteria);
        return statusColetaRepository.findAll(specification, page).map(statusColetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StatusColetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StatusColeta> specification = createSpecification(criteria);
        return statusColetaRepository.count(specification);
    }

    /**
     * Function to convert {@link StatusColetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StatusColeta> createSpecification(StatusColetaCriteria criteria) {
        Specification<StatusColeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StatusColeta_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), StatusColeta_.nome));
            }
            if (criteria.getCor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCor(), StatusColeta_.cor));
            }
            if (criteria.getOrdem() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrdem(), StatusColeta_.ordem));
            }
            if (criteria.getEstadoInicial() != null) {
                specification = specification.and(buildSpecification(criteria.getEstadoInicial(), StatusColeta_.estadoInicial));
            }
            if (criteria.getEstadoFinal() != null) {
                specification = specification.and(buildSpecification(criteria.getEstadoFinal(), StatusColeta_.estadoFinal));
            }
            if (criteria.getPermiteCancelar() != null) {
                specification = specification.and(buildSpecification(criteria.getPermiteCancelar(), StatusColeta_.permiteCancelar));
            }
            if (criteria.getPermiteEditar() != null) {
                specification = specification.and(buildSpecification(criteria.getPermiteEditar(), StatusColeta_.permiteEditar));
            }
            if (criteria.getPermiteExcluir() != null) {
                specification = specification.and(buildSpecification(criteria.getPermiteExcluir(), StatusColeta_.permiteExcluir));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), StatusColeta_.descricao));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), StatusColeta_.ativo));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), StatusColeta_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), StatusColeta_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), StatusColeta_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), StatusColeta_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), StatusColeta_.lastModifiedDate));
            }
            if (criteria.getSolicitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoColetaId(),
                            root -> root.join(StatusColeta_.solicitacaoColetas, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
            if (criteria.getHistoricoStatusColetaOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoricoStatusColetaOrigemId(),
                            root -> root.join(StatusColeta_.historicoStatusColetaOrigems, JoinType.LEFT).get(HistoricoStatusColeta_.id)
                        )
                    );
            }
            if (criteria.getHistoricoStatusColetaDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoricoStatusColetaDestinoId(),
                            root -> root.join(StatusColeta_.historicoStatusColetaDestinos, JoinType.LEFT).get(HistoricoStatusColeta_.id)
                        )
                    );
            }
            if (criteria.getRoteirizacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoteirizacaoId(),
                            root -> root.join(StatusColeta_.roteirizacaos, JoinType.LEFT).get(Roteirizacao_.id)
                        )
                    );
            }
            if (criteria.getStatusColetaOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusColetaOrigemId(),
                            root -> root.join(StatusColeta_.statusColetaOrigems, JoinType.LEFT).get(StatusColeta_.id)
                        )
                    );
            }
            if (criteria.getStatusColetaDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusColetaDestinoId(),
                            root -> root.join(StatusColeta_.statusColetaDestinos, JoinType.LEFT).get(StatusColeta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
