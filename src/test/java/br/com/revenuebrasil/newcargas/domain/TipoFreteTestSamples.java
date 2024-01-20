package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoFreteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoFrete getTipoFreteSample1() {
        return new TipoFrete().id(1L).nome("nome1").descricao("descricao1");
    }

    public static TipoFrete getTipoFreteSample2() {
        return new TipoFrete().id(2L).nome("nome2").descricao("descricao2");
    }

    public static TipoFrete getTipoFreteRandomSampleGenerator() {
        return new TipoFrete().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
