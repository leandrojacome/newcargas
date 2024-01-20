package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.CidadeTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EnderecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.NotaFiscalColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnderecoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endereco.class);
        Endereco endereco1 = getEnderecoSample1();
        Endereco endereco2 = new Endereco();
        assertThat(endereco1).isNotEqualTo(endereco2);

        endereco2.setId(endereco1.getId());
        assertThat(endereco1).isEqualTo(endereco2);

        endereco2 = getEnderecoSample2();
        assertThat(endereco1).isNotEqualTo(endereco2);
    }

    @Test
    void cidadeTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Cidade cidadeBack = getCidadeRandomSampleGenerator();

        endereco.setCidade(cidadeBack);
        assertThat(endereco.getCidade()).isEqualTo(cidadeBack);

        endereco.cidade(null);
        assertThat(endereco.getCidade()).isNull();
    }

    @Test
    void embarcadorTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        endereco.setEmbarcador(embarcadorBack);
        assertThat(endereco.getEmbarcador()).isEqualTo(embarcadorBack);

        endereco.embarcador(null);
        assertThat(endereco.getEmbarcador()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        endereco.setTransportadora(transportadoraBack);
        assertThat(endereco.getTransportadora()).isEqualTo(transportadoraBack);

        endereco.transportadora(null);
        assertThat(endereco.getTransportadora()).isNull();
    }

    @Test
    void notaFiscalColetaOrigemTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        NotaFiscalColeta notaFiscalColetaBack = getNotaFiscalColetaRandomSampleGenerator();

        endereco.setNotaFiscalColetaOrigem(notaFiscalColetaBack);
        assertThat(endereco.getNotaFiscalColetaOrigem()).isEqualTo(notaFiscalColetaBack);

        endereco.notaFiscalColetaOrigem(null);
        assertThat(endereco.getNotaFiscalColetaOrigem()).isNull();
    }

    @Test
    void notaFiscalColetaDestinoTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        NotaFiscalColeta notaFiscalColetaBack = getNotaFiscalColetaRandomSampleGenerator();

        endereco.setNotaFiscalColetaDestino(notaFiscalColetaBack);
        assertThat(endereco.getNotaFiscalColetaDestino()).isEqualTo(notaFiscalColetaBack);

        endereco.notaFiscalColetaDestino(null);
        assertThat(endereco.getNotaFiscalColetaDestino()).isNull();
    }

    @Test
    void solicitacaoColetaOrigemTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        endereco.setSolicitacaoColetaOrigem(solicitacaoColetaBack);
        assertThat(endereco.getSolicitacaoColetaOrigem()).isEqualTo(solicitacaoColetaBack);

        endereco.solicitacaoColetaOrigem(null);
        assertThat(endereco.getSolicitacaoColetaOrigem()).isNull();
    }

    @Test
    void solicitacaoColetaDestinoTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        endereco.setSolicitacaoColetaDestino(solicitacaoColetaBack);
        assertThat(endereco.getSolicitacaoColetaDestino()).isEqualTo(solicitacaoColetaBack);

        endereco.solicitacaoColetaDestino(null);
        assertThat(endereco.getSolicitacaoColetaDestino()).isNull();
    }
}
