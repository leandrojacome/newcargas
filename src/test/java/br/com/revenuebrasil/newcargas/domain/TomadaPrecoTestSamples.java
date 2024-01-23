package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TomadaPrecoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TomadaPreco getTomadaPrecoSample1() {
        return new TomadaPreco()
            .id(1L)
            .prazoResposta(1)
            .observacao("observacao1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static TomadaPreco getTomadaPrecoSample2() {
        return new TomadaPreco()
            .id(2L)
            .prazoResposta(2)
            .observacao("observacao2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static TomadaPreco getTomadaPrecoRandomSampleGenerator() {
        return new TomadaPreco()
            .id(longCount.incrementAndGet())
            .prazoResposta(intCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
