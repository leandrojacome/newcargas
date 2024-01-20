package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotaFiscalColeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotaFiscalColetaRepository extends JpaRepository<NotaFiscalColeta, Long> {}
