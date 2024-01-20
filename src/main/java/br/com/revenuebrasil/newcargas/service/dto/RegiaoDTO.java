package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Regiao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegiaoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    private String nome;

    @Size(min = 2, max = 5)
    private String sigla;

    @Size(min = 2, max = 500)
    private String descricao;

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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegiaoDTO)) {
            return false;
        }

        RegiaoDTO regiaoDTO = (RegiaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, regiaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegiaoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sigla='" + getSigla() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
