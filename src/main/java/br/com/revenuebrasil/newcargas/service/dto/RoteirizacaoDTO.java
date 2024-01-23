package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
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

    private Boolean cancelado;

    private Boolean removido;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getRemovido() {
        return removido;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
            ", cancelado='" + getCancelado() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", statusColeta=" + getStatusColeta() +
            "}";
    }
}
