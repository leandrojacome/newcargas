package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contratacao} and its DTO {@link ContratacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContratacaoMapper extends EntityMapper<ContratacaoDTO, Contratacao> {
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    ContratacaoDTO toDto(Contratacao s);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);
}
