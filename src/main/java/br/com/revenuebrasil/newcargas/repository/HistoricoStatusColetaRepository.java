package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoricoStatusColeta entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface HistoricoStatusColetaRepository
    extends JpaRepository<HistoricoStatusColeta, Long>, JpaSpecificationExecutor<HistoricoStatusColeta> {}
