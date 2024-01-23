package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.repository.EnderecoRepository;
import br.com.revenuebrasil.newcargas.repository.search.EnderecoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.EnderecoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.EnderecoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.EnderecoMapper;
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
 * Service for executing complex queries for {@link Endereco} entities in the database.
 * The main input is a {@link EnderecoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EnderecoDTO} or a {@link Page} of {@link EnderecoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnderecoQueryService extends QueryService<Endereco> {

    private final Logger log = LoggerFactory.getLogger(EnderecoQueryService.class);

    private final EnderecoRepository enderecoRepository;

    private final EnderecoMapper enderecoMapper;

    private final EnderecoSearchRepository enderecoSearchRepository;

    public EnderecoQueryService(
        EnderecoRepository enderecoRepository,
        EnderecoMapper enderecoMapper,
        EnderecoSearchRepository enderecoSearchRepository
    ) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.enderecoSearchRepository = enderecoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EnderecoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EnderecoDTO> findByCriteria(EnderecoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Endereco> specification = createSpecification(criteria);
        return enderecoMapper.toDto(enderecoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EnderecoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnderecoDTO> findByCriteria(EnderecoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Endereco> specification = createSpecification(criteria);
        return enderecoRepository.findAll(specification, page).map(enderecoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnderecoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Endereco> specification = createSpecification(criteria);
        return enderecoRepository.count(specification);
    }

    /**
     * Function to convert {@link EnderecoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Endereco> createSpecification(EnderecoCriteria criteria) {
        Specification<Endereco> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Endereco_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Endereco_.tipo));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Endereco_.cep));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), Endereco_.endereco));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Endereco_.numero));
            }
            if (criteria.getComplemento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComplemento(), Endereco_.complemento));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Endereco_.bairro));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Endereco_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Endereco_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Endereco_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Endereco_.lastModifiedDate));
            }
            if (criteria.getCidadeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCidadeId(), root -> root.join(Endereco_.cidade, JoinType.LEFT).get(Cidade_.id))
                    );
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(Endereco_.embarcador, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(Endereco_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
            if (criteria.getNotaFiscalColetaOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotaFiscalColetaOrigemId(),
                            root -> root.join(Endereco_.notaFiscalColetaOrigem, JoinType.LEFT).get(NotaFiscalColeta_.id)
                        )
                    );
            }
            if (criteria.getNotaFiscalColetaDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotaFiscalColetaDestinoId(),
                            root -> root.join(Endereco_.notaFiscalColetaDestino, JoinType.LEFT).get(NotaFiscalColeta_.id)
                        )
                    );
            }
            if (criteria.getSolicitacaoColetaOrigemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoColetaOrigemId(),
                            root -> root.join(Endereco_.solicitacaoColetaOrigem, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
            if (criteria.getSolicitacaoColetaDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoColetaDestinoId(),
                            root -> root.join(Endereco_.solicitacaoColetaDestino, JoinType.LEFT).get(SolicitacaoColeta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
