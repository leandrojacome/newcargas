package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StatusColetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StatusColeta getStatusColetaSample1() {
        return new StatusColeta()
            .id(1L)
            .nome("nome1")
            .cor("cor1")
            .ordem(1)
            .descricao("descricao1")
            .usuarioCadastro("usuarioCadastro1")
            .usuarioAtualizacao("usuarioAtualizacao1")
            .usuarioRemocao("usuarioRemocao1");
    }

    public static StatusColeta getStatusColetaSample2() {
        return new StatusColeta()
            .id(2L)
            .nome("nome2")
            .cor("cor2")
            .ordem(2)
            .descricao("descricao2")
            .usuarioCadastro("usuarioCadastro2")
            .usuarioAtualizacao("usuarioAtualizacao2")
            .usuarioRemocao("usuarioRemocao2");
    }

    public static StatusColeta getStatusColetaRandomSampleGenerator() {
        return new StatusColeta()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cor(UUID.randomUUID().toString())
            .ordem(intCount.incrementAndGet())
            .descricao(UUID.randomUUID().toString())
            .usuarioCadastro(UUID.randomUUID().toString())
            .usuarioAtualizacao(UUID.randomUUID().toString())
            .usuarioRemocao(UUID.randomUUID().toString());
    }
}
