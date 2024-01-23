package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.repository.NotaFiscalColetaRepository;
import br.com.revenuebrasil.newcargas.repository.search.NotaFiscalColetaSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.NotaFiscalColetaCriteria;
import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
import br.com.revenuebrasil.newcargas.service.mapper.NotaFiscalColetaMapper;
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
 * Service for executing complex queries for {@link NotaFiscalColeta} entities in the database.
 * The main input is a {@link NotaFiscalColetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotaFiscalColetaDTO} or a {@link Page} of {@link NotaFiscalColetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotaFiscalColetaQueryService extends QueryService<NotaFiscalColeta> {

    private final Logger log = LoggerFactory.getLogger(NotaFiscalColetaQueryService.class);

    private final NotaFiscalColetaRepository notaFiscalColetaRepository;

    private final NotaFiscalColetaMapper notaFiscalColetaMapper;

    private final NotaFiscalColetaSearchRepository notaFiscalColetaSearchRepository;

    public NotaFiscalColetaQueryService(
        NotaFiscalColetaRepository notaFiscalColetaRepository,
        NotaFiscalColetaMapper notaFiscalColetaMapper,
        NotaFiscalColetaSearchRepository notaFiscalColetaSearchRepository
    ) {
        this.notaFiscalColetaRepository = notaFiscalColetaRepository;
        this.notaFiscalColetaMapper = notaFiscalColetaMapper;
        this.notaFiscalColetaSearchRepository = notaFiscalColetaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NotaFiscalColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotaFiscalColetaDTO> findByCriteria(NotaFiscalColetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NotaFiscalColeta> specification = createSpecification(criteria);
        return notaFiscalColetaMapper.toDto(notaFiscalColetaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotaFiscalColetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaFiscalColetaDTO> findByCriteria(NotaFiscalColetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotaFiscalColeta> specification = createSpecification(criteria);
        return notaFiscalColetaRepository.findAll(specification, page).map(notaFiscalColetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotaFiscalColetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NotaFiscalColeta> specification = createSpecification(criteria);
        return notaFiscalColetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NotaFiscalColetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotaFiscalColeta> createSpecification(NotaFiscalColetaCriteria criteria) {
        Specification<NotaFiscalColeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NotaFiscalColeta_.id));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), NotaFiscalColeta_.numero));
            }
            if (criteria.getSerie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerie(), NotaFiscalColeta_.serie));
            }
            if (criteria.getRemetente() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemetente(), NotaFiscalColeta_.remetente));
            }
            if (criteria.getDestinatario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDestinatario(), NotaFiscalColeta_.destinatario));
            }
            if (criteria.getMetroCubico() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMetroCubico(), NotaFiscalColeta_.metroCubico));
            }
            if (criteria.getQuantidade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantidade(), NotaFiscalColeta_.quantidade));
            }
            if (criteria.getPeso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeso(), NotaFiscalColeta_.peso));
            }
            if (criteria.getDataEmissao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataEmissao(), NotaFiscalColeta_.dataEmissao));
            }
            if (criteria.getDataSaida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataSaida(), NotaFiscalColeta_.dataSaida));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), NotaFiscalColeta_.valorTotal));
            }
            if (criteria.getPesoTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPesoTotal(), NotaFiscalColeta_.pesoTotal));
            }
            if (criteria.getQuantidadeTotal() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getQuantidadeTotal(), NotaFiscalColeta_.quantidadeTotal));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), NotaFiscalColeta_.observacao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), NotaFiscalColeta_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), NotaFiscalColeta_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), NotaFiscalColeta_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), NotaFiscalColeta_.lastModifiedDate));
            }
            if (criteria.getEnderecoOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnderecoOrigemId(),
                            root -> root.join(NotaFiscalColeta_.enderecoOrigems, JoinType.LEFT).get(Endereco_.id)
                        )
                    );
            }
            if (criteria.getEnderecoDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnderecoDestinoId(),
                            root -> root.join(NotaFiscalColeta_.enderecoDestinos, JoinType.LEFT).get(Endereco_.id)
                        )
                    );
            }
            if (criteria.getSolicitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoColetaId(),
                            root -> root.join(NotaFiscalColeta_.solicitacaoColeta, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
