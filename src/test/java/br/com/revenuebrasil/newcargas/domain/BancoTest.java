package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.BancoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.ContaBancariaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BancoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banco.class);
        Banco banco1 = getBancoSample1();
        Banco banco2 = new Banco();
        assertThat(banco1).isNotEqualTo(banco2);

        banco2.setId(banco1.getId());
        assertThat(banco1).isEqualTo(banco2);

        banco2 = getBancoSample2();
        assertThat(banco1).isNotEqualTo(banco2);
    }

    @Test
    void contaBancariaTest() throws Exception {
        Banco banco = getBancoRandomSampleGenerator();
        ContaBancaria contaBancariaBack = getContaBancariaRandomSampleGenerator();

        banco.addContaBancaria(contaBancariaBack);
        assertThat(banco.getContaBancarias()).containsOnly(contaBancariaBack);
        assertThat(contaBancariaBack.getBanco()).isEqualTo(banco);

        banco.removeContaBancaria(contaBancariaBack);
        assertThat(banco.getContaBancarias()).doesNotContain(contaBancariaBack);
        assertThat(contaBancariaBack.getBanco()).isNull();

        banco.contaBancarias(new HashSet<>(Set.of(contaBancariaBack)));
        assertThat(banco.getContaBancarias()).containsOnly(contaBancariaBack);
        assertThat(contaBancariaBack.getBanco()).isEqualTo(banco);

        banco.setContaBancarias(new HashSet<>());
        assertThat(banco.getContaBancarias()).doesNotContain(contaBancariaBack);
        assertThat(contaBancariaBack.getBanco()).isNull();
    }
}
