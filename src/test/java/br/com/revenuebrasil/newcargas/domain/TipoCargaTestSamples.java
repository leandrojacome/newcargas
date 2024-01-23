package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoCargaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoCarga getTipoCargaSample1() {
        return new TipoCarga().id(1L).nome("nome1").descricao("descricao1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static TipoCarga getTipoCargaSample2() {
        return new TipoCarga().id(2L).nome("nome2").descricao("descricao2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static TipoCarga getTipoCargaRandomSampleGenerator() {
        return new TipoCarga()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
