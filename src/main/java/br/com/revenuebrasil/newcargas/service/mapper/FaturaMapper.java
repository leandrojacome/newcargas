package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Contratacao;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Fatura;
import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.ContratacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.dto.FaturaDTO;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fatura} and its DTO {@link FaturaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FaturaMapper extends EntityMapper<FaturaDTO, Fatura> {
    @Mapping(target = "embarcador", source = "embarcador", qualifiedByName = "embarcadorId")
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    @Mapping(target = "contratacao", source = "contratacao", qualifiedByName = "contratacaoId")
    @Mapping(target = "formaCobranca", source = "formaCobranca", qualifiedByName = "formaCobrancaId")
    FaturaDTO toDto(Fatura s);

    @Named("embarcadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmbarcadorDTO toDtoEmbarcadorId(Embarcador embarcador);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);

    @Named("contratacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContratacaoDTO toDtoContratacaoId(Contratacao contratacao);

    @Named("formaCobrancaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormaCobrancaDTO toDtoFormaCobrancaId(FormaCobranca formaCobranca);
}
