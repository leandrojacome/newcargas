package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CidadeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cidade getCidadeSample1() {
        return new Cidade().id(1L).nome("nome1").codigoIbge(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Cidade getCidadeSample2() {
        return new Cidade().id(2L).nome("nome2").codigoIbge(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Cidade getCidadeRandomSampleGenerator() {
        return new Cidade()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .codigoIbge(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
