package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FormaCobrancaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FormaCobranca getFormaCobrancaSample1() {
        return new FormaCobranca().id(1L).nome("nome1").descricao("descricao1");
    }

    public static FormaCobranca getFormaCobrancaSample2() {
        return new FormaCobranca().id(2L).nome("nome2").descricao("descricao2");
    }

    public static FormaCobranca getFormaCobrancaRandomSampleGenerator() {
        return new FormaCobranca()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString());
    }
}
