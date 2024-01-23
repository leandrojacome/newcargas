package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.TomadaPreco} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.TomadaPrecoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tomada-precos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TomadaPrecoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dataHoraEnvio;

    private IntegerFilter prazoResposta;

    private DoubleFilter valorTotal;

    private StringFilter observacao;

    private BooleanFilter aprovado;

    private BooleanFilter cancelado;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter contratacaoId;

    private LongFilter transportadoraId;

    private LongFilter roteirizacaoId;

    private Boolean distinct;

    public TomadaPrecoCriteria() {}

    public TomadaPrecoCriteria(TomadaPrecoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataHoraEnvio = other.dataHoraEnvio == null ? null : other.dataHoraEnvio.copy();
        this.prazoResposta = other.prazoResposta == null ? null : other.prazoResposta.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.aprovado = other.aprovado == null ? null : other.aprovado.copy();
        this.cancelado = other.cancelado == null ? null : other.cancelado.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.contratacaoId = other.contratacaoId == null ? null : other.contratacaoId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.roteirizacaoId = other.roteirizacaoId == null ? null : other.roteirizacaoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TomadaPrecoCriteria copy() {
        return new TomadaPrecoCriteria(this);
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

    public ZonedDateTimeFilter getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public ZonedDateTimeFilter dataHoraEnvio() {
        if (dataHoraEnvio == null) {
            dataHoraEnvio = new ZonedDateTimeFilter();
        }
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(ZonedDateTimeFilter dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public IntegerFilter getPrazoResposta() {
        return prazoResposta;
    }

    public IntegerFilter prazoResposta() {
        if (prazoResposta == null) {
            prazoResposta = new IntegerFilter();
        }
        return prazoResposta;
    }

    public void setPrazoResposta(IntegerFilter prazoResposta) {
        this.prazoResposta = prazoResposta;
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

    public BooleanFilter getAprovado() {
        return aprovado;
    }

    public BooleanFilter aprovado() {
        if (aprovado == null) {
            aprovado = new BooleanFilter();
        }
        return aprovado;
    }

    public void setAprovado(BooleanFilter aprovado) {
        this.aprovado = aprovado;
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

    public LongFilter getContratacaoId() {
        return contratacaoId;
    }

    public LongFilter contratacaoId() {
        if (contratacaoId == null) {
            contratacaoId = new LongFilter();
        }
        return contratacaoId;
    }

    public void setContratacaoId(LongFilter contratacaoId) {
        this.contratacaoId = contratacaoId;
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
        final TomadaPrecoCriteria that = (TomadaPrecoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataHoraEnvio, that.dataHoraEnvio) &&
            Objects.equals(prazoResposta, that.prazoResposta) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(aprovado, that.aprovado) &&
            Objects.equals(cancelado, that.cancelado) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(contratacaoId, that.contratacaoId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(roteirizacaoId, that.roteirizacaoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataHoraEnvio,
            prazoResposta,
            valorTotal,
            observacao,
            aprovado,
            cancelado,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            contratacaoId,
            transportadoraId,
            roteirizacaoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TomadaPrecoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataHoraEnvio != null ? "dataHoraEnvio=" + dataHoraEnvio + ", " : "") +
            (prazoResposta != null ? "prazoResposta=" + prazoResposta + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (aprovado != null ? "aprovado=" + aprovado + ", " : "") +
            (cancelado != null ? "cancelado=" + cancelado + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (contratacaoId != null ? "contratacaoId=" + contratacaoId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (roteirizacaoId != null ? "roteirizacaoId=" + roteirizacaoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
