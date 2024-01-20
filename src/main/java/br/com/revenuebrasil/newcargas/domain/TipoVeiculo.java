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
 * A TipoVeiculo.
 */
@Entity
@Table(name = "tipo_veiculo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoVeiculo implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoVeiculo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoVeiculo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoVeiculo nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TipoVeiculo descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<SolicitacaoColeta> getSolitacaoColetas() {
        return this.solitacaoColetas;
    }

    public void setSolitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        if (this.solitacaoColetas != null) {
            this.solitacaoColetas.forEach(i -> i.setTipoVeiculo(null));
        }
        if (solicitacaoColetas != null) {
            solicitacaoColetas.forEach(i -> i.setTipoVeiculo(this));
        }
        this.solitacaoColetas = solicitacaoColetas;
    }

    public TipoVeiculo solitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        this.setSolitacaoColetas(solicitacaoColetas);
        return this;
    }

    public TipoVeiculo addSolitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solitacaoColetas.add(solicitacaoColeta);
        solicitacaoColeta.setTipoVeiculo(this);
        return this;
    }

    public TipoVeiculo removeSolitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solitacaoColetas.remove(solicitacaoColeta);
        solicitacaoColeta.setTipoVeiculo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoVeiculo)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoVeiculo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoVeiculo{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
