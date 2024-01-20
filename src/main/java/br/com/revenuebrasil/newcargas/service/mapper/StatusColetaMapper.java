package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.service.dto.StatusColetaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StatusColeta} and its DTO {@link StatusColetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatusColetaMapper extends EntityMapper<StatusColetaDTO, StatusColeta> {
    @Mapping(target = "statusColetaOrigems", source = "statusColetaOrigems", qualifiedByName = "statusColetaIdSet")
    StatusColetaDTO toDto(StatusColeta s);

    @Mapping(target = "removeStatusColetaOrigem", ignore = true)
    StatusColeta toEntity(StatusColetaDTO statusColetaDTO);

    @Named("statusColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StatusColetaDTO toDtoStatusColetaId(StatusColeta statusColeta);

    @Named("statusColetaIdSet")
    default Set<StatusColetaDTO> toDtoStatusColetaIdSet(Set<StatusColeta> statusColeta) {
        return statusColeta.stream().map(this::toDtoStatusColetaId).collect(Collectors.toSet());
    }
}
