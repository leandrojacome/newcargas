package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Roteirizacao;
import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.service.dto.RoteirizacaoDTO;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Roteirizacao} and its DTO {@link RoteirizacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoteirizacaoMapper extends EntityMapper<RoteirizacaoDTO, Roteirizacao> {
    @Mapping(target = "statusColeta", source = "statusColeta", qualifiedByName = "statusColetaId")
    RoteirizacaoDTO toDto(Roteirizacao s);

    @Named("statusColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StatusColetaDTO toDtoStatusColetaId(StatusColeta statusColeta);
}
