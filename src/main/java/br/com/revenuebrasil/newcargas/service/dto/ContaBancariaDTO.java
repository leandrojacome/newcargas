package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.ContaBancaria} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaBancariaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    private String agencia;

    @NotNull
    @Size(min = 1, max = 10)
    private String conta;

    @Size(min = 2, max = 500)
    private String observacao;

    @Size(min = 2, max = 150)
    private String tipo;

    @Size(min = 2, max = 150)
    private String pix;

    @Size(min = 2, max = 150)
    private String titular;

    @NotNull
    private ZonedDateTime dataCadastro;

    private ZonedDateTime dataAtualizacao;

    private BancoDTO banco;

    private EmbarcadorDTO embarcador;

    private TransportadoraDTO transportadora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPix() {
        return pix;
    }

    public void setPix(String pix) {
        this.pix = pix;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public BancoDTO getBanco() {
        return banco;
    }

    public void setBanco(BancoDTO banco) {
        this.banco = banco;
    }

    public EmbarcadorDTO getEmbarcador() {
        return embarcador;
    }

    public void setEmbarcador(EmbarcadorDTO embarcador) {
        this.embarcador = embarcador;
    }

    public TransportadoraDTO getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(TransportadoraDTO transportadora) {
        this.transportadora = transportadora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaBancariaDTO)) {
            return false;
        }

        ContaBancariaDTO contaBancariaDTO = (ContaBancariaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contaBancariaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaBancariaDTO{" +
            "id=" + getId() +
            ", agencia='" + getAgencia() + "'" +
            ", conta='" + getConta() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", pix='" + getPix() + "'" +
            ", titular='" + getTitular() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", banco=" + getBanco() +
            ", embarcador=" + getEmbarcador() +
            ", transportadora=" + getTransportadora() +
            "}";
    }
}
