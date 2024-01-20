package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricoStatusColetaDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dataCriacao;

    @Size(min = 2, max = 500)
    private String observacao;

    private SolicitacaoColetaDTO solicitacaoColeta;

    private RoteirizacaoDTO roteirizacao;

    private StatusColetaDTO statusColetaOrigem;

    private StatusColetaDTO statusColetaDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(ZonedDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public SolicitacaoColetaDTO getSolicitacaoColeta() {
        return solicitacaoColeta;
    }

    public void setSolicitacaoColeta(SolicitacaoColetaDTO solicitacaoColeta) {
        this.solicitacaoColeta = solicitacaoColeta;
    }

    public RoteirizacaoDTO getRoteirizacao() {
        return roteirizacao;
    }

    public void setRoteirizacao(RoteirizacaoDTO roteirizacao) {
        this.roteirizacao = roteirizacao;
    }

    public StatusColetaDTO getStatusColetaOrigem() {
        return statusColetaOrigem;
    }

    public void setStatusColetaOrigem(StatusColetaDTO statusColetaOrigem) {
        this.statusColetaOrigem = statusColetaOrigem;
    }

    public StatusColetaDTO getStatusColetaDestino() {
        return statusColetaDestino;
    }

    public void setStatusColetaDestino(StatusColetaDTO statusColetaDestino) {
        this.statusColetaDestino = statusColetaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricoStatusColetaDTO)) {
            return false;
        }

        HistoricoStatusColetaDTO historicoStatusColetaDTO = (HistoricoStatusColetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historicoStatusColetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoStatusColetaDTO{" +
            "id=" + getId() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", solicitacaoColeta=" + getSolicitacaoColeta() +
            ", roteirizacao=" + getRoteirizacao() +
            ", statusColetaOrigem=" + getStatusColetaOrigem() +
            ", statusColetaDestino=" + getStatusColetaDestino() +
            "}";
    }
}
