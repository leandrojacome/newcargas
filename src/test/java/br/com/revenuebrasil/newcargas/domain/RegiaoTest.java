package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.RegiaoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TabelaFreteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RegiaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regiao.class);
        Regiao regiao1 = getRegiaoSample1();
        Regiao regiao2 = new Regiao();
        assertThat(regiao1).isNotEqualTo(regiao2);

        regiao2.setId(regiao1.getId());
        assertThat(regiao1).isEqualTo(regiao2);

        regiao2 = getRegiaoSample2();
        assertThat(regiao1).isNotEqualTo(regiao2);
    }

    @Test
    void tabelaFreteOrigemTest() throws Exception {
        Regiao regiao = getRegiaoRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        regiao.addTabelaFreteOrigem(tabelaFreteBack);
        assertThat(regiao.getTabelaFreteOrigems()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoOrigem()).isEqualTo(regiao);

        regiao.removeTabelaFreteOrigem(tabelaFreteBack);
        assertThat(regiao.getTabelaFreteOrigems()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoOrigem()).isNull();

        regiao.tabelaFreteOrigems(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(regiao.getTabelaFreteOrigems()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoOrigem()).isEqualTo(regiao);

        regiao.setTabelaFreteOrigems(new HashSet<>());
        assertThat(regiao.getTabelaFreteOrigems()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoOrigem()).isNull();
    }

    @Test
    void tabelaFreteDestinoTest() throws Exception {
        Regiao regiao = getRegiaoRandomSampleGenerator();
        TabelaFrete tabelaFreteBack = getTabelaFreteRandomSampleGenerator();

        regiao.addTabelaFreteDestino(tabelaFreteBack);
        assertThat(regiao.getTabelaFreteDestinos()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoDestino()).isEqualTo(regiao);

        regiao.removeTabelaFreteDestino(tabelaFreteBack);
        assertThat(regiao.getTabelaFreteDestinos()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoDestino()).isNull();

        regiao.tabelaFreteDestinos(new HashSet<>(Set.of(tabelaFreteBack)));
        assertThat(regiao.getTabelaFreteDestinos()).containsOnly(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoDestino()).isEqualTo(regiao);

        regiao.setTabelaFreteDestinos(new HashSet<>());
        assertThat(regiao.getTabelaFreteDestinos()).doesNotContain(tabelaFreteBack);
        assertThat(tabelaFreteBack.getRegiaoDestino()).isNull();
    }
}
