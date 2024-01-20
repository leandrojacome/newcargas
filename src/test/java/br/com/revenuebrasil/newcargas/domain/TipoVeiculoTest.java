package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.TipoVeiculoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoVeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoVeiculo.class);
        TipoVeiculo tipoVeiculo1 = getTipoVeiculoSample1();
        TipoVeiculo tipoVeiculo2 = new TipoVeiculo();
        assertThat(tipoVeiculo1).isNotEqualTo(tipoVeiculo2);

        tipoVeiculo2.setId(tipoVeiculo1.getId());
        assertThat(tipoVeiculo1).isEqualTo(tipoVeiculo2);

        tipoVeiculo2 = getTipoVeiculoSample2();
        assertThat(tipoVeiculo1).isNotEqualTo(tipoVeiculo2);
    }

    @Test
    void solitacaoColetaTest() throws Exception {
        TipoVeiculo tipoVeiculo = getTipoVeiculoRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        tipoVeiculo.addSolitacaoColeta(solicitacaoColetaBack);
        assertThat(tipoVeiculo.getSolitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getTipoVeiculo()).isEqualTo(tipoVeiculo);

        tipoVeiculo.removeSolitacaoColeta(solicitacaoColetaBack);
        assertThat(tipoVeiculo.getSolitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getTipoVeiculo()).isNull();

        tipoVeiculo.solitacaoColetas(new HashSet<>(Set.of(solicitacaoColetaBack)));
        assertThat(tipoVeiculo.getSolitacaoColetas()).containsOnly(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getTipoVeiculo()).isEqualTo(tipoVeiculo);

        tipoVeiculo.setSolitacaoColetas(new HashSet<>());
        assertThat(tipoVeiculo.getSolitacaoColetas()).doesNotContain(solicitacaoColetaBack);
        assertThat(solicitacaoColetaBack.getTipoVeiculo()).isNull();
    }
}
