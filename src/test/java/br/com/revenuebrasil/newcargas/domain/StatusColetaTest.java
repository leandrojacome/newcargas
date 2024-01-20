package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.HistoricoStatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.RoteirizacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.StatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.StatusColetaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StatusColetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusColeta.class);
        StatusColeta statusColeta1 = getStatusColetaSample1();
        StatusColeta statusColeta2 = new StatusColeta();
        assertThat(statusColeta1).isNotEqualTo(statusColeta2);

        statusColeta2.setId(statusColeta1.getId());
        assertThat(statusColeta1).isEqualTo(statusColeta2);

        statusColeta2 = getStatusColetaSample2();
        assertThat(statusColeta1).isNotEqualTo(statusColeta2);
    }

    @Test
    void solicitacaoColetaTest() throws Exception {
        StatusColeta statusColeta = getStatusColetaRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        statusColeta.addSolicitacaoColeta(solicitacaoColetaBack);
        assertThat(statusColeta.getSolicitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getStatusColeta()).isEqualTo(statusColeta);

        statusColeta.removeSolicitacaoColeta(solicitacaoColetaBack);
        assertThat(statusColeta.getSolicitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getStatusColeta()).isNull();

        statusColeta.solicitacaoColetas(new HashSet<>(Set.of(solicitacaoColetaBack)));
        assertThat(statusColeta.getSolicitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getStatusColeta()).isEqualTo(statusColeta);

        statusColeta.setSolicitacaoColetas(new HashSet<>());
        assertThat(statusColeta.getSolicitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getStatusColeta()).isNull();
    }

    @Test
    void historicoStatusColetaOrigemTest() throws Exception {
        StatusColeta statusColeta = getStatusColetaRandomSampleGenerator();
        HistoricoStatusColeta historicoStatusColetaBack = getHistoricoStatusColetaRandomSampleGenerator();

        statusColeta.addHistoricoStatusColetaOrigem(historicoStatusColetaBack);
        assertThat(statusColeta.getHistoricoStatusColetaOrigems()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaOrigem()).isEqualTo(statusColeta);

        statusColeta.removeHistoricoStatusColetaOrigem(historicoStatusColetaBack);
        assertThat(statusColeta.getHistoricoStatusColetaOrigems()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaOrigem()).isNull();

        statusColeta.historicoStatusColetaOrigems(new HashSet<>(Set.of(historicoStatusColetaBack)));
        assertThat(statusColeta.getHistoricoStatusColetaOrigems()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaOrigem()).isEqualTo(statusColeta);

        statusColeta.setHistoricoStatusColetaOrigems(new HashSet<>());
        assertThat(statusColeta.getHistoricoStatusColetaOrigems()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaOrigem()).isNull();
    }

    @Test
    void historicoStatusColetaDestinoTest() throws Exception {
        StatusColeta statusColeta = getStatusColetaRandomSampleGenerator();
        HistoricoStatusColeta historicoStatusColetaBack = getHistoricoStatusColetaRandomSampleGenerator();

        statusColeta.addHistoricoStatusColetaDestino(historicoStatusColetaBack);
        assertThat(statusColeta.getHistoricoStatusColetaDestinos()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaDestino()).isEqualTo(statusColeta);

        statusColeta.removeHistoricoStatusColetaDestino(historicoStatusColetaBack);
        assertThat(statusColeta.getHistoricoStatusColetaDestinos()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaDestino()).isNull();

        statusColeta.historicoStatusColetaDestinos(new HashSet<>(Set.of(historicoStatusColetaBack)));
        assertThat(statusColeta.getHistoricoStatusColetaDestinos()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaDestino()).isEqualTo(statusColeta);

        statusColeta.setHistoricoStatusColetaDestinos(new HashSet<>());
        assertThat(statusColeta.getHistoricoStatusColetaDestinos()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getStatusColetaDestino()).isNull();
    }

    @Test
    void roteirizacaoTest() throws Exception {
        StatusColeta statusColeta = getStatusColetaRandomSampleGenerator();
        Roteirizacao roteirizacaoBack = getRoteirizacaoRandomSampleGenerator();

        statusColeta.addRoteirizacao(roteirizacaoBack);
        assertThat(statusColeta.getRoteirizacaos()).containsOnly(roteirizacaoBack);
        assertThat(roteirizacaoBack.getStatusColeta()).isEqualTo(statusColeta);

        statusColeta.removeRoteirizacao(roteirizacaoBack);
        assertThat(statusColeta.getRoteirizacaos()).doesNotContain(roteirizacaoBack);
        assertThat(roteirizacaoBack.getStatusColeta()).isNull();

        statusColeta.roteirizacaos(new HashSet<>(Set.of(roteirizacaoBack)));
        assertThat(statusColeta.getRoteirizacaos()).containsOnly(roteirizacaoBack);
        assertThat(roteirizacaoBack.getStatusColeta()).isEqualTo(statusColeta);

        statusColeta.setRoteirizacaos(new HashSet<>());
        assertThat(statusColeta.getRoteirizacaos()).doesNotContain(roteirizacaoBack);
        assertThat(roteirizacaoBack.getStatusColeta()).isNull();
    }

    @Test
    void statusColetaOrigemTest() throws Exception {
        StatusColeta statusColeta = getStatusColetaRandomSampleGenerator();
        StatusColeta statusColetaBack = getStatusColetaRandomSampleGenerator();

        statusColeta.addStatusColetaOrigem(statusColetaBack);
        assertThat(statusColeta.getStatusColetaOrigems()).containsOnly(statusColetaBack);

        statusColeta.removeStatusColetaOrigem(statusColetaBack);
        assertThat(statusColeta.getStatusColetaOrigems()).doesNotContain(statusColetaBack);

        statusColeta.statusColetaOrigems(new HashSet<>(Set.of(statusColetaBack)));
        assertThat(statusColeta.getStatusColetaOrigems()).containsOnly(statusColetaBack);

        statusColeta.setStatusColetaOrigems(new HashSet<>());
        assertThat(statusColeta.getStatusColetaOrigems()).doesNotContain(statusColetaBack);
    }

    @Test
    void statusColetaDestinoTest() throws Exception {
        StatusColeta statusColeta = getStatusColetaRandomSampleGenerator();
        StatusColeta statusColetaBack = getStatusColetaRandomSampleGenerator();

        statusColeta.addStatusColetaDestino(statusColetaBack);
        assertThat(statusColeta.getStatusColetaDestinos()).containsOnly(statusColetaBack);
        assertThat(statusColetaBack.getStatusColetaOrigems()).containsOnly(statusColeta);

        statusColeta.removeStatusColetaDestino(statusColetaBack);
        assertThat(statusColeta.getStatusColetaDestinos()).doesNotContain(statusColetaBack);
        assertThat(statusColetaBack.getStatusColetaOrigems()).doesNotContain(statusColeta);

        statusColeta.statusColetaDestinos(new HashSet<>(Set.of(statusColetaBack)));
        assertThat(statusColeta.getStatusColetaDestinos()).containsOnly(statusColetaBack);
        assertThat(statusColetaBack.getStatusColetaOrigems()).containsOnly(statusColeta);

        statusColeta.setStatusColetaDestinos(new HashSet<>());
        assertThat(statusColeta.getStatusColetaDestinos()).doesNotContain(statusColetaBack);
        assertThat(statusColetaBack.getStatusColetaOrigems()).doesNotContain(statusColeta);
    }
}
