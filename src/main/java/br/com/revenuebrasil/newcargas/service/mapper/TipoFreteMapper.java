package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoFrete} and its DTO {@link TipoFreteDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoFreteMapper extends EntityMapper<TipoFreteDTO, TipoFrete> {}
