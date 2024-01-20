package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.CidadeTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EnderecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EstadoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CidadeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cidade.class);
        Cidade cidade1 = getCidadeSample1();
        Cidade cidade2 = new Cidade();
        assertThat(cidade1).isNotEqualTo(cidade2);

        cidade2.setId(cidade1.getId());
        assertThat(cidade1).isEqualTo(cidade2);

        cidade2 = getCidadeSample2();
        assertThat(cidade1).isNotEqualTo(cidade2);
    }

    @Test
    void enderecoTest() throws Exception {
        Cidade cidade = getCidadeRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        cidade.addEndereco(enderecoBack);
        assertThat(cidade.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getCidade()).isEqualTo(cidade);

        cidade.removeEndereco(enderecoBack);
        assertThat(cidade.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getCidade()).isNull();

        cidade.enderecos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(cidade.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getCidade()).isEqualTo(cidade);

        cidade.setEnderecos(new HashSet<>());
        assertThat(cidade.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getCidade()).isNull();
    }

    @Test
    void estadoTest() throws Exception {
        Cidade cidade = getCidadeRandomSampleGenerator();
        Estado estadoBack = getEstadoRandomSampleGenerator();

        cidade.setEstado(estadoBack);
        assertThat(cidade.getEstado()).isEqualTo(estadoBack);

        cidade.estado(null);
        assertThat(cidade.getEstado()).isNull();
    }

    @Test
    void embarcadorTest() throws Exception {
        Cidade cidade = getCidadeRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        cidade.setEmbarcador(embarcadorBack);
        assertThat(cidade.getEmbarcador()).isEqualTo(embarcadorBack);

        cidade.embarcador(null);
        assertThat(cidade.getEmbarcador()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        Cidade cidade = getCidadeRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        cidade.setTransportadora(transportadoraBack);
        assertThat(cidade.getTransportadora()).isEqualTo(transportadoraBack);

        cidade.transportadora(null);
        assertThat(cidade.getTransportadora()).isNull();
    }
}
