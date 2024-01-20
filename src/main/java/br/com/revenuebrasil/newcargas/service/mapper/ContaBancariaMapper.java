package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Banco;
import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.BancoDTO;
import br.com.revenuebrasil.newcargas.service.dto.ContaBancariaDTO;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaBancaria} and its DTO {@link ContaBancariaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContaBancariaMapper extends EntityMapper<ContaBancariaDTO, ContaBancaria> {
    @Mapping(target = "banco", source = "banco", qualifiedByName = "bancoId")
    @Mapping(target = "embarcador", source = "embarcador", qualifiedByName = "embarcadorId")
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    ContaBancariaDTO toDto(ContaBancaria s);

    @Named("bancoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BancoDTO toDtoBancoId(Banco banco);

    @Named("embarcadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmbarcadorDTO toDtoEmbarcadorId(Embarcador embarcador);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);
}
