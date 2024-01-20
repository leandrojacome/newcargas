package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TipoFreteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoFreteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoFrete.class);
        TipoFrete tipoFrete1 = getTipoFreteSample1();
        TipoFrete tipoFrete2 = new TipoFrete();
        assertThat(tipoFrete1).isNotEqualTo(tipoFrete2);

        tipoFrete2.setId(tipoFrete1.getId());
        assertThat(tipoFrete1).isEqualTo(tipoFrete2);

        tipoFrete2 = getTipoFreteSample2();
        assertThat(tipoFrete1).isNotEqualTo(tipoFrete2);
    }

    @Test
    void tabelaFreteTest() throws Exception {
        TipoFrete tipoFrete = getTipoFreteRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        tipoFrete.addTabelaFrete(tabelaFreteBack);
        assertThat(tipoFrete.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoFrete()).isEqualTo(tipoFrete);

        tipoFrete.removeTabelaFrete(tabelaFreteBack);
        assertThat(tipoFrete.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoFrete()).isNull();

        tipoFrete.tabelaFretes(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(tipoFrete.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoFrete()).isEqualTo(tipoFrete);

        tipoFrete.setTabelaFretes(new HashSet<>());
        assertThat(tipoFrete.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getTipoFrete()).isNull();
    }
}
