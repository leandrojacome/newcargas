package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ContratacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Contratacao getContratacaoSample1() {
        return new Contratacao()
            .id(1L)
            .validadeEmDias(1)
            .observacao("observacao1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Contratacao getContratacaoSample2() {
        return new Contratacao()
            .id(2L)
            .validadeEmDias(2)
            .observacao("observacao2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Contratacao getContratacaoRandomSampleGenerator() {
        return new Contratacao()
            .id(longCount.incrementAndGet())
            .validadeEmDias(intCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
