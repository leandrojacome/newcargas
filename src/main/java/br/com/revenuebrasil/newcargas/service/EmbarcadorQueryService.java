package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.repository.EmbarcadorRepository;
import br.com.revenuebrasil.newcargas.repository.search.EmbarcadorSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.EmbarcadorCriteria;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EmbarcadorMapper;
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
 * Service for executing complex queries for {@link Embarcador} entities in the database.
 * The main input is a {@link EmbarcadorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmbarcadorDTO} or a {@link Page} of {@link EmbarcadorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmbarcadorQueryService extends QueryService<Embarcador> {

    private final Logger log = LoggerFactory.getLogger(EmbarcadorQueryService.class);

    private final EmbarcadorRepository embarcadorRepository;

    private final EmbarcadorMapper embarcadorMapper;

    private final EmbarcadorSearchRepository embarcadorSearchRepository;

    public EmbarcadorQueryService(
        EmbarcadorRepository embarcadorRepository,
        EmbarcadorMapper embarcadorMapper,
        EmbarcadorSearchRepository embarcadorSearchRepository
    ) {
        this.embarcadorRepository = embarcadorRepository;
        this.embarcadorMapper = embarcadorMapper;
        this.embarcadorSearchRepository = embarcadorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EmbarcadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmbarcadorDTO> findByCriteria(EmbarcadorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Embarcador> specification = createSpecification(criteria);
        return embarcadorMapper.toDto(embarcadorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmbarcadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmbarcadorDTO> findByCriteria(EmbarcadorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Embarcador> specification = createSpecification(criteria);
        return embarcadorRepository.findAll(specification, page).map(embarcadorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmbarcadorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Embarcador> specification = createSpecification(criteria);
        return embarcadorRepository.count(specification);
    }

    /**
     * Function to convert {@link EmbarcadorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Embarcador> createSpecification(EmbarcadorCriteria criteria) {
        Specification<Embarcador> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Embarcador_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Embarcador_.nome));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Embarcador_.cnpj));
            }
            if (criteria.getRazaoSocial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazaoSocial(), Embarcador_.razaoSocial));
            }
            if (criteria.getInscricaoEstadual() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInscricaoEstadual(), Embarcador_.inscricaoEstadual));
            }
            if (criteria.getInscricaoMunicipal() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInscricaoMunicipal(), Embarcador_.inscricaoMunicipal));
            }
            if (criteria.getResponsavel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsavel(), Embarcador_.responsavel));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Embarcador_.cep));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), Embarcador_.endereco));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Embarcador_.numero));
            }
            if (criteria.getComplemento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComplemento(), Embarcador_.complemento));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Embarcador_.bairro));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Embarcador_.telefone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Embarcador_.email));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Embarcador_.observacao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Embarcador_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Embarcador_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Embarcador_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Embarcador_.lastModifiedDate));
            }
            if (criteria.getEnderecoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnderecoId(),
                            root -> root.join(Embarcador_.enderecos, JoinType.LEFT).get(Endereco_.id)
                        )
                    );
            }
            if (criteria.getContaBancariaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContaBancariaId(),
                            root -> root.join(Embarcador_.contaBancarias, JoinType.LEFT).get(ContaBancaria_.id)
                        )
                    );
            }
            if (criteria.getTabelaFreteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTabelaFreteId(),
                            root -> root.join(Embarcador_.tabelaFretes, JoinType.LEFT).get(TabelaFrete_.id)
                        )
                    );
            }
            if (criteria.getSolitacaoColetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolitacaoColetaId(),
                            root -> root.join(Embarcador_.solitacaoColetas, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
            if (criteria.getNotificacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificacaoId(),
                            root -> root.join(Embarcador_.notificacaos, JoinType.LEFT).get(Notificacao_.id)
                        )
                    );
            }
            if (criteria.getFaturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFaturaId(), root -> root.join(Embarcador_.faturas, JoinType.LEFT).get(Fatura_.id))
                    );
            }
            if (criteria.getCidadeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCidadeId(), root -> root.join(Embarcador_.cidade, JoinType.LEFT).get(Cidade_.id))
                    );
            }
        }
        return specification;
    }
}
