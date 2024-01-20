package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Roteirizacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoteirizacaoRepository extends JpaRepository<Roteirizacao, Long> {}
