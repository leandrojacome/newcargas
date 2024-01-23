package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Transportadora;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transportadora entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long>, JpaSpecificationExecutor<Transportadora> {}
