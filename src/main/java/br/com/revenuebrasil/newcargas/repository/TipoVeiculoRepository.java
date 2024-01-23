package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoVeiculo entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface TipoVeiculoRepository extends JpaRepository<TipoVeiculo, Long>, JpaSpecificationExecutor<TipoVeiculo> {}
