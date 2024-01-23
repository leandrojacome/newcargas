package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Embarcador} and its DTO {@link EmbarcadorDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmbarcadorMapper extends EntityMapper<EmbarcadorDTO, Embarcador> {
    @Mapping(target = "cidade", source = "cidade", qualifiedByName = "cidadeId")
    EmbarcadorDTO toDto(Embarcador s);

    @Named("cidadeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CidadeDTO toDtoCidadeId(Cidade cidade);
}
