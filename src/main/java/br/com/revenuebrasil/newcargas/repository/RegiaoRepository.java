package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Regiao;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Regiao entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface RegiaoRepository extends JpaRepository<Regiao, Long>, JpaSpecificationExecutor<Regiao> {}
