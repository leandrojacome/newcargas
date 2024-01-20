package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoVeiculo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoVeiculoRepository extends JpaRepository<TipoVeiculo, Long> {}
