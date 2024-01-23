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
 * A Regiao.
 */
@Entity
@Table(name = "regiao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "regiao")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Regiao extends AbstractAuditingEntity<Long> implements Serializable {

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

    @Size(min = 2, max = 5)
    @Column(name = "sigla", length = 5, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sigla;

    @Size(min = 2, max = 500)
    @Column(name = "descricao", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String descricao;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "regiaoOrigem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "embarcador", "transportadora", "tipoCarga", "tipoFrete", "formaCobranca", "regiaoOrigem", "regiaoDestino" },
        allowSetters = true
    )
    private Set<TabelaFrete> tabelaFreteOrigems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "regiaoDestino")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "embarcador", "transportadora", "tipoCarga", "tipoFrete", "formaCobranca", "regiaoOrigem", "regiaoDestino" },
        allowSetters = true
    )
    private Set<TabelaFrete> tabelaFreteDestinos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Regiao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Regiao nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return this.sigla;
    }

    public Regiao sigla(String sigla) {
        this.setSigla(sigla);
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Regiao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Inherited createdBy methods
    public Regiao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Regiao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Regiao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Regiao lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<TabelaFrete> getTabelaFreteOrigems() {
        return this.tabelaFreteOrigems;
    }

    public void setTabelaFreteOrigems(Set<TabelaFrete> tabelaFretes) {
        if (this.tabelaFreteOrigems != null) {
            this.tabelaFreteOrigems.forEach(i -> i.setRegiaoOrigem(null));
        }
        if (tabelaFretes != null) {
            tabelaFretes.forEach(i -> i.setRegiaoOrigem(this));
        }
        this.tabelaFreteOrigems = tabelaFretes;
    }

    public Regiao tabelaFreteOrigems(Set<TabelaFrete> tabelaFretes) {
        this.setTabelaFreteOrigems(tabelaFretes);
        return this;
    }

    public Regiao addTabelaFreteOrigem(TabelaFrete tabelaFrete) {
        this.tabelaFreteOrigems.add(tabelaFrete);
        tabelaFrete.setRegiaoOrigem(this);
        return this;
    }

    public Regiao removeTabelaFreteOrigem(TabelaFrete tabelaFrete) {
        this.tabelaFreteOrigems.remove(tabelaFrete);
        tabelaFrete.setRegiaoOrigem(null);
        return this;
    }

    public Set<TabelaFrete> getTabelaFreteDestinos() {
        return this.tabelaFreteDestinos;
    }

    public void setTabelaFreteDestinos(Set<TabelaFrete> tabelaFretes) {
        if (this.tabelaFreteDestinos != null) {
            this.tabelaFreteDestinos.forEach(i -> i.setRegiaoDestino(null));
        }
        if (tabelaFretes != null) {
            tabelaFretes.forEach(i -> i.setRegiaoDestino(this));
        }
        this.tabelaFreteDestinos = tabelaFretes;
    }

    public Regiao tabelaFreteDestinos(Set<TabelaFrete> tabelaFretes) {
        this.setTabelaFreteDestinos(tabelaFretes);
        return this;
    }

    public Regiao addTabelaFreteDestino(TabelaFrete tabelaFrete) {
        this.tabelaFreteDestinos.add(tabelaFrete);
        tabelaFrete.setRegiaoDestino(this);
        return this;
    }

    public Regiao removeTabelaFreteDestino(TabelaFrete tabelaFrete) {
        this.tabelaFreteDestinos.remove(tabelaFrete);
        tabelaFrete.setRegiaoDestino(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regiao)) {
            return false;
        }
        return getId() != null && getId().equals(((Regiao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Regiao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sigla='" + getSigla() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
