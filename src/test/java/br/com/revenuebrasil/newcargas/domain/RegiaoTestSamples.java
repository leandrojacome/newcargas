package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RegiaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Regiao getRegiaoSample1() {
        return new Regiao()
            .id(1L)
            .nome("nome1")
            .sigla("sigla1")
            .descricao("descricao1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Regiao getRegiaoSample2() {
        return new Regiao()
            .id(2L)
            .nome("nome2")
            .sigla("sigla2")
            .descricao("descricao2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Regiao getRegiaoRandomSampleGenerator() {
        return new Regiao()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .sigla(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
