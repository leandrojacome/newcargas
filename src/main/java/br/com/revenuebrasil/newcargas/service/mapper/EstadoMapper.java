package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Estado;
import br.com.revenuebrasil.newcargas.service.dto.EstadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estado} and its DTO {@link EstadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstadoMapper extends EntityMapper<EstadoDTO, Estado> {}
