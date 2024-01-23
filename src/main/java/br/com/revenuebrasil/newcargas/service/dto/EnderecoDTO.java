package br.com.revenuebrasil.newcargas.service.dto;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoEndereco;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Endereco} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnderecoDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoEndereco tipo;

    @NotNull
    @Size(min = 8, max = 8)
    private String cep;

    @NotNull
    @Size(min = 2, max = 150)
    private String endereco;

    @Size(min = 1, max = 10)
    private String numero;

    @Size(min = 2, max = 150)
    private String complemento;

    @NotNull
    @Size(min = 2, max = 150)
    private String bairro;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private CidadeDTO cidade;

    private EmbarcadorDTO embarcador;

    private TransportadoraDTO transportadora;

    private NotaFiscalColetaDTO notaFiscalColetaOrigem;

    private NotaFiscalColetaDTO notaFiscalColetaDestino;

    private SolicitacaoColetaDTO solicitacaoColetaOrigem;

    private SolicitacaoColetaDTO solicitacaoColetaDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEndereco getTipo() {
        return tipo;
    }

    public void setTipo(TipoEndereco tipo) {
        this.tipo = tipo;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
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

    public CidadeDTO getCidade() {
        return cidade;
    }

    public void setCidade(CidadeDTO cidade) {
        this.cidade = cidade;
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

    public NotaFiscalColetaDTO getNotaFiscalColetaOrigem() {
        return notaFiscalColetaOrigem;
    }

    public void setNotaFiscalColetaOrigem(NotaFiscalColetaDTO notaFiscalColetaOrigem) {
        this.notaFiscalColetaOrigem = notaFiscalColetaOrigem;
    }

    public NotaFiscalColetaDTO getNotaFiscalColetaDestino() {
        return notaFiscalColetaDestino;
    }

    public void setNotaFiscalColetaDestino(NotaFiscalColetaDTO notaFiscalColetaDestino) {
        this.notaFiscalColetaDestino = notaFiscalColetaDestino;
    }

    public SolicitacaoColetaDTO getSolicitacaoColetaOrigem() {
        return solicitacaoColetaOrigem;
    }

    public void setSolicitacaoColetaOrigem(SolicitacaoColetaDTO solicitacaoColetaOrigem) {
        this.solicitacaoColetaOrigem = solicitacaoColetaOrigem;
    }

    public SolicitacaoColetaDTO getSolicitacaoColetaDestino() {
        return solicitacaoColetaDestino;
    }

    public void setSolicitacaoColetaDestino(SolicitacaoColetaDTO solicitacaoColetaDestino) {
        this.solicitacaoColetaDestino = solicitacaoColetaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnderecoDTO)) {
            return false;
        }

        EnderecoDTO enderecoDTO = (EnderecoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enderecoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnderecoDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", cep='" + getCep() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", numero='" + getNumero() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", cidade=" + getCidade() +
            ", embarcador=" + getEmbarcador() +
            ", transportadora=" + getTransportadora() +
            ", notaFiscalColetaOrigem=" + getNotaFiscalColetaOrigem() +
            ", notaFiscalColetaDestino=" + getNotaFiscalColetaDestino() +
            ", solicitacaoColetaOrigem=" + getSolicitacaoColetaOrigem() +
            ", solicitacaoColetaDestino=" + getSolicitacaoColetaDestino() +
            "}";
    }
}
