package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class StatusColetaRepositoryWithBagRelationshipsImpl implements StatusColetaRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<StatusColeta> fetchBagRelationships(Optional<StatusColeta> statusColeta) {
        return statusColeta.map(this::fetchStatusColetaOrigems);
    }

    @Override
    public Page<StatusColeta> fetchBagRelationships(Page<StatusColeta> statusColetas) {
        return new PageImpl<>(
            fetchBagRelationships(statusColetas.getContent()),
            statusColetas.getPageable(),
            statusColetas.getTotalElements()
        );
    }

    @Override
    public List<StatusColeta> fetchBagRelationships(List<StatusColeta> statusColetas) {
        return Optional.of(statusColetas).map(this::fetchStatusColetaOrigems).orElse(Collections.emptyList());
    }

    StatusColeta fetchStatusColetaOrigems(StatusColeta result) {
        return entityManager
            .createQuery(
                "select statusColeta from StatusColeta statusColeta left join fetch statusColeta.statusColetaOrigems where statusColeta.id = :id",
                StatusColeta.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<StatusColeta> fetchStatusColetaOrigems(List<StatusColeta> statusColetas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, statusColetas.size()).forEach(index -> order.put(statusColetas.get(index).getId(), index));
        List<StatusColeta> result = entityManager
            .createQuery(
                "select statusColeta from StatusColeta statusColeta left join fetch statusColeta.statusColetaOrigems where statusColeta in :statusColetas",
                StatusColeta.class
            )
            .setParameter("statusColetas", statusColetas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
