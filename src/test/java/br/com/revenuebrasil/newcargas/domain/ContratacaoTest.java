package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.ContratacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FaturaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TomadaPrecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContratacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contratacao.class);
        Contratacao contratacao1 = getContratacaoSample1();
        Contratacao contratacao2 = new Contratacao();
        assertThat(contratacao1).isNotEqualTo(contratacao2);

        contratacao2.setId(contratacao1.getId());
        assertThat(contratacao1).isEqualTo(contratacao2);

        contratacao2 = getContratacaoSample2();
        assertThat(contratacao1).isNotEqualTo(contratacao2);
    }

    @Test
    void faturaTest() throws Exception {
        Contratacao contratacao = getContratacaoRandomSampleGenerator();
        Fatura faturaBack = getFaturaRandomSampleGenerator();

        contratacao.addFatura(faturaBack);
        assertThat(contratacao.getFaturas()).containsOnly(faturaBack);
        assertThat(faturaBack.getContratacao()).isEqualTo(contratacao);

        contratacao.removeFatura(faturaBack);
        assertThat(contratacao.getFaturas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getContratacao()).isNull();

        contratacao.faturas(new HashSet<>(Set.of(faturaBack)));
        assertThat(contratacao.getFaturas()).containsOnly(faturaBack);
        assertThat(faturaBack.getContratacao()).isEqualTo(contratacao);

        contratacao.setFaturas(new HashSet<>());
        assertThat(contratacao.getFaturas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getContratacao()).isNull();
    }

    @Test
    void solicitacaoColetaTest() throws Exception {
        Contratacao contratacao = getContratacaoRandomSampleGenerator();
        TomadaPreco tomadaPrecoBack = getTomadaPrecoRandomSampleGenerator();

        contratacao.setSolicitacaoColeta(tomadaPrecoBack);
        assertThat(contratacao.getSolicitacaoColeta()).isEqualTo(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getContratacao()).isEqualTo(contratacao);

        contratacao.solicitacaoColeta(null);
        assertThat(contratacao.getSolicitacaoColeta()).isNull();
        assertThat(tomadaPrecoBack.getContratacao()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        Contratacao contratacao = getContratacaoRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        contratacao.setTransportadora(transportadoraBack);
        assertThat(contratacao.getTransportadora()).isEqualTo(transportadoraBack);

        contratacao.transportadora(null);
        assertThat(contratacao.getTransportadora()).isNull();
    }
}
