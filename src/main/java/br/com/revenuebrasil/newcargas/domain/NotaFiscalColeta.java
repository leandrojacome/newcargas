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
 * A NotaFiscalColeta.
 */
@Entity
@Table(name = "nota_fiscal_coleta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "notafiscalcoleta")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class NotaFiscalColeta extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "numero", length = 20, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String numero;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "serie", length = 20, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String serie;

    @Size(min = 2, max = 150)
    @Column(name = "remetente", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String remetente;

    @Size(min = 2, max = 150)
    @Column(name = "destinatario", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String destinatario;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "metro_cubico")
    private Double metroCubico;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "quantidade")
    private Double quantidade;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "peso")
    private Double peso;

    @Column(name = "data_emissao")
    private ZonedDateTime dataEmissao;

    @Column(name = "data_saida")
    private ZonedDateTime dataSaida;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total")
    private Double valorTotal;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "peso_total")
    private Double pesoTotal;

    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "quantidade_total")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer quantidadeTotal;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observacao;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "notaFiscalColetaOrigem")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "notaFiscalColetaDestino")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotaFiscalColeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public NotaFiscalColeta numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return this.serie;
    }

    public NotaFiscalColeta serie(String serie) {
        this.setSerie(serie);
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getRemetente() {
        return this.remetente;
    }

    public NotaFiscalColeta remetente(String remetente) {
        this.setRemetente(remetente);
        return this;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return this.destinatario;
    }

    public NotaFiscalColeta destinatario(String destinatario) {
        this.setDestinatario(destinatario);
        return this;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Double getMetroCubico() {
        return this.metroCubico;
    }

    public NotaFiscalColeta metroCubico(Double metroCubico) {
        this.setMetroCubico(metroCubico);
        return this;
    }

    public void setMetroCubico(Double metroCubico) {
        this.metroCubico = metroCubico;
    }

    public Double getQuantidade() {
        return this.quantidade;
    }

    public NotaFiscalColeta quantidade(Double quantidade) {
        this.setQuantidade(quantidade);
        return this;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPeso() {
        return this.peso;
    }

    public NotaFiscalColeta peso(Double peso) {
        this.setPeso(peso);
        return this;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public ZonedDateTime getDataEmissao() {
        return this.dataEmissao;
    }

    public NotaFiscalColeta dataEmissao(ZonedDateTime dataEmissao) {
        this.setDataEmissao(dataEmissao);
        return this;
    }

    public void setDataEmissao(ZonedDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public ZonedDateTime getDataSaida() {
        return this.dataSaida;
    }

    public NotaFiscalColeta dataSaida(ZonedDateTime dataSaida) {
        this.setDataSaida(dataSaida);
        return this;
    }

    public void setDataSaida(ZonedDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public NotaFiscalColeta valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getPesoTotal() {
        return this.pesoTotal;
    }

    public NotaFiscalColeta pesoTotal(Double pesoTotal) {
        this.setPesoTotal(pesoTotal);
        return this;
    }

    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public Integer getQuantidadeTotal() {
        return this.quantidadeTotal;
    }

    public NotaFiscalColeta quantidadeTotal(Integer quantidadeTotal) {
        this.setQuantidadeTotal(quantidadeTotal);
        return this;
    }

    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public NotaFiscalColeta observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Inherited createdBy methods
    public NotaFiscalColeta createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public NotaFiscalColeta createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public NotaFiscalColeta lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public NotaFiscalColeta lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Endereco> getEnderecoOrigems() {
        return this.enderecoOrigems;
    }

    public void setEnderecoOrigems(Set<Endereco> enderecos) {
        if (this.enderecoOrigems != null) {
            this.enderecoOrigems.forEach(i -> i.setNotaFiscalColetaOrigem(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setNotaFiscalColetaOrigem(this));
        }
        this.enderecoOrigems = enderecos;
    }

    public NotaFiscalColeta enderecoOrigems(Set<Endereco> enderecos) {
        this.setEnderecoOrigems(enderecos);
        return this;
    }

    public NotaFiscalColeta addEnderecoOrigem(Endereco endereco) {
        this.enderecoOrigems.add(endereco);
        endereco.setNotaFiscalColetaOrigem(this);
        return this;
    }

    public NotaFiscalColeta removeEnderecoOrigem(Endereco endereco) {
        this.enderecoOrigems.remove(endereco);
        endereco.setNotaFiscalColetaOrigem(null);
        return this;
    }

    public Set<Endereco> getEnderecoDestinos() {
        return this.enderecoDestinos;
    }

    public void setEnderecoDestinos(Set<Endereco> enderecos) {
        if (this.enderecoDestinos != null) {
            this.enderecoDestinos.forEach(i -> i.setNotaFiscalColetaDestino(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setNotaFiscalColetaDestino(this));
        }
        this.enderecoDestinos = enderecos;
    }

    public NotaFiscalColeta enderecoDestinos(Set<Endereco> enderecos) {
        this.setEnderecoDestinos(enderecos);
        return this;
    }

    public NotaFiscalColeta addEnderecoDestino(Endereco endereco) {
        this.enderecoDestinos.add(endereco);
        endereco.setNotaFiscalColetaDestino(this);
        return this;
    }

    public NotaFiscalColeta removeEnderecoDestino(Endereco endereco) {
        this.enderecoDestinos.remove(endereco);
        endereco.setNotaFiscalColetaDestino(null);
        return this;
    }

    public SolicitacaoColeta getSolicitacaoColeta() {
        return this.solicitacaoColeta;
    }

    public void setSolicitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solicitacaoColeta = solicitacaoColeta;
    }

    public NotaFiscalColeta solicitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.setSolicitacaoColeta(solicitacaoColeta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotaFiscalColeta)) {
            return false;
        }
        return getId() != null && getId().equals(((NotaFiscalColeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaFiscalColeta{" +
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
            "}";
    }
}
