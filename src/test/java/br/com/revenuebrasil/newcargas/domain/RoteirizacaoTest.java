package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.HistoricoStatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.RoteirizacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.StatusColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TomadaPrecoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoteirizacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roteirizacao.class);
        Roteirizacao roteirizacao1 = getRoteirizacaoSample1();
        Roteirizacao roteirizacao2 = new Roteirizacao();
        assertThat(roteirizacao1).isNotEqualTo(roteirizacao2);

        roteirizacao2.setId(roteirizacao1.getId());
        assertThat(roteirizacao1).isEqualTo(roteirizacao2);

        roteirizacao2 = getRoteirizacaoSample2();
        assertThat(roteirizacao1).isNotEqualTo(roteirizacao2);
    }

    @Test
    void historicoStatusColetaTest() throws Exception {
        Roteirizacao roteirizacao = getRoteirizacaoRandomSampleGenerator();
        HistoricoStatusColeta historicoStatusColetaBack = getHistoricoStatusColetaRandomSampleGenerator();

        roteirizacao.addHistoricoStatusColeta(historicoStatusColetaBack);
        assertThat(roteirizacao.getHistoricoStatusColetas()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getRoteirizacao()).isEqualTo(roteirizacao);

        roteirizacao.removeHistoricoStatusColeta(historicoStatusColetaBack);
        assertThat(roteirizacao.getHistoricoStatusColetas()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getRoteirizacao()).isNull();

        roteirizacao.historicoStatusColetas(new HashSet<>(Set.of(historicoStatusColetaBack)));
        assertThat(roteirizacao.getHistoricoStatusColetas()).containsOnly(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getRoteirizacao()).isEqualTo(roteirizacao);

        roteirizacao.setHistoricoStatusColetas(new HashSet<>());
        assertThat(roteirizacao.getHistoricoStatusColetas()).doesNotContain(historicoStatusColetaBack);
        assertThat(historicoStatusColetaBack.getRoteirizacao()).isNull();
    }

    @Test
    void solitacaoColetaTest() throws Exception {
        Roteirizacao roteirizacao = getRoteirizacaoRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        roteirizacao.addSolitacaoColeta(solicitacaoColetaBack);
        assertThat(roteirizacao.getSolitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getRoteirizacao()).isEqualTo(roteirizacao);

        roteirizacao.removeSolitacaoColeta(solicitacaoColetaBack);
        assertThat(roteirizacao.getSolitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getRoteirizacao()).isNull();

        roteirizacao.solitacaoColetas(new HashSet<>(Set.of(solicitacaoColetaBack)));
        assertThat(roteirizacao.getSolitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getRoteirizacao()).isEqualTo(roteirizacao);

        roteirizacao.setSolitacaoColetas(new HashSet<>());
        assertThat(roteirizacao.getSolitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getRoteirizacao()).isNull();
    }

    @Test
    void tomadaPrecoTest() throws Exception {
        Roteirizacao roteirizacao = getRoteirizacaoRandomSampleGenerator();
        TomadaPreco tomadaPrecoBack = getTomadaPrecoRandomSampleGenerator();

        roteirizacao.addTomadaPreco(tomadaPrecoBack);
        assertThat(roteirizacao.getTomadaPrecos()).containsOnly(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getRoteirizacao()).isEqualTo(roteirizacao);

        roteirizacao.removeTomadaPreco(tomadaPrecoBack);
        assertThat(roteirizacao.getTomadaPrecos()).doesNotContain(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getRoteirizacao()).isNull();

        roteirizacao.tomadaPrecos(new HashSet<>(Set.of(tomadaPrecoBack)));
        assertThat(roteirizacao.getTomadaPrecos()).containsOnly(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getRoteirizacao()).isEqualTo(roteirizacao);

        roteirizacao.setTomadaPrecos(new HashSet<>());
        assertThat(roteirizacao.getTomadaPrecos()).doesNotContain(tomadaPrecoBack);
        assertThat(tomadaPrecoBack.getRoteirizacao()).isNull();
    }

    @Test
    void statusColetaTest() throws Exception {
        Roteirizacao roteirizacao = getRoteirizacaoRandomSampleGenerator();
        StatusColeta statusColetaBack = getStatusColetaRandomSampleGenerator();

        roteirizacao.setStatusColeta(statusColetaBack);
        assertThat(roteirizacao.getStatusColeta()).isEqualTo(statusColetaBack);

        roteirizacao.statusColeta(null);
        assertThat(roteirizacao.getStatusColeta()).isNull();
    }
}
