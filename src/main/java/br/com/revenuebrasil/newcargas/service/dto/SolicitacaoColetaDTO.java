package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SolicitacaoColetaDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean coletado;

    @NotNull
    private ZonedDateTime dataHoraColeta;

    @NotNull
    private Boolean entregue;

    private ZonedDateTime dataHoraEntrega;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @Size(min = 2, max = 500)
    private String observacao;

    @NotNull
    private ZonedDateTime dataCadastro;

    private ZonedDateTime dataAtualizacao;

    private Boolean cancelado;

    private ZonedDateTime dataCancelamento;

    @Size(min = 2, max = 150)
    private String usuarioCancelamento;

    private Boolean removido;

    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    private String usuarioRemocao;

    private EmbarcadorDTO embarcador;

    private StatusColetaDTO statusColeta;

    private RoteirizacaoDTO roteirizacao;

    private TipoVeiculoDTO tipoVeiculo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getColetado() {
        return coletado;
    }

    public void setColetado(Boolean coletado) {
        this.coletado = coletado;
    }

    public ZonedDateTime getDataHoraColeta() {
        return dataHoraColeta;
    }

    public void setDataHoraColeta(ZonedDateTime dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }

    public Boolean getEntregue() {
        return entregue;
    }

    public void setEntregue(Boolean entregue) {
        this.entregue = entregue;
    }

    public ZonedDateTime getDataHoraEntrega() {
        return dataHoraEntrega;
    }

    public void setDataHoraEntrega(ZonedDateTime dataHoraEntrega) {
        this.dataHoraEntrega = dataHoraEntrega;
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

    public ZonedDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
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

    public EmbarcadorDTO getEmbarcador() {
        return embarcador;
    }

    public void setEmbarcador(EmbarcadorDTO embarcador) {
        this.embarcador = embarcador;
    }

    public StatusColetaDTO getStatusColeta() {
        return statusColeta;
    }

    public void setStatusColeta(StatusColetaDTO statusColeta) {
        this.statusColeta = statusColeta;
    }

    public RoteirizacaoDTO getRoteirizacao() {
        return roteirizacao;
    }

    public void setRoteirizacao(RoteirizacaoDTO roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    public TipoVeiculoDTO getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculoDTO tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolicitacaoColetaDTO)) {
            return false;
        }

        SolicitacaoColetaDTO solicitacaoColetaDTO = (SolicitacaoColetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, solicitacaoColetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoColetaDTO{" +
            "id=" + getId() +
            ", coletado='" + getColetado() + "'" +
            ", dataHoraColeta='" + getDataHoraColeta() + "'" +
            ", entregue='" + getEntregue() + "'" +
            ", dataHoraEntrega='" + getDataHoraEntrega() + "'" +
            ", valorTotal=" + getValorTotal() +
            ", observacao='" + getObservacao() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", cancelado='" + getCancelado() + "'" +
            ", dataCancelamento='" + getDataCancelamento() + "'" +
            ", usuarioCancelamento='" + getUsuarioCancelamento() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            ", embarcador=" + getEmbarcador() +
            ", statusColeta=" + getStatusColeta() +
            ", roteirizacao=" + getRoteirizacao() +
            ", tipoVeiculo=" + getTipoVeiculo() +
            "}";
    }
}
