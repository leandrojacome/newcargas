package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Contratacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContratacaoDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    private Integer validadeEmDias;

    @NotNull
    private LocalDate dataValidade;

    @Size(min = 2, max = 500)
    private String observacao;

    private Boolean cancelado;

    private Boolean removido;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private TransportadoraDTO transportadora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getValidadeEmDias() {
        return validadeEmDias;
    }

    public void setValidadeEmDias(Integer validadeEmDias) {
        this.validadeEmDias = validadeEmDias;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
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
        if (!(o instanceof ContratacaoDTO)) {
            return false;
        }

        ContratacaoDTO contratacaoDTO = (ContratacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contratacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratacaoDTO{" +
            "id=" + getId() +
            ", valorTotal=" + getValorTotal() +
            ", validadeEmDias=" + getValidadeEmDias() +
            ", dataValidade='" + getDataValidade() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", cancelado='" + getCancelado() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", transportadora=" + getTransportadora() +
            "}";
    }
}
