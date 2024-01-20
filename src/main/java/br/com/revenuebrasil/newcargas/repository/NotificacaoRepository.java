package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.Notificacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notificacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {}
