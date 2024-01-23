package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import java.util.List;
import java.util.Optional;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StatusColeta entity.
 *
 * When extending this class, extend StatusColetaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
@JaversSpringDataAuditable
public interface StatusColetaRepository
    extends StatusColetaRepositoryWithBagRelationships, JpaRepository<StatusColeta, Long>, JpaSpecificationExecutor<StatusColeta> {
    default Optional<StatusColeta> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<StatusColeta> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<StatusColeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
