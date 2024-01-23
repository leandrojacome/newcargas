package br.com.revenuebrasil.newcargas.domain;

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
 * A TomadaPreco.
 */
@Entity
@Table(name = "tomada_preco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tomadapreco")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class TomadaPreco extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_hora_envio", nullable = false)
    private ZonedDateTime dataHoraEnvio;

    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "prazo_resposta")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer prazoResposta;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total")
    private Double valorTotal;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observacao;

    @Column(name = "aprovado")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean aprovado;

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

    @JsonIgnoreProperties(value = { "faturas", "solicitacaoColeta", "transportadora" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Contratacao contratacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Transportadora transportadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "historicoStatusColetas", "solitacaoColetas", "tomadaPrecos", "statusColeta" }, allowSetters = true)
    private Roteirizacao roteirizacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TomadaPreco id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraEnvio() {
        return this.dataHoraEnvio;
    }

    public TomadaPreco dataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.setDataHoraEnvio(dataHoraEnvio);
        return this;
    }

    public void setDataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public Integer getPrazoResposta() {
        return this.prazoResposta;
    }

    public TomadaPreco prazoResposta(Integer prazoResposta) {
        this.setPrazoResposta(prazoResposta);
        return this;
    }

    public void setPrazoResposta(Integer prazoResposta) {
        this.prazoResposta = prazoResposta;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public TomadaPreco valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public TomadaPreco observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getAprovado() {
        return this.aprovado;
    }

    public TomadaPreco aprovado(Boolean aprovado) {
        this.setAprovado(aprovado);
        return this;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    public Boolean getCancelado() {
        return this.cancelado;
    }

    public TomadaPreco cancelado(Boolean cancelado) {
        this.setCancelado(cancelado);
        return this;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public TomadaPreco removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    // Inherited createdBy methods
    public TomadaPreco createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public TomadaPreco createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public TomadaPreco lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public TomadaPreco lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Contratacao getContratacao() {
        return this.contratacao;
    }

    public void setContratacao(Contratacao contratacao) {
        this.contratacao = contratacao;
    }

    public TomadaPreco contratacao(Contratacao contratacao) {
        this.setContratacao(contratacao);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public TomadaPreco transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    public Roteirizacao getRoteirizacao() {
        return this.roteirizacao;
    }

    public void setRoteirizacao(Roteirizacao roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    public TomadaPreco roteirizacao(Roteirizacao roteirizacao) {
        this.setRoteirizacao(roteirizacao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TomadaPreco)) {
            return false;
        }
        return getId() != null && getId().equals(((TomadaPreco) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TomadaPreco{" +
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
            "}";
    }
}
