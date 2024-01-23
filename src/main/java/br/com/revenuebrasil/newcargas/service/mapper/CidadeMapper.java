package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.Estado;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.dto.EstadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cidade} and its DTO {@link CidadeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CidadeMapper extends EntityMapper<CidadeDTO, Cidade> {
    @Mapping(target = "estado", source = "estado", qualifiedByName = "estadoId")
    CidadeDTO toDto(Cidade s);

    @Named("estadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstadoDTO toDtoEstadoId(Estado estado);
}
