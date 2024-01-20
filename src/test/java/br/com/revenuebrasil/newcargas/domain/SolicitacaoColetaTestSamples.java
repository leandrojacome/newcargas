package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SolicitacaoColetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SolicitacaoColeta getSolicitacaoColetaSample1() {
        return new SolicitacaoColeta()
            .id(1L)
            .observacao("observacao1")
            .usuarioCancelamento("usuarioCancelamento1")
            .usuarioRemocao("usuarioRemocao1");
    }

    public static SolicitacaoColeta getSolicitacaoColetaSample2() {
        return new SolicitacaoColeta()
            .id(2L)
            .observacao("observacao2")
            .usuarioCancelamento("usuarioCancelamento2")
            .usuarioRemocao("usuarioRemocao2");
    }

    public static SolicitacaoColeta getSolicitacaoColetaRandomSampleGenerator() {
        return new SolicitacaoColeta()
            .id(longCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .usuarioCancelamento(UUID.randomUUID().toString())
            .usuarioRemocao(UUID.randomUUID().toString());
    }
}
