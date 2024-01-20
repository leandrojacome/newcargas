package br.com.revenuebrasil.newcargas.domain;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoNotificacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notificacao.
 */
@Entity
@Table(name = "notificacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notificacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacao tipo;

    @Size(min = 2, max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Size(min = 10, max = 11)
    @Column(name = "telefone", length = 11)
    private String telefone;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "assunto", length = 150, nullable = false)
    private String assunto;

    @NotNull
    @Size(min = 2, max = 500)
    @Column(name = "mensagem", length = 500, nullable = false)
    private String mensagem;

    @NotNull
    @Column(name = "data_hora_envio", nullable = false)
    private ZonedDateTime dataHoraEnvio;

    @Column(name = "data_hora_leitura")
    private ZonedDateTime dataHoraLeitura;

    @NotNull
    @Column(name = "data_cadastro", nullable = false)
    private ZonedDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private ZonedDateTime dataAtualizacao;

    @Column(name = "lido")
    private Boolean lido;

    @Column(name = "data_leitura")
    private ZonedDateTime dataLeitura;

    @Column(name = "removido")
    private Boolean removido;

    @Column(name = "data_remocao")
    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_remocao", length = 150)
    private String usuarioRemocao;

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

    public Notificacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNotificacao getTipo() {
        return this.tipo;
    }

    public Notificacao tipo(TipoNotificacao tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return this.email;
    }

    public Notificacao email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Notificacao telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getAssunto() {
        return this.assunto;
    }

    public Notificacao assunto(String assunto) {
        this.setAssunto(assunto);
        return this;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public Notificacao mensagem(String mensagem) {
        this.setMensagem(mensagem);
        return this;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public ZonedDateTime getDataHoraEnvio() {
        return this.dataHoraEnvio;
    }

    public Notificacao dataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.setDataHoraEnvio(dataHoraEnvio);
        return this;
    }

    public void setDataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public ZonedDateTime getDataHoraLeitura() {
        return this.dataHoraLeitura;
    }

    public Notificacao dataHoraLeitura(ZonedDateTime dataHoraLeitura) {
        this.setDataHoraLeitura(dataHoraLeitura);
        return this;
    }

    public void setDataHoraLeitura(ZonedDateTime dataHoraLeitura) {
        this.dataHoraLeitura = dataHoraLeitura;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public Notificacao dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public Notificacao dataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.setDataAtualizacao(dataAtualizacao);
        return this;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getLido() {
        return this.lido;
    }

    public Notificacao lido(Boolean lido) {
        this.setLido(lido);
        return this;
    }

    public void setLido(Boolean lido) {
        this.lido = lido;
    }

    public ZonedDateTime getDataLeitura() {
        return this.dataLeitura;
    }

    public Notificacao dataLeitura(ZonedDateTime dataLeitura) {
        this.setDataLeitura(dataLeitura);
        return this;
    }

    public void setDataLeitura(ZonedDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public Notificacao removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public ZonedDateTime getDataRemocao() {
        return this.dataRemocao;
    }

    public Notificacao dataRemocao(ZonedDateTime dataRemocao) {
        this.setDataRemocao(dataRemocao);
        return this;
    }

    public void setDataRemocao(ZonedDateTime dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getUsuarioRemocao() {
        return this.usuarioRemocao;
    }

    public Notificacao usuarioRemocao(String usuarioRemocao) {
        this.setUsuarioRemocao(usuarioRemocao);
        return this;
    }

    public void setUsuarioRemocao(String usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public Notificacao embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Notificacao transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Notificacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notificacao{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", assunto='" + getAssunto() + "'" +
            ", mensagem='" + getMensagem() + "'" +
            ", dataHoraEnvio='" + getDataHoraEnvio() + "'" +
            ", dataHoraLeitura='" + getDataHoraLeitura() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", lido='" + getLido() + "'" +
            ", dataLeitura='" + getDataLeitura() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            "}";
    }
}
