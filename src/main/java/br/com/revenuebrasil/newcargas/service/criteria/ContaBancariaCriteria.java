package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.ContaBancaria} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.ContaBancariaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conta-bancarias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaBancariaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter agencia;

    private StringFilter conta;

    private StringFilter observacao;

    private StringFilter tipo;

    private StringFilter pix;

    private StringFilter titular;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter bancoId;

    private LongFilter embarcadorId;

    private LongFilter transportadoraId;

    private Boolean distinct;

    public ContaBancariaCriteria() {}

    public ContaBancariaCriteria(ContaBancariaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.agencia = other.agencia == null ? null : other.agencia.copy();
        this.conta = other.conta == null ? null : other.conta.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.pix = other.pix == null ? null : other.pix.copy();
        this.titular = other.titular == null ? null : other.titular.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.bancoId = other.bancoId == null ? null : other.bancoId.copy();
        this.embarcadorId = other.embarcadorId == null ? null : other.embarcadorId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContaBancariaCriteria copy() {
        return new ContaBancariaCriteria(this);
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

    public StringFilter getAgencia() {
        return agencia;
    }

    public StringFilter agencia() {
        if (agencia == null) {
            agencia = new StringFilter();
        }
        return agencia;
    }

    public void setAgencia(StringFilter agencia) {
        this.agencia = agencia;
    }

    public StringFilter getConta() {
        return conta;
    }

    public StringFilter conta() {
        if (conta == null) {
            conta = new StringFilter();
        }
        return conta;
    }

    public void setConta(StringFilter conta) {
        this.conta = conta;
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

    public StringFilter getTipo() {
        return tipo;
    }

    public StringFilter tipo() {
        if (tipo == null) {
            tipo = new StringFilter();
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getPix() {
        return pix;
    }

    public StringFilter pix() {
        if (pix == null) {
            pix = new StringFilter();
        }
        return pix;
    }

    public void setPix(StringFilter pix) {
        this.pix = pix;
    }

    public StringFilter getTitular() {
        return titular;
    }

    public StringFilter titular() {
        if (titular == null) {
            titular = new StringFilter();
        }
        return titular;
    }

    public void setTitular(StringFilter titular) {
        this.titular = titular;
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

    public LongFilter getBancoId() {
        return bancoId;
    }

    public LongFilter bancoId() {
        if (bancoId == null) {
            bancoId = new LongFilter();
        }
        return bancoId;
    }

    public void setBancoId(LongFilter bancoId) {
        this.bancoId = bancoId;
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
        final ContaBancariaCriteria that = (ContaBancariaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(agencia, that.agencia) &&
            Objects.equals(conta, that.conta) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(pix, that.pix) &&
            Objects.equals(titular, that.titular) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(bancoId, that.bancoId) &&
            Objects.equals(embarcadorId, that.embarcadorId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            agencia,
            conta,
            observacao,
            tipo,
            pix,
            titular,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            bancoId,
            embarcadorId,
            transportadoraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaBancariaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (agencia != null ? "agencia=" + agencia + ", " : "") +
            (conta != null ? "conta=" + conta + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (pix != null ? "pix=" + pix + ", " : "") +
            (titular != null ? "titular=" + titular + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (bancoId != null ? "bancoId=" + bancoId + ", " : "") +
            (embarcadorId != null ? "embarcadorId=" + embarcadorId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
