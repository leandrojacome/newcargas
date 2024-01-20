package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.HistoricoStatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.RoteirizacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.StatusColetaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricoStatusColetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricoStatusColeta.class);
        HistoricoStatusColeta historicoStatusColeta1 = getHistoricoStatusColetaSample1();
        HistoricoStatusColeta historicoStatusColeta2 = new HistoricoStatusColeta();
        assertThat(historicoStatusColeta1).isNotEqualTo(historicoStatusColeta2);

        historicoStatusColeta2.setId(historicoStatusColeta1.getId());
        assertThat(historicoStatusColeta1).isEqualTo(historicoStatusColeta2);

        historicoStatusColeta2 = getHistoricoStatusColetaSample2();
        assertThat(historicoStatusColeta1).isNotEqualTo(historicoStatusColeta2);
    }

    @Test
    void solicitacaoColetaTest() throws Exception {
        HistoricoStatusColeta historicoStatusColeta = getHistoricoStatusColetaRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        historicoStatusColeta.setSolicitacaoColeta(solicitacaoColetaBack);
        assertThat(historicoStatusColeta.getSolicitacaoColeta()).isEqualTo(solicitacaoColetaBack);

        historicoStatusColeta.solicitacaoColeta(null);
        assertThat(historicoStatusColeta.getSolicitacaoColeta()).isNull();
    }

    @Test
    void roteirizacaoTest() throws Exception {
        HistoricoStatusColeta historicoStatusColeta = getHistoricoStatusColetaRandomSampleGenerator();
        Roteirizacao roteirizacaoBack = getRoteirizacaoRandomSampleGenerator();

        historicoStatusColeta.setRoteirizacao(roteirizacaoBack);
        assertThat(historicoStatusColeta.getRoteirizacao()).isEqualTo(roteirizacaoBack);

        historicoStatusColeta.roteirizacao(null);
        assertThat(historicoStatusColeta.getRoteirizacao()).isNull();
    }

    @Test
    void statusColetaOrigemTest() throws Exception {
        HistoricoStatusColeta historicoStatusColeta = getHistoricoStatusColetaRandomSampleGenerator();
        StatusColeta statusColetaBack = getStatusColetaRandomSampleGenerator();

        historicoStatusColeta.setStatusColetaOrigem(statusColetaBack);
        assertThat(historicoStatusColeta.getStatusColetaOrigem()).isEqualTo(statusColetaBack);

        historicoStatusColeta.statusColetaOrigem(null);
        assertThat(historicoStatusColeta.getStatusColetaOrigem()).isNull();
    }

    @Test
    void statusColetaDestinoTest() throws Exception {
        HistoricoStatusColeta historicoStatusColeta = getHistoricoStatusColetaRandomSampleGenerator();
        StatusColeta statusColetaBack = getStatusColetaRandomSampleGenerator();

        historicoStatusColeta.setStatusColetaDestino(statusColetaBack);
        assertThat(historicoStatusColeta.getStatusColetaDestino()).isEqualTo(statusColetaBack);

        historicoStatusColeta.statusColetaDestino(null);
        assertThat(historicoStatusColeta.getStatusColetaDestino()).isNull();
    }
}
