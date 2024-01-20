package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "nome", length = 150, nullable = false, unique = true)
    private String nome;

    @Min(value = 7)
    @Max(value = 7)
    @Column(name = "codigo_ibge")
    private Integer codigoIbge;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cidades" }, allowSetters = true)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "cidades", "contaBancarias", "tabelaFretes", "solitacaoColetas", "notificacaos", "faturas" },
        allowSetters = true
    )
    private Embarcador embarcador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "cidades", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas" },
        allowSetters = true
    )
    private Transportadora transportadora;

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

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public Cidade embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Cidade transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
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
            "}";
    }
}
