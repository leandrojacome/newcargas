package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.repository.SolicitacaoColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.SolicitacaoColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.SolicitacaoColetaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.SolicitacaoColetaMapper;
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
 * Service for executing complex queries for {@link SolicitacaoColeta} entities in the database.
 * The main input is a {@link SolicitacaoColetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SolicitacaoColetaDTO} or a {@link Page} of {@link SolicitacaoColetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SolicitacaoColetaQueryService extends QueryService<SolicitacaoColeta> {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoColetaQueryService.class);

    private final SolicitacaoColetaRepository solicitacaoColetaRepository;

    private final SolicitacaoColetaMapper solicitacaoColetaMapper;

    private final SolicitacaoColetaSearchRepository solicitacaoColetaSearchRepository;

    public SolicitacaoColetaQueryService(
        SolicitacaoColetaRepository solicitacaoColetaRepository,
        SolicitacaoColetaMapper solicitacaoColetaMapper,
        SolicitacaoColetaSearchRepository solicitacaoColetaSearchRepository
    ) {
        this.solicitacaoColetaRepository = solicitacaoColetaRepository;
        this.solicitacaoColetaMapper = solicitacaoColetaMapper;
        this.solicitacaoColetaSearchRepository = solicitacaoColetaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SolicitacaoColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SolicitacaoColetaDTO> findByCriteria(SolicitacaoColetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SolicitacaoColeta> specification = createSpecification(criteria);
        return solicitacaoColetaMapper.toDto(solicitacaoColetaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SolicitacaoColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SolicitacaoColetaDTO> findByCriteria(SolicitacaoColetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SolicitacaoColeta> specification = createSpecification(criteria);
        return solicitacaoColetaRepository.findAll(specification, page).map(solicitacaoColetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SolicitacaoColetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SolicitacaoColeta> specification = createSpecification(criteria);
        return solicitacaoColetaRepository.count(specification);
    }

    /**
     * Function to convert {@link SolicitacaoColetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SolicitacaoColeta> createSpecification(SolicitacaoColetaCriteria criteria) {
        Specification<SolicitacaoColeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SolicitacaoColeta_.id));
            }
            if (criteria.getColetado() != null) {
                specification = specification.and(buildSpecification(criteria.getColetado(), SolicitacaoColeta_.coletado));
            }
            if (criteria.getDataHoraColeta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHoraColeta(), SolicitacaoColeta_.dataHoraColeta));
            }
            if (criteria.getEntregue() != null) {
                specification = specification.and(buildSpecification(criteria.getEntregue(), SolicitacaoColeta_.entregue));
            }
            if (criteria.getDataHoraEntrega() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataHoraEntrega(), SolicitacaoColeta_.dataHoraEntrega));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), SolicitacaoColeta_.valorTotal));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), SolicitacaoColeta_.observacao));
            }
            if (criteria.getCancelado() != null) {
                specification = specification.and(buildSpecification(criteria.getCancelado(), SolicitacaoColeta_.cancelado));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), SolicitacaoColeta_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), SolicitacaoColeta_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), SolicitacaoColeta_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), SolicitacaoColeta_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), SolicitacaoColeta_.lastModifiedDate));
            }
            if (criteria.getNotaFiscalColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotaFiscalColetaId(),
                            root -> root.join(SolicitacaoColeta_.notaFiscalColetas, JoinType.LEFT).get(NotaFiscalColeta_.id)
                        )
                    );
            }
            if (criteria.getEnderecoOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnderecoOrigemId(),
                            root -> root.join(SolicitacaoColeta_.enderecoOrigems, JoinType.LEFT).get(Endereco_.id)
                        )
                    );
            }
            if (criteria.getEnderecoDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnderecoDestinoId(),
                            root -> root.join(SolicitacaoColeta_.enderecoDestinos, JoinType.LEFT).get(Endereco_.id)
                        )
                    );
            }
            if (criteria.getHistoricoStatusColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoricoStatusColetaId(),
                            root -> root.join(SolicitacaoColeta_.historicoStatusColetas, JoinType.LEFT).get(HistoricoStatusColeta_.id)
                        )
                    );
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(SolicitacaoColeta_.embarcador, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getStatusColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStatusColetaId(),
                            root -> root.join(SolicitacaoColeta_.statusColeta, JoinType.LEFT).get(StatusColeta_.id)
                        )
                    );
            }
            if (criteria.getRoteirizacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoteirizacaoId(),
                            root -> root.join(SolicitacaoColeta_.roteirizacao, JoinType.LEFT).get(Roteirizacao_.id)
                        )
                    );
            }
            if (criteria.getTipoVeiculoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoVeiculoId(),
                            root -> root.join(SolicitacaoColeta_.tipoVeiculo, JoinType.LEFT).get(TipoVeiculo_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
