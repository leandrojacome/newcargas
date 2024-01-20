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
 * A TipoCarga.
 */
@Entity
@Table(name = "tipo_carga")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoCarga implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoCarga")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "embarcador", "transportadora", "tipoCarga", "tipoFrete", "formaCobranca", "regiaoOrigem", "regiaoDestino" },
        allowSetters = true
    )
    private Set<TabelaFrete> tabelaFretes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoCarga id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoCarga nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TipoCarga descricao(String descricao) {
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
            this.tabelaFretes.forEach(i -> i.setTipoCarga(null));
        }
        if (tabelaFretes != null) {
            tabelaFretes.forEach(i -> i.setTipoCarga(this));
        }
        this.tabelaFretes = tabelaFretes;
    }

    public TipoCarga tabelaFretes(Set<TabelaFrete> tabelaFretes) {
        this.setTabelaFretes(tabelaFretes);
        return this;
    }

    public TipoCarga addTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.add(tabelaFrete);
        tabelaFrete.setTipoCarga(this);
        return this;
    }

    public TipoCarga removeTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.remove(tabelaFrete);
        tabelaFrete.setTipoCarga(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoCarga)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoCarga) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoCarga{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
