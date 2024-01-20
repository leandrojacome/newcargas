package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotaFiscalColeta} and its DTO {@link NotaFiscalColetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotaFiscalColetaMapper extends EntityMapper<NotaFiscalColetaDTO, NotaFiscalColeta> {
    @Mapping(target = "solicitacaoColeta", source = "solicitacaoColeta", qualifiedByName = "solicitacaoColetaId")
    NotaFiscalColetaDTO toDto(NotaFiscalColeta s);

    @Named("solicitacaoColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SolicitacaoColetaDTO toDtoSolicitacaoColetaId(SolicitacaoColeta solicitacaoColeta);
}
