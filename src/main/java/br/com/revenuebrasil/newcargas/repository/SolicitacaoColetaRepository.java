package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SolicitacaoColeta entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface SolicitacaoColetaRepository extends JpaRepository<SolicitacaoColeta, Long>, JpaSpecificationExecutor<SolicitacaoColeta> {}
