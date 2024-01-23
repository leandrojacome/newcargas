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
    void embarcadorTest() throws Exception {
        Cidade cidade = getCidadeRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        cidade.addEmbarcador(embarcadorBack);
        assertThat(cidade.getEmbarcadors()).containsOnly(embarcadorBack);
        assertThat(embarcadorBack.getCidade()).isEqualTo(cidade);

        cidade.removeEmbarcador(embarcadorBack);
        assertThat(cidade.getEmbarcadors()).doesNotContain(embarcadorBack);
        assertThat(embarcadorBack.getCidade()).isNull();

        cidade.embarcadors(new HashSet<>(Set.of(embarcadorBack)));
        assertThat(cidade.getEmbarcadors()).containsOnly(embarcadorBack);
        assertThat(embarcadorBack.getCidade()).isEqualTo(cidade);

        cidade.setEmbarcadors(new HashSet<>());
        assertThat(cidade.getEmbarcadors()).doesNotContain(embarcadorBack);
        assertThat(embarcadorBack.getCidade()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        Cidade cidade = getCidadeRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        cidade.addTransportadora(transportadoraBack);
        assertThat(cidade.getTransportadoras()).containsOnly(transportadoraBack);
        assertThat(transportadoraBack.getCidade()).isEqualTo(cidade);

        cidade.removeTransportadora(transportadoraBack);
        assertThat(cidade.getTransportadoras()).doesNotContain(transportadoraBack);
        assertThat(transportadoraBack.getCidade()).isNull();

        cidade.transportadoras(new HashSet<>(Set.of(transportadoraBack)));
        assertThat(cidade.getTransportadoras()).containsOnly(transportadoraBack);
        assertThat(transportadoraBack.getCidade()).isEqualTo(cidade);

        cidade.setTransportadoras(new HashSet<>());
        assertThat(cidade.getTransportadoras()).doesNotContain(transportadoraBack);
        assertThat(transportadoraBack.getCidade()).isNull();
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
}
