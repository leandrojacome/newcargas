package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contratacao.
 */
@Entity
@Table(name = "contratacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contratacao")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Contratacao extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "validade_em_dias", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer validadeEmDias;

    @NotNull
    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contratacao")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "embarcador", "transportadora", "contratacao", "formaCobranca" }, allowSetters = true)
    private Set<Fatura> faturas = new HashSet<>();

    @JsonIgnoreProperties(value = { "contratacao", "transportadora", "roteirizacao" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contratacao")
    @org.springframework.data.annotation.Transient
    private TomadaPreco solicitacaoColeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Transportadora transportadora;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contratacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public Contratacao valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getValidadeEmDias() {
        return this.validadeEmDias;
    }

    public Contratacao validadeEmDias(Integer validadeEmDias) {
        this.setValidadeEmDias(validadeEmDias);
        return this;
    }

    public void setValidadeEmDias(Integer validadeEmDias) {
        this.validadeEmDias = validadeEmDias;
    }

    public LocalDate getDataValidade() {
        return this.dataValidade;
    }

    public Contratacao dataValidade(LocalDate dataValidade) {
        this.setDataValidade(dataValidade);
        return this;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Contratacao observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getCancelado() {
        return this.cancelado;
    }

    public Contratacao cancelado(Boolean cancelado) {
        this.setCancelado(cancelado);
        return this;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public Contratacao removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    // Inherited createdBy methods
    public Contratacao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Contratacao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Contratacao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Contratacao lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Fatura> getFaturas() {
        return this.faturas;
    }

    public void setFaturas(Set<Fatura> faturas) {
        if (this.faturas != null) {
            this.faturas.forEach(i -> i.setContratacao(null));
        }
        if (faturas != null) {
            faturas.forEach(i -> i.setContratacao(this));
        }
        this.faturas = faturas;
    }

    public Contratacao faturas(Set<Fatura> faturas) {
        this.setFaturas(faturas);
        return this;
    }

    public Contratacao addFatura(Fatura fatura) {
        this.faturas.add(fatura);
        fatura.setContratacao(this);
        return this;
    }

    public Contratacao removeFatura(Fatura fatura) {
        this.faturas.remove(fatura);
        fatura.setContratacao(null);
        return this;
    }

    public TomadaPreco getSolicitacaoColeta() {
        return this.solicitacaoColeta;
    }

    public void setSolicitacaoColeta(TomadaPreco tomadaPreco) {
        if (this.solicitacaoColeta != null) {
            this.solicitacaoColeta.setContratacao(null);
        }
        if (tomadaPreco != null) {
            tomadaPreco.setContratacao(this);
        }
        this.solicitacaoColeta = tomadaPreco;
    }

    public Contratacao solicitacaoColeta(TomadaPreco tomadaPreco) {
        this.setSolicitacaoColeta(tomadaPreco);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Contratacao transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contratacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Contratacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contratacao{" +
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
            "}";
    }
}
