package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.EnderecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.HistoricoStatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.NotaFiscalColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.RoteirizacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.StatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TipoVeiculoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SolicitacaoColetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoColeta.class);
        SolicitacaoColeta solicitacaoColeta1 = getSolicitacaoColetaSample1();
        SolicitacaoColeta solicitacaoColeta2 = new SolicitacaoColeta();
        assertThat(solicitacaoColeta1).isNotEqualTo(solicitacaoColeta2);

        solicitacaoColeta2.setId(solicitacaoColeta1.getId());
        assertThat(solicitacaoColeta1).isEqualTo(solicitacaoColeta2);

        solicitacaoColeta2 = getSolicitacaoColetaSample2();
        assertThat(solicitacaoColeta1).isNotEqualTo(solicitacaoColeta2);
    }

    @Test
    void notaFiscalColetaTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        NotaFiscalColeta notaFiscalColetaBack = getNotaFiscalColetaRandomSampleGenerator();

        solicitacaoColeta.addNotaFiscalColeta(notaFiscalColetaBack);
        assertThat(solicitacaoColeta.getNotaFiscalColetas()).containsOnly(notaFiscalColetaBack);
        assertThat(notaFiscalColetaBack.getSolicitacaoColeta()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.removeNotaFiscalColeta(notaFiscalColetaBack);
        assertThat(solicitacaoColeta.getNotaFiscalColetas()).doesNotContain(notaFiscalColetaBack);
        assertThat(notaFiscalColetaBack.getSolicitacaoColeta()).isNull();

        solicitacaoColeta.notaFiscalColetas(new HashSet<>(Set.of(notaFiscalColetaBack)));
        assertThat(solicitacaoColeta.getNotaFiscalColetas()).containsOnly(notaFiscalColetaBack);
        assertThat(notaFiscalColetaBack.getSolicitacaoColeta()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.setNotaFiscalColetas(new HashSet<>());
        assertThat(solicitacaoColeta.getNotaFiscalColetas()).doesNotContain(notaFiscalColetaBack);
        assertThat(notaFiscalColetaBack.getSolicitacaoColeta()).isNull();
    }

    @Test
    void enderecoOrigemTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        solicitacaoColeta.addEnderecoOrigem(enderecoBack);
        assertThat(solicitacaoColeta.getEnderecoOrigems()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaOrigem()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.removeEnderecoOrigem(enderecoBack);
        assertThat(solicitacaoColeta.getEnderecoOrigems()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaOrigem()).isNull();

        solicitacaoColeta.enderecoOrigems(new HashSet<>(Set.of(enderecoBack)));
        assertThat(solicitacaoColeta.getEnderecoOrigems()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaOrigem()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.setEnderecoOrigems(new HashSet<>());
        assertThat(solicitacaoColeta.getEnderecoOrigems()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaOrigem()).isNull();
    }

    @Test
    void enderecoDestinoTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        solicitacaoColeta.addEnderecoDestino(enderecoBack);
        assertThat(solicitacaoColeta.getEnderecoDestinos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaDestino()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.removeEnderecoDestino(enderecoBack);
        assertThat(solicitacaoColeta.getEnderecoDestinos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaDestino()).isNull();

        solicitacaoColeta.enderecoDestinos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(solicitacaoColeta.getEnderecoDestinos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaDestino()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.setEnderecoDestinos(new HashSet<>());
        assertThat(solicitacaoColeta.getEnderecoDestinos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getSolicitacaoColetaDestino()).isNull();
    }

    @Test
    void historicoStatusColetaTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        HistoricoStatusColeta historicoStatusColetaBack = getHistoricoStatusColetaRandomSampleGenerator();

        solicitacaoColeta.addHistoricoStatusColeta(historicoStatusColetaBack);
        assertThat(solicitacaoColeta.getHistoricoStatusColetas()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getSolicitacaoColeta()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.removeHistoricoStatusColeta(historicoStatusColetaBack);
        assertThat(solicitacaoColeta.getHistoricoStatusColetas()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getSolicitacaoColeta()).isNull();

        solicitacaoColeta.historicoStatusColetas(new HashSet<>(Set.of(historicoStatusColetaBack)));
        assertThat(solicitacaoColeta.getHistoricoStatusColetas()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getSolicitacaoColeta()).isEqualTo(solicitacaoColeta);

        solicitacaoColeta.setHistoricoStatusColetas(new HashSet<>());
        assertThat(solicitacaoColeta.getHistoricoStatusColetas()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getSolicitacaoColeta()).isNull();
    }

    @Test
    void embarcadorTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        solicitacaoColeta.setEmbarcador(embarcadorBack);
        assertThat(solicitacaoColeta.getEmbarcador()).isEqualTo(embarcadorBack);

        solicitacaoColeta.embarcador(null);
        assertThat(solicitacaoColeta.getEmbarcador()).isNull();
    }

    @Test
    void statusColetaTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        StatusColeta statusColetaBack = getStatusColetaRandomSampleGenerator();

        solicitacaoColeta.setStatusColeta(statusColetaBack);
        assertThat(solicitacaoColeta.getStatusColeta()).isEqualTo(statusColetaBack);

        solicitacaoColeta.statusColeta(null);
        assertThat(solicitacaoColeta.getStatusColeta()).isNull();
    }

    @Test
    void roteirizacaoTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        Roteirizacao roteirizacaoBack = getRoteirizacaoRandomSampleGenerator();

        solicitacaoColeta.setRoteirizacao(roteirizacaoBack);
        assertThat(solicitacaoColeta.getRoteirizacao()).isEqualTo(roteirizacaoBack);

        solicitacaoColeta.roteirizacao(null);
        assertThat(solicitacaoColeta.getRoteirizacao()).isNull();
    }

    @Test
    void tipoVeiculoTest() throws Exception {
        SolicitacaoColeta solicitacaoColeta = getSolicitacaoColetaRandomSampleGenerator();
        TipoVeiculo tipoVeiculoBack = getTipoVeiculoRandomSampleGenerator();

        solicitacaoColeta.setTipoVeiculo(tipoVeiculoBack);
        assertThat(solicitacaoColeta.getTipoVeiculo()).isEqualTo(tipoVeiculoBack);

        solicitacaoColeta.tipoVeiculo(null);
        assertThat(solicitacaoColeta.getTipoVeiculo()).isNull();
    }
}
