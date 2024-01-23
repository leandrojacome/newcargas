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
 * A SolicitacaoColeta.
 */
@Entity
@Table(name = "solicitacao_coleta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "solicitacaocoleta")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class SolicitacaoColeta extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "coletado", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean coletado;

    @NotNull
    @Column(name = "data_hora_coleta", nullable = false)
    private ZonedDateTime dataHoraColeta;

    @NotNull
    @Column(name = "entregue", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean entregue;

    @Column(name = "data_hora_entrega")
    private ZonedDateTime dataHoraEntrega;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitacaoColeta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "enderecoOrigems", "enderecoDestinos", "solicitacaoColeta" }, allowSetters = true)
    private Set<NotaFiscalColeta> notaFiscalColetas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitacaoColetaOrigem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "cidade",
            "embarcador",
            "transportadora",
            "notaFiscalColetaOrigem",
            "notaFiscalColetaDestino",
            "solicitacaoColetaOrigem",
            "solicitacaoColetaDestino",
        },
        allowSetters = true
    )
    private Set<Endereco> enderecoOrigems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitacaoColetaDestino")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "cidade",
            "embarcador",
            "transportadora",
            "notaFiscalColetaOrigem",
            "notaFiscalColetaDestino",
            "solicitacaoColetaOrigem",
            "solicitacaoColetaDestino",
        },
        allowSetters = true
    )
    private Set<Endereco> enderecoDestinos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitacaoColeta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "solicitacaoColeta", "roteirizacao", "statusColetaOrigem", "statusColetaDestino" }, allowSetters = true)
    private Set<HistoricoStatusColeta> historicoStatusColetas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "solitacaoColetas", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Embarcador embarcador;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "historicoStatusColetas", "solitacaoColetas", "tomadaPrecos", "statusColeta" }, allowSetters = true)
    private Roteirizacao roteirizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "solitacaoColetas" }, allowSetters = true)
    private TipoVeiculo tipoVeiculo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SolicitacaoColeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getColetado() {
        return this.coletado;
    }

    public SolicitacaoColeta coletado(Boolean coletado) {
        this.setColetado(coletado);
        return this;
    }

    public void setColetado(Boolean coletado) {
        this.coletado = coletado;
    }

    public ZonedDateTime getDataHoraColeta() {
        return this.dataHoraColeta;
    }

    public SolicitacaoColeta dataHoraColeta(ZonedDateTime dataHoraColeta) {
        this.setDataHoraColeta(dataHoraColeta);
        return this;
    }

    public void setDataHoraColeta(ZonedDateTime dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }

    public Boolean getEntregue() {
        return this.entregue;
    }

    public SolicitacaoColeta entregue(Boolean entregue) {
        this.setEntregue(entregue);
        return this;
    }

    public void setEntregue(Boolean entregue) {
        this.entregue = entregue;
    }

    public ZonedDateTime getDataHoraEntrega() {
        return this.dataHoraEntrega;
    }

    public SolicitacaoColeta dataHoraEntrega(ZonedDateTime dataHoraEntrega) {
        this.setDataHoraEntrega(dataHoraEntrega);
        return this;
    }

    public void setDataHoraEntrega(ZonedDateTime dataHoraEntrega) {
        this.dataHoraEntrega = dataHoraEntrega;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public SolicitacaoColeta valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public SolicitacaoColeta observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getCancelado() {
        return this.cancelado;
    }

    public SolicitacaoColeta cancelado(Boolean cancelado) {
        this.setCancelado(cancelado);
        return this;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public SolicitacaoColeta removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    // Inherited createdBy methods
    public SolicitacaoColeta createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public SolicitacaoColeta createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public SolicitacaoColeta lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public SolicitacaoColeta lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<NotaFiscalColeta> getNotaFiscalColetas() {
        return this.notaFiscalColetas;
    }

    public void setNotaFiscalColetas(Set<NotaFiscalColeta> notaFiscalColetas) {
        if (this.notaFiscalColetas != null) {
            this.notaFiscalColetas.forEach(i -> i.setSolicitacaoColeta(null));
        }
        if (notaFiscalColetas != null) {
            notaFiscalColetas.forEach(i -> i.setSolicitacaoColeta(this));
        }
        this.notaFiscalColetas = notaFiscalColetas;
    }

    public SolicitacaoColeta notaFiscalColetas(Set<NotaFiscalColeta> notaFiscalColetas) {
        this.setNotaFiscalColetas(notaFiscalColetas);
        return this;
    }

    public SolicitacaoColeta addNotaFiscalColeta(NotaFiscalColeta notaFiscalColeta) {
        this.notaFiscalColetas.add(notaFiscalColeta);
        notaFiscalColeta.setSolicitacaoColeta(this);
        return this;
    }

    public SolicitacaoColeta removeNotaFiscalColeta(NotaFiscalColeta notaFiscalColeta) {
        this.notaFiscalColetas.remove(notaFiscalColeta);
        notaFiscalColeta.setSolicitacaoColeta(null);
        return this;
    }

    public Set<Endereco> getEnderecoOrigems() {
        return this.enderecoOrigems;
    }

    public void setEnderecoOrigems(Set<Endereco> enderecos) {
        if (this.enderecoOrigems != null) {
            this.enderecoOrigems.forEach(i -> i.setSolicitacaoColetaOrigem(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setSolicitacaoColetaOrigem(this));
        }
        this.enderecoOrigems = enderecos;
    }

    public SolicitacaoColeta enderecoOrigems(Set<Endereco> enderecos) {
        this.setEnderecoOrigems(enderecos);
        return this;
    }

    public SolicitacaoColeta addEnderecoOrigem(Endereco endereco) {
        this.enderecoOrigems.add(endereco);
        endereco.setSolicitacaoColetaOrigem(this);
        return this;
    }

    public SolicitacaoColeta removeEnderecoOrigem(Endereco endereco) {
        this.enderecoOrigems.remove(endereco);
        endereco.setSolicitacaoColetaOrigem(null);
        return this;
    }

    public Set<Endereco> getEnderecoDestinos() {
        return this.enderecoDestinos;
    }

    public void setEnderecoDestinos(Set<Endereco> enderecos) {
        if (this.enderecoDestinos != null) {
            this.enderecoDestinos.forEach(i -> i.setSolicitacaoColetaDestino(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setSolicitacaoColetaDestino(this));
        }
        this.enderecoDestinos = enderecos;
    }

    public SolicitacaoColeta enderecoDestinos(Set<Endereco> enderecos) {
        this.setEnderecoDestinos(enderecos);
        return this;
    }

    public SolicitacaoColeta addEnderecoDestino(Endereco endereco) {
        this.enderecoDestinos.add(endereco);
        endereco.setSolicitacaoColetaDestino(this);
        return this;
    }

    public SolicitacaoColeta removeEnderecoDestino(Endereco endereco) {
        this.enderecoDestinos.remove(endereco);
        endereco.setSolicitacaoColetaDestino(null);
        return this;
    }

    public Set<HistoricoStatusColeta> getHistoricoStatusColetas() {
        return this.historicoStatusColetas;
    }

    public void setHistoricoStatusColetas(Set<HistoricoStatusColeta> historicoStatusColetas) {
        if (this.historicoStatusColetas != null) {
            this.historicoStatusColetas.forEach(i -> i.setSolicitacaoColeta(null));
        }
        if (historicoStatusColetas != null) {
            historicoStatusColetas.forEach(i -> i.setSolicitacaoColeta(this));
        }
        this.historicoStatusColetas = historicoStatusColetas;
    }

    public SolicitacaoColeta historicoStatusColetas(Set<HistoricoStatusColeta> historicoStatusColetas) {
        this.setHistoricoStatusColetas(historicoStatusColetas);
        return this;
    }

    public SolicitacaoColeta addHistoricoStatusColeta(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetas.add(historicoStatusColeta);
        historicoStatusColeta.setSolicitacaoColeta(this);
        return this;
    }

    public SolicitacaoColeta removeHistoricoStatusColeta(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetas.remove(historicoStatusColeta);
        historicoStatusColeta.setSolicitacaoColeta(null);
        return this;
    }

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public SolicitacaoColeta embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public StatusColeta getStatusColeta() {
        return this.statusColeta;
    }

    public void setStatusColeta(StatusColeta statusColeta) {
        this.statusColeta = statusColeta;
    }

    public SolicitacaoColeta statusColeta(StatusColeta statusColeta) {
        this.setStatusColeta(statusColeta);
        return this;
    }

    public Roteirizacao getRoteirizacao() {
        return this.roteirizacao;
    }

    public void setRoteirizacao(Roteirizacao roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    public SolicitacaoColeta roteirizacao(Roteirizacao roteirizacao) {
        this.setRoteirizacao(roteirizacao);
        return this;
    }

    public TipoVeiculo getTipoVeiculo() {
        return this.tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public SolicitacaoColeta tipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.setTipoVeiculo(tipoVeiculo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolicitacaoColeta)) {
            return false;
        }
        return getId() != null && getId().equals(((SolicitacaoColeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoColeta{" +
            "id=" + getId() +
            ", coletado='" + getColetado() + "'" +
            ", dataHoraColeta='" + getDataHoraColeta() + "'" +
            ", entregue='" + getEntregue() + "'" +
            ", dataHoraEntrega='" + getDataHoraEntrega() + "'" +
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
