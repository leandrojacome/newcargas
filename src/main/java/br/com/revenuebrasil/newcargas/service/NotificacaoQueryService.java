package br.com.revenuebrasil.newcargas.service;

import br.com.revenuebrasil.newcargas.domain.*; // for static metamodels
import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.repository.NotificacaoRepository;
import br.com.revenuebrasil.newcargas.repository.search.NotificacaoSearchRepository;
import br.com.revenuebrasil.newcargas.service.criteria.NotificacaoCriteria;
import br.com.revenuebrasil.newcargas.service.dto.NotificacaoDTO;
import br.com.revenuebrasil.newcargas.service.mapper.NotificacaoMapper;
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
 * Service for executing complex queries for {@link Notificacao} entities in the database.
 * The main input is a {@link NotificacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotificacaoDTO} or a {@link Page} of {@link NotificacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificacaoQueryService extends QueryService<Notificacao> {

    private final Logger log = LoggerFactory.getLogger(NotificacaoQueryService.class);

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoMapper notificacaoMapper;

    private final NotificacaoSearchRepository notificacaoSearchRepository;

    public NotificacaoQueryService(
        NotificacaoRepository notificacaoRepository,
        NotificacaoMapper notificacaoMapper,
        NotificacaoSearchRepository notificacaoSearchRepository
    ) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoMapper = notificacaoMapper;
        this.notificacaoSearchRepository = notificacaoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NotificacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotificacaoDTO> findByCriteria(NotificacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notificacao> specification = createSpecification(criteria);
        return notificacaoMapper.toDto(notificacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotificacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificacaoDTO> findByCriteria(NotificacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notificacao> specification = createSpecification(criteria);
        return notificacaoRepository.findAll(specification, page).map(notificacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notificacao> specification = createSpecification(criteria);
        return notificacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notificacao> createSpecification(NotificacaoCriteria criteria) {
        Specification<Notificacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Notificacao_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Notificacao_.tipo));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Notificacao_.email));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Notificacao_.telefone));
            }
            if (criteria.getAssunto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssunto(), Notificacao_.assunto));
            }
            if (criteria.getMensagem() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMensagem(), Notificacao_.mensagem));
            }
            if (criteria.getDataHoraEnvio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHoraEnvio(), Notificacao_.dataHoraEnvio));
            }
            if (criteria.getDataHoraLeitura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHoraLeitura(), Notificacao_.dataHoraLeitura));
            }
            if (criteria.getLido() != null) {
                specification = specification.and(buildSpecification(criteria.getLido(), Notificacao_.lido));
            }
            if (criteria.getDataLeitura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataLeitura(), Notificacao_.dataLeitura));
            }
            if (criteria.getRemovido() != null) {
                specification = specification.and(buildSpecification(criteria.getRemovido(), Notificacao_.removido));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Notificacao_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Notificacao_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Notificacao_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Notificacao_.lastModifiedDate));
            }
            if (criteria.getEmbarcadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmbarcadorId(),
                            root -> root.join(Notificacao_.embarcador, JoinType.LEFT).get(Embarcador_.id)
                        )
                    );
            }
            if (criteria.getTransportadoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransportadoraId(),
                            root -> root.join(Notificacao_.transportadora, JoinType.LEFT).get(Transportadora_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
