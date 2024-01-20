package br.com.revenuebrasil.newcargas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TomadaPreco.
 */
@Entity
@Table(name = "tomada_preco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TomadaPreco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_hora_envio", nullable = false)
    private ZonedDateTime dataHoraEnvio;

    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "prazo_resposta")
    private Integer prazoResposta;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total")
    private Double valorTotal;

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

    @Column(name = "aprovado")
    private Boolean aprovado;

    @Column(name = "data_aprovacao")
    private ZonedDateTime dataAprovacao;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_aprovacao", length = 150)
    private String usuarioAprovacao;

    @Column(name = "cancelado")
    private Boolean cancelado;

    @Column(name = "data_cancelamento")
    private ZonedDateTime dataCancelamento;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_cancelamento", length = 150)
    private String usuarioCancelamento;

    @Column(name = "removido")
    private Boolean removido;

    @Column(name = "data_remocao")
    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    @Column(name = "usuario_remocao", length = 150)
    private String usuarioRemocao;

    @JsonIgnoreProperties(value = { "faturas", "solicitacaoColeta", "transportadora" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Contratacao contratacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "cidades", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas" },
        allowSetters = true
    )
    private Transportadora transportadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "historicoStatusColetas", "solitacaoColetas", "tomadaPrecos", "statusColeta" }, allowSetters = true)
    private Roteirizacao roteirizacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TomadaPreco id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraEnvio() {
        return this.dataHoraEnvio;
    }

    public TomadaPreco dataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.setDataHoraEnvio(dataHoraEnvio);
        return this;
    }

    public void setDataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public Integer getPrazoResposta() {
        return this.prazoResposta;
    }

    public TomadaPreco prazoResposta(Integer prazoResposta) {
        this.setPrazoResposta(prazoResposta);
        return this;
    }

    public void setPrazoResposta(Integer prazoResposta) {
        this.prazoResposta = prazoResposta;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public TomadaPreco valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public TomadaPreco observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public TomadaPreco dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuarioCadastro() {
        return this.usuarioCadastro;
    }

    public TomadaPreco usuarioCadastro(String usuarioCadastro) {
        this.setUsuarioCadastro(usuarioCadastro);
        return this;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public TomadaPreco dataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.setDataAtualizacao(dataAtualizacao);
        return this;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioAtualizacao() {
        return this.usuarioAtualizacao;
    }

    public TomadaPreco usuarioAtualizacao(String usuarioAtualizacao) {
        this.setUsuarioAtualizacao(usuarioAtualizacao);
        return this;
    }

    public void setUsuarioAtualizacao(String usuarioAtualizacao) {
        this.usuarioAtualizacao = usuarioAtualizacao;
    }

    public Boolean getAprovado() {
        return this.aprovado;
    }

    public TomadaPreco aprovado(Boolean aprovado) {
        this.setAprovado(aprovado);
        return this;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    public ZonedDateTime getDataAprovacao() {
        return this.dataAprovacao;
    }

    public TomadaPreco dataAprovacao(ZonedDateTime dataAprovacao) {
        this.setDataAprovacao(dataAprovacao);
        return this;
    }

    public void setDataAprovacao(ZonedDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getUsuarioAprovacao() {
        return this.usuarioAprovacao;
    }

    public TomadaPreco usuarioAprovacao(String usuarioAprovacao) {
        this.setUsuarioAprovacao(usuarioAprovacao);
        return this;
    }

    public void setUsuarioAprovacao(String usuarioAprovacao) {
        this.usuarioAprovacao = usuarioAprovacao;
    }

    public Boolean getCancelado() {
        return this.cancelado;
    }

    public TomadaPreco cancelado(Boolean cancelado) {
        this.setCancelado(cancelado);
        return this;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public ZonedDateTime getDataCancelamento() {
        return this.dataCancelamento;
    }

    public TomadaPreco dataCancelamento(ZonedDateTime dataCancelamento) {
        this.setDataCancelamento(dataCancelamento);
        return this;
    }

    public void setDataCancelamento(ZonedDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getUsuarioCancelamento() {
        return this.usuarioCancelamento;
    }

    public TomadaPreco usuarioCancelamento(String usuarioCancelamento) {
        this.setUsuarioCancelamento(usuarioCancelamento);
        return this;
    }

    public void setUsuarioCancelamento(String usuarioCancelamento) {
        this.usuarioCancelamento = usuarioCancelamento;
    }

    public Boolean getRemovido() {
        return this.removido;
    }

    public TomadaPreco removido(Boolean removido) {
        this.setRemovido(removido);
        return this;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public ZonedDateTime getDataRemocao() {
        return this.dataRemocao;
    }

    public TomadaPreco dataRemocao(ZonedDateTime dataRemocao) {
        this.setDataRemocao(dataRemocao);
        return this;
    }

    public void setDataRemocao(ZonedDateTime dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getUsuarioRemocao() {
        return this.usuarioRemocao;
    }

    public TomadaPreco usuarioRemocao(String usuarioRemocao) {
        this.setUsuarioRemocao(usuarioRemocao);
        return this;
    }

    public void setUsuarioRemocao(String usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public Contratacao getContratacao() {
        return this.contratacao;
    }

    public void setContratacao(Contratacao contratacao) {
        this.contratacao = contratacao;
    }

    public TomadaPreco contratacao(Contratacao contratacao) {
        this.setContratacao(contratacao);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public TomadaPreco transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    public Roteirizacao getRoteirizacao() {
        return this.roteirizacao;
    }

    public void setRoteirizacao(Roteirizacao roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    public TomadaPreco roteirizacao(Roteirizacao roteirizacao) {
        this.setRoteirizacao(roteirizacao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TomadaPreco)) {
            return false;
        }
        return getId() != null && getId().equals(((TomadaPreco) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TomadaPreco{" +
            "id=" + getId() +
            ", dataHoraEnvio='" + getDataHoraEnvio() + "'" +
            ", prazoResposta=" + getPrazoResposta() +
            ", valorTotal=" + getValorTotal() +
            ", observacao='" + getObservacao() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuarioCadastro='" + getUsuarioCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", usuarioAtualizacao='" + getUsuarioAtualizacao() + "'" +
            ", aprovado='" + getAprovado() + "'" +
            ", dataAprovacao='" + getDataAprovacao() + "'" +
            ", usuarioAprovacao='" + getUsuarioAprovacao() + "'" +
            ", cancelado='" + getCancelado() + "'" +
            ", dataCancelamento='" + getDataCancelamento() + "'" +
            ", usuarioCancelamento='" + getUsuarioCancelamento() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            "}";
    }
}
