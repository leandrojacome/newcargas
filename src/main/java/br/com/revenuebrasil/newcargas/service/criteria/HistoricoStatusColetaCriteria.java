package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.HistoricoStatusColetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historico-status-coletas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricoStatusColetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dataCriacao;

    private StringFilter observacao;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter solicitacaoColetaId;

    private LongFilter roteirizacaoId;

    private LongFilter statusColetaOrigemId;

    private LongFilter statusColetaDestinoId;

    private Boolean distinct;

    public HistoricoStatusColetaCriteria() {}

    public HistoricoStatusColetaCriteria(HistoricoStatusColetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataCriacao = other.dataCriacao == null ? null : other.dataCriacao.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.solicitacaoColetaId = other.solicitacaoColetaId == null ? null : other.solicitacaoColetaId.copy();
        this.roteirizacaoId = other.roteirizacaoId == null ? null : other.roteirizacaoId.copy();
        this.statusColetaOrigemId = other.statusColetaOrigemId == null ? null : other.statusColetaOrigemId.copy();
        this.statusColetaDestinoId = other.statusColetaDestinoId == null ? null : other.statusColetaDestinoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HistoricoStatusColetaCriteria copy() {
        return new HistoricoStatusColetaCriteria(this);
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

    public ZonedDateTimeFilter getDataCriacao() {
        return dataCriacao;
    }

    public ZonedDateTimeFilter dataCriacao() {
        if (dataCriacao == null) {
            dataCriacao = new ZonedDateTimeFilter();
        }
        return dataCriacao;
    }

    public void setDataCriacao(ZonedDateTimeFilter dataCriacao) {
        this.dataCriacao = dataCriacao;
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

    public LongFilter getStatusColetaOrigemId() {
        return statusColetaOrigemId;
    }

    public LongFilter statusColetaOrigemId() {
        if (statusColetaOrigemId == null) {
            statusColetaOrigemId = new LongFilter();
        }
        return statusColetaOrigemId;
    }

    public void setStatusColetaOrigemId(LongFilter statusColetaOrigemId) {
        this.statusColetaOrigemId = statusColetaOrigemId;
    }

    public LongFilter getStatusColetaDestinoId() {
        return statusColetaDestinoId;
    }

    public LongFilter statusColetaDestinoId() {
        if (statusColetaDestinoId == null) {
            statusColetaDestinoId = new LongFilter();
        }
        return statusColetaDestinoId;
    }

    public void setStatusColetaDestinoId(LongFilter statusColetaDestinoId) {
        this.statusColetaDestinoId = statusColetaDestinoId;
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
        final HistoricoStatusColetaCriteria that = (HistoricoStatusColetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataCriacao, that.dataCriacao) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(solicitacaoColetaId, that.solicitacaoColetaId) &&
            Objects.equals(roteirizacaoId, that.roteirizacaoId) &&
            Objects.equals(statusColetaOrigemId, that.statusColetaOrigemId) &&
            Objects.equals(statusColetaDestinoId, that.statusColetaDestinoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataCriacao,
            observacao,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            solicitacaoColetaId,
            roteirizacaoId,
            statusColetaOrigemId,
            statusColetaDestinoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoStatusColetaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataCriacao != null ? "dataCriacao=" + dataCriacao + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (solicitacaoColetaId != null ? "solicitacaoColetaId=" + solicitacaoColetaId + ", " : "") +
            (roteirizacaoId != null ? "roteirizacaoId=" + roteirizacaoId + ", " : "") +
            (statusColetaOrigemId != null ? "statusColetaOrigemId=" + statusColetaOrigemId + ", " : "") +
            (statusColetaDestinoId != null ? "statusColetaDestinoId=" + statusColetaDestinoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
