package br.com.revenuebrasil.newcargas.service.dto;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoFatura;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Fatura} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FaturaDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoFatura tipo;

    @NotNull
    private ZonedDateTime dataFatura;

    @NotNull
    private ZonedDateTime dataVencimento;

    private ZonedDateTime dataPagamento;

    @Min(value = 1)
    @Max(value = 4)
    private Integer numeroParcela;

    @NotNull
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

    private EmbarcadorDTO embarcador;

    private TransportadoraDTO transportadora;

    private ContratacaoDTO contratacao;

    private FormaCobrancaDTO formaCobranca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoFatura getTipo() {
        return tipo;
    }

    public void setTipo(TipoFatura tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getDataFatura() {
        return dataFatura;
    }

    public void setDataFatura(ZonedDateTime dataFatura) {
        this.dataFatura = dataFatura;
    }

    public ZonedDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(ZonedDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public ZonedDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(ZonedDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
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

    public ContratacaoDTO getContratacao() {
        return contratacao;
    }

    public void setContratacao(ContratacaoDTO contratacao) {
        this.contratacao = contratacao;
    }

    public FormaCobrancaDTO getFormaCobranca() {
        return formaCobranca;
    }

    public void setFormaCobranca(FormaCobrancaDTO formaCobranca) {
        this.formaCobranca = formaCobranca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FaturaDTO)) {
            return false;
        }

        FaturaDTO faturaDTO = (FaturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, faturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FaturaDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", dataFatura='" + getDataFatura() + "'" +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", dataPagamento='" + getDataPagamento() + "'" +
            ", numeroParcela=" + getNumeroParcela() +
            ", valorTotal=" + getValorTotal() +
            ", observacao='" + getObservacao() + "'" +
            ", cancelado='" + getCancelado() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", embarcador=" + getEmbarcador() +
            ", transportadora=" + getTransportadora() +
            ", contratacao=" + getContratacao() +
            ", formaCobranca=" + getFormaCobranca() +
            "}";
    }
}
