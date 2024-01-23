package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Contratacao} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.ContratacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contratacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContratacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter valorTotal;

    private IntegerFilter validadeEmDias;

    private LocalDateFilter dataValidade;

    private StringFilter observacao;

    private BooleanFilter cancelado;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter faturaId;

    private LongFilter solicitacaoColetaId;

    private LongFilter transportadoraId;

    private Boolean distinct;

    public ContratacaoCriteria() {}

    public ContratacaoCriteria(ContratacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.validadeEmDias = other.validadeEmDias == null ? null : other.validadeEmDias.copy();
        this.dataValidade = other.dataValidade == null ? null : other.dataValidade.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.cancelado = other.cancelado == null ? null : other.cancelado.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.faturaId = other.faturaId == null ? null : other.faturaId.copy();
        this.solicitacaoColetaId = other.solicitacaoColetaId == null ? null : other.solicitacaoColetaId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContratacaoCriteria copy() {
        return new ContratacaoCriteria(this);
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

    public IntegerFilter getValidadeEmDias() {
        return validadeEmDias;
    }

    public IntegerFilter validadeEmDias() {
        if (validadeEmDias == null) {
            validadeEmDias = new IntegerFilter();
        }
        return validadeEmDias;
    }

    public void setValidadeEmDias(IntegerFilter validadeEmDias) {
        this.validadeEmDias = validadeEmDias;
    }

    public LocalDateFilter getDataValidade() {
        return dataValidade;
    }

    public LocalDateFilter dataValidade() {
        if (dataValidade == null) {
            dataValidade = new LocalDateFilter();
        }
        return dataValidade;
    }

    public void setDataValidade(LocalDateFilter dataValidade) {
        this.dataValidade = dataValidade;
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

    public LongFilter getFaturaId() {
        return faturaId;
    }

    public LongFilter faturaId() {
        if (faturaId == null) {
            faturaId = new LongFilter();
        }
        return faturaId;
    }

    public void setFaturaId(LongFilter faturaId) {
        this.faturaId = faturaId;
    }

    public LongFilter getSolicitacaoColetaId() {
        return solicitacaoColetaId;
    }

    public LongFilter solicitacaoColetaId() {
        if (solicitacaoColetaId == null) {
            solicitacaoColetaId = new LongFilter();
        }
        return solicitacaoColetaId;
    }

    public void setSolicitacaoColetaId(LongFilter solicitacaoColetaId) {
        this.solicitacaoColetaId = solicitacaoColetaId;
    }

    public LongFilter getTransportadoraId() {
        return transportadoraId;
    }

    public LongFilter transportadoraId() {
        if (transportadoraId == null) {
            transportadoraId = new LongFilter();
        }
        return transportadoraId;
    }

    public void setTransportadoraId(LongFilter transportadoraId) {
        this.transportadoraId = transportadoraId;
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
        final ContratacaoCriteria that = (ContratacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(validadeEmDias, that.validadeEmDias) &&
            Objects.equals(dataValidade, that.dataValidade) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(cancelado, that.cancelado) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(faturaId, that.faturaId) &&
            Objects.equals(solicitacaoColetaId, that.solicitacaoColetaId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            valorTotal,
            validadeEmDias,
            dataValidade,
            observacao,
            cancelado,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            faturaId,
            solicitacaoColetaId,
            transportadoraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (validadeEmDias != null ? "validadeEmDias=" + validadeEmDias + ", " : "") +
            (dataValidade != null ? "dataValidade=" + dataValidade + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (cancelado != null ? "cancelado=" + cancelado + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (faturaId != null ? "faturaId=" + faturaId + ", " : "") +
            (solicitacaoColetaId != null ? "solicitacaoColetaId=" + solicitacaoColetaId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
