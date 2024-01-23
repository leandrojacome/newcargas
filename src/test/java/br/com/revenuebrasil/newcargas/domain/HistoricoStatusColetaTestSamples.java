package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoricoStatusColetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoricoStatusColeta getHistoricoStatusColetaSample1() {
        return new HistoricoStatusColeta().id(1L).observacao("observacao1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HistoricoStatusColeta getHistoricoStatusColetaSample2() {
        return new HistoricoStatusColeta().id(2L).observacao("observacao2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HistoricoStatusColeta getHistoricoStatusColetaRandomSampleGenerator() {
        return new HistoricoStatusColeta()
            .id(longCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
