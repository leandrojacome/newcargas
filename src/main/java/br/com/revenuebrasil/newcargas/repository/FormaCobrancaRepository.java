package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FormaCobranca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormaCobrancaRepository extends JpaRepository<FormaCobranca, Long> {}
