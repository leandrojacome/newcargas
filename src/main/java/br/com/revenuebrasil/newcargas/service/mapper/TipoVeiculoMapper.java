package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.service.dto.TipoVeiculoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoVeiculo} and its DTO {@link TipoVeiculoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoVeiculoMapper extends EntityMapper<TipoVeiculoDTO, TipoVeiculo> {}
