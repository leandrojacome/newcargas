package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoVeiculoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoVeiculo getTipoVeiculoSample1() {
        return new TipoVeiculo().id(1L).nome("nome1").descricao("descricao1");
    }

    public static TipoVeiculo getTipoVeiculoSample2() {
        return new TipoVeiculo().id(2L).nome("nome2").descricao("descricao2");
    }

    public static TipoVeiculo getTipoVeiculoRandomSampleGenerator() {
        return new TipoVeiculo().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
