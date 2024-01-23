package br.com.revenuebrasil.newcargas.service.criteria;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoFatura;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Fatura} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.FaturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /faturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FaturaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoFatura
     */
    public static class TipoFaturaFilter extends Filter<TipoFatura> {

        public TipoFaturaFilter() {}

        public TipoFaturaFilter(TipoFaturaFilter filter) {
            super(filter);
        }

        @Override
        public TipoFaturaFilter copy() {
            return new TipoFaturaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoFaturaFilter tipo;

    private ZonedDateTimeFilter dataFatura;

    private ZonedDateTimeFilter dataVencimento;

    private ZonedDateTimeFilter dataPagamento;

    private IntegerFilter numeroParcela;

    private DoubleFilter valorTotal;

    private StringFilter observacao;

    private BooleanFilter cancelado;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter embarcadorId;

    private LongFilter transportadoraId;

    private LongFilter contratacaoId;

    private LongFilter formaCobrancaId;

    private Boolean distinct;

    public FaturaCriteria() {}

    public FaturaCriteria(FaturaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.dataFatura = other.dataFatura == null ? null : other.dataFatura.copy();
        this.dataVencimento = other.dataVencimento == null ? null : other.dataVencimento.copy();
        this.dataPagamento = other.dataPagamento == null ? null : other.dataPagamento.copy();
        this.numeroParcela = other.numeroParcela == null ? null : other.numeroParcela.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.cancelado = other.cancelado == null ? null : other.cancelado.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.embarcadorId = other.embarcadorId == null ? null : other.embarcadorId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.contratacaoId = other.contratacaoId == null ? null : other.contratacaoId.copy();
        this.formaCobrancaId = other.formaCobrancaId == null ? null : other.formaCobrancaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FaturaCriteria copy() {
        return new FaturaCriteria(this);
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

    public TipoFaturaFilter getTipo() {
        return tipo;
    }

    public TipoFaturaFilter tipo() {
        if (tipo == null) {
            tipo = new TipoFaturaFilter();
        }
        return tipo;
    }

    public void setTipo(TipoFaturaFilter tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTimeFilter getDataFatura() {
        return dataFatura;
    }

    public ZonedDateTimeFilter dataFatura() {
        if (dataFatura == null) {
            dataFatura = new ZonedDateTimeFilter();
        }
        return dataFatura;
    }

    public void setDataFatura(ZonedDateTimeFilter dataFatura) {
        this.dataFatura = dataFatura;
    }

    public ZonedDateTimeFilter getDataVencimento() {
        return dataVencimento;
    }

    public ZonedDateTimeFilter dataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new ZonedDateTimeFilter();
        }
        return dataVencimento;
    }

    public void setDataVencimento(ZonedDateTimeFilter dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public ZonedDateTimeFilter getDataPagamento() {
        return dataPagamento;
    }

    public ZonedDateTimeFilter dataPagamento() {
        if (dataPagamento == null) {
            dataPagamento = new ZonedDateTimeFilter();
        }
        return dataPagamento;
    }

    public void setDataPagamento(ZonedDateTimeFilter dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public IntegerFilter getNumeroParcela() {
        return numeroParcela;
    }

    public IntegerFilter numeroParcela() {
        if (numeroParcela == null) {
            numeroParcela = new IntegerFilter();
        }
        return numeroParcela;
    }

    public void setNumeroParcela(IntegerFilter numeroParcela) {
        this.numeroParcela = numeroParcela;
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

    public LongFilter getFormaCobrancaId() {
        return formaCobrancaId;
    }

    public LongFilter formaCobrancaId() {
        if (formaCobrancaId == null) {
            formaCobrancaId = new LongFilter();
        }
        return formaCobrancaId;
    }

    public void setFormaCobrancaId(LongFilter formaCobrancaId) {
        this.formaCobrancaId = formaCobrancaId;
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
        final FaturaCriteria that = (FaturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(dataFatura, that.dataFatura) &&
            Objects.equals(dataVencimento, that.dataVencimento) &&
            Objects.equals(dataPagamento, that.dataPagamento) &&
            Objects.equals(numeroParcela, that.numeroParcela) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(cancelado, that.cancelado) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(embarcadorId, that.embarcadorId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(contratacaoId, that.contratacaoId) &&
            Objects.equals(formaCobrancaId, that.formaCobrancaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipo,
            dataFatura,
            dataVencimento,
            dataPagamento,
            numeroParcela,
            valorTotal,
            observacao,
            cancelado,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            embarcadorId,
            transportadoraId,
            contratacaoId,
            formaCobrancaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FaturaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (dataFatura != null ? "dataFatura=" + dataFatura + ", " : "") +
            (dataVencimento != null ? "dataVencimento=" + dataVencimento + ", " : "") +
            (dataPagamento != null ? "dataPagamento=" + dataPagamento + ", " : "") +
            (numeroParcela != null ? "numeroParcela=" + numeroParcela + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (cancelado != null ? "cancelado=" + cancelado + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (embarcadorId != null ? "embarcadorId=" + embarcadorId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (contratacaoId != null ? "contratacaoId=" + contratacaoId + ", " : "") +
            (formaCobrancaId != null ? "formaCobrancaId=" + formaCobrancaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
