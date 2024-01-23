package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.CidadeTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.ContaBancariaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EnderecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FaturaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.NotificacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmbarcadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Embarcador.class);
        Embarcador embarcador1 = getEmbarcadorSample1();
        Embarcador embarcador2 = new Embarcador();
        assertThat(embarcador1).isNotEqualTo(embarcador2);

        embarcador2.setId(embarcador1.getId());
        assertThat(embarcador1).isEqualTo(embarcador2);

        embarcador2 = getEmbarcadorSample2();
        assertThat(embarcador1).isNotEqualTo(embarcador2);
    }

    @Test
    void enderecoTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        embarcador.addEndereco(enderecoBack);
        assertThat(embarcador.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.removeEndereco(enderecoBack);
        assertThat(embarcador.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getEmbarcador()).isNull();

        embarcador.enderecos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(embarcador.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.setEnderecos(new HashSet<>());
        assertThat(embarcador.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getEmbarcador()).isNull();
    }

    @Test
    void contaBancariaTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        ContaBancaria contaBancariaBack = getContaBancariaRandomSampleGenerator();

        embarcador.addContaBancaria(contaBancariaBack);
        assertThat(embarcador.getContaBancarias()).containsOnly(contaBancariaBack);
        assertThat(contaBancariaBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.removeContaBancaria(contaBancariaBack);
        assertThat(embarcador.getContaBancarias()).doesNotContain(contaBancariaBack);
        assertThat(contaBancariaBack.getEmbarcador()).isNull();

        embarcador.contaBancarias(new HashSet<>(Set.of(contaBancariaBack)));
        assertThat(embarcador.getContaBancarias()).containsOnly(contaBancariaBack);
        assertThat(contaBancariaBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.setContaBancarias(new HashSet<>());
        assertThat(embarcador.getContaBancarias()).doesNotContain(contaBancariaBack);
        assertThat(contaBancariaBack.getEmbarcador()).isNull();
    }

    @Test
    void tabelaFreteTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        embarcador.addTabelaFrete(tabelaFreteBack);
        assertThat(embarcador.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.removeTabelaFrete(tabelaFreteBack);
        assertThat(embarcador.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getEmbarcador()).isNull();

        embarcador.tabelaFretes(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(embarcador.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.setTabelaFretes(new HashSet<>());
        assertThat(embarcador.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getEmbarcador()).isNull();
    }

    @Test
    void solitacaoColetaTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        embarcador.addSolitacaoColeta(solicitacaoColetaBack);
        assertThat(embarcador.getSolitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.removeSolitacaoColeta(solicitacaoColetaBack);
        assertThat(embarcador.getSolitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getEmbarcador()).isNull();

        embarcador.solitacaoColetas(new HashSet<>(Set.of(solicitacaoColetaBack)));
        assertThat(embarcador.getSolitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.setSolitacaoColetas(new HashSet<>());
        assertThat(embarcador.getSolitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getEmbarcador()).isNull();
    }

    @Test
    void notificacaoTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        Notificacao notificacaoBack = getNotificacaoRandomSampleGenerator();

        embarcador.addNotificacao(notificacaoBack);
        assertThat(embarcador.getNotificacaos()).containsOnly(notificacaoBack);
        assertThat(notificacaoBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.removeNotificacao(notificacaoBack);
        assertThat(embarcador.getNotificacaos()).doesNotContain(notificacaoBack);
        assertThat(notificacaoBack.getEmbarcador()).isNull();

        embarcador.notificacaos(new HashSet<>(Set.of(notificacaoBack)));
        assertThat(embarcador.getNotificacaos()).containsOnly(notificacaoBack);
        assertThat(notificacaoBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.setNotificacaos(new HashSet<>());
        assertThat(embarcador.getNotificacaos()).doesNotContain(notificacaoBack);
        assertThat(notificacaoBack.getEmbarcador()).isNull();
    }

    @Test
    void faturaTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        Fatura faturaBack = getFaturaRandomSampleGenerator();

        embarcador.addFatura(faturaBack);
        assertThat(embarcador.getFaturas()).containsOnly(faturaBack);
        assertThat(faturaBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.removeFatura(faturaBack);
        assertThat(embarcador.getFaturas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getEmbarcador()).isNull();

        embarcador.faturas(new HashSet<>(Set.of(faturaBack)));
        assertThat(embarcador.getFaturas()).containsOnly(faturaBack);
        assertThat(faturaBack.getEmbarcador()).isEqualTo(embarcador);

        embarcador.setFaturas(new HashSet<>());
        assertThat(embarcador.getFaturas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getEmbarcador()).isNull();
    }

    @Test
    void cidadeTest() throws Exception {
        Embarcador embarcador = getEmbarcadorRandomSampleGenerator();
        Cidade cidadeBack = getCidadeRandomSampleGenerator();

        embarcador.setCidade(cidadeBack);
        assertThat(embarcador.getCidade()).isEqualTo(cidadeBack);

        embarcador.cidade(null);
        assertThat(embarcador.getCidade()).isNull();
    }
}
