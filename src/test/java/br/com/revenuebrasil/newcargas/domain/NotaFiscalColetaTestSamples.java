package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NotaFiscalColetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NotaFiscalColeta getNotaFiscalColetaSample1() {
        return new NotaFiscalColeta()
            .id(1L)
            .numero("numero1")
            .serie("serie1")
            .remetente("remetente1")
            .destinatario("destinatario1")
            .quantidadeTotal(1)
            .observacao("observacao1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static NotaFiscalColeta getNotaFiscalColetaSample2() {
        return new NotaFiscalColeta()
            .id(2L)
            .numero("numero2")
            .serie("serie2")
            .remetente("remetente2")
            .destinatario("destinatario2")
            .quantidadeTotal(2)
            .observacao("observacao2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static NotaFiscalColeta getNotaFiscalColetaRandomSampleGenerator() {
        return new NotaFiscalColeta()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .serie(UUID.randomUUID().toString())
            .remetente(UUID.randomUUID().toString())
            .destinatario(UUID.randomUUID().toString())
            .quantidadeTotal(intCount.incrementAndGet())
            .observacao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
