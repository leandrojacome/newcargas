package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FaturaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Fatura getFaturaSample1() {
        return new Fatura()
            .id(1L)
            .numeroParcela(1)
            .observacao("observacao1")
            .usuarioCadastro("usuarioCadastro1")
            .usuarioAtualizacao("usuarioAtualizacao1")
            .usuarioCancelamento("usuarioCancelamento1")
            .usuarioRemocao("usuarioRemocao1");
    }

    public static Fatura getFaturaSample2() {
        return new Fatura()
            .id(2L)
            .numeroParcela(2)
            .observacao("observacao2")
            .usuarioCadastro("usuarioCadastro2")
            .usuarioAtualizacao("usuarioAtualizacao2")
            .usuarioCancelamento("usuarioCancelamento2")
            .usuarioRemocao("usuarioRemocao2");
    }

    public static Fatura getFaturaRandomSampleGenerator() {
        return new Fatura()
            .id(longCount.incrementAndGet())
            .numeroParcela(intCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .usuarioCadastro(UUID.randomUUID().toString())
            .usuarioAtualizacao(UUID.randomUUID().toString())
            .usuarioCancelamento(UUID.randomUUID().toString())
            .usuarioRemocao(UUID.randomUUID().toString());
    }
}
