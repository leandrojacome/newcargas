package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.EmbarcadorTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FormaCobrancaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.RegiaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TipoCargaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TipoFreteTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TransportadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TabelaFreteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TabelaFrete.class);
        TabelaFrete tabelaFrete1 = getTabelaFreteSample1();
        TabelaFrete tabelaFrete2 = new TabelaFrete();
        assertThat(tabelaFrete1).isNotEqualTo(tabelaFrete2);

        tabelaFrete2.setId(tabelaFrete1.getId());
        assertThat(tabelaFrete1).isEqualTo(tabelaFrete2);

        tabelaFrete2 = getTabelaFreteSample2();
        assertThat(tabelaFrete1).isNotEqualTo(tabelaFrete2);
    }

    @Test
    void embarcadorTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        Embarcador embarcadorBack = getEmbarcadorRandomSampleGenerator();

        tabelaFrete.setEmbarcador(embarcadorBack);
        assertThat(tabelaFrete.getEmbarcador()).isEqualTo(embarcadorBack);

        tabelaFrete.embarcador(null);
        assertThat(tabelaFrete.getEmbarcador()).isNull();
    }

    @Test
    void transportadoraTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        Transportadora transportadoraBack = getTransportadoraRandomSampleGenerator();

        tabelaFrete.setTransportadora(transportadoraBack);
        assertThat(tabelaFrete.getTransportadora()).isEqualTo(transportadoraBack);

        tabelaFrete.transportadora(null);
        assertThat(tabelaFrete.getTransportadora()).isNull();
    }

    @Test
    void tipoCargaTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        TipoCarga tipoCargaBack = getTipoCargaRandomSampleGenerator();

        tabelaFrete.setTipoCarga(tipoCargaBack);
        assertThat(tabelaFrete.getTipoCarga()).isEqualTo(tipoCargaBack);

        tabelaFrete.tipoCarga(null);
        assertThat(tabelaFrete.getTipoCarga()).isNull();
    }

    @Test
    void tipoFreteTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        TipoFrete tipoFreteBack = getTipoFreteRandomSampleGenerator();

        tabelaFrete.setTipoFrete(tipoFreteBack);
        assertThat(tabelaFrete.getTipoFrete()).isEqualTo(tipoFreteBack);

        tabelaFrete.tipoFrete(null);
        assertThat(tabelaFrete.getTipoFrete()).isNull();
    }

    @Test
    void formaCobrancaTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        FormaCobranca formaCobrancaBack = getFormaCobrancaRandomSampleGenerator();

        tabelaFrete.setFormaCobranca(formaCobrancaBack);
        assertThat(tabelaFrete.getFormaCobranca()).isEqualTo(formaCobrancaBack);

        tabelaFrete.formaCobranca(null);
        assertThat(tabelaFrete.getFormaCobranca()).isNull();
    }

    @Test
    void regiaoOrigemTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        Regiao regiaoBack = getRegiaoRandomSampleGenerator();

        tabelaFrete.setRegiaoOrigem(regiaoBack);
        assertThat(tabelaFrete.getRegiaoOrigem()).isEqualTo(regiaoBack);

        tabelaFrete.regiaoOrigem(null);
        assertThat(tabelaFrete.getRegiaoOrigem()).isNull();
    }

    @Test
    void regiaoDestinoTest() throws Exception {
        TabelaFrete tabelaFrete = getTabelaFreteRandomSampleGenerator();
        Regiao regiaoBack = getRegiaoRandomSampleGenerator();

        tabelaFrete.setRegiaoDestino(regiaoBack);
        assertThat(tabelaFrete.getRegiaoDestino()).isEqualTo(regiaoBack);

        tabelaFrete.regiaoDestino(null);
        assertThat(tabelaFrete.getRegiaoDestino()).isNull();
    }
}
