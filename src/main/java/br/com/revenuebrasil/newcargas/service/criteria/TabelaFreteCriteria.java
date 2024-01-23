package br.com.revenuebrasil.newcargas.service.criteria;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoTabelaFrete;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.TabelaFrete} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.TabelaFreteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tabela-fretes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TabelaFreteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoTabelaFrete
     */
    public static class TipoTabelaFreteFilter extends Filter<TipoTabelaFrete> {

        public TipoTabelaFreteFilter() {}

        public TipoTabelaFreteFilter(TipoTabelaFreteFilter filter) {
            super(filter);
        }

        @Override
        public TipoTabelaFreteFilter copy() {
            return new TipoTabelaFreteFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoTabelaFreteFilter tipo;

    private StringFilter nome;

    private StringFilter descricao;

    private IntegerFilter leadTime;

    private DoubleFilter freteMinimo;

    private DoubleFilter valorTonelada;

    private DoubleFilter valorMetroCubico;

    private DoubleFilter valorUnidade;

    private DoubleFilter valorKm;

    private DoubleFilter valorAdicional;

    private DoubleFilter valorColeta;

    private DoubleFilter valorEntrega;

    private DoubleFilter valorTotal;

    private DoubleFilter valorKmAdicional;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter embarcadorId;

    private LongFilter transportadoraId;

    private LongFilter tipoCargaId;

    private LongFilter tipoFreteId;

    private LongFilter formaCobrancaId;

    private LongFilter regiaoOrigemId;

    private LongFilter regiaoDestinoId;

    private Boolean distinct;

    public TabelaFreteCriteria() {}

    public TabelaFreteCriteria(TabelaFreteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.leadTime = other.leadTime == null ? null : other.leadTime.copy();
        this.freteMinimo = other.freteMinimo == null ? null : other.freteMinimo.copy();
        this.valorTonelada = other.valorTonelada == null ? null : other.valorTonelada.copy();
        this.valorMetroCubico = other.valorMetroCubico == null ? null : other.valorMetroCubico.copy();
        this.valorUnidade = other.valorUnidade == null ? null : other.valorUnidade.copy();
        this.valorKm = other.valorKm == null ? null : other.valorKm.copy();
        this.valorAdicional = other.valorAdicional == null ? null : other.valorAdicional.copy();
        this.valorColeta = other.valorColeta == null ? null : other.valorColeta.copy();
        this.valorEntrega = other.valorEntrega == null ? null : other.valorEntrega.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.valorKmAdicional = other.valorKmAdicional == null ? null : other.valorKmAdicional.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.embarcadorId = other.embarcadorId == null ? null : other.embarcadorId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.tipoCargaId = other.tipoCargaId == null ? null : other.tipoCargaId.copy();
        this.tipoFreteId = other.tipoFreteId == null ? null : other.tipoFreteId.copy();
        this.formaCobrancaId = other.formaCobrancaId == null ? null : other.formaCobrancaId.copy();
        this.regiaoOrigemId = other.regiaoOrigemId == null ? null : other.regiaoOrigemId.copy();
        this.regiaoDestinoId = other.regiaoDestinoId == null ? null : other.regiaoDestinoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TabelaFreteCriteria copy() {
        return new TabelaFreteCriteria(this);
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

    public TipoTabelaFreteFilter getTipo() {
        return tipo;
    }

    public TipoTabelaFreteFilter tipo() {
        if (tipo == null) {
            tipo = new TipoTabelaFreteFilter();
        }
        return tipo;
    }

    public void setTipo(TipoTabelaFreteFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public IntegerFilter getLeadTime() {
        return leadTime;
    }

    public IntegerFilter leadTime() {
        if (leadTime == null) {
            leadTime = new IntegerFilter();
        }
        return leadTime;
    }

    public void setLeadTime(IntegerFilter leadTime) {
        this.leadTime = leadTime;
    }

    public DoubleFilter getFreteMinimo() {
        return freteMinimo;
    }

    public DoubleFilter freteMinimo() {
        if (freteMinimo == null) {
            freteMinimo = new DoubleFilter();
        }
        return freteMinimo;
    }

    public void setFreteMinimo(DoubleFilter freteMinimo) {
        this.freteMinimo = freteMinimo;
    }

    public DoubleFilter getValorTonelada() {
        return valorTonelada;
    }

    public DoubleFilter valorTonelada() {
        if (valorTonelada == null) {
            valorTonelada = new DoubleFilter();
        }
        return valorTonelada;
    }

    public void setValorTonelada(DoubleFilter valorTonelada) {
        this.valorTonelada = valorTonelada;
    }

    public DoubleFilter getValorMetroCubico() {
        return valorMetroCubico;
    }

    public DoubleFilter valorMetroCubico() {
        if (valorMetroCubico == null) {
            valorMetroCubico = new DoubleFilter();
        }
        return valorMetroCubico;
    }

    public void setValorMetroCubico(DoubleFilter valorMetroCubico) {
        this.valorMetroCubico = valorMetroCubico;
    }

    public DoubleFilter getValorUnidade() {
        return valorUnidade;
    }

    public DoubleFilter valorUnidade() {
        if (valorUnidade == null) {
            valorUnidade = new DoubleFilter();
        }
        return valorUnidade;
    }

    public void setValorUnidade(DoubleFilter valorUnidade) {
        this.valorUnidade = valorUnidade;
    }

    public DoubleFilter getValorKm() {
        return valorKm;
    }

    public DoubleFilter valorKm() {
        if (valorKm == null) {
            valorKm = new DoubleFilter();
        }
        return valorKm;
    }

    public void setValorKm(DoubleFilter valorKm) {
        this.valorKm = valorKm;
    }

    public DoubleFilter getValorAdicional() {
        return valorAdicional;
    }

    public DoubleFilter valorAdicional() {
        if (valorAdicional == null) {
            valorAdicional = new DoubleFilter();
        }
        return valorAdicional;
    }

    public void setValorAdicional(DoubleFilter valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    public DoubleFilter getValorColeta() {
        return valorColeta;
    }

    public DoubleFilter valorColeta() {
        if (valorColeta == null) {
            valorColeta = new DoubleFilter();
        }
        return valorColeta;
    }

    public void setValorColeta(DoubleFilter valorColeta) {
        this.valorColeta = valorColeta;
    }

    public DoubleFilter getValorEntrega() {
        return valorEntrega;
    }

    public DoubleFilter valorEntrega() {
        if (valorEntrega == null) {
            valorEntrega = new DoubleFilter();
        }
        return valorEntrega;
    }

    public void setValorEntrega(DoubleFilter valorEntrega) {
        this.valorEntrega = valorEntrega;
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

    public DoubleFilter getValorKmAdicional() {
        return valorKmAdicional;
    }

    public DoubleFilter valorKmAdicional() {
        if (valorKmAdicional == null) {
            valorKmAdicional = new DoubleFilter();
        }
        return valorKmAdicional;
    }

    public void setValorKmAdicional(DoubleFilter valorKmAdicional) {
        this.valorKmAdicional = valorKmAdicional;
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

    public LongFilter getTipoCargaId() {
        return tipoCargaId;
    }

    public LongFilter tipoCargaId() {
        if (tipoCargaId == null) {
            tipoCargaId = new LongFilter();
        }
        return tipoCargaId;
    }

    public void setTipoCargaId(LongFilter tipoCargaId) {
        this.tipoCargaId = tipoCargaId;
    }

    public LongFilter getTipoFreteId() {
        return tipoFreteId;
    }

    public LongFilter tipoFreteId() {
        if (tipoFreteId == null) {
            tipoFreteId = new LongFilter();
        }
        return tipoFreteId;
    }

    public void setTipoFreteId(LongFilter tipoFreteId) {
        this.tipoFreteId = tipoFreteId;
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

    public LongFilter getRegiaoOrigemId() {
        return regiaoOrigemId;
    }

    public LongFilter regiaoOrigemId() {
        if (regiaoOrigemId == null) {
            regiaoOrigemId = new LongFilter();
        }
        return regiaoOrigemId;
    }

    public void setRegiaoOrigemId(LongFilter regiaoOrigemId) {
        this.regiaoOrigemId = regiaoOrigemId;
    }

    public LongFilter getRegiaoDestinoId() {
        return regiaoDestinoId;
    }

    public LongFilter regiaoDestinoId() {
        if (regiaoDestinoId == null) {
            regiaoDestinoId = new LongFilter();
        }
        return regiaoDestinoId;
    }

    public void setRegiaoDestinoId(LongFilter regiaoDestinoId) {
        this.regiaoDestinoId = regiaoDestinoId;
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
        final TabelaFreteCriteria that = (TabelaFreteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(leadTime, that.leadTime) &&
            Objects.equals(freteMinimo, that.freteMinimo) &&
            Objects.equals(valorTonelada, that.valorTonelada) &&
            Objects.equals(valorMetroCubico, that.valorMetroCubico) &&
            Objects.equals(valorUnidade, that.valorUnidade) &&
            Objects.equals(valorKm, that.valorKm) &&
            Objects.equals(valorAdicional, that.valorAdicional) &&
            Objects.equals(valorColeta, that.valorColeta) &&
            Objects.equals(valorEntrega, that.valorEntrega) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(valorKmAdicional, that.valorKmAdicional) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(embarcadorId, that.embarcadorId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(tipoCargaId, that.tipoCargaId) &&
            Objects.equals(tipoFreteId, that.tipoFreteId) &&
            Objects.equals(formaCobrancaId, that.formaCobrancaId) &&
            Objects.equals(regiaoOrigemId, that.regiaoOrigemId) &&
            Objects.equals(regiaoDestinoId, that.regiaoDestinoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipo,
            nome,
            descricao,
            leadTime,
            freteMinimo,
            valorTonelada,
            valorMetroCubico,
            valorUnidade,
            valorKm,
            valorAdicional,
            valorColeta,
            valorEntrega,
            valorTotal,
            valorKmAdicional,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            embarcadorId,
            transportadoraId,
            tipoCargaId,
            tipoFreteId,
            formaCobrancaId,
            regiaoOrigemId,
            regiaoDestinoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TabelaFreteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (leadTime != null ? "leadTime=" + leadTime + ", " : "") +
            (freteMinimo != null ? "freteMinimo=" + freteMinimo + ", " : "") +
            (valorTonelada != null ? "valorTonelada=" + valorTonelada + ", " : "") +
            (valorMetroCubico != null ? "valorMetroCubico=" + valorMetroCubico + ", " : "") +
            (valorUnidade != null ? "valorUnidade=" + valorUnidade + ", " : "") +
            (valorKm != null ? "valorKm=" + valorKm + ", " : "") +
            (valorAdicional != null ? "valorAdicional=" + valorAdicional + ", " : "") +
            (valorColeta != null ? "valorColeta=" + valorColeta + ", " : "") +
            (valorEntrega != null ? "valorEntrega=" + valorEntrega + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (valorKmAdicional != null ? "valorKmAdicional=" + valorKmAdicional + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (embarcadorId != null ? "embarcadorId=" + embarcadorId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (tipoCargaId != null ? "tipoCargaId=" + tipoCargaId + ", " : "") +
            (tipoFreteId != null ? "tipoFreteId=" + tipoFreteId + ", " : "") +
            (formaCobrancaId != null ? "formaCobrancaId=" + formaCobrancaId + ", " : "") +
            (regiaoOrigemId != null ? "regiaoOrigemId=" + regiaoOrigemId + ", " : "") +
            (regiaoDestinoId != null ? "regiaoDestinoId=" + regiaoDestinoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
