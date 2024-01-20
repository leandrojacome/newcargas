package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StatusColeta.
 */
@Entity
@Table(name = "status_coleta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusColeta implements Serializable {

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

    @Size(min = 2, max = 8)
    @Column(name = "cor", length = 8)
    private String cor;

    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "ordem")
    private Integer ordem;

    @Column(name = "estado_inicial")
    private Boolean estadoInicial;

    @Column(name = "estado_final")
    private Boolean estadoFinal;

    @Column(name = "permite_cancelar")
    private Boolean permiteCancelar;

    @Column(name = "permite_editar")
    private Boolean permiteEditar;

    @Column(name = "permite_excluir")
    private Boolean permiteExcluir;

    @Size(min = 2, max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Column(name = "data_cadastro", nullable = false)
    private ZonedDateTime dataCadastro;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_cadastro", length = 150)
    private String usuarioCadastro;

    @Column(name = "data_atualizacao")
    private ZonedDateTime dataAtualizacao;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_atualizacao", length = 150)
    private String usuarioAtualizacao;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "removido")
    private Boolean removido;

    @Column(name = "data_remocao")
    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_remocao", length = 150)
    private String usuarioRemocao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statusColeta")
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
    private Set<SolicitacaoColeta> solicitacaoColetas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statusColetaOrigem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "solicitacaoColeta", "roteirizacao", "statusColetaOrigem", "statusColetaDestino" }, allowSetters = true)
    private Set<HistoricoStatusColeta> historicoStatusColetaOrigems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statusColetaDestino")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "solicitacaoColeta", "roteirizacao", "statusColetaOrigem", "statusColetaDestino" }, allowSetters = true)
    private Set<HistoricoStatusColeta> historicoStatusColetaDestinos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statusColeta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historicoStatusColetas", "solitacaoColetas", "tomadaPrecos", "statusColeta" }, allowSetters = true)
    private Set<Roteirizacao> roteirizacaos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_status_coleta__status_coleta_origem",
        joinColumns = @JoinColumn(name = "status_coleta_id"),
        inverseJoinColumns = @JoinColumn(name = "status_coleta_origem_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<StatusColeta> statusColetaOrigems = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "statusColetaOrigems")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<StatusColeta> statusColetaDestinos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StatusColeta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public StatusColeta nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return this.cor;
    }

    public StatusColeta cor(String cor) {
        this.setCor(cor);
        return this;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getOrdem() {
        return this.ordem;
    }

    public StatusColeta ordem(Integer ordem) {
        this.setOrdem(ordem);
        return this;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getEstadoInicial() {
        return this.estadoInicial;
    }

    public StatusColeta estadoInicial(Boolean estadoInicial) {
        this.setEstadoInicial(estadoInicial);
        return this;
    }

    public void setEstadoInicial(Boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public Boolean getEstadoFinal() {
        return this.estadoFinal;
    }

    public StatusColeta estadoFinal(Boolean estadoFinal) {
        this.setEstadoFinal(estadoFinal);
        return this;
    }

    public void setEstadoFinal(Boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public Boolean getPermiteCancelar() {
        return this.permiteCancelar;
    }

    public StatusColeta permiteCancelar(Boolean permiteCancelar) {
        this.setPermiteCancelar(permiteCancelar);
        return this;
    }

    public void setPermiteCancelar(Boolean permiteCancelar) {
        this.permiteCancelar = permiteCancelar;
    }

    public Boolean getPermiteEditar() {
        return this.permiteEditar;
    }

    public StatusColeta permiteEditar(Boolean permiteEditar) {
        this.setPermiteEditar(permiteEditar);
        return this;
    }

    public void setPermiteEditar(Boolean permiteEditar) {
        this.permiteEditar = permiteEditar;
    }

    public Boolean getPermiteExcluir() {
        return this.permiteExcluir;
    }

    public StatusColeta permiteExcluir(Boolean permiteExcluir) {
        this.setPermiteExcluir(permiteExcluir);
        return this;
    }

    public void setPermiteExcluir(Boolean permiteExcluir) {
        this.permiteExcluir = permiteExcluir;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public StatusColeta descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public StatusColeta dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuarioCadastro() {
        return this.usuarioCadastro;
    }

    public StatusColeta usuarioCadastro(String usuarioCadastro) {
        this.setUsuarioCadastro(usuarioCadastro);
        return this;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public StatusColeta dataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.setDataAtualizacao(dataAtualizacao);
        return this;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioAtualizacao() {
        return this.usuarioAtualizacao;
    }

    public StatusColeta usuarioAtualizacao(String usuarioAtualizacao) {
        this.setUsuarioAtualizacao(usuarioAtualizacao);
        return this;
    }

    public void setUsuarioAtualizacao(String usuarioAtualizacao) {
        this.usuarioAtualizacao = usuarioAtualizacao;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public StatusColeta ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public StatusColeta removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public ZonedDateTime getDataRemocao() {
        return this.dataRemocao;
    }

    public StatusColeta dataRemocao(ZonedDateTime dataRemocao) {
        this.setDataRemocao(dataRemocao);
        return this;
    }

    public void setDataRemocao(ZonedDateTime dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getUsuarioRemocao() {
        return this.usuarioRemocao;
    }

    public StatusColeta usuarioRemocao(String usuarioRemocao) {
        this.setUsuarioRemocao(usuarioRemocao);
        return this;
    }

    public void setUsuarioRemocao(String usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public Set<SolicitacaoColeta> getSolicitacaoColetas() {
        return this.solicitacaoColetas;
    }

    public void setSolicitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        if (this.solicitacaoColetas != null) {
            this.solicitacaoColetas.forEach(i -> i.setStatusColeta(null));
        }
        if (solicitacaoColetas != null) {
            solicitacaoColetas.forEach(i -> i.setStatusColeta(this));
        }
        this.solicitacaoColetas = solicitacaoColetas;
    }

    public StatusColeta solicitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        this.setSolicitacaoColetas(solicitacaoColetas);
        return this;
    }

    public StatusColeta addSolicitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solicitacaoColetas.add(solicitacaoColeta);
        solicitacaoColeta.setStatusColeta(this);
        return this;
    }

    public StatusColeta removeSolicitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solicitacaoColetas.remove(solicitacaoColeta);
        solicitacaoColeta.setStatusColeta(null);
        return this;
    }

    public Set<HistoricoStatusColeta> getHistoricoStatusColetaOrigems() {
        return this.historicoStatusColetaOrigems;
    }

    public void setHistoricoStatusColetaOrigems(Set<HistoricoStatusColeta> historicoStatusColetas) {
        if (this.historicoStatusColetaOrigems != null) {
            this.historicoStatusColetaOrigems.forEach(i -> i.setStatusColetaOrigem(null));
        }
        if (historicoStatusColetas != null) {
            historicoStatusColetas.forEach(i -> i.setStatusColetaOrigem(this));
        }
        this.historicoStatusColetaOrigems = historicoStatusColetas;
    }

    public StatusColeta historicoStatusColetaOrigems(Set<HistoricoStatusColeta> historicoStatusColetas) {
        this.setHistoricoStatusColetaOrigems(historicoStatusColetas);
        return this;
    }

    public StatusColeta addHistoricoStatusColetaOrigem(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetaOrigems.add(historicoStatusColeta);
        historicoStatusColeta.setStatusColetaOrigem(this);
        return this;
    }

    public StatusColeta removeHistoricoStatusColetaOrigem(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetaOrigems.remove(historicoStatusColeta);
        historicoStatusColeta.setStatusColetaOrigem(null);
        return this;
    }

    public Set<HistoricoStatusColeta> getHistoricoStatusColetaDestinos() {
        return this.historicoStatusColetaDestinos;
    }

    public void setHistoricoStatusColetaDestinos(Set<HistoricoStatusColeta> historicoStatusColetas) {
        if (this.historicoStatusColetaDestinos != null) {
            this.historicoStatusColetaDestinos.forEach(i -> i.setStatusColetaDestino(null));
        }
        if (historicoStatusColetas != null) {
            historicoStatusColetas.forEach(i -> i.setStatusColetaDestino(this));
        }
        this.historicoStatusColetaDestinos = historicoStatusColetas;
    }

    public StatusColeta historicoStatusColetaDestinos(Set<HistoricoStatusColeta> historicoStatusColetas) {
        this.setHistoricoStatusColetaDestinos(historicoStatusColetas);
        return this;
    }

    public StatusColeta addHistoricoStatusColetaDestino(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetaDestinos.add(historicoStatusColeta);
        historicoStatusColeta.setStatusColetaDestino(this);
        return this;
    }

    public StatusColeta removeHistoricoStatusColetaDestino(HistoricoStatusColeta historicoStatusColeta) {
        this.historicoStatusColetaDestinos.remove(historicoStatusColeta);
        historicoStatusColeta.setStatusColetaDestino(null);
        return this;
    }

    public Set<Roteirizacao> getRoteirizacaos() {
        return this.roteirizacaos;
    }

    public void setRoteirizacaos(Set<Roteirizacao> roteirizacaos) {
        if (this.roteirizacaos != null) {
            this.roteirizacaos.forEach(i -> i.setStatusColeta(null));
        }
        if (roteirizacaos != null) {
            roteirizacaos.forEach(i -> i.setStatusColeta(this));
        }
        this.roteirizacaos = roteirizacaos;
    }

    public StatusColeta roteirizacaos(Set<Roteirizacao> roteirizacaos) {
        this.setRoteirizacaos(roteirizacaos);
        return this;
    }

    public StatusColeta addRoteirizacao(Roteirizacao roteirizacao) {
        this.roteirizacaos.add(roteirizacao);
        roteirizacao.setStatusColeta(this);
        return this;
    }

    public StatusColeta removeRoteirizacao(Roteirizacao roteirizacao) {
        this.roteirizacaos.remove(roteirizacao);
        roteirizacao.setStatusColeta(null);
        return this;
    }

    public Set<StatusColeta> getStatusColetaOrigems() {
        return this.statusColetaOrigems;
    }

    public void setStatusColetaOrigems(Set<StatusColeta> statusColetas) {
        this.statusColetaOrigems = statusColetas;
    }

    public StatusColeta statusColetaOrigems(Set<StatusColeta> statusColetas) {
        this.setStatusColetaOrigems(statusColetas);
        return this;
    }

    public StatusColeta addStatusColetaOrigem(StatusColeta statusColeta) {
        this.statusColetaOrigems.add(statusColeta);
        return this;
    }

    public StatusColeta removeStatusColetaOrigem(StatusColeta statusColeta) {
        this.statusColetaOrigems.remove(statusColeta);
        return this;
    }

    public Set<StatusColeta> getStatusColetaDestinos() {
        return this.statusColetaDestinos;
    }

    public void setStatusColetaDestinos(Set<StatusColeta> statusColetas) {
        if (this.statusColetaDestinos != null) {
            this.statusColetaDestinos.forEach(i -> i.removeStatusColetaOrigem(this));
        }
        if (statusColetas != null) {
            statusColetas.forEach(i -> i.addStatusColetaOrigem(this));
        }
        this.statusColetaDestinos = statusColetas;
    }

    public StatusColeta statusColetaDestinos(Set<StatusColeta> statusColetas) {
        this.setStatusColetaDestinos(statusColetas);
        return this;
    }

    public StatusColeta addStatusColetaDestino(StatusColeta statusColeta) {
        this.statusColetaDestinos.add(statusColeta);
        statusColeta.getStatusColetaOrigems().add(this);
        return this;
    }

    public StatusColeta removeStatusColetaDestino(StatusColeta statusColeta) {
        this.statusColetaDestinos.remove(statusColeta);
        statusColeta.getStatusColetaOrigems().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusColeta)) {
            return false;
        }
        return getId() != null && getId().equals(((StatusColeta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusColeta{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cor='" + getCor() + "'" +
            ", ordem=" + getOrdem() +
            ", estadoInicial='" + getEstadoInicial() + "'" +
            ", estadoFinal='" + getEstadoFinal() + "'" +
            ", permiteCancelar='" + getPermiteCancelar() + "'" +
            ", permiteEditar='" + getPermiteEditar() + "'" +
            ", permiteExcluir='" + getPermiteExcluir() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuarioCadastro='" + getUsuarioCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", usuarioAtualizacao='" + getUsuarioAtualizacao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            "}";
    }
}
