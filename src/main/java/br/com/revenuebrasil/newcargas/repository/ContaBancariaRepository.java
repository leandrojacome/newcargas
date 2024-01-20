package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContaBancaria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {}
