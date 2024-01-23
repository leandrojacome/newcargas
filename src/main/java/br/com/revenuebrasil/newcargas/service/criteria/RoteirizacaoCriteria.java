package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Roteirizacao} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.RoteirizacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /roteirizacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoteirizacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dataHoraPrimeiraColeta;

    private ZonedDateTimeFilter dataHoraUltimaColeta;

    private ZonedDateTimeFilter dataHoraPrimeiraEntrega;

    private ZonedDateTimeFilter dataHoraUltimaEntrega;

    private DoubleFilter valorTotal;

    private StringFilter observacao;

    private BooleanFilter cancelado;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter historicoStatusColetaId;

    private LongFilter solitacaoColetaId;

    private LongFilter tomadaPrecoId;

    private LongFilter statusColetaId;

    private Boolean distinct;

    public RoteirizacaoCriteria() {}

    public RoteirizacaoCriteria(RoteirizacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataHoraPrimeiraColeta = other.dataHoraPrimeiraColeta == null ? null : other.dataHoraPrimeiraColeta.copy();
        this.dataHoraUltimaColeta = other.dataHoraUltimaColeta == null ? null : other.dataHoraUltimaColeta.copy();
        this.dataHoraPrimeiraEntrega = other.dataHoraPrimeiraEntrega == null ? null : other.dataHoraPrimeiraEntrega.copy();
        this.dataHoraUltimaEntrega = other.dataHoraUltimaEntrega == null ? null : other.dataHoraUltimaEntrega.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.cancelado = other.cancelado == null ? null : other.cancelado.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.historicoStatusColetaId = other.historicoStatusColetaId == null ? null : other.historicoStatusColetaId.copy();
        this.solitacaoColetaId = other.solitacaoColetaId == null ? null : other.solitacaoColetaId.copy();
        this.tomadaPrecoId = other.tomadaPrecoId == null ? null : other.tomadaPrecoId.copy();
        this.statusColetaId = other.statusColetaId == null ? null : other.statusColetaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RoteirizacaoCriteria copy() {
        return new RoteirizacaoCriteria(this);
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

    public ZonedDateTimeFilter getDataHoraPrimeiraColeta() {
        return dataHoraPrimeiraColeta;
    }

    public ZonedDateTimeFilter dataHoraPrimeiraColeta() {
        if (dataHoraPrimeiraColeta == null) {
            dataHoraPrimeiraColeta = new ZonedDateTimeFilter();
        }
        return dataHoraPrimeiraColeta;
    }

    public void setDataHoraPrimeiraColeta(ZonedDateTimeFilter dataHoraPrimeiraColeta) {
        this.dataHoraPrimeiraColeta = dataHoraPrimeiraColeta;
    }

    public ZonedDateTimeFilter getDataHoraUltimaColeta() {
        return dataHoraUltimaColeta;
    }

    public ZonedDateTimeFilter dataHoraUltimaColeta() {
        if (dataHoraUltimaColeta == null) {
            dataHoraUltimaColeta = new ZonedDateTimeFilter();
        }
        return dataHoraUltimaColeta;
    }

    public void setDataHoraUltimaColeta(ZonedDateTimeFilter dataHoraUltimaColeta) {
        this.dataHoraUltimaColeta = dataHoraUltimaColeta;
    }

    public ZonedDateTimeFilter getDataHoraPrimeiraEntrega() {
        return dataHoraPrimeiraEntrega;
    }

    public ZonedDateTimeFilter dataHoraPrimeiraEntrega() {
        if (dataHoraPrimeiraEntrega == null) {
            dataHoraPrimeiraEntrega = new ZonedDateTimeFilter();
        }
        return dataHoraPrimeiraEntrega;
    }

    public void setDataHoraPrimeiraEntrega(ZonedDateTimeFilter dataHoraPrimeiraEntrega) {
        this.dataHoraPrimeiraEntrega = dataHoraPrimeiraEntrega;
    }

    public ZonedDateTimeFilter getDataHoraUltimaEntrega() {
        return dataHoraUltimaEntrega;
    }

    public ZonedDateTimeFilter dataHoraUltimaEntrega() {
        if (dataHoraUltimaEntrega == null) {
            dataHoraUltimaEntrega = new ZonedDateTimeFilter();
        }
        return dataHoraUltimaEntrega;
    }

    public void setDataHoraUltimaEntrega(ZonedDateTimeFilter dataHoraUltimaEntrega) {
        this.dataHoraUltimaEntrega = dataHoraUltimaEntrega;
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

    public LongFilter getSolitacaoColetaId() {
        return solitacaoColetaId;
    }

    public LongFilter solitacaoColetaId() {
        if (solitacaoColetaId == null) {
            solitacaoColetaId = new LongFilter();
        }
        return solitacaoColetaId;
    }

    public void setSolitacaoColetaId(LongFilter solitacaoColetaId) {
        this.solitacaoColetaId = solitacaoColetaId;
    }

    public LongFilter getTomadaPrecoId() {
        return tomadaPrecoId;
    }

    public LongFilter tomadaPrecoId() {
        if (tomadaPrecoId == null) {
            tomadaPrecoId = new LongFilter();
        }
        return tomadaPrecoId;
    }

    public void setTomadaPrecoId(LongFilter tomadaPrecoId) {
        this.tomadaPrecoId = tomadaPrecoId;
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
        final RoteirizacaoCriteria that = (RoteirizacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataHoraPrimeiraColeta, that.dataHoraPrimeiraColeta) &&
            Objects.equals(dataHoraUltimaColeta, that.dataHoraUltimaColeta) &&
            Objects.equals(dataHoraPrimeiraEntrega, that.dataHoraPrimeiraEntrega) &&
            Objects.equals(dataHoraUltimaEntrega, that.dataHoraUltimaEntrega) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(cancelado, that.cancelado) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(historicoStatusColetaId, that.historicoStatusColetaId) &&
            Objects.equals(solitacaoColetaId, that.solitacaoColetaId) &&
            Objects.equals(tomadaPrecoId, that.tomadaPrecoId) &&
            Objects.equals(statusColetaId, that.statusColetaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataHoraPrimeiraColeta,
            dataHoraUltimaColeta,
            dataHoraPrimeiraEntrega,
            dataHoraUltimaEntrega,
            valorTotal,
            observacao,
            cancelado,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            historicoStatusColetaId,
            solitacaoColetaId,
            tomadaPrecoId,
            statusColetaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoteirizacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataHoraPrimeiraColeta != null ? "dataHoraPrimeiraColeta=" + dataHoraPrimeiraColeta + ", " : "") +
            (dataHoraUltimaColeta != null ? "dataHoraUltimaColeta=" + dataHoraUltimaColeta + ", " : "") +
            (dataHoraPrimeiraEntrega != null ? "dataHoraPrimeiraEntrega=" + dataHoraPrimeiraEntrega + ", " : "") +
            (dataHoraUltimaEntrega != null ? "dataHoraUltimaEntrega=" + dataHoraUltimaEntrega + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (cancelado != null ? "cancelado=" + cancelado + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (historicoStatusColetaId != null ? "historicoStatusColetaId=" + historicoStatusColetaId + ", " : "") +
            (solitacaoColetaId != null ? "solitacaoColetaId=" + solitacaoColetaId + ", " : "") +
            (tomadaPrecoId != null ? "tomadaPrecoId=" + tomadaPrecoId + ", " : "") +
            (statusColetaId != null ? "statusColetaId=" + statusColetaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
