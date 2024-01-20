package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.TomadaPreco} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TomadaPrecoDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dataHoraEnvio;

    @Min(value = 1)
    @Max(value = 4)
    private Integer prazoResposta;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @Size(min = 2, max = 500)
    private String observacao;

    @NotNull
    private ZonedDateTime dataCadastro;

    @Size(min = 2, max = 150)
    private String usuarioCadastro;

    private ZonedDateTime dataAtualizacao;

    @Size(min = 2, max = 150)
    private String usuarioAtualizacao;

    private Boolean aprovado;

    private ZonedDateTime dataAprovacao;

    @Size(min = 2, max = 150)
    private String usuarioAprovacao;

    private Boolean cancelado;

    private ZonedDateTime dataCancelamento;

    @Size(min = 2, max = 150)
    private String usuarioCancelamento;

    private Boolean removido;

    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    private String usuarioRemocao;

    private ContratacaoDTO contratacao;

    private TransportadoraDTO transportadora;

    private RoteirizacaoDTO roteirizacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public Integer getPrazoResposta() {
        return prazoResposta;
    }

    public void setPrazoResposta(Integer prazoResposta) {
        this.prazoResposta = prazoResposta;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioAtualizacao() {
        return usuarioAtualizacao;
    }

    public void setUsuarioAtualizacao(String usuarioAtualizacao) {
        this.usuarioAtualizacao = usuarioAtualizacao;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    public ZonedDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(ZonedDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getUsuarioAprovacao() {
        return usuarioAprovacao;
    }

    public void setUsuarioAprovacao(String usuarioAprovacao) {
        this.usuarioAprovacao = usuarioAprovacao;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public ZonedDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(ZonedDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getUsuarioCancelamento() {
        return usuarioCancelamento;
    }

    public void setUsuarioCancelamento(String usuarioCancelamento) {
        this.usuarioCancelamento = usuarioCancelamento;
    }

    public Boolean getRemovido() {
        return removido;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public ZonedDateTime getDataRemocao() {
        return dataRemocao;
    }

    public void setDataRemocao(ZonedDateTime dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getUsuarioRemocao() {
        return usuarioRemocao;
    }

    public void setUsuarioRemocao(String usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public ContratacaoDTO getContratacao() {
        return contratacao;
    }

    public void setContratacao(ContratacaoDTO contratacao) {
        this.contratacao = contratacao;
    }

    public TransportadoraDTO getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(TransportadoraDTO transportadora) {
        this.transportadora = transportadora;
    }

    public RoteirizacaoDTO getRoteirizacao() {
        return roteirizacao;
    }

    public void setRoteirizacao(RoteirizacaoDTO roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TomadaPrecoDTO)) {
            return false;
        }

        TomadaPrecoDTO tomadaPrecoDTO = (TomadaPrecoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tomadaPrecoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TomadaPrecoDTO{" +
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
            ", contratacao=" + getContratacao() +
            ", transportadora=" + getTransportadora() +
            ", roteirizacao=" + getRoteirizacao() +
            "}";
    }
}
