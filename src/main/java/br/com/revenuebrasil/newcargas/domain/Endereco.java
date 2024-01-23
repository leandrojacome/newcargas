package br.com.revenuebrasil.newcargas.domain;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoEndereco;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Endereco.
 */
@Entity
@Table(name = "endereco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "endereco")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Endereco extends AbstractAuditingEntity<Long> implements Serializable {

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
    private TipoEndereco tipo;

    @NotNull
    @Size(min = 8, max = 8)
    @Column(name = "cep", length = 8, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cep;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "endereco", length = 150, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String endereco;

    @Size(min = 1, max = 10)
    @Column(name = "numero", length = 10)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String numero;

    @Size(min = 2, max = 150)
    @Column(name = "complemento", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String complemento;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "bairro", length = 150, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String bairro;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "enderecos", "embarcadors", "transportadoras", "estado" }, allowSetters = true)
    private Cidade cidade;

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
    @JsonIgnoreProperties(value = { "enderecoOrigems", "enderecoDestinos", "solicitacaoColeta" }, allowSetters = true)
    private NotaFiscalColeta notaFiscalColetaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "enderecoOrigems", "enderecoDestinos", "solicitacaoColeta" }, allowSetters = true)
    private NotaFiscalColeta notaFiscalColetaDestino;

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
    private SolicitacaoColeta solicitacaoColetaOrigem;

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
    private SolicitacaoColeta solicitacaoColetaDestino;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Endereco id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEndereco getTipo() {
        return this.tipo;
    }

    public Endereco tipo(TipoEndereco tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoEndereco tipo) {
        this.tipo = tipo;
    }

    public String getCep() {
        return this.cep;
    }

    public Endereco cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Endereco endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return this.numero;
    }

    public Endereco numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public Endereco complemento(String complemento) {
        this.setComplemento(complemento);
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Endereco bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    // Inherited createdBy methods
    public Endereco createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Endereco createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Endereco lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Endereco lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Cidade getCidade() {
        return this.cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Endereco cidade(Cidade cidade) {
        this.setCidade(cidade);
        return this;
    }

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public Endereco embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Endereco transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    public NotaFiscalColeta getNotaFiscalColetaOrigem() {
        return this.notaFiscalColetaOrigem;
    }

    public void setNotaFiscalColetaOrigem(NotaFiscalColeta notaFiscalColeta) {
        this.notaFiscalColetaOrigem = notaFiscalColeta;
    }

    public Endereco notaFiscalColetaOrigem(NotaFiscalColeta notaFiscalColeta) {
        this.setNotaFiscalColetaOrigem(notaFiscalColeta);
        return this;
    }

    public NotaFiscalColeta getNotaFiscalColetaDestino() {
        return this.notaFiscalColetaDestino;
    }

    public void setNotaFiscalColetaDestino(NotaFiscalColeta notaFiscalColeta) {
        this.notaFiscalColetaDestino = notaFiscalColeta;
    }

    public Endereco notaFiscalColetaDestino(NotaFiscalColeta notaFiscalColeta) {
        this.setNotaFiscalColetaDestino(notaFiscalColeta);
        return this;
    }

    public SolicitacaoColeta getSolicitacaoColetaOrigem() {
        return this.solicitacaoColetaOrigem;
    }

    public void setSolicitacaoColetaOrigem(SolicitacaoColeta solicitacaoColeta) {
        this.solicitacaoColetaOrigem = solicitacaoColeta;
    }

    public Endereco solicitacaoColetaOrigem(SolicitacaoColeta solicitacaoColeta) {
        this.setSolicitacaoColetaOrigem(solicitacaoColeta);
        return this;
    }

    public SolicitacaoColeta getSolicitacaoColetaDestino() {
        return this.solicitacaoColetaDestino;
    }

    public void setSolicitacaoColetaDestino(SolicitacaoColeta solicitacaoColeta) {
        this.solicitacaoColetaDestino = solicitacaoColeta;
    }

    public Endereco solicitacaoColetaDestino(SolicitacaoColeta solicitacaoColeta) {
        this.setSolicitacaoColetaDestino(solicitacaoColeta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Endereco)) {
            return false;
        }
        return getId() != null && getId().equals(((Endereco) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Endereco{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", cep='" + getCep() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", numero='" + getNumero() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
