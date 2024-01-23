package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContaBancariaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContaBancaria getContaBancariaSample1() {
        return new ContaBancaria()
            .id(1L)
            .agencia("agencia1")
            .conta("conta1")
            .observacao("observacao1")
            .tipo("tipo1")
            .pix("pix1")
            .titular("titular1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ContaBancaria getContaBancariaSample2() {
        return new ContaBancaria()
            .id(2L)
            .agencia("agencia2")
            .conta("conta2")
            .observacao("observacao2")
            .tipo("tipo2")
            .pix("pix2")
            .titular("titular2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ContaBancaria getContaBancariaRandomSampleGenerator() {
        return new ContaBancaria()
            .id(longCount.incrementAndGet())
            .agencia(UUID.randomUUID().toString())
            .conta(UUID.randomUUID().toString())
            .observacao(UUID.randomUUID().toString())
            .tipo(UUID.randomUUID().toString())
            .pix(UUID.randomUUID().toString())
            .titular(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
