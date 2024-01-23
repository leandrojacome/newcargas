package br.com.revenuebrasil.newcargas.service.criteria;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoNotificacao;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Notificacao} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.NotificacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notificacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoNotificacao
     */
    public static class TipoNotificacaoFilter extends Filter<TipoNotificacao> {

        public TipoNotificacaoFilter() {}

        public TipoNotificacaoFilter(TipoNotificacaoFilter filter) {
            super(filter);
        }

        @Override
        public TipoNotificacaoFilter copy() {
            return new TipoNotificacaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoNotificacaoFilter tipo;

    private StringFilter email;

    private StringFilter telefone;

    private StringFilter assunto;

    private StringFilter mensagem;

    private ZonedDateTimeFilter dataHoraEnvio;

    private ZonedDateTimeFilter dataHoraLeitura;

    private BooleanFilter lido;

    private ZonedDateTimeFilter dataLeitura;

    private BooleanFilter removido;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter embarcadorId;

    private LongFilter transportadoraId;

    private Boolean distinct;

    public NotificacaoCriteria() {}

    public NotificacaoCriteria(NotificacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.assunto = other.assunto == null ? null : other.assunto.copy();
        this.mensagem = other.mensagem == null ? null : other.mensagem.copy();
        this.dataHoraEnvio = other.dataHoraEnvio == null ? null : other.dataHoraEnvio.copy();
        this.dataHoraLeitura = other.dataHoraLeitura == null ? null : other.dataHoraLeitura.copy();
        this.lido = other.lido == null ? null : other.lido.copy();
        this.dataLeitura = other.dataLeitura == null ? null : other.dataLeitura.copy();
        this.removido = other.removido == null ? null : other.removido.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.embarcadorId = other.embarcadorId == null ? null : other.embarcadorId.copy();
        this.transportadoraId = other.transportadoraId == null ? null : other.transportadoraId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificacaoCriteria copy() {
        return new NotificacaoCriteria(this);
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

    public TipoNotificacaoFilter getTipo() {
        return tipo;
    }

    public TipoNotificacaoFilter tipo() {
        if (tipo == null) {
            tipo = new TipoNotificacaoFilter();
        }
        return tipo;
    }

    public void setTipo(TipoNotificacaoFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public StringFilter telefone() {
        if (telefone == null) {
            telefone = new StringFilter();
        }
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getAssunto() {
        return assunto;
    }

    public StringFilter assunto() {
        if (assunto == null) {
            assunto = new StringFilter();
        }
        return assunto;
    }

    public void setAssunto(StringFilter assunto) {
        this.assunto = assunto;
    }

    public StringFilter getMensagem() {
        return mensagem;
    }

    public StringFilter mensagem() {
        if (mensagem == null) {
            mensagem = new StringFilter();
        }
        return mensagem;
    }

    public void setMensagem(StringFilter mensagem) {
        this.mensagem = mensagem;
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

    public ZonedDateTimeFilter getDataHoraLeitura() {
        return dataHoraLeitura;
    }

    public ZonedDateTimeFilter dataHoraLeitura() {
        if (dataHoraLeitura == null) {
            dataHoraLeitura = new ZonedDateTimeFilter();
        }
        return dataHoraLeitura;
    }

    public void setDataHoraLeitura(ZonedDateTimeFilter dataHoraLeitura) {
        this.dataHoraLeitura = dataHoraLeitura;
    }

    public BooleanFilter getLido() {
        return lido;
    }

    public BooleanFilter lido() {
        if (lido == null) {
            lido = new BooleanFilter();
        }
        return lido;
    }

    public void setLido(BooleanFilter lido) {
        this.lido = lido;
    }

    public ZonedDateTimeFilter getDataLeitura() {
        return dataLeitura;
    }

    public ZonedDateTimeFilter dataLeitura() {
        if (dataLeitura == null) {
            dataLeitura = new ZonedDateTimeFilter();
        }
        return dataLeitura;
    }

    public void setDataLeitura(ZonedDateTimeFilter dataLeitura) {
        this.dataLeitura = dataLeitura;
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
        final NotificacaoCriteria that = (NotificacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(assunto, that.assunto) &&
            Objects.equals(mensagem, that.mensagem) &&
            Objects.equals(dataHoraEnvio, that.dataHoraEnvio) &&
            Objects.equals(dataHoraLeitura, that.dataHoraLeitura) &&
            Objects.equals(lido, that.lido) &&
            Objects.equals(dataLeitura, that.dataLeitura) &&
            Objects.equals(removido, that.removido) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(embarcadorId, that.embarcadorId) &&
            Objects.equals(transportadoraId, that.transportadoraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipo,
            email,
            telefone,
            assunto,
            mensagem,
            dataHoraEnvio,
            dataHoraLeitura,
            lido,
            dataLeitura,
            removido,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            embarcadorId,
            transportadoraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (telefone != null ? "telefone=" + telefone + ", " : "") +
            (assunto != null ? "assunto=" + assunto + ", " : "") +
            (mensagem != null ? "mensagem=" + mensagem + ", " : "") +
            (dataHoraEnvio != null ? "dataHoraEnvio=" + dataHoraEnvio + ", " : "") +
            (dataHoraLeitura != null ? "dataHoraLeitura=" + dataHoraLeitura + ", " : "") +
            (lido != null ? "lido=" + lido + ", " : "") +
            (dataLeitura != null ? "dataLeitura=" + dataLeitura + ", " : "") +
            (removido != null ? "removido=" + removido + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (embarcadorId != null ? "embarcadorId=" + embarcadorId + ", " : "") +
            (transportadoraId != null ? "transportadoraId=" + transportadoraId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
