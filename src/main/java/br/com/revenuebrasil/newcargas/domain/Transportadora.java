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
 * A Transportadora.
 */
@Entity
@Table(name = "transportadora")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transportadora")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Transportadora extends AbstractAuditingEntity<Long> implements Serializable {

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

    @NotNull
    @Size(min = 14, max = 14)
    @Column(name = "cnpj", length = 14, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cnpj;

    @Size(min = 2, max = 150)
    @Column(name = "razao_social", length = 150, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String razaoSocial;

    @Size(min = 2, max = 20)
    @Column(name = "inscricao_estadual", length = 20, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String inscricaoEstadual;

    @Size(min = 2, max = 20)
    @Column(name = "inscricao_municipal", length = 20, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String inscricaoMunicipal;

    @Size(min = 2, max = 150)
    @Column(name = "responsavel", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String responsavel;

    @Size(min = 8, max = 8)
    @Column(name = "cep", length = 8)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cep;

    @Size(min = 2, max = 150)
    @Column(name = "endereco", length = 150)
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

    @Size(min = 2, max = 150)
    @Column(name = "bairro", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String bairro;

    @Size(min = 10, max = 11)
    @Column(name = "telefone", length = 11)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String telefone;

    @Size(min = 2, max = 150)
    @Column(name = "email", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observacao;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "banco", "embarcador", "transportadora" }, allowSetters = true)
    private Set<ContaBancaria> contaBancarias = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "embarcador", "transportadora", "tipoCarga", "tipoFrete", "formaCobranca", "regiaoOrigem", "regiaoDestino" },
        allowSetters = true
    )
    private Set<TabelaFrete> tabelaFretes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "contratacao", "transportadora", "roteirizacao" }, allowSetters = true)
    private Set<TomadaPreco> tomadaPrecos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "faturas", "solicitacaoColeta", "transportadora" }, allowSetters = true)
    private Set<Contratacao> contratacaos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "embarcador", "transportadora" }, allowSetters = true)
    private Set<Notificacao> notificacaos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transportadora")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "embarcador", "transportadora", "contratacao", "formaCobranca" }, allowSetters = true)
    private Set<Fatura> faturas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "enderecos", "embarcadors", "transportadoras", "estado" }, allowSetters = true)
    private Cidade cidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transportadora id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Transportadora nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Transportadora cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return this.razaoSocial;
    }

    public Transportadora razaoSocial(String razaoSocial) {
        this.setRazaoSocial(razaoSocial);
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return this.inscricaoEstadual;
    }

    public Transportadora inscricaoEstadual(String inscricaoEstadual) {
        this.setInscricaoEstadual(inscricaoEstadual);
        return this;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return this.inscricaoMunicipal;
    }

    public Transportadora inscricaoMunicipal(String inscricaoMunicipal) {
        this.setInscricaoMunicipal(inscricaoMunicipal);
        return this;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getResponsavel() {
        return this.responsavel;
    }

    public Transportadora responsavel(String responsavel) {
        this.setResponsavel(responsavel);
        return this;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getCep() {
        return this.cep;
    }

    public Transportadora cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Transportadora endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return this.numero;
    }

    public Transportadora numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public Transportadora complemento(String complemento) {
        this.setComplemento(complemento);
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Transportadora bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Transportadora telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Transportadora email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Transportadora observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Inherited createdBy methods
    public Transportadora createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Transportadora createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Transportadora lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Transportadora lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Endereco> getEnderecos() {
        return this.enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        if (this.enderecos != null) {
            this.enderecos.forEach(i -> i.setTransportadora(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setTransportadora(this));
        }
        this.enderecos = enderecos;
    }

    public Transportadora enderecos(Set<Endereco> enderecos) {
        this.setEnderecos(enderecos);
        return this;
    }

    public Transportadora addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setTransportadora(this);
        return this;
    }

    public Transportadora removeEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setTransportadora(null);
        return this;
    }

    public Set<ContaBancaria> getContaBancarias() {
        return this.contaBancarias;
    }

    public void setContaBancarias(Set<ContaBancaria> contaBancarias) {
        if (this.contaBancarias != null) {
            this.contaBancarias.forEach(i -> i.setTransportadora(null));
        }
        if (contaBancarias != null) {
            contaBancarias.forEach(i -> i.setTransportadora(this));
        }
        this.contaBancarias = contaBancarias;
    }

    public Transportadora contaBancarias(Set<ContaBancaria> contaBancarias) {
        this.setContaBancarias(contaBancarias);
        return this;
    }

    public Transportadora addContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancarias.add(contaBancaria);
        contaBancaria.setTransportadora(this);
        return this;
    }

    public Transportadora removeContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancarias.remove(contaBancaria);
        contaBancaria.setTransportadora(null);
        return this;
    }

    public Set<TabelaFrete> getTabelaFretes() {
        return this.tabelaFretes;
    }

    public void setTabelaFretes(Set<TabelaFrete> tabelaFretes) {
        if (this.tabelaFretes != null) {
            this.tabelaFretes.forEach(i -> i.setTransportadora(null));
        }
        if (tabelaFretes != null) {
            tabelaFretes.forEach(i -> i.setTransportadora(this));
        }
        this.tabelaFretes = tabelaFretes;
    }

    public Transportadora tabelaFretes(Set<TabelaFrete> tabelaFretes) {
        this.setTabelaFretes(tabelaFretes);
        return this;
    }

    public Transportadora addTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.add(tabelaFrete);
        tabelaFrete.setTransportadora(this);
        return this;
    }

    public Transportadora removeTabelaFrete(TabelaFrete tabelaFrete) {
        this.tabelaFretes.remove(tabelaFrete);
        tabelaFrete.setTransportadora(null);
        return this;
    }

    public Set<TomadaPreco> getTomadaPrecos() {
        return this.tomadaPrecos;
    }

    public void setTomadaPrecos(Set<TomadaPreco> tomadaPrecos) {
        if (this.tomadaPrecos != null) {
            this.tomadaPrecos.forEach(i -> i.setTransportadora(null));
        }
        if (tomadaPrecos != null) {
            tomadaPrecos.forEach(i -> i.setTransportadora(this));
        }
        this.tomadaPrecos = tomadaPrecos;
    }

    public Transportadora tomadaPrecos(Set<TomadaPreco> tomadaPrecos) {
        this.setTomadaPrecos(tomadaPrecos);
        return this;
    }

    public Transportadora addTomadaPreco(TomadaPreco tomadaPreco) {
        this.tomadaPrecos.add(tomadaPreco);
        tomadaPreco.setTransportadora(this);
        return this;
    }

    public Transportadora removeTomadaPreco(TomadaPreco tomadaPreco) {
        this.tomadaPrecos.remove(tomadaPreco);
        tomadaPreco.setTransportadora(null);
        return this;
    }

    public Set<Contratacao> getContratacaos() {
        return this.contratacaos;
    }

    public void setContratacaos(Set<Contratacao> contratacaos) {
        if (this.contratacaos != null) {
            this.contratacaos.forEach(i -> i.setTransportadora(null));
        }
        if (contratacaos != null) {
            contratacaos.forEach(i -> i.setTransportadora(this));
        }
        this.contratacaos = contratacaos;
    }

    public Transportadora contratacaos(Set<Contratacao> contratacaos) {
        this.setContratacaos(contratacaos);
        return this;
    }

    public Transportadora addContratacao(Contratacao contratacao) {
        this.contratacaos.add(contratacao);
        contratacao.setTransportadora(this);
        return this;
    }

    public Transportadora removeContratacao(Contratacao contratacao) {
        this.contratacaos.remove(contratacao);
        contratacao.setTransportadora(null);
        return this;
    }

    public Set<Notificacao> getNotificacaos() {
        return this.notificacaos;
    }

    public void setNotificacaos(Set<Notificacao> notificacaos) {
        if (this.notificacaos != null) {
            this.notificacaos.forEach(i -> i.setTransportadora(null));
        }
        if (notificacaos != null) {
            notificacaos.forEach(i -> i.setTransportadora(this));
        }
        this.notificacaos = notificacaos;
    }

    public Transportadora notificacaos(Set<Notificacao> notificacaos) {
        this.setNotificacaos(notificacaos);
        return this;
    }

    public Transportadora addNotificacao(Notificacao notificacao) {
        this.notificacaos.add(notificacao);
        notificacao.setTransportadora(this);
        return this;
    }

    public Transportadora removeNotificacao(Notificacao notificacao) {
        this.notificacaos.remove(notificacao);
        notificacao.setTransportadora(null);
        return this;
    }

    public Set<Fatura> getFaturas() {
        return this.faturas;
    }

    public void setFaturas(Set<Fatura> faturas) {
        if (this.faturas != null) {
            this.faturas.forEach(i -> i.setTransportadora(null));
        }
        if (faturas != null) {
            faturas.forEach(i -> i.setTransportadora(this));
        }
        this.faturas = faturas;
    }

    public Transportadora faturas(Set<Fatura> faturas) {
        this.setFaturas(faturas);
        return this;
    }

    public Transportadora addFatura(Fatura fatura) {
        this.faturas.add(fatura);
        fatura.setTransportadora(this);
        return this;
    }

    public Transportadora removeFatura(Fatura fatura) {
        this.faturas.remove(fatura);
        fatura.setTransportadora(null);
        return this;
    }

    public Cidade getCidade() {
        return this.cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Transportadora cidade(Cidade cidade) {
        this.setCidade(cidade);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transportadora)) {
            return false;
        }
        return getId() != null && getId().equals(((Transportadora) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transportadora{" +
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
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
