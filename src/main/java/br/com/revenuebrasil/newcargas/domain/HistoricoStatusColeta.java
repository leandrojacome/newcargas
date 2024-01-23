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
 * A HistoricoStatusColeta.
 */
@Entity
@Table(name = "historico_status_coleta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "historicostatuscoleta")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class HistoricoStatusColeta extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_criacao", nullable = false)
    private ZonedDateTime dataCriacao;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observacao;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @ManyToOne(fetch = FetchType.LAZY)
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
    private SolicitacaoColeta solicitacaoColeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "historicoStatusColetas", "solitacaoColetas", "tomadaPrecos", "statusColeta" }, allowSetters = true)
    private Roteirizacao roteirizacao;

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
    private StatusColeta statusColetaOrigem;

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
    private StatusColeta statusColetaDestino;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoricoStatusColeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataCriacao() {
        return this.dataCriacao;
    }

    public HistoricoStatusColeta dataCriacao(ZonedDateTime dataCriacao) {
        this.setDataCriacao(dataCriacao);
        return this;
    }

    public void setDataCriacao(ZonedDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public HistoricoStatusColeta observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Inherited createdBy methods
    public HistoricoStatusColeta createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HistoricoStatusColeta createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HistoricoStatusColeta lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HistoricoStatusColeta lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public SolicitacaoColeta getSolicitacaoColeta() {
        return this.solicitacaoColeta;
    }

    public void setSolicitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solicitacaoColeta = solicitacaoColeta;
    }

    public HistoricoStatusColeta solicitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.setSolicitacaoColeta(solicitacaoColeta);
        return this;
    }

    public Roteirizacao getRoteirizacao() {
        return this.roteirizacao;
    }

    public void setRoteirizacao(Roteirizacao roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    public HistoricoStatusColeta roteirizacao(Roteirizacao roteirizacao) {
        this.setRoteirizacao(roteirizacao);
        return this;
    }

    public StatusColeta getStatusColetaOrigem() {
        return this.statusColetaOrigem;
    }

    public void setStatusColetaOrigem(StatusColeta statusColeta) {
        this.statusColetaOrigem = statusColeta;
    }

    public HistoricoStatusColeta statusColetaOrigem(StatusColeta statusColeta) {
        this.setStatusColetaOrigem(statusColeta);
        return this;
    }

    public StatusColeta getStatusColetaDestino() {
        return this.statusColetaDestino;
    }

    public void setStatusColetaDestino(StatusColeta statusColeta) {
        this.statusColetaDestino = statusColeta;
    }

    public HistoricoStatusColeta statusColetaDestino(StatusColeta statusColeta) {
        this.setStatusColetaDestino(statusColeta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricoStatusColeta)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoricoStatusColeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoStatusColeta{" +
            "id=" + getId() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
