package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.SolicitacaoColetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /solicitacao-coletas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SolicitacaoColetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter coletado;

    private ZonedDateTimeFilter dataHoraColeta;

    private BooleanFilter entregue;

    private ZonedDateTimeFilter dataHoraEntrega;

    private DoubleFilter valorTotal;

    private StringFilter observacao;

    private BooleanFilter cancelado;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter notaFiscalColetaId;

    private LongFilter enderecoOrigemId;

    private LongFilter enderecoDestinoId;

    private LongFilter historicoStatusColetaId;

    private LongFilter embarcadorId;

    private LongFilter statusColetaId;

    private LongFilter roteirizacaoId;

    private LongFilter tipoVeiculoId;

    private Boolean distinct;

    public SolicitacaoColetaCriteria() {}

    public SolicitacaoColetaCriteria(SolicitacaoColetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.coletado = other.coletado == null ? null : other.coletado.copy();
        this.dataHoraColeta = other.dataHoraColeta == null ? null : other.dataHoraColeta.copy();
        this.entregue = other.entregue == null ? null : other.entregue.copy();
        this.dataHoraEntrega = other.dataHoraEntrega == null ? null : other.dataHoraEntrega.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.cancelado = other.cancelado == null ? null : other.cancelado.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.notaFiscalColetaId = other.notaFiscalColetaId == null ? null : other.notaFiscalColetaId.copy();
        this.enderecoOrigemId = other.enderecoOrigemId == null ? null : other.enderecoOrigemId.copy();
        this.enderecoDestinoId = other.enderecoDestinoId == null ? null : other.enderecoDestinoId.copy();
        this.historicoStatusColetaId = other.historicoStatusColetaId == null ? null : other.historicoStatusColetaId.copy();
        this.embarcadorId = other.embarcadorId == null ? null : other.embarcadorId.copy();
        this.statusColetaId = other.statusColetaId == null ? null : other.statusColetaId.copy();
        this.roteirizacaoId = other.roteirizacaoId == null ? null : other.roteirizacaoId.copy();
        this.tipoVeiculoId = other.tipoVeiculoId == null ? null : other.tipoVeiculoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SolicitacaoColetaCriteria copy() {
        return new SolicitacaoColetaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getColetado() {
        return coletado;
    }

    public BooleanFilter coletado() {
        if (coletado == null) {
            coletado = new BooleanFilter();
        }
        return coletado;
    }

    public void setColetado(BooleanFilter coletado) {
        this.coletado = coletado;
    }

    public ZonedDateTimeFilter getDataHoraColeta() {
        return dataHoraColeta;
    }

    public ZonedDateTimeFilter dataHoraColeta() {
        if (dataHoraColeta == null) {
            dataHoraColeta = new ZonedDateTimeFilter();
        }
        return dataHoraColeta;
    }

    public void setDataHoraColeta(ZonedDateTimeFilter dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }

    public BooleanFilter getEntregue() {
        return entregue;
    }

    public BooleanFilter entregue() {
        if (entregue == null) {
            entregue = new BooleanFilter();
        }
        return entregue;
    }

    public void setEntregue(BooleanFilter entregue) {
        this.entregue = entregue;
    }

    public ZonedDateTimeFilter getDataHoraEntrega() {
        return dataHoraEntrega;
    }

    public ZonedDateTimeFilter dataHoraEntrega() {
        if (dataHoraEntrega == null) {
            dataHoraEntrega = new ZonedDateTimeFilter();
        }
        return dataHoraEntrega;
    }

    public void setDataHoraEntrega(ZonedDateTimeFilter dataHoraEntrega) {
        this.dataHoraEntrega = dataHoraEntrega;
    }

    public DoubleFilter getValorTotal() {
        return valorTotal;
    }

    public DoubleFilter valorTotal() {
        if (valorTotal == null) {
            valorTotal = new DoubleFilter();
        }
        return valorTotal;
    }

    public void setValorTotal(DoubleFilter valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StringFilter getObservacao() {
        return observacao;
    }

    public StringFilter observacao() {
        if (observacao == null) {
            observacao = new StringFilter();
        }
        return observacao;
    }

    public void setObservacao(StringFilter observacao) {
        this.observacao = observacao;
    }

    public BooleanFilter getCancelado() {
        return cancelado;
    }

    public BooleanFilter cancelado() {
        if (cancelado == null) {
            cancelado = new BooleanFilter();
        }
        return cancelado;
    }

    public void setCancelado(BooleanFilter cancelado) {
        this.cancelado = cancelado;
    }

    public BooleanFilter getRemovido() {
        return removido;
    }

    public BooleanFilter removido() {
        if (removido == null) {
            removido = new BooleanFilter();
        }
        return removido;
    }

    public void setRemovido(BooleanFilter removido) {
        this.removido = removido;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getNotaFiscalColetaId() {
        return notaFiscalColetaId;
    }

    public LongFilter notaFiscalColetaId() {
        if (notaFiscalColetaId == null) {
            notaFiscalColetaId = new LongFilter();
        }
        return notaFiscalColetaId;
    }

    public void setNotaFiscalColetaId(LongFilter notaFiscalColetaId) {
        this.notaFiscalColetaId = notaFiscalColetaId;
    }

    public LongFilter getEnderecoOrigemId() {
        return enderecoOrigemId;
    }

    public LongFilter enderecoOrigemId() {
        if (enderecoOrigemId == null) {
            enderecoOrigemId = new LongFilter();
        }
        return enderecoOrigemId;
    }

    public void setEnderecoOrigemId(LongFilter enderecoOrigemId) {
        this.enderecoOrigemId = enderecoOrigemId;
    }

    public LongFilter getEnderecoDestinoId() {
        return enderecoDestinoId;
    }

    public LongFilter enderecoDestinoId() {
        if (enderecoDestinoId == null) {
            enderecoDestinoId = new LongFilter();
        }
        return enderecoDestinoId;
    }

    public void setEnderecoDestinoId(LongFilter enderecoDestinoId) {
        this.enderecoDestinoId = enderecoDestinoId;
    }

    public LongFilter getHistoricoStatusColetaId() {
        return historicoStatusColetaId;
    }

    public LongFilter historicoStatusColetaId() {
        if (historicoStatusColetaId == null) {
            historicoStatusColetaId = new LongFilter();
        }
        return historicoStatusColetaId;
    }

    public void setHistoricoStatusColetaId(LongFilter historicoStatusColetaId) {
        this.historicoStatusColetaId = historicoStatusColetaId;
    }

    public LongFilter getEmbarcadorId() {
        return embarcadorId;
    }

    public LongFilter embarcadorId() {
        if (embarcadorId == null) {
            embarcadorId = new LongFilter();
        }
        return embarcadorId;
    }

    public void setEmbarcadorId(LongFilter embarcadorId) {
        this.embarcadorId = embarcadorId;
    }

    public LongFilter getStatusColetaId() {
        return statusColetaId;
    }

    public LongFilter statusColetaId() {
        if (statusColetaId == null) {
            statusColetaId = new LongFilter();
        }
        return statusColetaId;
    }

    public void setStatusColetaId(LongFilter statusColetaId) {
        this.statusColetaId = statusColetaId;
    }

    public LongFilter getRoteirizacaoId() {
        return roteirizacaoId;
    }

    public LongFilter roteirizacaoId() {
        if (roteirizacaoId == null) {
            roteirizacaoId = new LongFilter();
        }
        return roteirizacaoId;
    }

    public void setRoteirizacaoId(LongFilter roteirizacaoId) {
        this.roteirizacaoId = roteirizacaoId;
    }

    public LongFilter getTipoVeiculoId() {
        return tipoVeiculoId;
    }

    public LongFilter tipoVeiculoId() {
        if (tipoVeiculoId == null) {
            tipoVeiculoId = new LongFilter();
        }
        return tipoVeiculoId;
    }

    public void setTipoVeiculoId(LongFilter tipoVeiculoId) {
        this.tipoVeiculoId = tipoVeiculoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SolicitacaoColetaCriteria that = (SolicitacaoColetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(coletado, that.coletado) &&
            Objects.equals(dataHoraColeta, that.dataHoraColeta) &&
            Objects.equals(entregue, that.entregue) &&
            Objects.equals(dataHoraEntrega, that.dataHoraEntrega) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(cancelado, that.cancelado) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(notaFiscalColetaId, that.notaFiscalColetaId) &&
            Objects.equals(enderecoOrigemId, that.enderecoOrigemId) &&
            Objects.equals(enderecoDestinoId, that.enderecoDestinoId) &&
            Objects.equals(historicoStatusColetaId, that.historicoStatusColetaId) &&
            Objects.equals(embarcadorId, that.embarcadorId) &&
            Objects.equals(statusColetaId, that.statusColetaId) &&
            Objects.equals(roteirizacaoId, that.roteirizacaoId) &&
            Objects.equals(tipoVeiculoId, that.tipoVeiculoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            coletado,
            dataHoraColeta,
            entregue,
            dataHoraEntrega,
            valorTotal,
            observacao,
            cancelado,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            notaFiscalColetaId,
            enderecoOrigemId,
            enderecoDestinoId,
            historicoStatusColetaId,
            embarcadorId,
            statusColetaId,
            roteirizacaoId,
            tipoVeiculoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoColetaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (coletado != null ? "coletado=" + coletado + ", " : "") +
            (dataHoraColeta != null ? "dataHoraColeta=" + dataHoraColeta + ", " : "") +
            (entregue != null ? "entregue=" + entregue + ", " : "") +
            (dataHoraEntrega != null ? "dataHoraEntrega=" + dataHoraEntrega + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (cancelado != null ? "cancelado=" + cancelado + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (notaFiscalColetaId != null ? "notaFiscalColetaId=" + notaFiscalColetaId + ", " : "") +
            (enderecoOrigemId != null ? "enderecoOrigemId=" + enderecoOrigemId + ", " : "") +
            (enderecoDestinoId != null ? "enderecoDestinoId=" + enderecoDestinoId + ", " : "") +
            (historicoStatusColetaId != null ? "historicoStatusColetaId=" + historicoStatusColetaId + ", " : "") +
            (embarcadorId != null ? "embarcadorId=" + embarcadorId + ", " : "") +
            (statusColetaId != null ? "statusColetaId=" + statusColetaId + ", " : "") +
            (roteirizacaoId != null ? "roteirizacaoId=" + roteirizacaoId + ", " : "") +
            (tipoVeiculoId != null ? "tipoVeiculoId=" + tipoVeiculoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
