package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoFrete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoFreteRepository extends JpaRepository<TipoFrete, Long> {}
