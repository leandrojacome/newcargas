package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.repository.TabelaFreteRepository;
import br.com.revenuebrasil.newcargas.repository.search.TabelaFreteSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.TabelaFreteCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TabelaFreteMapper;
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
 * Service for executing complex queries for {@link TabelaFrete} entities in the database.
 * The main input is a {@link TabelaFreteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TabelaFreteDTO} or a {@link Page} of {@link TabelaFreteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TabelaFreteQueryService extends QueryService<TabelaFrete> {

    private final Logger log = LoggerFactory.getLogger(TabelaFreteQueryService.class);

    private final TabelaFreteRepository tabelaFreteRepository;

    private final TabelaFreteMapper tabelaFreteMapper;

    private final TabelaFreteSearchRepository tabelaFreteSearchRepository;

    public TabelaFreteQueryService(
        TabelaFreteRepository tabelaFreteRepository,
        TabelaFreteMapper tabelaFreteMapper,
        TabelaFreteSearchRepository tabelaFreteSearchRepository
    ) {
        this.tabelaFreteRepository = tabelaFreteRepository;
        this.tabelaFreteMapper = tabelaFreteMapper;
        this.tabelaFreteSearchRepository = tabelaFreteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TabelaFreteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TabelaFreteDTO> findByCriteria(TabelaFreteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TabelaFrete> specification = createSpecification(criteria);
        return tabelaFreteMapper.toDto(tabelaFreteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TabelaFreteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TabelaFreteDTO> findByCriteria(TabelaFreteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TabelaFrete> specification = createSpecification(criteria);
        return tabelaFreteRepository.findAll(specification, page).map(tabelaFreteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TabelaFreteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TabelaFrete> specification = createSpecification(criteria);
        return tabelaFreteRepository.count(specification);
    }

    /**
     * Function to convert {@link TabelaFreteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TabelaFrete> createSpecification(TabelaFreteCriteria criteria) {
        Specification<TabelaFrete> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TabelaFrete_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), TabelaFrete_.tipo));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), TabelaFrete_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), TabelaFrete_.descricao));
            }
            if (criteria.getLeadTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeadTime(), TabelaFrete_.leadTime));
            }
            if (criteria.getFreteMinimo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFreteMinimo(), TabelaFrete_.freteMinimo));
            }
            if (criteria.getValorTonelada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTonelada(), TabelaFrete_.valorTonelada));
            }
            if (criteria.getValorMetroCubico() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorMetroCubico(), TabelaFrete_.valorMetroCubico));
            }
            if (criteria.getValorUnidade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorUnidade(), TabelaFrete_.valorUnidade));
            }
            if (criteria.getValorKm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorKm(), TabelaFrete_.valorKm));
            }
            if (criteria.getValorAdicional() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorAdicional(), TabelaFrete_.valorAdicional));
            }
            if (criteria.getValorColeta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorColeta(), TabelaFrete_.valorColeta));
            }
            if (criteria.getValorEntrega() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorEntrega(), TabelaFrete_.valorEntrega));
            }
            if (criteria.getValorTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotal(), TabelaFrete_.valorTotal));
            }
            if (criteria.getValorKmAdicional() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorKmAdicional(), TabelaFrete_.valorKmAdicional));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), TabelaFrete_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TabelaFrete_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), TabelaFrete_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TabelaFrete_.lastModifiedDate));
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(TabelaFrete_.embarcador, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(TabelaFrete_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
            if (criteria.getTipoCargaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoCargaId(),
                            root -> root.join(TabelaFrete_.tipoCarga, JoinType.LEFT).get(TipoCarga_.id)
                        )
                    );
            }
            if (criteria.getTipoFreteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoFreteId(),
                            root -> root.join(TabelaFrete_.tipoFrete, JoinType.LEFT).get(TipoFrete_.id)
                        )
                    );
            }
            if (criteria.getFormaCobrancaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFormaCobrancaId(),
                            root -> root.join(TabelaFrete_.formaCobranca, JoinType.LEFT).get(FormaCobranca_.id)
                        )
                    );
            }
            if (criteria.getRegiaoOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegiaoOrigemId(),
                            root -> root.join(TabelaFrete_.regiaoOrigem, JoinType.LEFT).get(Regiao_.id)
                        )
                    );
            }
            if (criteria.getRegiaoDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegiaoDestinoId(),
                            root -> root.join(TabelaFrete_.regiaoDestino, JoinType.LEFT).get(Regiao_.id)
                        )
                    );
            }

            if (criteria.getTipoVeiculoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoVeiculoId(),
                            root -> root.join(TabelaFrete_.tipoVeiculo, JoinType.LEFT).get(TipoVeiculo_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
