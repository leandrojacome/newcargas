package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TomadaPreco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TomadaPrecoRepository extends JpaRepository<TomadaPreco, Long> {}
