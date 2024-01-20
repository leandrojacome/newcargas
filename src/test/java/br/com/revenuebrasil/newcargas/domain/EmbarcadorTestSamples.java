package br.com.revenuebrasil.newcargas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmbarcadorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Embarcador getEmbarcadorSample1() {
        return new Embarcador()
            .id(1L)
            .nome("nome1")
            .cnpj("cnpj1")
            .razaoSocial("razaoSocial1")
            .inscricaoEstadual("inscricaoEstadual1")
            .inscricaoMunicipal("inscricaoMunicipal1")
            .responsavel("responsavel1")
            .cep("cep1")
            .endereco("endereco1")
            .numero("numero1")
            .complemento("complemento1")
            .bairro("bairro1")
            .telefone("telefone1")
            .email("email1")
            .observacao("observacao1")
            .usuarioCadastro("usuarioCadastro1")
            .usuarioAtualizacao("usuarioAtualizacao1");
    }

    public static Embarcador getEmbarcadorSample2() {
        return new Embarcador()
            .id(2L)
            .nome("nome2")
            .cnpj("cnpj2")
            .razaoSocial("razaoSocial2")
            .inscricaoEstadual("inscricaoEstadual2")
            .inscricaoMunicipal("inscricaoMunicipal2")
            .responsavel("responsavel2")
            .cep("cep2")
            .endereco("endereco2")
            .numero("numero2")
            .complemento("complemento2")
            .bairro("bairro2")
            .telefone("telefone2")
            .email("email2")
            .observacao("observacao2")
            .usuarioCadastro("usuarioCadastro2")
            .usuarioAtualizacao("usuarioAtualizacao2");
    }

    public static Embarcador getEmbarcadorRandomSampleGenerator() {
        return new Embarcador()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cnpj(UUID.randomUUID().toString())
            .razaoSocial(UUID.randomUUID().toString())
            .inscricaoEstadual(UUID.randomUUID().toString())
            .inscricaoMunicipal(UUID.randomUUID().toString())
            .responsavel(UUID.randomUUID().toString())
            .cep(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString())
            .complemento(UUID.randomUUID().toString())
            .bairro(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .observacao(UUID.randomUUID().toString())
            .usuarioCadastro(UUID.randomUUID().toString())
            .usuarioAtualizacao(UUID.randomUUID().toString());
    }
}
