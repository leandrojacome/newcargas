package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContaBancaria.
 */
@Entity
@Table(name = "conta_bancaria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "agencia", length = 10, nullable = false)
    private String agencia;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "conta", length = 10, nullable = false)
    private String conta;

    @Size(min = 2, max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

    @Size(min = 2, max = 150)
    @Column(name = "tipo", length = 150)
    private String tipo;

    @Size(min = 2, max = 150)
    @Column(name = "pix", length = 150)
    private String pix;

    @Size(min = 2, max = 150)
    @Column(name = "titular", length = 150)
    private String titular;

    @NotNull
    @Column(name = "data_cadastro", nullable = false)
    private ZonedDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private ZonedDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contaBancarias" }, allowSetters = true)
    private Banco banco;

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

    public ContaBancaria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgencia() {
        return this.agencia;
    }

    public ContaBancaria agencia(String agencia) {
        this.setAgencia(agencia);
        return this;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return this.conta;
    }

    public ContaBancaria conta(String conta) {
        this.setConta(conta);
        return this;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public ContaBancaria observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTipo() {
        return this.tipo;
    }

    public ContaBancaria tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPix() {
        return this.pix;
    }

    public ContaBancaria pix(String pix) {
        this.setPix(pix);
        return this;
    }

    public void setPix(String pix) {
        this.pix = pix;
    }

    public String getTitular() {
        return this.titular;
    }

    public ContaBancaria titular(String titular) {
        this.setTitular(titular);
        return this;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public ContaBancaria dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public ContaBancaria dataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.setDataAtualizacao(dataAtualizacao);
        return this;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Banco getBanco() {
        return this.banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public ContaBancaria banco(Banco banco) {
        this.setBanco(banco);
        return this;
    }

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public ContaBancaria embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public ContaBancaria transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaBancaria)) {
            return false;
        }
        return getId() != null && getId().equals(((ContaBancaria) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaBancaria{" +
            "id=" + getId() +
            ", agencia='" + getAgencia() + "'" +
            ", conta='" + getConta() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", pix='" + getPix() + "'" +
            ", titular='" + getTitular() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            "}";
    }
}
