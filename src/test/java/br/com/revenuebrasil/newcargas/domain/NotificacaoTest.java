package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.NotificacaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notificacao.class);
        Notificacao notificacao1 = getNotificacaoSample1();
        Notificacao notificacao2 = new Notificacao();
        assertThat(notificacao1).isNotEqualTo(notificacao2);

        notificacao2.setId(notificacao1.getId());
        assertThat(notificacao1).isEqualTo(notificacao2);

        notificacao2 = getNotificacaoSample2();
        assertThat(notificacao1).isNotEqualTo(notificacao2);
    }

    @Test
    void embarcadorTest() throws Exception {
        Notificacao notificacao = getNotificacaoRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        notificacao.setEmbarcador(embarcadorBack);
        assertThat(notificacao.getEmbarcador()).isEqualTo(embarcadorBack);

        notificacao.embarcador(null);
        assertThat(notificacao.getEmbarcador()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        Notificacao notificacao = getNotificacaoRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        notificacao.setTransportadora(transportadoraBack);
        assertThat(notificacao.getTransportadora()).isEqualTo(transportadoraBack);

        notificacao.transportadora(null);
        assertThat(notificacao.getTransportadora()).isNull();
    }
}
