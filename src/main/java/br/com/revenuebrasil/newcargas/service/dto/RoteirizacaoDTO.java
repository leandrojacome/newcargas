package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Roteirizacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoteirizacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dataHoraPrimeiraColeta;

    private ZonedDateTime dataHoraUltimaColeta;

    private ZonedDateTime dataHoraPrimeiraEntrega;

    private ZonedDateTime dataHoraUltimaEntrega;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @Size(min = 2, max = 500)
    private String observacao;

    @NotNull
    private ZonedDateTime dataCadastro;

    @Size(min = 2, max = 150)
    private String usuarioCadastro;

    private ZonedDateTime dataAtualizacao;

    @Size(min = 2, max = 150)
    private String usuarioAtualizacao;

    private Boolean cancelado;

    private ZonedDateTime dataCancelamento;

    @Size(min = 2, max = 150)
    private String usuarioCancelamento;

    private Boolean removido;

    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    private String usuarioRemocao;

    private StatusColetaDTO statusColeta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraPrimeiraColeta() {
        return dataHoraPrimeiraColeta;
    }

    public void setDataHoraPrimeiraColeta(ZonedDateTime dataHoraPrimeiraColeta) {
        this.dataHoraPrimeiraColeta = dataHoraPrimeiraColeta;
    }

    public ZonedDateTime getDataHoraUltimaColeta() {
        return dataHoraUltimaColeta;
    }

    public void setDataHoraUltimaColeta(ZonedDateTime dataHoraUltimaColeta) {
        this.dataHoraUltimaColeta = dataHoraUltimaColeta;
    }

    public ZonedDateTime getDataHoraPrimeiraEntrega() {
        return dataHoraPrimeiraEntrega;
    }

    public void setDataHoraPrimeiraEntrega(ZonedDateTime dataHoraPrimeiraEntrega) {
        this.dataHoraPrimeiraEntrega = dataHoraPrimeiraEntrega;
    }

    public ZonedDateTime getDataHoraUltimaEntrega() {
        return dataHoraUltimaEntrega;
    }

    public void setDataHoraUltimaEntrega(ZonedDateTime dataHoraUltimaEntrega) {
        this.dataHoraUltimaEntrega = dataHoraUltimaEntrega;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioAtualizacao() {
        return usuarioAtualizacao;
    }

    public void setUsuarioAtualizacao(String usuarioAtualizacao) {
        this.usuarioAtualizacao = usuarioAtualizacao;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public ZonedDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(ZonedDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getUsuarioCancelamento() {
        return usuarioCancelamento;
    }

    public void setUsuarioCancelamento(String usuarioCancelamento) {
        this.usuarioCancelamento = usuarioCancelamento;
    }

    public Boolean getRemovido() {
        return removido;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public ZonedDateTime getDataRemocao() {
        return dataRemocao;
    }

    public void setDataRemocao(ZonedDateTime dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getUsuarioRemocao() {
        return usuarioRemocao;
    }

    public void setUsuarioRemocao(String usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public StatusColetaDTO getStatusColeta() {
        return statusColeta;
    }

    public void setStatusColeta(StatusColetaDTO statusColeta) {
        this.statusColeta = statusColeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoteirizacaoDTO)) {
            return false;
        }

        RoteirizacaoDTO roteirizacaoDTO = (RoteirizacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roteirizacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoteirizacaoDTO{" +
            "id=" + getId() +
            ", dataHoraPrimeiraColeta='" + getDataHoraPrimeiraColeta() + "'" +
            ", dataHoraUltimaColeta='" + getDataHoraUltimaColeta() + "'" +
            ", dataHoraPrimeiraEntrega='" + getDataHoraPrimeiraEntrega() + "'" +
            ", dataHoraUltimaEntrega='" + getDataHoraUltimaEntrega() + "'" +
            ", valorTotal=" + getValorTotal() +
            ", observacao='" + getObservacao() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuarioCadastro='" + getUsuarioCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", usuarioAtualizacao='" + getUsuarioAtualizacao() + "'" +
            ", cancelado='" + getCancelado() + "'" +
            ", dataCancelamento='" + getDataCancelamento() + "'" +
            ", usuarioCancelamento='" + getUsuarioCancelamento() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            ", statusColeta=" + getStatusColeta() +
            "}";
    }
}
