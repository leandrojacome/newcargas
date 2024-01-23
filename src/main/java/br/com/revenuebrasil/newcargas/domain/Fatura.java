package br.com.revenuebrasil.newcargas.domain;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoFatura;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fatura.
 */
@Entity
@Table(name = "fatura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fatura")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Fatura extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TipoFatura tipo;

    @NotNull
    @Column(name = "data_fatura", nullable = false)
    private ZonedDateTime dataFatura;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private ZonedDateTime dataVencimento;

    @Column(name = "data_pagamento")
    private ZonedDateTime dataPagamento;

    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "numero_parcela")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numeroParcela;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observacao;

    @Column(name = "cancelado")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean cancelado;

    @Column(name = "removido")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean removido;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "solitacaoColetas", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Embarcador embarcador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Transportadora transportadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "faturas", "solicitacaoColeta", "transportadora" }, allowSetters = true)
    private Contratacao contratacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tabelaFretes", "fatutas" }, allowSetters = true)
    private FormaCobranca formaCobranca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fatura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoFatura getTipo() {
        return this.tipo;
    }

    public Fatura tipo(TipoFatura tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoFatura tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getDataFatura() {
        return this.dataFatura;
    }

    public Fatura dataFatura(ZonedDateTime dataFatura) {
        this.setDataFatura(dataFatura);
        return this;
    }

    public void setDataFatura(ZonedDateTime dataFatura) {
        this.dataFatura = dataFatura;
    }

    public ZonedDateTime getDataVencimento() {
        return this.dataVencimento;
    }

    public Fatura dataVencimento(ZonedDateTime dataVencimento) {
        this.setDataVencimento(dataVencimento);
        return this;
    }

    public void setDataVencimento(ZonedDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public ZonedDateTime getDataPagamento() {
        return this.dataPagamento;
    }

    public Fatura dataPagamento(ZonedDateTime dataPagamento) {
        this.setDataPagamento(dataPagamento);
        return this;
    }

    public void setDataPagamento(ZonedDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getNumeroParcela() {
        return this.numeroParcela;
    }

    public Fatura numeroParcela(Integer numeroParcela) {
        this.setNumeroParcela(numeroParcela);
        return this;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public Fatura valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Fatura observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getCancelado() {
        return this.cancelado;
    }

    public Fatura cancelado(Boolean cancelado) {
        this.setCancelado(cancelado);
        return this;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public Fatura removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    // Inherited createdBy methods
    public Fatura createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Fatura createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Fatura lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Fatura lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public Fatura embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Fatura transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    public Contratacao getContratacao() {
        return this.contratacao;
    }

    public void setContratacao(Contratacao contratacao) {
        this.contratacao = contratacao;
    }

    public Fatura contratacao(Contratacao contratacao) {
        this.setContratacao(contratacao);
        return this;
    }

    public FormaCobranca getFormaCobranca() {
        return this.formaCobranca;
    }

    public void setFormaCobranca(FormaCobranca formaCobranca) {
        this.formaCobranca = formaCobranca;
    }

    public Fatura formaCobranca(FormaCobranca formaCobranca) {
        this.setFormaCobranca(formaCobranca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fatura)) {
            return false;
        }
        return getId() != null && getId().equals(((Fatura) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fatura{" +
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
            "}";
    }
}
