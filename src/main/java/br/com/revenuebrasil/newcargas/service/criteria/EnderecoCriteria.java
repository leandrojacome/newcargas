package br.com.revenuebrasil.newcargas.service.criteria;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoEndereco;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Endereco} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.EnderecoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enderecos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnderecoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoEndereco
     */
    public static class TipoEnderecoFilter extends Filter<TipoEndereco> {

        public TipoEnderecoFilter() {}

        public TipoEnderecoFilter(TipoEnderecoFilter filter) {
            super(filter);
        }

        @Override
        public TipoEnderecoFilter copy() {
            return new TipoEnderecoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoEnderecoFilter tipo;

    private StringFilter cep;

    private StringFilter endereco;

    private StringFilter numero;

    private StringFilter complemento;

    private StringFilter bairro;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter cidadeId;

    private LongFilter embarcadorId;

    private LongFilter transportadoraId;

    private LongFilter notaFiscalColetaOrigemId;

    private LongFilter notaFiscalColetaDestinoId;

    private LongFilter solicitacaoColetaOrigemId;

    private LongFilter solicitacaoColetaDestinoId;

    private Boolean distinct;

    public EnderecoCriteria() {}

    public EnderecoCriteria(EnderecoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.endereco = other.endereco == null ? null : other.endereco.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.complemento = other.complemento == null ? null : other.complemento.copy();
        this.bairro = other.bairro == null ? null : other.bairro.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.cidadeId = other.cidadeId == null ? null : other.cidadeId.copy();
        this.embarcadorId = other.embarcadorId == null ? null : other.embarcadorId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.notaFiscalColetaOrigemId = other.notaFiscalColetaOrigemId == null ? null : other.notaFiscalColetaOrigemId.copy();
        this.notaFiscalColetaDestinoId = other.notaFiscalColetaDestinoId == null ? null : other.notaFiscalColetaDestinoId.copy();
        this.solicitacaoColetaOrigemId = other.solicitacaoColetaOrigemId == null ? null : other.solicitacaoColetaOrigemId.copy();
        this.solicitacaoColetaDestinoId = other.solicitacaoColetaDestinoId == null ? null : other.solicitacaoColetaDestinoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EnderecoCriteria copy() {
        return new EnderecoCriteria(this);
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

    public TipoEnderecoFilter getTipo() {
        return tipo;
    }

    public TipoEnderecoFilter tipo() {
        if (tipo == null) {
            tipo = new TipoEnderecoFilter();
        }
        return tipo;
    }

    public void setTipo(TipoEnderecoFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getCep() {
        return cep;
    }

    public StringFilter cep() {
        if (cep == null) {
            cep = new StringFilter();
        }
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public StringFilter getEndereco() {
        return endereco;
    }

    public StringFilter endereco() {
        if (endereco == null) {
            endereco = new StringFilter();
        }
        return endereco;
    }

    public void setEndereco(StringFilter endereco) {
        this.endereco = endereco;
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

    public StringFilter getComplemento() {
        return complemento;
    }

    public StringFilter complemento() {
        if (complemento == null) {
            complemento = new StringFilter();
        }
        return complemento;
    }

    public void setComplemento(StringFilter complemento) {
        this.complemento = complemento;
    }

    public StringFilter getBairro() {
        return bairro;
    }

    public StringFilter bairro() {
        if (bairro == null) {
            bairro = new StringFilter();
        }
        return bairro;
    }

    public void setBairro(StringFilter bairro) {
        this.bairro = bairro;
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

    public LongFilter getCidadeId() {
        return cidadeId;
    }

    public LongFilter cidadeId() {
        if (cidadeId == null) {
            cidadeId = new LongFilter();
        }
        return cidadeId;
    }

    public void setCidadeId(LongFilter cidadeId) {
        this.cidadeId = cidadeId;
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

    public LongFilter getNotaFiscalColetaOrigemId() {
        return notaFiscalColetaOrigemId;
    }

    public LongFilter notaFiscalColetaOrigemId() {
        if (notaFiscalColetaOrigemId == null) {
            notaFiscalColetaOrigemId = new LongFilter();
        }
        return notaFiscalColetaOrigemId;
    }

    public void setNotaFiscalColetaOrigemId(LongFilter notaFiscalColetaOrigemId) {
        this.notaFiscalColetaOrigemId = notaFiscalColetaOrigemId;
    }

    public LongFilter getNotaFiscalColetaDestinoId() {
        return notaFiscalColetaDestinoId;
    }

    public LongFilter notaFiscalColetaDestinoId() {
        if (notaFiscalColetaDestinoId == null) {
            notaFiscalColetaDestinoId = new LongFilter();
        }
        return notaFiscalColetaDestinoId;
    }

    public void setNotaFiscalColetaDestinoId(LongFilter notaFiscalColetaDestinoId) {
        this.notaFiscalColetaDestinoId = notaFiscalColetaDestinoId;
    }

    public LongFilter getSolicitacaoColetaOrigemId() {
        return solicitacaoColetaOrigemId;
    }

    public LongFilter solicitacaoColetaOrigemId() {
        if (solicitacaoColetaOrigemId == null) {
            solicitacaoColetaOrigemId = new LongFilter();
        }
        return solicitacaoColetaOrigemId;
    }

    public void setSolicitacaoColetaOrigemId(LongFilter solicitacaoColetaOrigemId) {
        this.solicitacaoColetaOrigemId = solicitacaoColetaOrigemId;
    }

    public LongFilter getSolicitacaoColetaDestinoId() {
        return solicitacaoColetaDestinoId;
    }

    public LongFilter solicitacaoColetaDestinoId() {
        if (solicitacaoColetaDestinoId == null) {
            solicitacaoColetaDestinoId = new LongFilter();
        }
        return solicitacaoColetaDestinoId;
    }

    public void setSolicitacaoColetaDestinoId(LongFilter solicitacaoColetaDestinoId) {
        this.solicitacaoColetaDestinoId = solicitacaoColetaDestinoId;
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
        final EnderecoCriteria that = (EnderecoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(endereco, that.endereco) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(complemento, that.complemento) &&
            Objects.equals(bairro, that.bairro) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(cidadeId, that.cidadeId) &&
            Objects.equals(embarcadorId, that.embarcadorId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(notaFiscalColetaOrigemId, that.notaFiscalColetaOrigemId) &&
            Objects.equals(notaFiscalColetaDestinoId, that.notaFiscalColetaDestinoId) &&
            Objects.equals(solicitacaoColetaOrigemId, that.solicitacaoColetaOrigemId) &&
            Objects.equals(solicitacaoColetaDestinoId, that.solicitacaoColetaDestinoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipo,
            cep,
            endereco,
            numero,
            complemento,
            bairro,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            cidadeId,
            embarcadorId,
            transportadoraId,
            notaFiscalColetaOrigemId,
            notaFiscalColetaDestinoId,
            solicitacaoColetaOrigemId,
            solicitacaoColetaDestinoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnderecoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (cep != null ? "cep=" + cep + ", " : "") +
            (endereco != null ? "endereco=" + endereco + ", " : "") +
            (numero != null ? "numero=" + numero + ", " : "") +
            (complemento != null ? "complemento=" + complemento + ", " : "") +
            (bairro != null ? "bairro=" + bairro + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (cidadeId != null ? "cidadeId=" + cidadeId + ", " : "") +
            (embarcadorId != null ? "embarcadorId=" + embarcadorId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (notaFiscalColetaOrigemId != null ? "notaFiscalColetaOrigemId=" + notaFiscalColetaOrigemId + ", " : "") +
            (notaFiscalColetaDestinoId != null ? "notaFiscalColetaDestinoId=" + notaFiscalColetaDestinoId + ", " : "") +
            (solicitacaoColetaOrigemId != null ? "solicitacaoColetaOrigemId=" + solicitacaoColetaOrigemId + ", " : "") +
            (solicitacaoColetaDestinoId != null ? "solicitacaoColetaDestinoId=" + solicitacaoColetaDestinoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
