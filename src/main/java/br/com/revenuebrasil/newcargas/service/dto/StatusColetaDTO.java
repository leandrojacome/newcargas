package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.StatusColeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusColetaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    private String nome;

    @Size(min = 2, max = 8)
    private String cor;

    @Min(value = 1)
    @Max(value = 4)
    private Integer ordem;

    private Boolean estadoInicial;

    private Boolean estadoFinal;

    private Boolean permiteCancelar;

    private Boolean permiteEditar;

    private Boolean permiteExcluir;

    @Size(min = 2, max = 500)
    private String descricao;

    @NotNull
    private ZonedDateTime dataCadastro;

    @Size(min = 2, max = 150)
    private String usuarioCadastro;

    private ZonedDateTime dataAtualizacao;

    @Size(min = 2, max = 150)
    private String usuarioAtualizacao;

    private Boolean ativo;

    private Boolean removido;

    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    private String usuarioRemocao;

    private Set<StatusColetaDTO> statusColetaOrigems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public Boolean getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(Boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public Boolean getPermiteCancelar() {
        return permiteCancelar;
    }

    public void setPermiteCancelar(Boolean permiteCancelar) {
        this.permiteCancelar = permiteCancelar;
    }

    public Boolean getPermiteEditar() {
        return permiteEditar;
    }

    public void setPermiteEditar(Boolean permiteEditar) {
        this.permiteEditar = permiteEditar;
    }

    public Boolean getPermiteExcluir() {
        return permiteExcluir;
    }

    public void setPermiteExcluir(Boolean permiteExcluir) {
        this.permiteExcluir = permiteExcluir;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioAtualizacao() {
        return usuarioAtualizacao;
    }

    public void setUsuarioAtualizacao(String usuarioAtualizacao) {
        this.usuarioAtualizacao = usuarioAtualizacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getRemovido() {
        return removido;
    }

    public void setRemovido(Boolean removido) {
        this.removido = removido;
    }

    public ZonedDateTime getDataRemocao() {
        return dataRemocao;
    }

    public void setDataRemocao(ZonedDateTime dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getUsuarioRemocao() {
        return usuarioRemocao;
    }

    public void setUsuarioRemocao(String usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public Set<StatusColetaDTO> getStatusColetaOrigems() {
        return statusColetaOrigems;
    }

    public void setStatusColetaOrigems(Set<StatusColetaDTO> statusColetaOrigems) {
        this.statusColetaOrigems = statusColetaOrigems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusColetaDTO)) {
            return false;
        }

        StatusColetaDTO statusColetaDTO = (StatusColetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, statusColetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusColetaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cor='" + getCor() + "'" +
            ", ordem=" + getOrdem() +
            ", estadoInicial='" + getEstadoInicial() + "'" +
            ", estadoFinal='" + getEstadoFinal() + "'" +
            ", permiteCancelar='" + getPermiteCancelar() + "'" +
            ", permiteEditar='" + getPermiteEditar() + "'" +
            ", permiteExcluir='" + getPermiteExcluir() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuarioCadastro='" + getUsuarioCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", usuarioAtualizacao='" + getUsuarioAtualizacao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            ", statusColetaOrigems=" + getStatusColetaOrigems() +
            "}";
    }
}
