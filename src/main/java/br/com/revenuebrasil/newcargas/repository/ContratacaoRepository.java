package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Contratacao;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contratacao entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface ContratacaoRepository extends JpaRepository<Contratacao, Long>, JpaSpecificationExecutor<Contratacao> {}
