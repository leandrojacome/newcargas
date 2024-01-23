package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cidade.
 */
@Entity
@Table(name = "cidade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cidade")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Cidade extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "nome", length = 150, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nome;

    @Min(value = 7)
    @Max(value = 7)
    @Column(name = "codigo_ibge")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer codigoIbge;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
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
    private Set<Endereco> enderecos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "solitacaoColetas", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Set<Embarcador> embarcadors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Set<Transportadora> transportadoras = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cidades" }, allowSetters = true)
    private Estado estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cidade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Cidade nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigoIbge() {
        return this.codigoIbge;
    }

    public Cidade codigoIbge(Integer codigoIbge) {
        this.setCodigoIbge(codigoIbge);
        return this;
    }

    public void setCodigoIbge(Integer codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    // Inherited createdBy methods
    public Cidade createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Cidade createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Cidade lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Cidade lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Endereco> getEnderecos() {
        return this.enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        if (this.enderecos != null) {
            this.enderecos.forEach(i -> i.setCidade(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setCidade(this));
        }
        this.enderecos = enderecos;
    }

    public Cidade enderecos(Set<Endereco> enderecos) {
        this.setEnderecos(enderecos);
        return this;
    }

    public Cidade addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setCidade(this);
        return this;
    }

    public Cidade removeEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setCidade(null);
        return this;
    }

    public Set<Embarcador> getEmbarcadors() {
        return this.embarcadors;
    }

    public void setEmbarcadors(Set<Embarcador> embarcadors) {
        if (this.embarcadors != null) {
            this.embarcadors.forEach(i -> i.setCidade(null));
        }
        if (embarcadors != null) {
            embarcadors.forEach(i -> i.setCidade(this));
        }
        this.embarcadors = embarcadors;
    }

    public Cidade embarcadors(Set<Embarcador> embarcadors) {
        this.setEmbarcadors(embarcadors);
        return this;
    }

    public Cidade addEmbarcador(Embarcador embarcador) {
        this.embarcadors.add(embarcador);
        embarcador.setCidade(this);
        return this;
    }

    public Cidade removeEmbarcador(Embarcador embarcador) {
        this.embarcadors.remove(embarcador);
        embarcador.setCidade(null);
        return this;
    }

    public Set<Transportadora> getTransportadoras() {
        return this.transportadoras;
    }

    public void setTransportadoras(Set<Transportadora> transportadoras) {
        if (this.transportadoras != null) {
            this.transportadoras.forEach(i -> i.setCidade(null));
        }
        if (transportadoras != null) {
            transportadoras.forEach(i -> i.setCidade(this));
        }
        this.transportadoras = transportadoras;
    }

    public Cidade transportadoras(Set<Transportadora> transportadoras) {
        this.setTransportadoras(transportadoras);
        return this;
    }

    public Cidade addTransportadora(Transportadora transportadora) {
        this.transportadoras.add(transportadora);
        transportadora.setCidade(this);
        return this;
    }

    public Cidade removeTransportadora(Transportadora transportadora) {
        this.transportadoras.remove(transportadora);
        transportadora.setCidade(null);
        return this;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Cidade estado(Estado estado) {
        this.setEstado(estado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cidade)) {
            return false;
        }
        return getId() != null && getId().equals(((Cidade) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cidade{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigoIbge=" + getCodigoIbge() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
