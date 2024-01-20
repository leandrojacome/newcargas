package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.TipoFrete} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoFreteDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    private String nome;

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
        if (!(o instanceof TipoFreteDTO)) {
            return false;
        }

        TipoFreteDTO tipoFreteDTO = (TipoFreteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoFreteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoFreteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
