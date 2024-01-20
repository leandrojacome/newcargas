package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnderecoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Endereco getEnderecoSample1() {
        return new Endereco().id(1L).cep("cep1").endereco("endereco1").numero("numero1").complemento("complemento1").bairro("bairro1");
    }

    public static Endereco getEnderecoSample2() {
        return new Endereco().id(2L).cep("cep2").endereco("endereco2").numero("numero2").complemento("complemento2").bairro("bairro2");
    }

    public static Endereco getEnderecoRandomSampleGenerator() {
        return new Endereco()
            .id(longCount.incrementAndGet())
            .cep(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString())
            .complemento(UUID.randomUUID().toString())
            .bairro(UUID.randomUUID().toString());
    }
}
