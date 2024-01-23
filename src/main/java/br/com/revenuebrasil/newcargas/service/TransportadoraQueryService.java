package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.TransportadoraRepository;
import br.com.revenuebrasil.newcargas.repository.search.TransportadoraSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.TransportadoraCriteria;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import br.com.revenuebrasil.newcargas.service.mapper.TransportadoraMapper;
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
 * Service for executing complex queries for {@link Transportadora} entities in the database.
 * The main input is a {@link TransportadoraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransportadoraDTO} or a {@link Page} of {@link TransportadoraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransportadoraQueryService extends QueryService<Transportadora> {

    private final Logger log = LoggerFactory.getLogger(TransportadoraQueryService.class);

    private final TransportadoraRepository transportadoraRepository;

    private final TransportadoraMapper transportadoraMapper;

    private final TransportadoraSearchRepository transportadoraSearchRepository;

    public TransportadoraQueryService(
        TransportadoraRepository transportadoraRepository,
        TransportadoraMapper transportadoraMapper,
        TransportadoraSearchRepository transportadoraSearchRepository
    ) {
        this.transportadoraRepository = transportadoraRepository;
        this.transportadoraMapper = transportadoraMapper;
        this.transportadoraSearchRepository = transportadoraSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TransportadoraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransportadoraDTO> findByCriteria(TransportadoraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transportadora> specification = createSpecification(criteria);
        return transportadoraMapper.toDto(transportadoraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransportadoraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransportadoraDTO> findByCriteria(TransportadoraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transportadora> specification = createSpecification(criteria);
        return transportadoraRepository.findAll(specification, page).map(transportadoraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransportadoraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transportadora> specification = createSpecification(criteria);
        return transportadoraRepository.count(specification);
    }

    /**
     * Function to convert {@link TransportadoraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transportadora> createSpecification(TransportadoraCriteria criteria) {
        Specification<Transportadora> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transportadora_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Transportadora_.nome));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Transportadora_.cnpj));
            }
            if (criteria.getRazaoSocial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazaoSocial(), Transportadora_.razaoSocial));
            }
            if (criteria.getInscricaoEstadual() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInscricaoEstadual(), Transportadora_.inscricaoEstadual));
            }
            if (criteria.getInscricaoMunicipal() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInscricaoMunicipal(), Transportadora_.inscricaoMunicipal));
            }
            if (criteria.getResponsavel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsavel(), Transportadora_.responsavel));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Transportadora_.cep));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), Transportadora_.endereco));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Transportadora_.numero));
            }
            if (criteria.getComplemento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComplemento(), Transportadora_.complemento));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Transportadora_.bairro));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Transportadora_.telefone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Transportadora_.email));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Transportadora_.observacao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Transportadora_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Transportadora_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Transportadora_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Transportadora_.lastModifiedDate));
            }
            if (criteria.getEnderecoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnderecoId(),
                            root -> root.join(Transportadora_.enderecos, JoinType.LEFT).get(Endereco_.id)
                        )
                    );
            }
            if (criteria.getContaBancariaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContaBancariaId(),
                            root -> root.join(Transportadora_.contaBancarias, JoinType.LEFT).get(ContaBancaria_.id)
                        )
                    );
            }
            if (criteria.getTabelaFreteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteId(),
                            root -> root.join(Transportadora_.tabelaFretes, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
            if (criteria.getTomadaPrecoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTomadaPrecoId(),
                            root -> root.join(Transportadora_.tomadaPrecos, JoinType.LEFT).get(TomadaPreco_.id)
                        )
                    );
            }
            if (criteria.getContratacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContratacaoId(),
                            root -> root.join(Transportadora_.contratacaos, JoinType.LEFT).get(Contratacao_.id)
                        )
                    );
            }
            if (criteria.getNotificacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificacaoId(),
                            root -> root.join(Transportadora_.notificacaos, JoinType.LEFT).get(Notificacao_.id)
                        )
                    );
            }
            if (criteria.getFaturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFaturaId(),
                            root -> root.join(Transportadora_.faturas, JoinType.LEFT).get(Fatura_.id)
                        )
                    );
            }
            if (criteria.getCidadeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCidadeId(), root -> root.join(Transportadora_.cidade, JoinType.LEFT).get(Cidade_.id))
                    );
            }
        }
        return specification;
    }
}
