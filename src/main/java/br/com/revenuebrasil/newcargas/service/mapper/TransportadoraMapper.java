package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transportadora} and its DTO {@link TransportadoraDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransportadoraMapper extends EntityMapper<TransportadoraDTO, Transportadora> {
    @Mapping(target = "cidade", source = "cidade", qualifiedByName = "cidadeId")
    TransportadoraDTO toDto(Transportadora s);

    @Named("cidadeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CidadeDTO toDtoCidadeId(Cidade cidade);
}
