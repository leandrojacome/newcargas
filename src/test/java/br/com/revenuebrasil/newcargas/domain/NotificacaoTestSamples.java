package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notificacao getNotificacaoSample1() {
        return new Notificacao()
            .id(1L)
            .email("email1")
            .telefone("telefone1")
            .assunto("assunto1")
            .mensagem("mensagem1")
            .usuarioRemocao("usuarioRemocao1");
    }

    public static Notificacao getNotificacaoSample2() {
        return new Notificacao()
            .id(2L)
            .email("email2")
            .telefone("telefone2")
            .assunto("assunto2")
            .mensagem("mensagem2")
            .usuarioRemocao("usuarioRemocao2");
    }

    public static Notificacao getNotificacaoRandomSampleGenerator() {
        return new Notificacao()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .assunto(UUID.randomUUID().toString())
            .mensagem(UUID.randomUUID().toString())
            .usuarioRemocao(UUID.randomUUID().toString());
    }
}
