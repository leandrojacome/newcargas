package br.com.revenuebrasil.newcargas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.revenuebrasil.newcargas.domain.Embarcador} entity. This class is used
 * in {@link br.com.revenuebrasil.newcargas.web.rest.EmbarcadorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /embarcadors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmbarcadorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cnpj;

    private StringFilter razaoSocial;

    private StringFilter inscricaoEstadual;

    private StringFilter inscricaoMunicipal;

    private StringFilter responsavel;

    private StringFilter cep;

    private StringFilter endereco;

    private StringFilter numero;

    private StringFilter complemento;

    private StringFilter bairro;

    private StringFilter telefone;

    private StringFilter email;

    private StringFilter observacao;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter enderecoId;

    private LongFilter contaBancariaId;

    private LongFilter tabelaFreteId;

    private LongFilter solitacaoColetaId;

    private LongFilter notificacaoId;

    private LongFilter faturaId;

    private LongFilter cidadeId;

    private Boolean distinct;

    public EmbarcadorCriteria() {}

    public EmbarcadorCriteria(EmbarcadorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.razaoSocial = other.razaoSocial == null ? null : other.razaoSocial.copy();
        this.inscricaoEstadual = other.inscricaoEstadual == null ? null : other.inscricaoEstadual.copy();
        this.inscricaoMunicipal = other.inscricaoMunicipal == null ? null : other.inscricaoMunicipal.copy();
        this.responsavel = other.responsavel == null ? null : other.responsavel.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.endereco = other.endereco == null ? null : other.endereco.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.complemento = other.complemento == null ? null : other.complemento.copy();
        this.bairro = other.bairro == null ? null : other.bairro.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.enderecoId = other.enderecoId == null ? null : other.enderecoId.copy();
        this.contaBancariaId = other.contaBancariaId == null ? null : other.contaBancariaId.copy();
        this.tabelaFreteId = other.tabelaFreteId == null ? null : other.tabelaFreteId.copy();
        this.solitacaoColetaId = other.solitacaoColetaId == null ? null : other.solitacaoColetaId.copy();
        this.notificacaoId = other.notificacaoId == null ? null : other.notificacaoId.copy();
        this.faturaId = other.faturaId == null ? null : other.faturaId.copy();
        this.cidadeId = other.cidadeId == null ? null : other.cidadeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmbarcadorCriteria copy() {
        return new EmbarcadorCriteria(this);
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

    public StringFilter getCnpj() {
        return cnpj;
    }

    public StringFilter cnpj() {
        if (cnpj == null) {
            cnpj = new StringFilter();
        }
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getRazaoSocial() {
        return razaoSocial;
    }

    public StringFilter razaoSocial() {
        if (razaoSocial == null) {
            razaoSocial = new StringFilter();
        }
        return razaoSocial;
    }

    public void setRazaoSocial(StringFilter razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public StringFilter getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public StringFilter inscricaoEstadual() {
        if (inscricaoEstadual == null) {
            inscricaoEstadual = new StringFilter();
        }
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(StringFilter inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public StringFilter getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public StringFilter inscricaoMunicipal() {
        if (inscricaoMunicipal == null) {
            inscricaoMunicipal = new StringFilter();
        }
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(StringFilter inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public StringFilter getResponsavel() {
        return responsavel;
    }

    public StringFilter responsavel() {
        if (responsavel == null) {
            responsavel = new StringFilter();
        }
        return responsavel;
    }

    public void setResponsavel(StringFilter responsavel) {
        this.responsavel = responsavel;
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

    public LongFilter getEnderecoId() {
        return enderecoId;
    }

    public LongFilter enderecoId() {
        if (enderecoId == null) {
            enderecoId = new LongFilter();
        }
        return enderecoId;
    }

    public void setEnderecoId(LongFilter enderecoId) {
        this.enderecoId = enderecoId;
    }

    public LongFilter getContaBancariaId() {
        return contaBancariaId;
    }

    public LongFilter contaBancariaId() {
        if (contaBancariaId == null) {
            contaBancariaId = new LongFilter();
        }
        return contaBancariaId;
    }

    public void setContaBancariaId(LongFilter contaBancariaId) {
        this.contaBancariaId = contaBancariaId;
    }

    public LongFilter getTabelaFreteId() {
        return tabelaFreteId;
    }

    public LongFilter tabelaFreteId() {
        if (tabelaFreteId == null) {
            tabelaFreteId = new LongFilter();
        }
        return tabelaFreteId;
    }

    public void setTabelaFreteId(LongFilter tabelaFreteId) {
        this.tabelaFreteId = tabelaFreteId;
    }

    public LongFilter getSolitacaoColetaId() {
        return solitacaoColetaId;
    }

    public LongFilter solitacaoColetaId() {
        if (solitacaoColetaId == null) {
            solitacaoColetaId = new LongFilter();
        }
        return solitacaoColetaId;
    }

    public void setSolitacaoColetaId(LongFilter solitacaoColetaId) {
        this.solitacaoColetaId = solitacaoColetaId;
    }

    public LongFilter getNotificacaoId() {
        return notificacaoId;
    }

    public LongFilter notificacaoId() {
        if (notificacaoId == null) {
            notificacaoId = new LongFilter();
        }
        return notificacaoId;
    }

    public void setNotificacaoId(LongFilter notificacaoId) {
        this.notificacaoId = notificacaoId;
    }

    public LongFilter getFaturaId() {
        return faturaId;
    }

    public LongFilter faturaId() {
        if (faturaId == null) {
            faturaId = new LongFilter();
        }
        return faturaId;
    }

    public void setFaturaId(LongFilter faturaId) {
        this.faturaId = faturaId;
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
        final EmbarcadorCriteria that = (EmbarcadorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(razaoSocial, that.razaoSocial) &&
            Objects.equals(inscricaoEstadual, that.inscricaoEstadual) &&
            Objects.equals(inscricaoMunicipal, that.inscricaoMunicipal) &&
            Objects.equals(responsavel, that.responsavel) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(endereco, that.endereco) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(complemento, that.complemento) &&
            Objects.equals(bairro, that.bairro) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(enderecoId, that.enderecoId) &&
            Objects.equals(contaBancariaId, that.contaBancariaId) &&
            Objects.equals(tabelaFreteId, that.tabelaFreteId) &&
            Objects.equals(solitacaoColetaId, that.solitacaoColetaId) &&
            Objects.equals(notificacaoId, that.notificacaoId) &&
            Objects.equals(faturaId, that.faturaId) &&
            Objects.equals(cidadeId, that.cidadeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            cnpj,
            razaoSocial,
            inscricaoEstadual,
            inscricaoMunicipal,
            responsavel,
            cep,
            endereco,
            numero,
            complemento,
            bairro,
            telefone,
            email,
            observacao,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            enderecoId,
            contaBancariaId,
            tabelaFreteId,
            solitacaoColetaId,
            notificacaoId,
            faturaId,
            cidadeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmbarcadorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
            (razaoSocial != null ? "razaoSocial=" + razaoSocial + ", " : "") +
            (inscricaoEstadual != null ? "inscricaoEstadual=" + inscricaoEstadual + ", " : "") +
            (inscricaoMunicipal != null ? "inscricaoMunicipal=" + inscricaoMunicipal + ", " : "") +
            (responsavel != null ? "responsavel=" + responsavel + ", " : "") +
            (cep != null ? "cep=" + cep + ", " : "") +
            (endereco != null ? "endereco=" + endereco + ", " : "") +
            (numero != null ? "numero=" + numero + ", " : "") +
            (complemento != null ? "complemento=" + complemento + ", " : "") +
            (bairro != null ? "bairro=" + bairro + ", " : "") +
            (telefone != null ? "telefone=" + telefone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (enderecoId != null ? "enderecoId=" + enderecoId + ", " : "") +
            (contaBancariaId != null ? "contaBancariaId=" + contaBancariaId + ", " : "") +
            (tabelaFreteId != null ? "tabelaFreteId=" + tabelaFreteId + ", " : "") +
            (solitacaoColetaId != null ? "solitacaoColetaId=" + solitacaoColetaId + ", " : "") +
            (notificacaoId != null ? "notificacaoId=" + notificacaoId + ", " : "") +
            (faturaId != null ? "faturaId=" + faturaId + ", " : "") +
            (cidadeId != null ? "cidadeId=" + cidadeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
