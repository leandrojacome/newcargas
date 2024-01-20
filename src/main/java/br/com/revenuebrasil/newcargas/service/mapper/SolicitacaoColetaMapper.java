package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SolicitacaoColeta} and its DTO {@link SolicitacaoColetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SolicitacaoColetaMapper extends EntityMapper<SolicitacaoColetaDTO, SolicitacaoColeta> {
    @Mapping(target = "embarcador", source = "embarcador", qualifiedByName = "embarcadorId")
    @Mapping(target = "statusColeta", source = "statusColeta", qualifiedByName = "statusColetaId")
    @Mapping(target = "roteirizacao", source = "roteirizacao", qualifiedByName = "roteirizacaoId")
    @Mapping(target = "tipoVeiculo", source = "tipoVeiculo", qualifiedByName = "tipoVeiculoId")
    SolicitacaoColetaDTO toDto(SolicitacaoColeta s);

    @Named("embarcadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmbarcadorDTO toDtoEmbarcadorId(Embarcador embarcador);

    @Named("statusColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StatusColetaDTO toDtoStatusColetaId(StatusColeta statusColeta);

    @Named("roteirizacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoteirizacaoDTO toDtoRoteirizacaoId(Roteirizacao roteirizacao);

    @Named("tipoVeiculoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoVeiculoDTO toDtoTipoVeiculoId(TipoVeiculo tipoVeiculo);
}
