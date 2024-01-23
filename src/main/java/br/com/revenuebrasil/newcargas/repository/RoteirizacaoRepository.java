package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Roteirizacao entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface RoteirizacaoRepository extends JpaRepository<Roteirizacao, Long>, JpaSpecificationExecutor<Roteirizacao> {}
