package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Roteirizacao.
 */
@Entity
@Table(name = "roteirizacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roteirizacao")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Roteirizacao extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_hora_primeira_coleta", nullable = false)
    private ZonedDateTime dataHoraPrimeiraColeta;

    @Column(name = "data_hora_ultima_coleta")
    private ZonedDateTime dataHoraUltimaColeta;

    @Column(name = "data_hora_primeira_entrega")
    private ZonedDateTime dataHoraPrimeiraEntrega;

    @Column(name = "data_hora_ultima_entrega")
    private ZonedDateTime dataHoraUltimaEntrega;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roteirizacao")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "solicitacaoColeta", "roteirizacao", "statusColetaOrigem", "statusColetaDestino" }, allowSetters = true)
    private Set<HistoricoStatusColeta> historicoStatusColetas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roteirizacao")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "notaFiscalColetas",
            "enderecoOrigems",
            "enderecoDestinos",
            "historicoStatusColetas",
            "embarcador",
            "statusColeta",
            "roteirizacao",
            "tipoVeiculo",
        },
        allowSetters = true
    )
    private Set<SolicitacaoColeta> solitacaoColetas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roteirizacao")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "contratacao", "transportadora", "roteirizacao" }, allowSetters = true)
    private Set<TomadaPreco> tomadaPrecos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "solicitacaoColetas",
            "historicoStatusColetaOrigems",
            "historicoStatusColetaDestinos",
            "roteirizacaos",
            "statusColetaOrigems",
            "statusColetaDestinos",
        },
        allowSetters = true
    )
    private StatusColeta statusColeta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Roteirizacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraPrimeiraColeta() {
        return this.dataHoraPrimeiraColeta;
    }

    public Roteirizacao dataHoraPrimeiraColeta(ZonedDateTime dataHoraPrimeiraColeta) {
        this.setDataHoraPrimeiraColeta(dataHoraPrimeiraColeta);
        return this;
    }

    public void setDataHoraPrimeiraColeta(ZonedDateTime dataHoraPrimeiraColeta) {
        this.dataHoraPrimeiraColeta = dataHoraPrimeiraColeta;
    }

    public ZonedDateTime getDataHoraUltimaColeta() {
        return this.dataHoraUltimaColeta;
    }

    public Roteirizacao dataHoraUltimaColeta(ZonedDateTime dataHoraUltimaColeta) {
        this.setDataHoraUltimaColeta(dataHoraUltimaColeta);
        return this;
    }

    public void setDataHoraUltimaColeta(ZonedDateTime dataHoraUltimaColeta) {
        this.dataHoraUltimaColeta = dataHoraUltimaColeta;
    }

    public ZonedDateTime getDataHoraPrimeiraEntrega() {
        return this.dataHoraPrimeiraEntrega;
    }

    public Roteirizacao dataHoraPrimeiraEntrega(ZonedDateTime dataHoraPrimeiraEntrega) {
        this.setDataHoraPrimeiraEntrega(dataHoraPrimeiraEntrega);
        return this;
    }

    public void setDataHoraPrimeiraEntrega(ZonedDateTime dataHoraPrimeiraEntrega) {
        this.dataHoraPrimeiraEntrega = dataHoraPrimeiraEntrega;
    }

    public ZonedDateTime getDataHoraUltimaEntrega() {
        return this.dataHoraUltimaEntrega;
    }

    public Roteirizacao dataHoraUltimaEntrega(ZonedDateTime dataHoraUltimaEntrega) {
        this.setDataHoraUltimaEntrega(dataHoraUltimaEntrega);
        return this;
    }

    public void setDataHoraUltimaEntrega(ZonedDateTime dataHoraUltimaEntrega) {
        this.dataHoraUltimaEntrega = dataHoraUltimaEntrega;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public Roteirizacao valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Roteirizacao observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getCancelado() {
        return this.cancelado;
    }

    public Roteirizacao cancelado(Boolean cancelado) {
        this.setCancelado(cancelado);
        return this;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public Roteirizacao removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    // Inherited createdBy methods
    public Roteirizacao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Roteirizacao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Roteirizacao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Roteirizacao lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<HistoricoStatusColeta> getHistoricoStatusColetas() {
        return this.historicoStatusColetas;
    }

    public void setHistoricoStatusColetas(Set<HistoricoStatusColeta> historicoStatusColetas) {
        if (this.historicoStatusColetas != null) {
            this.historicoStatusColetas.forEach(i -> i.setRoteirizacao(null));
        }
        if (historicoStatusColetas != null) {
            historicoStatusColetas.forEach(i -> i.setRoteirizacao(this));
        }
        this.historicoStatusColetas = historicoStatusColetas;
    }

    public Roteirizacao historicoStatusColetas(Set<HistoricoStatusColeta> historicoStatusColetas) {
        this.setHistoricoStatusColetas(historicoStatusColetas);
        return this;
    }

    public Roteirizacao addHistoricoStatusColeta(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetas.add(historicoStatusColeta);
        historicoStatusColeta.setRoteirizacao(this);
        return this;
    }

    public Roteirizacao removeHistoricoStatusColeta(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetas.remove(historicoStatusColeta);
        historicoStatusColeta.setRoteirizacao(null);
        return this;
    }

    public Set<SolicitacaoColeta> getSolitacaoColetas() {
        return this.solitacaoColetas;
    }

    public void setSolitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        if (this.solitacaoColetas != null) {
            this.solitacaoColetas.forEach(i -> i.setRoteirizacao(null));
        }
        if (solicitacaoColetas != null) {
            solicitacaoColetas.forEach(i -> i.setRoteirizacao(this));
        }
        this.solitacaoColetas = solicitacaoColetas;
    }

    public Roteirizacao solitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        this.setSolitacaoColetas(solicitacaoColetas);
        return this;
    }

    public Roteirizacao addSolitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solitacaoColetas.add(solicitacaoColeta);
        solicitacaoColeta.setRoteirizacao(this);
        return this;
    }

    public Roteirizacao removeSolitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solitacaoColetas.remove(solicitacaoColeta);
        solicitacaoColeta.setRoteirizacao(null);
        return this;
    }

    public Set<TomadaPreco> getTomadaPrecos() {
        return this.tomadaPrecos;
    }

    public void setTomadaPrecos(Set<TomadaPreco> tomadaPrecos) {
        if (this.tomadaPrecos != null) {
            this.tomadaPrecos.forEach(i -> i.setRoteirizacao(null));
        }
        if (tomadaPrecos != null) {
            tomadaPrecos.forEach(i -> i.setRoteirizacao(this));
        }
        this.tomadaPrecos = tomadaPrecos;
    }

    public Roteirizacao tomadaPrecos(Set<TomadaPreco> tomadaPrecos) {
        this.setTomadaPrecos(tomadaPrecos);
        return this;
    }

    public Roteirizacao addTomadaPreco(TomadaPreco tomadaPreco) {
        this.tomadaPrecos.add(tomadaPreco);
        tomadaPreco.setRoteirizacao(this);
        return this;
    }

    public Roteirizacao removeTomadaPreco(TomadaPreco tomadaPreco) {
        this.tomadaPrecos.remove(tomadaPreco);
        tomadaPreco.setRoteirizacao(null);
        return this;
    }

    public StatusColeta getStatusColeta() {
        return this.statusColeta;
    }

    public void setStatusColeta(StatusColeta statusColeta) {
        this.statusColeta = statusColeta;
    }

    public Roteirizacao statusColeta(StatusColeta statusColeta) {
        this.setStatusColeta(statusColeta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roteirizacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Roteirizacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Roteirizacao{" +
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
            "}";
    }
}
