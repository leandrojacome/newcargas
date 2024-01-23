package br.com.revenuebrasil.newcargas.service.dto;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoTabelaFrete;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.TabelaFrete} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TabelaFreteDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoTabelaFrete tipo;

    @NotNull
    @Size(min = 2, max = 150)
    private String nome;

    @Size(min = 2, max = 500)
    private String descricao;

    @Min(value = 1)
    @Max(value = 4)
    private Integer leadTime;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double freteMinimo;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTonelada;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorMetroCubico;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorUnidade;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorKm;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorAdicional;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorColeta;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorEntrega;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorTotal;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    private Double valorKmAdicional;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private EmbarcadorDTO embarcador;

    private TransportadoraDTO transportadora;

    private TipoCargaDTO tipoCarga;

    private TipoFreteDTO tipoFrete;

    private FormaCobrancaDTO formaCobranca;

    private RegiaoDTO regiaoOrigem;

    private RegiaoDTO regiaoDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTabelaFrete getTipo() {
        return tipo;
    }

    public void setTipo(TipoTabelaFrete tipo) {
        this.tipo = tipo;
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

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Double getFreteMinimo() {
        return freteMinimo;
    }

    public void setFreteMinimo(Double freteMinimo) {
        this.freteMinimo = freteMinimo;
    }

    public Double getValorTonelada() {
        return valorTonelada;
    }

    public void setValorTonelada(Double valorTonelada) {
        this.valorTonelada = valorTonelada;
    }

    public Double getValorMetroCubico() {
        return valorMetroCubico;
    }

    public void setValorMetroCubico(Double valorMetroCubico) {
        this.valorMetroCubico = valorMetroCubico;
    }

    public Double getValorUnidade() {
        return valorUnidade;
    }

    public void setValorUnidade(Double valorUnidade) {
        this.valorUnidade = valorUnidade;
    }

    public Double getValorKm() {
        return valorKm;
    }

    public void setValorKm(Double valorKm) {
        this.valorKm = valorKm;
    }

    public Double getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(Double valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    public Double getValorColeta() {
        return valorColeta;
    }

    public void setValorColeta(Double valorColeta) {
        this.valorColeta = valorColeta;
    }

    public Double getValorEntrega() {
        return valorEntrega;
    }

    public void setValorEntrega(Double valorEntrega) {
        this.valorEntrega = valorEntrega;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorKmAdicional() {
        return valorKmAdicional;
    }

    public void setValorKmAdicional(Double valorKmAdicional) {
        this.valorKmAdicional = valorKmAdicional;
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

    public TipoCargaDTO getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(TipoCargaDTO tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public TipoFreteDTO getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(TipoFreteDTO tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public FormaCobrancaDTO getFormaCobranca() {
        return formaCobranca;
    }

    public void setFormaCobranca(FormaCobrancaDTO formaCobranca) {
        this.formaCobranca = formaCobranca;
    }

    public RegiaoDTO getRegiaoOrigem() {
        return regiaoOrigem;
    }

    public void setRegiaoOrigem(RegiaoDTO regiaoOrigem) {
        this.regiaoOrigem = regiaoOrigem;
    }

    public RegiaoDTO getRegiaoDestino() {
        return regiaoDestino;
    }

    public void setRegiaoDestino(RegiaoDTO regiaoDestino) {
        this.regiaoDestino = regiaoDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabelaFreteDTO)) {
            return false;
        }

        TabelaFreteDTO tabelaFreteDTO = (TabelaFreteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tabelaFreteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TabelaFreteDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", leadTime=" + getLeadTime() +
            ", freteMinimo=" + getFreteMinimo() +
            ", valorTonelada=" + getValorTonelada() +
            ", valorMetroCubico=" + getValorMetroCubico() +
            ", valorUnidade=" + getValorUnidade() +
            ", valorKm=" + getValorKm() +
            ", valorAdicional=" + getValorAdicional() +
            ", valorColeta=" + getValorColeta() +
            ", valorEntrega=" + getValorEntrega() +
            ", valorTotal=" + getValorTotal() +
            ", valorKmAdicional=" + getValorKmAdicional() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", embarcador=" + getEmbarcador() +
            ", transportadora=" + getTransportadora() +
            ", tipoCarga=" + getTipoCarga() +
            ", tipoFrete=" + getTipoFrete() +
            ", formaCobranca=" + getFormaCobranca() +
            ", regiaoOrigem=" + getRegiaoOrigem() +
            ", regiaoDestino=" + getRegiaoDestino() +
            "}";
    }
}
