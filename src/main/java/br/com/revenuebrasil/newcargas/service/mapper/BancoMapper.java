package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Banco;
import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Banco} and its DTO {@link BancoDTO}.
 */
@Mapper(componentModel = "spring")
public interface BancoMapper extends EntityMapper<BancoDTO, Banco> {}
