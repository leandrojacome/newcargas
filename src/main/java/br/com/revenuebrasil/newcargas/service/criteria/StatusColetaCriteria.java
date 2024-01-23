package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.StatusColeta} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.StatusColetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /status-coletas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusColetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cor;

    private IntegerFilter ordem;

    private BooleanFilter estadoInicial;

    private BooleanFilter estadoFinal;

    private BooleanFilter permiteCancelar;

    private BooleanFilter permiteEditar;

    private BooleanFilter permiteExcluir;

    private StringFilter descricao;

    private BooleanFilter ativo;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter solicitacaoColetaId;

    private LongFilter historicoStatusColetaOrigemId;

    private LongFilter historicoStatusColetaDestinoId;

    private LongFilter roteirizacaoId;

    private LongFilter statusColetaOrigemId;

    private LongFilter statusColetaDestinoId;

    private Boolean distinct;

    public StatusColetaCriteria() {}

    public StatusColetaCriteria(StatusColetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cor = other.cor == null ? null : other.cor.copy();
        this.ordem = other.ordem == null ? null : other.ordem.copy();
        this.estadoInicial = other.estadoInicial == null ? null : other.estadoInicial.copy();
        this.estadoFinal = other.estadoFinal == null ? null : other.estadoFinal.copy();
        this.permiteCancelar = other.permiteCancelar == null ? null : other.permiteCancelar.copy();
        this.permiteEditar = other.permiteEditar == null ? null : other.permiteEditar.copy();
        this.permiteExcluir = other.permiteExcluir == null ? null : other.permiteExcluir.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.solicitacaoColetaId = other.solicitacaoColetaId == null ? null : other.solicitacaoColetaId.copy();
        this.historicoStatusColetaOrigemId =
            other.historicoStatusColetaOrigemId == null ? null : other.historicoStatusColetaOrigemId.copy();
        this.historicoStatusColetaDestinoId =
            other.historicoStatusColetaDestinoId == null ? null : other.historicoStatusColetaDestinoId.copy();
        this.roteirizacaoId = other.roteirizacaoId == null ? null : other.roteirizacaoId.copy();
        this.statusColetaOrigemId = other.statusColetaOrigemId == null ? null : other.statusColetaOrigemId.copy();
        this.statusColetaDestinoId = other.statusColetaDestinoId == null ? null : other.statusColetaDestinoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StatusColetaCriteria copy() {
        return new StatusColetaCriteria(this);
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

    public StringFilter getCor() {
        return cor;
    }

    public StringFilter cor() {
        if (cor == null) {
            cor = new StringFilter();
        }
        return cor;
    }

    public void setCor(StringFilter cor) {
        this.cor = cor;
    }

    public IntegerFilter getOrdem() {
        return ordem;
    }

    public IntegerFilter ordem() {
        if (ordem == null) {
            ordem = new IntegerFilter();
        }
        return ordem;
    }

    public void setOrdem(IntegerFilter ordem) {
        this.ordem = ordem;
    }

    public BooleanFilter getEstadoInicial() {
        return estadoInicial;
    }

    public BooleanFilter estadoInicial() {
        if (estadoInicial == null) {
            estadoInicial = new BooleanFilter();
        }
        return estadoInicial;
    }

    public void setEstadoInicial(BooleanFilter estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public BooleanFilter getEstadoFinal() {
        return estadoFinal;
    }

    public BooleanFilter estadoFinal() {
        if (estadoFinal == null) {
            estadoFinal = new BooleanFilter();
        }
        return estadoFinal;
    }

    public void setEstadoFinal(BooleanFilter estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public BooleanFilter getPermiteCancelar() {
        return permiteCancelar;
    }

    public BooleanFilter permiteCancelar() {
        if (permiteCancelar == null) {
            permiteCancelar = new BooleanFilter();
        }
        return permiteCancelar;
    }

    public void setPermiteCancelar(BooleanFilter permiteCancelar) {
        this.permiteCancelar = permiteCancelar;
    }

    public BooleanFilter getPermiteEditar() {
        return permiteEditar;
    }

    public BooleanFilter permiteEditar() {
        if (permiteEditar == null) {
            permiteEditar = new BooleanFilter();
        }
        return permiteEditar;
    }

    public void setPermiteEditar(BooleanFilter permiteEditar) {
        this.permiteEditar = permiteEditar;
    }

    public BooleanFilter getPermiteExcluir() {
        return permiteExcluir;
    }

    public BooleanFilter permiteExcluir() {
        if (permiteExcluir == null) {
            permiteExcluir = new BooleanFilter();
        }
        return permiteExcluir;
    }

    public void setPermiteExcluir(BooleanFilter permiteExcluir) {
        this.permiteExcluir = permiteExcluir;
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

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            ativo = new BooleanFilter();
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
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

    public LongFilter getHistoricoStatusColetaOrigemId() {
        return historicoStatusColetaOrigemId;
    }

    public LongFilter historicoStatusColetaOrigemId() {
        if (historicoStatusColetaOrigemId == null) {
            historicoStatusColetaOrigemId = new LongFilter();
        }
        return historicoStatusColetaOrigemId;
    }

    public void setHistoricoStatusColetaOrigemId(LongFilter historicoStatusColetaOrigemId) {
        this.historicoStatusColetaOrigemId = historicoStatusColetaOrigemId;
    }

    public LongFilter getHistoricoStatusColetaDestinoId() {
        return historicoStatusColetaDestinoId;
    }

    public LongFilter historicoStatusColetaDestinoId() {
        if (historicoStatusColetaDestinoId == null) {
            historicoStatusColetaDestinoId = new LongFilter();
        }
        return historicoStatusColetaDestinoId;
    }

    public void setHistoricoStatusColetaDestinoId(LongFilter historicoStatusColetaDestinoId) {
        this.historicoStatusColetaDestinoId = historicoStatusColetaDestinoId;
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
        final StatusColetaCriteria that = (StatusColetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cor, that.cor) &&
            Objects.equals(ordem, that.ordem) &&
            Objects.equals(estadoInicial, that.estadoInicial) &&
            Objects.equals(estadoFinal, that.estadoFinal) &&
            Objects.equals(permiteCancelar, that.permiteCancelar) &&
            Objects.equals(permiteEditar, that.permiteEditar) &&
            Objects.equals(permiteExcluir, that.permiteExcluir) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(solicitacaoColetaId, that.solicitacaoColetaId) &&
            Objects.equals(historicoStatusColetaOrigemId, that.historicoStatusColetaOrigemId) &&
            Objects.equals(historicoStatusColetaDestinoId, that.historicoStatusColetaDestinoId) &&
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
            nome,
            cor,
            ordem,
            estadoInicial,
            estadoFinal,
            permiteCancelar,
            permiteEditar,
            permiteExcluir,
            descricao,
            ativo,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            solicitacaoColetaId,
            historicoStatusColetaOrigemId,
            historicoStatusColetaDestinoId,
            roteirizacaoId,
            statusColetaOrigemId,
            statusColetaDestinoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusColetaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cor != null ? "cor=" + cor + ", " : "") +
            (ordem != null ? "ordem=" + ordem + ", " : "") +
            (estadoInicial != null ? "estadoInicial=" + estadoInicial + ", " : "") +
            (estadoFinal != null ? "estadoFinal=" + estadoFinal + ", " : "") +
            (permiteCancelar != null ? "permiteCancelar=" + permiteCancelar + ", " : "") +
            (permiteEditar != null ? "permiteEditar=" + permiteEditar + ", " : "") +
            (permiteExcluir != null ? "permiteExcluir=" + permiteExcluir + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (ativo != null ? "ativo=" + ativo + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (solicitacaoColetaId != null ? "solicitacaoColetaId=" + solicitacaoColetaId + ", " : "") +
            (historicoStatusColetaOrigemId != null ? "historicoStatusColetaOrigemId=" + historicoStatusColetaOrigemId + ", " : "") +
            (historicoStatusColetaDestinoId != null ? "historicoStatusColetaDestinoId=" + historicoStatusColetaDestinoId + ", " : "") +
            (roteirizacaoId != null ? "roteirizacaoId=" + roteirizacaoId + ", " : "") +
            (statusColetaOrigemId != null ? "statusColetaOrigemId=" + statusColetaOrigemId + ", " : "") +
            (statusColetaDestinoId != null ? "statusColetaDestinoId=" + statusColetaDestinoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
