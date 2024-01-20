package br.com.revenuebrasil.newcargas.domain;

import static br.com.revenuebrasil.newcargas.domain.EnderecoTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.NotaFiscalColetaTestSamples.*;
import static br.com.revenuebrasil.newcargas.domain.SolicitacaoColetaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NotaFiscalColetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotaFiscalColeta.class);
        NotaFiscalColeta notaFiscalColeta1 = getNotaFiscalColetaSample1();
        NotaFiscalColeta notaFiscalColeta2 = new NotaFiscalColeta();
        assertThat(notaFiscalColeta1).isNotEqualTo(notaFiscalColeta2);

        notaFiscalColeta2.setId(notaFiscalColeta1.getId());
        assertThat(notaFiscalColeta1).isEqualTo(notaFiscalColeta2);

        notaFiscalColeta2 = getNotaFiscalColetaSample2();
        assertThat(notaFiscalColeta1).isNotEqualTo(notaFiscalColeta2);
    }

    @Test
    void enderecoOrigemTest() throws Exception {
        NotaFiscalColeta notaFiscalColeta = getNotaFiscalColetaRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        notaFiscalColeta.addEnderecoOrigem(enderecoBack);
        assertThat(notaFiscalColeta.getEnderecoOrigems()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaOrigem()).isEqualTo(notaFiscalColeta);

        notaFiscalColeta.removeEnderecoOrigem(enderecoBack);
        assertThat(notaFiscalColeta.getEnderecoOrigems()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaOrigem()).isNull();

        notaFiscalColeta.enderecoOrigems(new HashSet<>(Set.of(enderecoBack)));
        assertThat(notaFiscalColeta.getEnderecoOrigems()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaOrigem()).isEqualTo(notaFiscalColeta);

        notaFiscalColeta.setEnderecoOrigems(new HashSet<>());
        assertThat(notaFiscalColeta.getEnderecoOrigems()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaOrigem()).isNull();
    }

    @Test
    void enderecoDestinoTest() throws Exception {
        NotaFiscalColeta notaFiscalColeta = getNotaFiscalColetaRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        notaFiscalColeta.addEnderecoDestino(enderecoBack);
        assertThat(notaFiscalColeta.getEnderecoDestinos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaDestino()).isEqualTo(notaFiscalColeta);

        notaFiscalColeta.removeEnderecoDestino(enderecoBack);
        assertThat(notaFiscalColeta.getEnderecoDestinos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaDestino()).isNull();

        notaFiscalColeta.enderecoDestinos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(notaFiscalColeta.getEnderecoDestinos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaDestino()).isEqualTo(notaFiscalColeta);

        notaFiscalColeta.setEnderecoDestinos(new HashSet<>());
        assertThat(notaFiscalColeta.getEnderecoDestinos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getNotaFiscalColetaDestino()).isNull();
    }

    @Test
    void solicitacaoColetaTest() throws Exception {
        NotaFiscalColeta notaFiscalColeta = getNotaFiscalColetaRandomSampleGenerator();
        SolicitacaoColeta solicitacaoColetaBack = getSolicitacaoColetaRandomSampleGenerator();

        notaFiscalColeta.setSolicitacaoColeta(solicitacaoColetaBack);
        assertThat(notaFiscalColeta.getSolicitacaoColeta()).isEqualTo(solicitacaoColetaBack);

        notaFiscalColeta.solicitacaoColeta(null);
        assertThat(notaFiscalColeta.getSolicitacaoColeta()).isNull();
    }
}
