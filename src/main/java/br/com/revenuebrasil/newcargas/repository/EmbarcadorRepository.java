package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Embarcador entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface EmbarcadorRepository extends JpaRepository<Embarcador, Long>, JpaSpecificationExecutor<Embarcador> {}
