package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.BancoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.ContaBancariaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaBancariaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaBancaria.class);
        ContaBancaria contaBancaria1 = getContaBancariaSample1();
        ContaBancaria contaBancaria2 = new ContaBancaria();
        assertThat(contaBancaria1).isNotEqualTo(contaBancaria2);

        contaBancaria2.setId(contaBancaria1.getId());
        assertThat(contaBancaria1).isEqualTo(contaBancaria2);

        contaBancaria2 = getContaBancariaSample2();
        assertThat(contaBancaria1).isNotEqualTo(contaBancaria2);
    }

    @Test
    void bancoTest() throws Exception {
        ContaBancaria contaBancaria = getContaBancariaRandomSampleGenerator();
        Banco bancoBack = getBancoRandomSampleGenerator();

        contaBancaria.setBanco(bancoBack);
        assertThat(contaBancaria.getBanco()).isEqualTo(bancoBack);

        contaBancaria.banco(null);
        assertThat(contaBancaria.getBanco()).isNull();
    }

    @Test
    void embarcadorTest() throws Exception {
        ContaBancaria contaBancaria = getContaBancariaRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        contaBancaria.setEmbarcador(embarcadorBack);
        assertThat(contaBancaria.getEmbarcador()).isEqualTo(embarcadorBack);

        contaBancaria.embarcador(null);
        assertThat(contaBancaria.getEmbarcador()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        ContaBancaria contaBancaria = getContaBancariaRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        contaBancaria.setTransportadora(transportadoraBack);
        assertThat(contaBancaria.getTransportadora()).isEqualTo(transportadoraBack);

        contaBancaria.transportadora(null);
        assertThat(contaBancaria.getTransportadora()).isNull();
    }
}
