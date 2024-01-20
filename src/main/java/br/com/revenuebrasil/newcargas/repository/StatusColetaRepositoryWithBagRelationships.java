package br.com.revenuebrasil.newcargas.repository;

import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StatusColetaRepositoryWithBagRelationships {
    Optional<StatusColeta> fetchBagRelationships(Optional<StatusColeta> statusColeta);

    List<StatusColeta> fetchBagRelationships(List<StatusColeta> statusColetas);

    Page<StatusColeta> fetchBagRelationships(Page<StatusColeta> statusColetas);
}
