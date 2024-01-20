package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.service.dto.RegiaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Regiao} and its DTO {@link RegiaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegiaoMapper extends EntityMapper<RegiaoDTO, Regiao> {}
