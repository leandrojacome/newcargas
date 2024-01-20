package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Transportadora;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transportadora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {}
