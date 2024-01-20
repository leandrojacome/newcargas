package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.FaturaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.FormaCobrancaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FormaCobrancaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormaCobranca.class);
        FormaCobranca formaCobranca1 = getFormaCobrancaSample1();
        FormaCobranca formaCobranca2 = new FormaCobranca();
        assertThat(formaCobranca1).isNotEqualTo(formaCobranca2);

        formaCobranca2.setId(formaCobranca1.getId());
        assertThat(formaCobranca1).isEqualTo(formaCobranca2);

        formaCobranca2 = getFormaCobrancaSample2();
        assertThat(formaCobranca1).isNotEqualTo(formaCobranca2);
    }

    @Test
    void tabelaFreteTest() throws Exception {
        FormaCobranca formaCobranca = getFormaCobrancaRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        formaCobranca.addTabelaFrete(tabelaFreteBack);
        assertThat(formaCobranca.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getFormaCobranca()).isEqualTo(formaCobranca);

        formaCobranca.removeTabelaFrete(tabelaFreteBack);
        assertThat(formaCobranca.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getFormaCobranca()).isNull();

        formaCobranca.tabelaFretes(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(formaCobranca.getTabelaFretes()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getFormaCobranca()).isEqualTo(formaCobranca);

        formaCobranca.setTabelaFretes(new HashSet<>());
        assertThat(formaCobranca.getTabelaFretes()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getFormaCobranca()).isNull();
    }

    @Test
    void fatutaTest() throws Exception {
        FormaCobranca formaCobranca = getFormaCobrancaRandomSampleGenerator();
        Fatura faturaBack = getFaturaRandomSampleGenerator();

        formaCobranca.addFatuta(faturaBack);
        assertThat(formaCobranca.getFatutas()).containsOnly(faturaBack);
        assertThat(faturaBack.getFormaCobranca()).isEqualTo(formaCobranca);

        formaCobranca.removeFatuta(faturaBack);
        assertThat(formaCobranca.getFatutas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getFormaCobranca()).isNull();

        formaCobranca.fatutas(new HashSet<>(Set.of(faturaBack)));
        assertThat(formaCobranca.getFatutas()).containsOnly(faturaBack);
        assertThat(faturaBack.getFormaCobranca()).isEqualTo(formaCobranca);

        formaCobranca.setFatutas(new HashSet<>());
        assertThat(formaCobranca.getFatutas()).doesNotContain(faturaBack);
        assertThat(faturaBack.getFormaCobranca()).isNull();
    }
}
