package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transportadora} and its DTO {@link TransportadoraDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransportadoraMapper extends EntityMapper<TransportadoraDTO, Transportadora> {}
