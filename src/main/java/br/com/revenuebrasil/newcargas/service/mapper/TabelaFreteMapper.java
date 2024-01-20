package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.TabelaFreteDTO;
import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
import br.com.revenuebrasil.newcargas.service.dto.TipoFreteDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TabelaFrete} and its DTO {@link TabelaFreteDTO}.
 */
@Mapper(componentModel = "spring")
public interface TabelaFreteMapper extends EntityMapper<TabelaFreteDTO, TabelaFrete> {
    @Mapping(target = "embarcador", source = "embarcador", qualifiedByName = "embarcadorId")
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    @Mapping(target = "tipoCarga", source = "tipoCarga", qualifiedByName = "tipoCargaId")
    @Mapping(target = "tipoFrete", source = "tipoFrete", qualifiedByName = "tipoFreteId")
    @Mapping(target = "formaCobranca", source = "formaCobranca", qualifiedByName = "formaCobrancaId")
    @Mapping(target = "regiaoOrigem", source = "regiaoOrigem", qualifiedByName = "regiaoId")
    @Mapping(target = "regiaoDestino", source = "regiaoDestino", qualifiedByName = "regiaoId")
    TabelaFreteDTO toDto(TabelaFrete s);

    @Named("embarcadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmbarcadorDTO toDtoEmbarcadorId(Embarcador embarcador);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);

    @Named("tipoCargaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoCargaDTO toDtoTipoCargaId(TipoCarga tipoCarga);

    @Named("tipoFreteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoFreteDTO toDtoTipoFreteId(TipoFrete tipoFrete);

    @Named("formaCobrancaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormaCobrancaDTO toDtoFormaCobrancaId(FormaCobranca formaCobranca);

    @Named("regiaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegiaoDTO toDtoRegiaoId(Regiao regiao);
}
