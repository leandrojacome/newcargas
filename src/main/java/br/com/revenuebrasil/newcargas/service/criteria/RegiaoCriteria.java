package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Regiao} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.RegiaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /regiaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegiaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter sigla;

    private StringFilter descricao;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter tabelaFreteOrigemId;

    private LongFilter tabelaFreteDestinoId;

    private Boolean distinct;

    public RegiaoCriteria() {}

    public RegiaoCriteria(RegiaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.sigla = other.sigla == null ? null : other.sigla.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.tabelaFreteOrigemId = other.tabelaFreteOrigemId == null ? null : other.tabelaFreteOrigemId.copy();
        this.tabelaFreteDestinoId = other.tabelaFreteDestinoId == null ? null : other.tabelaFreteDestinoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RegiaoCriteria copy() {
        return new RegiaoCriteria(this);
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

    public StringFilter getSigla() {
        return sigla;
    }

    public StringFilter sigla() {
        if (sigla == null) {
            sigla = new StringFilter();
        }
        return sigla;
    }

    public void setSigla(StringFilter sigla) {
        this.sigla = sigla;
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

    public LongFilter getTabelaFreteOrigemId() {
        return tabelaFreteOrigemId;
    }

    public LongFilter tabelaFreteOrigemId() {
        if (tabelaFreteOrigemId == null) {
            tabelaFreteOrigemId = new LongFilter();
        }
        return tabelaFreteOrigemId;
    }

    public void setTabelaFreteOrigemId(LongFilter tabelaFreteOrigemId) {
        this.tabelaFreteOrigemId = tabelaFreteOrigemId;
    }

    public LongFilter getTabelaFreteDestinoId() {
        return tabelaFreteDestinoId;
    }

    public LongFilter tabelaFreteDestinoId() {
        if (tabelaFreteDestinoId == null) {
            tabelaFreteDestinoId = new LongFilter();
        }
        return tabelaFreteDestinoId;
    }

    public void setTabelaFreteDestinoId(LongFilter tabelaFreteDestinoId) {
        this.tabelaFreteDestinoId = tabelaFreteDestinoId;
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
        final RegiaoCriteria that = (RegiaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(sigla, that.sigla) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(tabelaFreteOrigemId, that.tabelaFreteOrigemId) &&
            Objects.equals(tabelaFreteDestinoId, that.tabelaFreteDestinoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            sigla,
            descricao,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            tabelaFreteOrigemId,
            tabelaFreteDestinoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegiaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (sigla != null ? "sigla=" + sigla + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (tabelaFreteOrigemId != null ? "tabelaFreteOrigemId=" + tabelaFreteOrigemId + ", " : "") +
            (tabelaFreteDestinoId != null ? "tabelaFreteDestinoId=" + tabelaFreteDestinoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
