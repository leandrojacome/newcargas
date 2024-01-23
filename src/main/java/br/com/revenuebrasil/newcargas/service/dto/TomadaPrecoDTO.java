package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.TomadaPreco} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TomadaPrecoDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dataHoraEnvio;

    @Min(value = 1)
    @Max(value = 4)
    private Integer prazoResposta;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @Size(min = 2, max = 500)
    private String observacao;

    private Boolean aprovado;

    private Boolean cancelado;

    private Boolean removido;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private ContratacaoDTO contratacao;

    private TransportadoraDTO transportadora;

    private RoteirizacaoDTO roteirizacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public Integer getPrazoResposta() {
        return prazoResposta;
    }

    public void setPrazoResposta(Integer prazoResposta) {
        this.prazoResposta = prazoResposta;
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

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
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

    public ContratacaoDTO getContratacao() {
        return contratacao;
    }

    public void setContratacao(ContratacaoDTO contratacao) {
        this.contratacao = contratacao;
    }

    public TransportadoraDTO getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(TransportadoraDTO transportadora) {
        this.transportadora = transportadora;
    }

    public RoteirizacaoDTO getRoteirizacao() {
        return roteirizacao;
    }

    public void setRoteirizacao(RoteirizacaoDTO roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TomadaPrecoDTO)) {
            return false;
        }

        TomadaPrecoDTO tomadaPrecoDTO = (TomadaPrecoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tomadaPrecoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TomadaPrecoDTO{" +
            "id=" + getId() +
            ", dataHoraEnvio='" + getDataHoraEnvio() + "'" +
            ", prazoResposta=" + getPrazoResposta() +
            ", valorTotal=" + getValorTotal() +
            ", observacao='" + getObservacao() + "'" +
            ", aprovado='" + getAprovado() + "'" +
            ", cancelado='" + getCancelado() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", contratacao=" + getContratacao() +
            ", transportadora=" + getTransportadora() +
            ", roteirizacao=" + getRoteirizacao() +
            "}";
    }
}
