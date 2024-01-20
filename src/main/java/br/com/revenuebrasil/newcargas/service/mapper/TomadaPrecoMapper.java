package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.TomadaPrecoDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TomadaPreco} and its DTO {@link TomadaPrecoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TomadaPrecoMapper extends EntityMapper<TomadaPrecoDTO, TomadaPreco> {
    @Mapping(target = "contratacao", source = "contratacao", qualifiedByName = "contratacaoId")
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    @Mapping(target = "roteirizacao", source = "roteirizacao", qualifiedByName = "roteirizacaoId")
    TomadaPrecoDTO toDto(TomadaPreco s);

    @Named("contratacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContratacaoDTO toDtoContratacaoId(Contratacao contratacao);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);

    @Named("roteirizacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoteirizacaoDTO toDtoRoteirizacaoId(Roteirizacao roteirizacao);
}
