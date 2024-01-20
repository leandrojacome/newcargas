package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Regiao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Regiao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegiaoRepository extends JpaRepository<Regiao, Long> {}
