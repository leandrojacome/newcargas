package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.ContratacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FaturaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FormaCobrancaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FaturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fatura.class);
        Fatura fatura1 = getFaturaSample1();
        Fatura fatura2 = new Fatura();
        assertThat(fatura1).isNotEqualTo(fatura2);

        fatura2.setId(fatura1.getId());
        assertThat(fatura1).isEqualTo(fatura2);

        fatura2 = getFaturaSample2();
        assertThat(fatura1).isNotEqualTo(fatura2);
    }

    @Test
    void embarcadorTest() throws Exception {
        Fatura fatura = getFaturaRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        fatura.setEmbarcador(embarcadorBack);
        assertThat(fatura.getEmbarcador()).isEqualTo(embarcadorBack);

        fatura.embarcador(null);
        assertThat(fatura.getEmbarcador()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        Fatura fatura = getFaturaRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        fatura.setTransportadora(transportadoraBack);
        assertThat(fatura.getTransportadora()).isEqualTo(transportadoraBack);

        fatura.transportadora(null);
        assertThat(fatura.getTransportadora()).isNull();
    }

    @Test
    void contratacaoTest() throws Exception {
        Fatura fatura = getFaturaRandomSampleGenerator();
        Contratacao contratacaoBack = getContratacaoRandomSampleGenerator();

        fatura.setContratacao(contratacaoBack);
        assertThat(fatura.getContratacao()).isEqualTo(contratacaoBack);

        fatura.contratacao(null);
        assertThat(fatura.getContratacao()).isNull();
    }

    @Test
    void formaCobrancaTest() throws Exception {
        Fatura fatura = getFaturaRandomSampleGenerator();
        FormaCobranca formaCobrancaBack = getFormaCobrancaRandomSampleGenerator();

        fatura.setFormaCobranca(formaCobrancaBack);
        assertThat(fatura.getFormaCobranca()).isEqualTo(formaCobrancaBack);

        fatura.formaCobranca(null);
        assertThat(fatura.getFormaCobranca()).isNull();
    }
}
