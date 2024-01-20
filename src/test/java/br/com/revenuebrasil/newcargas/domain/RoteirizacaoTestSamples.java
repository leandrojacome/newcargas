package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoteirizacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Roteirizacao getRoteirizacaoSample1() {
        return new Roteirizacao()
            .id(1L)
            .observacao("observacao1")
            .usuarioCadastro("usuarioCadastro1")
            .usuarioAtualizacao("usuarioAtualizacao1")
            .usuarioCancelamento("usuarioCancelamento1")
            .usuarioRemocao("usuarioRemocao1");
    }

    public static Roteirizacao getRoteirizacaoSample2() {
        return new Roteirizacao()
            .id(2L)
            .observacao("observacao2")
            .usuarioCadastro("usuarioCadastro2")
            .usuarioAtualizacao("usuarioAtualizacao2")
            .usuarioCancelamento("usuarioCancelamento2")
            .usuarioRemocao("usuarioRemocao2");
    }

    public static Roteirizacao getRoteirizacaoRandomSampleGenerator() {
        return new Roteirizacao()
            .id(longCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .usuarioCadastro(UUID.randomUUID().toString())
            .usuarioAtualizacao(UUID.randomUUID().toString())
            .usuarioCancelamento(UUID.randomUUID().toString())
            .usuarioRemocao(UUID.randomUUID().toString());
    }
}
