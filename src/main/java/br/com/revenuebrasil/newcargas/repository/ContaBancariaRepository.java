package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContaBancaria entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long>, JpaSpecificationExecutor<ContaBancaria> {}
