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
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static StatusColeta getStatusColetaSample2() {
        return new StatusColeta()
            .id(2L)
            .nome("nome2")
            .cor("cor2")
            .ordem(2)
            .descricao("descricao2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static StatusColeta getStatusColetaRandomSampleGenerator() {
        return new StatusColeta()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cor(UUID.randomUUID().toString())
            .ordem(intCount.incrementAndGet())
            .descricao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
