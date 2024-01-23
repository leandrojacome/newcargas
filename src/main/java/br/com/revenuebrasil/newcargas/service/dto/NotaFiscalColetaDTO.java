package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotaFiscalColetaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    private String numero;

    @NotNull
    @Size(min = 2, max = 20)
    private String serie;

    @Size(min = 2, max = 150)
    private String remetente;

    @Size(min = 2, max = 150)
    private String destinatario;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double metroCubico;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double quantidade;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double peso;

    private ZonedDateTime dataEmissao;

    private ZonedDateTime dataSaida;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double pesoTotal;

    @Min(value = 1)
    @Max(value = 4)
    private Integer quantidadeTotal;

    @Size(min = 2, max = 500)
    private String observacao;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private SolicitacaoColetaDTO solicitacaoColeta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Double getMetroCubico() {
        return metroCubico;
    }

    public void setMetroCubico(Double metroCubico) {
        this.metroCubico = metroCubico;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public ZonedDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(ZonedDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public ZonedDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(ZonedDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

    public SolicitacaoColetaDTO getSolicitacaoColeta() {
        return solicitacaoColeta;
    }

    public void setSolicitacaoColeta(SolicitacaoColetaDTO solicitacaoColeta) {
        this.solicitacaoColeta = solicitacaoColeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotaFiscalColetaDTO)) {
            return false;
        }

        NotaFiscalColetaDTO notaFiscalColetaDTO = (NotaFiscalColetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notaFiscalColetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaFiscalColetaDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", serie='" + getSerie() + "'" +
            ", remetente='" + getRemetente() + "'" +
            ", destinatario='" + getDestinatario() + "'" +
            ", metroCubico=" + getMetroCubico() +
            ", quantidade=" + getQuantidade() +
            ", peso=" + getPeso() +
            ", dataEmissao='" + getDataEmissao() + "'" +
            ", dataSaida='" + getDataSaida() + "'" +
            ", valorTotal=" + getValorTotal() +
            ", pesoTotal=" + getPesoTotal() +
            ", quantidadeTotal=" + getQuantidadeTotal() +
            ", observacao='" + getObservacao() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", solicitacaoColeta=" + getSolicitacaoColeta() +
            "}";
    }
}
