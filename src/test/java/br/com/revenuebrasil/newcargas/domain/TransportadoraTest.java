package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.CidadeTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.ContaBancariaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.ContratacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EnderecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FaturaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.NotificacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TomadaPrecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransportadoraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transportadora.class);
        Transportadora transportadora1 = getTransportadoraSample1();
        Transportadora transportadora2 = new Transportadora();
        assertThat(transportadora1).isNotEqualTo(transportadora2);

        transportadora2.setId(transportadora1.getId());
        assertThat(transportadora1).isEqualTo(transportadora2);

        transportadora2 = getTransportadoraSample2();
        assertThat(transportadora1).isNotEqualTo(transportadora2);
    }

    @Test
    void enderecoTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        transportadora.addEndereco(enderecoBack);
        assertThat(transportadora.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeEndereco(enderecoBack);
        assertThat(transportadora.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getTransportadora()).isNull();

        transportadora.enderecos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(transportadora.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setEnderecos(new HashSet<>());
        assertThat(transportadora.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getTransportadora()).isNull();
    }

    @Test
    void cidadeTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        Cidade cidadeBack = getCidadeRandomSampleGenerator();

        transportadora.addCidade(cidadeBack);
        assertThat(transportadora.getCidades()).containsOnly(cidadeBack);
        assertThat(cidadeBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeCidade(cidadeBack);
        assertThat(transportadora.getCidades()).doesNotContain(cidadeBack);
        assertThat(cidadeBack.getTransportadora()).isNull();

        transportadora.cidades(new HashSet<>(Set.of(cidadeBack)));
        assertThat(transportadora.getCidades()).containsOnly(cidadeBack);
        assertThat(cidadeBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setCidades(new HashSet<>());
        assertThat(transportadora.getCidades()).doesNotContain(cidadeBack);
        assertThat(cidadeBack.getTransportadora()).isNull();
    }

    @Test
    void contaBancariaTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        ContaBancaria contaBancariaBack = getContaBancariaRandomSampleGenerator();

        transportadora.addContaBancaria(contaBancariaBack);
        assertThat(transportadora.getContaBancarias()).containsOnly(contaBancariaBack);
        assertThat(contaBancariaBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeContaBancaria(contaBancariaBack);
        assertThat(transportadora.getContaBancarias()).doesNotContain(contaBancariaBack);
        assertThat(contaBancariaBack.getTransportadora()).isNull();

        transportadora.contaBancarias(new HashSet<>(Set.of(contaBancariaBack)));
        assertThat(transportadora.getContaBancarias()).containsOnly(contaBancariaBack);
        assertThat(contaBancariaBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setContaBancarias(new HashSet<>());
        assertThat(transportadora.getContaBancarias()).doesNotContain(contaBancariaBack);
        assertThat(contaBancariaBack.getTransportadora()).isNull();
    }

    @Test
    void tabelaFreteTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        transportadora.addTabelaFrete(tabelaFreteBack);
        assertThat(transportadora.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeTabelaFrete(tabelaFreteBack);
        assertThat(transportadora.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTransportadora()).isNull();

        transportadora.tabelaFretes(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(transportadora.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setTabelaFretes(new HashSet<>());
        assertThat(transportadora.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTransportadora()).isNull();
    }

    @Test
    void tomadaPrecoTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        TomadaPreco tomadaPrecoBack = getTomadaPrecoRandomSampleGenerator();

        transportadora.addTomadaPreco(tomadaPrecoBack);
        assertThat(transportadora.getTomadaPrecos()).containsOnly(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeTomadaPreco(tomadaPrecoBack);
        assertThat(transportadora.getTomadaPrecos()).doesNotContain(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getTransportadora()).isNull();

        transportadora.tomadaPrecos(new HashSet<>(Set.of(tomadaPrecoBack)));
        assertThat(transportadora.getTomadaPrecos()).containsOnly(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setTomadaPrecos(new HashSet<>());
        assertThat(transportadora.getTomadaPrecos()).doesNotContain(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getTransportadora()).isNull();
    }

    @Test
    void contratacaoTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        Contratacao contratacaoBack = getContratacaoRandomSampleGenerator();

        transportadora.addContratacao(contratacaoBack);
        assertThat(transportadora.getContratacaos()).containsOnly(contratacaoBack);
        assertThat(contratacaoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeContratacao(contratacaoBack);
        assertThat(transportadora.getContratacaos()).doesNotContain(contratacaoBack);
        assertThat(contratacaoBack.getTransportadora()).isNull();

        transportadora.contratacaos(new HashSet<>(Set.of(contratacaoBack)));
        assertThat(transportadora.getContratacaos()).containsOnly(contratacaoBack);
        assertThat(contratacaoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setContratacaos(new HashSet<>());
        assertThat(transportadora.getContratacaos()).doesNotContain(contratacaoBack);
        assertThat(contratacaoBack.getTransportadora()).isNull();
    }

    @Test
    void notificacaoTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        Notificacao notificacaoBack = getNotificacaoRandomSampleGenerator();

        transportadora.addNotificacao(notificacaoBack);
        assertThat(transportadora.getNotificacaos()).containsOnly(notificacaoBack);
        assertThat(notificacaoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeNotificacao(notificacaoBack);
        assertThat(transportadora.getNotificacaos()).doesNotContain(notificacaoBack);
        assertThat(notificacaoBack.getTransportadora()).isNull();

        transportadora.notificacaos(new HashSet<>(Set.of(notificacaoBack)));
        assertThat(transportadora.getNotificacaos()).containsOnly(notificacaoBack);
        assertThat(notificacaoBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setNotificacaos(new HashSet<>());
        assertThat(transportadora.getNotificacaos()).doesNotContain(notificacaoBack);
        assertThat(notificacaoBack.getTransportadora()).isNull();
    }

    @Test
    void faturaTest() throws Exception {
        Transportadora transportadora = getTransportadoraRandomSampleGenerator();
        Fatura faturaBack = getFaturaRandomSampleGenerator();

        transportadora.addFatura(faturaBack);
        assertThat(transportadora.getFaturas()).containsOnly(faturaBack);
        assertThat(faturaBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.removeFatura(faturaBack);
        assertThat(transportadora.getFaturas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getTransportadora()).isNull();

        transportadora.faturas(new HashSet<>(Set.of(faturaBack)));
        assertThat(transportadora.getFaturas()).containsOnly(faturaBack);
        assertThat(faturaBack.getTransportadora()).isEqualTo(transportadora);

        transportadora.setFaturas(new HashSet<>());
        assertThat(transportadora.getFaturas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getTransportadora()).isNull();
    }
}
