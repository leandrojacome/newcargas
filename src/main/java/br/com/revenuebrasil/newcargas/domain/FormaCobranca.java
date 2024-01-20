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
 * A FormaCobranca.
 */
@Entity
@Table(name = "forma_cobranca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormaCobranca implements Serializable {

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

    @Size(min = 2, max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formaCobranca")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "embarcador", "transportadora", "tipoCarga", "tipoFrete", "formaCobranca", "regiaoOrigem", "regiaoDestino" },
        allowSetters = true
    )
    private Set<TabelaFrete> tabelaFretes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formaCobranca")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "embarcador", "transportadora", "contratacao", "formaCobranca" }, allowSetters = true)
    private Set<Fatura> fatutas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormaCobranca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public FormaCobranca nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public FormaCobranca descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<TabelaFrete> getTabelaFretes() {
        return this.tabelaFretes;
    }

    public void setTabelaFretes(Set<TabelaFrete> tabelaFretes) {
        if (this.tabelaFretes != null) {
            this.tabelaFretes.forEach(i -> i.setFormaCobranca(null));
        }
        if (tabelaFretes != null) {
            tabelaFretes.forEach(i -> i.setFormaCobranca(this));
        }
        this.tabelaFretes = tabelaFretes;
    }

    public FormaCobranca tabelaFretes(Set<TabelaFrete> tabelaFretes) {
        this.setTabelaFretes(tabelaFretes);
        return this;
    }

    public FormaCobranca addTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.add(tabelaFrete);
        tabelaFrete.setFormaCobranca(this);
        return this;
    }

    public FormaCobranca removeTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.remove(tabelaFrete);
        tabelaFrete.setFormaCobranca(null);
        return this;
    }

    public Set<Fatura> getFatutas() {
        return this.fatutas;
    }

    public void setFatutas(Set<Fatura> faturas) {
        if (this.fatutas != null) {
            this.fatutas.forEach(i -> i.setFormaCobranca(null));
        }
        if (faturas != null) {
            faturas.forEach(i -> i.setFormaCobranca(this));
        }
        this.fatutas = faturas;
    }

    public FormaCobranca fatutas(Set<Fatura> faturas) {
        this.setFatutas(faturas);
        return this;
    }

    public FormaCobranca addFatuta(Fatura fatura) {
        this.fatutas.add(fatura);
        fatura.setFormaCobranca(this);
        return this;
    }

    public FormaCobranca removeFatuta(Fatura fatura) {
        this.fatutas.remove(fatura);
        fatura.setFormaCobranca(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormaCobranca)) {
            return false;
        }
        return getId() != null && getId().equals(((FormaCobranca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormaCobranca{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
