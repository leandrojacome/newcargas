package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TipoCargaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoCargaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoCarga.class);
        TipoCarga tipoCarga1 = getTipoCargaSample1();
        TipoCarga tipoCarga2 = new TipoCarga();
        assertThat(tipoCarga1).isNotEqualTo(tipoCarga2);

        tipoCarga2.setId(tipoCarga1.getId());
        assertThat(tipoCarga1).isEqualTo(tipoCarga2);

        tipoCarga2 = getTipoCargaSample2();
        assertThat(tipoCarga1).isNotEqualTo(tipoCarga2);
    }

    @Test
    void tabelaFreteTest() throws Exception {
        TipoCarga tipoCarga = getTipoCargaRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        tipoCarga.addTabelaFrete(tabelaFreteBack);
        assertThat(tipoCarga.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoCarga()).isEqualTo(tipoCarga);

        tipoCarga.removeTabelaFrete(tabelaFreteBack);
        assertThat(tipoCarga.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoCarga()).isNull();

        tipoCarga.tabelaFretes(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(tipoCarga.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoCarga()).isEqualTo(tipoCarga);

        tipoCarga.setTabelaFretes(new HashSet<>());
        assertThat(tipoCarga.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoCarga()).isNull();
    }
}
