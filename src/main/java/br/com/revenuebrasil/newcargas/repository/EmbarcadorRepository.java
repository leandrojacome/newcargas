package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Embarcador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmbarcadorRepository extends JpaRepository<Embarcador, Long> {}
