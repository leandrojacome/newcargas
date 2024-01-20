package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.ContratacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.RoteirizacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TomadaPrecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TomadaPrecoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TomadaPreco.class);
        TomadaPreco tomadaPreco1 = getTomadaPrecoSample1();
        TomadaPreco tomadaPreco2 = new TomadaPreco();
        assertThat(tomadaPreco1).isNotEqualTo(tomadaPreco2);

        tomadaPreco2.setId(tomadaPreco1.getId());
        assertThat(tomadaPreco1).isEqualTo(tomadaPreco2);

        tomadaPreco2 = getTomadaPrecoSample2();
        assertThat(tomadaPreco1).isNotEqualTo(tomadaPreco2);
    }

    @Test
    void contratacaoTest() throws Exception {
        TomadaPreco tomadaPreco = getTomadaPrecoRandomSampleGenerator();
        Contratacao contratacaoBack = getContratacaoRandomSampleGenerator();

        tomadaPreco.setContratacao(contratacaoBack);
        assertThat(tomadaPreco.getContratacao()).isEqualTo(contratacaoBack);

        tomadaPreco.contratacao(null);
        assertThat(tomadaPreco.getContratacao()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        TomadaPreco tomadaPreco = getTomadaPrecoRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        tomadaPreco.setTransportadora(transportadoraBack);
        assertThat(tomadaPreco.getTransportadora()).isEqualTo(transportadoraBack);

        tomadaPreco.transportadora(null);
        assertThat(tomadaPreco.getTransportadora()).isNull();
    }

    @Test
    void roteirizacaoTest() throws Exception {
        TomadaPreco tomadaPreco = getTomadaPrecoRandomSampleGenerator();
        Roteirizacao roteirizacaoBack = getRoteirizacaoRandomSampleGenerator();

        tomadaPreco.setRoteirizacao(roteirizacaoBack);
        assertThat(tomadaPreco.getRoteirizacao()).isEqualTo(roteirizacaoBack);

        tomadaPreco.roteirizacao(null);
        assertThat(tomadaPreco.getRoteirizacao()).isNull();
    }
}
