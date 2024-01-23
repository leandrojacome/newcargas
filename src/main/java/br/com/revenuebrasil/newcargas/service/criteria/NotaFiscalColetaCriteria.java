package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.NotaFiscalColetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nota-fiscal-coletas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotaFiscalColetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private StringFilter serie;

    private StringFilter remetente;

    private StringFilter destinatario;

    private DoubleFilter metroCubico;

    private DoubleFilter quantidade;

    private DoubleFilter peso;

    private ZonedDateTimeFilter dataEmissao;

    private ZonedDateTimeFilter dataSaida;

    private DoubleFilter valorTotal;

    private DoubleFilter pesoTotal;

    private IntegerFilter quantidadeTotal;

    private StringFilter observacao;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter enderecoOrigemId;

    private LongFilter enderecoDestinoId;

    private LongFilter solicitacaoColetaId;

    private Boolean distinct;

    public NotaFiscalColetaCriteria() {}

    public NotaFiscalColetaCriteria(NotaFiscalColetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.serie = other.serie == null ? null : other.serie.copy();
        this.remetente = other.remetente == null ? null : other.remetente.copy();
        this.destinatario = other.destinatario == null ? null : other.destinatario.copy();
        this.metroCubico = other.metroCubico == null ? null : other.metroCubico.copy();
        this.quantidade = other.quantidade == null ? null : other.quantidade.copy();
        this.peso = other.peso == null ? null : other.peso.copy();
        this.dataEmissao = other.dataEmissao == null ? null : other.dataEmissao.copy();
        this.dataSaida = other.dataSaida == null ? null : other.dataSaida.copy();
        this.valorTotal = other.valorTotal == null ? null : other.valorTotal.copy();
        this.pesoTotal = other.pesoTotal == null ? null : other.pesoTotal.copy();
        this.quantidadeTotal = other.quantidadeTotal == null ? null : other.quantidadeTotal.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.enderecoOrigemId = other.enderecoOrigemId == null ? null : other.enderecoOrigemId.copy();
        this.enderecoDestinoId = other.enderecoDestinoId == null ? null : other.enderecoDestinoId.copy();
        this.solicitacaoColetaId = other.solicitacaoColetaId == null ? null : other.solicitacaoColetaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotaFiscalColetaCriteria copy() {
        return new NotaFiscalColetaCriteria(this);
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

    public StringFilter getNumero() {
        return numero;
    }

    public StringFilter numero() {
        if (numero == null) {
            numero = new StringFilter();
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public StringFilter getSerie() {
        return serie;
    }

    public StringFilter serie() {
        if (serie == null) {
            serie = new StringFilter();
        }
        return serie;
    }

    public void setSerie(StringFilter serie) {
        this.serie = serie;
    }

    public StringFilter getRemetente() {
        return remetente;
    }

    public StringFilter remetente() {
        if (remetente == null) {
            remetente = new StringFilter();
        }
        return remetente;
    }

    public void setRemetente(StringFilter remetente) {
        this.remetente = remetente;
    }

    public StringFilter getDestinatario() {
        return destinatario;
    }

    public StringFilter destinatario() {
        if (destinatario == null) {
            destinatario = new StringFilter();
        }
        return destinatario;
    }

    public void setDestinatario(StringFilter destinatario) {
        this.destinatario = destinatario;
    }

    public DoubleFilter getMetroCubico() {
        return metroCubico;
    }

    public DoubleFilter metroCubico() {
        if (metroCubico == null) {
            metroCubico = new DoubleFilter();
        }
        return metroCubico;
    }

    public void setMetroCubico(DoubleFilter metroCubico) {
        this.metroCubico = metroCubico;
    }

    public DoubleFilter getQuantidade() {
        return quantidade;
    }

    public DoubleFilter quantidade() {
        if (quantidade == null) {
            quantidade = new DoubleFilter();
        }
        return quantidade;
    }

    public void setQuantidade(DoubleFilter quantidade) {
        this.quantidade = quantidade;
    }

    public DoubleFilter getPeso() {
        return peso;
    }

    public DoubleFilter peso() {
        if (peso == null) {
            peso = new DoubleFilter();
        }
        return peso;
    }

    public void setPeso(DoubleFilter peso) {
        this.peso = peso;
    }

    public ZonedDateTimeFilter getDataEmissao() {
        return dataEmissao;
    }

    public ZonedDateTimeFilter dataEmissao() {
        if (dataEmissao == null) {
            dataEmissao = new ZonedDateTimeFilter();
        }
        return dataEmissao;
    }

    public void setDataEmissao(ZonedDateTimeFilter dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public ZonedDateTimeFilter getDataSaida() {
        return dataSaida;
    }

    public ZonedDateTimeFilter dataSaida() {
        if (dataSaida == null) {
            dataSaida = new ZonedDateTimeFilter();
        }
        return dataSaida;
    }

    public void setDataSaida(ZonedDateTimeFilter dataSaida) {
        this.dataSaida = dataSaida;
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

    public DoubleFilter getPesoTotal() {
        return pesoTotal;
    }

    public DoubleFilter pesoTotal() {
        if (pesoTotal == null) {
            pesoTotal = new DoubleFilter();
        }
        return pesoTotal;
    }

    public void setPesoTotal(DoubleFilter pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public IntegerFilter getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public IntegerFilter quantidadeTotal() {
        if (quantidadeTotal == null) {
            quantidadeTotal = new IntegerFilter();
        }
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(IntegerFilter quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
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
        final NotaFiscalColetaCriteria that = (NotaFiscalColetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(serie, that.serie) &&
            Objects.equals(remetente, that.remetente) &&
            Objects.equals(destinatario, that.destinatario) &&
            Objects.equals(metroCubico, that.metroCubico) &&
            Objects.equals(quantidade, that.quantidade) &&
            Objects.equals(peso, that.peso) &&
            Objects.equals(dataEmissao, that.dataEmissao) &&
            Objects.equals(dataSaida, that.dataSaida) &&
            Objects.equals(valorTotal, that.valorTotal) &&
            Objects.equals(pesoTotal, that.pesoTotal) &&
            Objects.equals(quantidadeTotal, that.quantidadeTotal) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(enderecoOrigemId, that.enderecoOrigemId) &&
            Objects.equals(enderecoDestinoId, that.enderecoDestinoId) &&
            Objects.equals(solicitacaoColetaId, that.solicitacaoColetaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numero,
            serie,
            remetente,
            destinatario,
            metroCubico,
            quantidade,
            peso,
            dataEmissao,
            dataSaida,
            valorTotal,
            pesoTotal,
            quantidadeTotal,
            observacao,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            enderecoOrigemId,
            enderecoDestinoId,
            solicitacaoColetaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaFiscalColetaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numero != null ? "numero=" + numero + ", " : "") +
            (serie != null ? "serie=" + serie + ", " : "") +
            (remetente != null ? "remetente=" + remetente + ", " : "") +
            (destinatario != null ? "destinatario=" + destinatario + ", " : "") +
            (metroCubico != null ? "metroCubico=" + metroCubico + ", " : "") +
            (quantidade != null ? "quantidade=" + quantidade + ", " : "") +
            (peso != null ? "peso=" + peso + ", " : "") +
            (dataEmissao != null ? "dataEmissao=" + dataEmissao + ", " : "") +
            (dataSaida != null ? "dataSaida=" + dataSaida + ", " : "") +
            (valorTotal != null ? "valorTotal=" + valorTotal + ", " : "") +
            (pesoTotal != null ? "pesoTotal=" + pesoTotal + ", " : "") +
            (quantidadeTotal != null ? "quantidadeTotal=" + quantidadeTotal + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (enderecoOrigemId != null ? "enderecoOrigemId=" + enderecoOrigemId + ", " : "") +
            (enderecoDestinoId != null ? "enderecoDestinoId=" + enderecoDestinoId + ", " : "") +
            (solicitacaoColetaId != null ? "solicitacaoColetaId=" + solicitacaoColetaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
