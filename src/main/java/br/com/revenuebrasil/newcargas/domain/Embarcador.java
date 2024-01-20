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
 * A Embarcador.
 */
@Entity
@Table(name = "embarcador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Embarcador implements Serializable {

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

    @NotNull
    @Size(min = 14, max = 14)
    @Column(name = "cnpj", length = 14, nullable = false, unique = true)
    private String cnpj;

    @Size(min = 2, max = 150)
    @Column(name = "razao_social", length = 150, unique = true)
    private String razaoSocial;

    @Size(min = 2, max = 20)
    @Column(name = "inscricao_estadual", length = 20, unique = true)
    private String inscricaoEstadual;

    @Size(min = 2, max = 20)
    @Column(name = "inscricao_municipal", length = 20, unique = true)
    private String inscricaoMunicipal;

    @Size(min = 2, max = 150)
    @Column(name = "responsavel", length = 150)
    private String responsavel;

    @Size(min = 8, max = 8)
    @Column(name = "cep", length = 8)
    private String cep;

    @Size(min = 2, max = 150)
    @Column(name = "endereco", length = 150)
    private String endereco;

    @Size(min = 1, max = 10)
    @Column(name = "numero", length = 10)
    private String numero;

    @Size(min = 2, max = 150)
    @Column(name = "complemento", length = 150)
    private String complemento;

    @Size(min = 2, max = 150)
    @Column(name = "bairro", length = 150)
    private String bairro;

    @Size(min = 10, max = 11)
    @Column(name = "telefone", length = 11)
    private String telefone;

    @Size(min = 2, max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enderecos", "estado", "embarcador", "transportadora" }, allowSetters = true)
    private Set<Cidade> cidades = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "banco", "embarcador", "transportadora" }, allowSetters = true)
    private Set<ContaBancaria> contaBancarias = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "embarcador", "transportadora", "tipoCarga", "tipoFrete", "formaCobranca", "regiaoOrigem", "regiaoDestino" },
        allowSetters = true
    )
    private Set<TabelaFrete> tabelaFretes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "embarcador", "transportadora" }, allowSetters = true)
    private Set<Notificacao> notificacaos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "embarcador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "embarcador", "transportadora", "contratacao", "formaCobranca" }, allowSetters = true)
    private Set<Fatura> faturas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Embarcador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Embarcador nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Embarcador cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return this.razaoSocial;
    }

    public Embarcador razaoSocial(String razaoSocial) {
        this.setRazaoSocial(razaoSocial);
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return this.inscricaoEstadual;
    }

    public Embarcador inscricaoEstadual(String inscricaoEstadual) {
        this.setInscricaoEstadual(inscricaoEstadual);
        return this;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return this.inscricaoMunicipal;
    }

    public Embarcador inscricaoMunicipal(String inscricaoMunicipal) {
        this.setInscricaoMunicipal(inscricaoMunicipal);
        return this;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getResponsavel() {
        return this.responsavel;
    }

    public Embarcador responsavel(String responsavel) {
        this.setResponsavel(responsavel);
        return this;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getCep() {
        return this.cep;
    }

    public Embarcador cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Embarcador endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return this.numero;
    }

    public Embarcador numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public Embarcador complemento(String complemento) {
        this.setComplemento(complemento);
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Embarcador bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Embarcador telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Embarcador email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Embarcador observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public Embarcador dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuarioCadastro() {
        return this.usuarioCadastro;
    }

    public Embarcador usuarioCadastro(String usuarioCadastro) {
        this.setUsuarioCadastro(usuarioCadastro);
        return this;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public Embarcador dataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.setDataAtualizacao(dataAtualizacao);
        return this;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioAtualizacao() {
        return this.usuarioAtualizacao;
    }

    public Embarcador usuarioAtualizacao(String usuarioAtualizacao) {
        this.setUsuarioAtualizacao(usuarioAtualizacao);
        return this;
    }

    public void setUsuarioAtualizacao(String usuarioAtualizacao) {
        this.usuarioAtualizacao = usuarioAtualizacao;
    }

    public Set<Endereco> getEnderecos() {
        return this.enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        if (this.enderecos != null) {
            this.enderecos.forEach(i -> i.setEmbarcador(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setEmbarcador(this));
        }
        this.enderecos = enderecos;
    }

    public Embarcador enderecos(Set<Endereco> enderecos) {
        this.setEnderecos(enderecos);
        return this;
    }

    public Embarcador addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setEmbarcador(this);
        return this;
    }

    public Embarcador removeEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setEmbarcador(null);
        return this;
    }

    public Set<Cidade> getCidades() {
        return this.cidades;
    }

    public void setCidades(Set<Cidade> cidades) {
        if (this.cidades != null) {
            this.cidades.forEach(i -> i.setEmbarcador(null));
        }
        if (cidades != null) {
            cidades.forEach(i -> i.setEmbarcador(this));
        }
        this.cidades = cidades;
    }

    public Embarcador cidades(Set<Cidade> cidades) {
        this.setCidades(cidades);
        return this;
    }

    public Embarcador addCidade(Cidade cidade) {
        this.cidades.add(cidade);
        cidade.setEmbarcador(this);
        return this;
    }

    public Embarcador removeCidade(Cidade cidade) {
        this.cidades.remove(cidade);
        cidade.setEmbarcador(null);
        return this;
    }

    public Set<ContaBancaria> getContaBancarias() {
        return this.contaBancarias;
    }

    public void setContaBancarias(Set<ContaBancaria> contaBancarias) {
        if (this.contaBancarias != null) {
            this.contaBancarias.forEach(i -> i.setEmbarcador(null));
        }
        if (contaBancarias != null) {
            contaBancarias.forEach(i -> i.setEmbarcador(this));
        }
        this.contaBancarias = contaBancarias;
    }

    public Embarcador contaBancarias(Set<ContaBancaria> contaBancarias) {
        this.setContaBancarias(contaBancarias);
        return this;
    }

    public Embarcador addContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancarias.add(contaBancaria);
        contaBancaria.setEmbarcador(this);
        return this;
    }

    public Embarcador removeContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancarias.remove(contaBancaria);
        contaBancaria.setEmbarcador(null);
        return this;
    }

    public Set<TabelaFrete> getTabelaFretes() {
        return this.tabelaFretes;
    }

    public void setTabelaFretes(Set<TabelaFrete> tabelaFretes) {
        if (this.tabelaFretes != null) {
            this.tabelaFretes.forEach(i -> i.setEmbarcador(null));
        }
        if (tabelaFretes != null) {
            tabelaFretes.forEach(i -> i.setEmbarcador(this));
        }
        this.tabelaFretes = tabelaFretes;
    }

    public Embarcador tabelaFretes(Set<TabelaFrete> tabelaFretes) {
        this.setTabelaFretes(tabelaFretes);
        return this;
    }

    public Embarcador addTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.add(tabelaFrete);
        tabelaFrete.setEmbarcador(this);
        return this;
    }

    public Embarcador removeTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.remove(tabelaFrete);
        tabelaFrete.setEmbarcador(null);
        return this;
    }

    public Set<SolicitacaoColeta> getSolitacaoColetas() {
        return this.solitacaoColetas;
    }

    public void setSolitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        if (this.solitacaoColetas != null) {
            this.solitacaoColetas.forEach(i -> i.setEmbarcador(null));
        }
        if (solicitacaoColetas != null) {
            solicitacaoColetas.forEach(i -> i.setEmbarcador(this));
        }
        this.solitacaoColetas = solicitacaoColetas;
    }

    public Embarcador solitacaoColetas(Set<SolicitacaoColeta> solicitacaoColetas) {
        this.setSolitacaoColetas(solicitacaoColetas);
        return this;
    }

    public Embarcador addSolitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solitacaoColetas.add(solicitacaoColeta);
        solicitacaoColeta.setEmbarcador(this);
        return this;
    }

    public Embarcador removeSolitacaoColeta(SolicitacaoColeta solicitacaoColeta) {
        this.solitacaoColetas.remove(solicitacaoColeta);
        solicitacaoColeta.setEmbarcador(null);
        return this;
    }

    public Set<Notificacao> getNotificacaos() {
        return this.notificacaos;
    }

    public void setNotificacaos(Set<Notificacao> notificacaos) {
        if (this.notificacaos != null) {
            this.notificacaos.forEach(i -> i.setEmbarcador(null));
        }
        if (notificacaos != null) {
            notificacaos.forEach(i -> i.setEmbarcador(this));
        }
        this.notificacaos = notificacaos;
    }

    public Embarcador notificacaos(Set<Notificacao> notificacaos) {
        this.setNotificacaos(notificacaos);
        return this;
    }

    public Embarcador addNotificacao(Notificacao notificacao) {
        this.notificacaos.add(notificacao);
        notificacao.setEmbarcador(this);
        return this;
    }

    public Embarcador removeNotificacao(Notificacao notificacao) {
        this.notificacaos.remove(notificacao);
        notificacao.setEmbarcador(null);
        return this;
    }

    public Set<Fatura> getFaturas() {
        return this.faturas;
    }

    public void setFaturas(Set<Fatura> faturas) {
        if (this.faturas != null) {
            this.faturas.forEach(i -> i.setEmbarcador(null));
        }
        if (faturas != null) {
            faturas.forEach(i -> i.setEmbarcador(this));
        }
        this.faturas = faturas;
    }

    public Embarcador faturas(Set<Fatura> faturas) {
        this.setFaturas(faturas);
        return this;
    }

    public Embarcador addFatura(Fatura fatura) {
        this.faturas.add(fatura);
        fatura.setEmbarcador(this);
        return this;
    }

    public Embarcador removeFatura(Fatura fatura) {
        this.faturas.remove(fatura);
        fatura.setEmbarcador(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Embarcador)) {
            return false;
        }
        return getId() != null && getId().equals(((Embarcador) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Embarcador{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", razaoSocial='" + getRazaoSocial() + "'" +
            ", inscricaoEstadual='" + getInscricaoEstadual() + "'" +
            ", inscricaoMunicipal='" + getInscricaoMunicipal() + "'" +
            ", responsavel='" + getResponsavel() + "'" +
            ", cep='" + getCep() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", numero='" + getNumero() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuarioCadastro='" + getUsuarioCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", usuarioAtualizacao='" + getUsuarioAtualizacao() + "'" +
            "}";
    }
}
