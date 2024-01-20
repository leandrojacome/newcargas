package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.service.dto.FormaCobrancaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormaCobranca} and its DTO {@link FormaCobrancaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormaCobrancaMapper extends EntityMapper<FormaCobrancaDTO, FormaCobranca> {}
