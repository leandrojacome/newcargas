package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Embarcador} and its DTO {@link EmbarcadorDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmbarcadorMapper extends EntityMapper<EmbarcadorDTO, Embarcador> {}
