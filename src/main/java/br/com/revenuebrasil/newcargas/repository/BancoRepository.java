package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Banco;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Banco entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface BancoRepository extends JpaRepository<Banco, Long>, JpaSpecificationExecutor<Banco> {}
