package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Notificacao;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.dto.NotificacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notificacao} and its DTO {@link NotificacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificacaoMapper extends EntityMapper<NotificacaoDTO, Notificacao> {
    @Mapping(target = "embarcador", source = "embarcador", qualifiedByName = "embarcadorId")
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    NotificacaoDTO toDto(Notificacao s);

    @Named("embarcadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmbarcadorDTO toDtoEmbarcadorId(Embarcador embarcador);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);
}
