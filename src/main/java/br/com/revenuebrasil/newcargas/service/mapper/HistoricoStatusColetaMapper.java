package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.service.dto.HistoricoStatusColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoricoStatusColeta} and its DTO {@link HistoricoStatusColetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoricoStatusColetaMapper extends EntityMapper<HistoricoStatusColetaDTO, HistoricoStatusColeta> {
    @Mapping(target = "solicitacaoColeta", source = "solicitacaoColeta", qualifiedByName = "solicitacaoColetaId")
    @Mapping(target = "roteirizacao", source = "roteirizacao", qualifiedByName = "roteirizacaoId")
    @Mapping(target = "statusColetaOrigem", source = "statusColetaOrigem", qualifiedByName = "statusColetaId")
    @Mapping(target = "statusColetaDestino", source = "statusColetaDestino", qualifiedByName = "statusColetaId")
    HistoricoStatusColetaDTO toDto(HistoricoStatusColeta s);

    @Named("solicitacaoColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SolicitacaoColetaDTO toDtoSolicitacaoColetaId(SolicitacaoColeta solicitacaoColeta);

    @Named("roteirizacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoteirizacaoDTO toDtoRoteirizacaoId(Roteirizacao roteirizacao);

    @Named("statusColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StatusColetaDTO toDtoStatusColetaId(StatusColeta statusColeta);
}
