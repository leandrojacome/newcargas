package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.service.dto.TipoCargaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoCarga} and its DTO {@link TipoCargaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoCargaMapper extends EntityMapper<TipoCargaDTO, TipoCarga> {}
