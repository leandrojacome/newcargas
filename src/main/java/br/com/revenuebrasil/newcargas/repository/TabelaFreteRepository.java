package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TabelaFrete entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface TabelaFreteRepository extends JpaRepository<TabelaFrete, Long>, JpaSpecificationExecutor<TabelaFrete> {}
