package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoCarga entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface TipoCargaRepository extends JpaRepository<TipoCarga, Long>, JpaSpecificationExecutor<TipoCarga> {}
