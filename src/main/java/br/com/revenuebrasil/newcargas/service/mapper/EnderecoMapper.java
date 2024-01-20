package br.com.revenuebrasil.newcargas.service.mapper;

import br.com.revenuebrasil.newcargas.domain.Cidade;
import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.service.dto.CidadeDTO;
import br.com.revenuebrasil.newcargas.service.dto.EmbarcadorDTO;
import br.com.revenuebrasil.newcargas.service.dto.EnderecoDTO;
import br.com.revenuebrasil.newcargas.service.dto.NotaFiscalColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.SolicitacaoColetaDTO;
import br.com.revenuebrasil.newcargas.service.dto.TransportadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Endereco} and its DTO {@link EnderecoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnderecoMapper extends EntityMapper<EnderecoDTO, Endereco> {
    @Mapping(target = "cidade", source = "cidade", qualifiedByName = "cidadeId")
    @Mapping(target = "embarcador", source = "embarcador", qualifiedByName = "embarcadorId")
    @Mapping(target = "transportadora", source = "transportadora", qualifiedByName = "transportadoraId")
    @Mapping(target = "notaFiscalColetaOrigem", source = "notaFiscalColetaOrigem", qualifiedByName = "notaFiscalColetaId")
    @Mapping(target = "notaFiscalColetaDestino", source = "notaFiscalColetaDestino", qualifiedByName = "notaFiscalColetaId")
    @Mapping(target = "solicitacaoColetaOrigem", source = "solicitacaoColetaOrigem", qualifiedByName = "solicitacaoColetaId")
    @Mapping(target = "solicitacaoColetaDestino", source = "solicitacaoColetaDestino", qualifiedByName = "solicitacaoColetaId")
    EnderecoDTO toDto(Endereco s);

    @Named("cidadeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CidadeDTO toDtoCidadeId(Cidade cidade);

    @Named("embarcadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmbarcadorDTO toDtoEmbarcadorId(Embarcador embarcador);

    @Named("transportadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadoraDTO toDtoTransportadoraId(Transportadora transportadora);

    @Named("notaFiscalColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NotaFiscalColetaDTO toDtoNotaFiscalColetaId(NotaFiscalColeta notaFiscalColeta);

    @Named("solicitacaoColetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SolicitacaoColetaDTO toDtoSolicitacaoColetaId(SolicitacaoColeta solicitacaoColeta);
}
