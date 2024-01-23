package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Fatura;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fatura entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface FaturaRepository extends JpaRepository<Fatura, Long>, JpaSpecificationExecutor<Fatura> {}
