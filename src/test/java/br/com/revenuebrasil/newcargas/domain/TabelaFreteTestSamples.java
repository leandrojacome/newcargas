package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TabelaFreteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TabelaFrete getTabelaFreteSample1() {
        return new TabelaFrete().id(1L).nome("nome1").descricao("descricao1").leadTime(1);
    }

    public static TabelaFrete getTabelaFreteSample2() {
        return new TabelaFrete().id(2L).nome("nome2").descricao("descricao2").leadTime(2);
    }

    public static TabelaFrete getTabelaFreteRandomSampleGenerator() {
        return new TabelaFrete()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .leadTime(intCount.incrementAndGet());
    }
}
