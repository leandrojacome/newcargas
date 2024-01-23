package br.com.revenuebrasil.newcargas.domain;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoNotificacao;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notificacao.
 */
@Entity
@Table(name = "notificacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "notificacao")
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonFilter("lazyPropertyFilter")
public class Notificacao extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TipoNotificacao tipo;

    @Size(min = 2, max = 150)
    @Column(name = "email", length = 150)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Size(min = 10, max = 11)
    @Column(name = "telefone", length = 11)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String telefone;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "assunto", length = 150, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assunto;

    @NotNull
    @Size(min = 2, max = 500)
    @Column(name = "mensagem", length = 500, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mensagem;

    @NotNull
    @Column(name = "data_hora_envio", nullable = false)
    private ZonedDateTime dataHoraEnvio;

    @Column(name = "data_hora_leitura")
    private ZonedDateTime dataHoraLeitura;

    @Column(name = "lido")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean lido;

    @Column(name = "data_leitura")
    private ZonedDateTime dataLeitura;

    @Column(name = "removido")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean removido;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "solitacaoColetas", "notificacaos", "faturas", "cidade" },
        allowSetters = true
    )
    private Embarcador embarcador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas", "cidade" },
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

    // Inherited createdBy methods
    public Notificacao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public Notificacao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public Notificacao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public Notificacao lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
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
            ", lido='" + getLido() + "'" +
            ", dataLeitura='" + getDataLeitura() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
