package br.com.revenuebrasil.newcargas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Cidade} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CidadeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    private String nome;

    @Min(value = 7)
    @Max(value = 7)
    private Integer codigoIbge;

    private EstadoDTO estado;

    private EmbarcadorDTO embarcador;

    private TransportadoraDTO transportadora;

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

    public Integer getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(Integer codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public EstadoDTO getEstado() {
        return estado;
    }

    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }

    public EmbarcadorDTO getEmbarcador() {
        return embarcador;
    }

    public void setEmbarcador(EmbarcadorDTO embarcador) {
        this.embarcador = embarcador;
    }

    public TransportadoraDTO getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(TransportadoraDTO transportadora) {
        this.transportadora = transportadora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CidadeDTO)) {
            return false;
        }

        CidadeDTO cidadeDTO = (CidadeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cidadeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CidadeDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigoIbge=" + getCodigoIbge() +
            ", estado=" + getEstado() +
            ", embarcador=" + getEmbarcador() +
            ", transportadora=" + getTransportadora() +
            "}";
    }
}
