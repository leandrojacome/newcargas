package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
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

    private Boolean ativo;

    private Boolean removido;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
            ", ativo='" + getAtivo() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", statusColetaOrigems=" + getStatusColetaOrigems() +
            "}";
    }
}
