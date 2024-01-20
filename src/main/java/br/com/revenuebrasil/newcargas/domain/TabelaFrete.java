package br.com.revenuebrasil.newcargas.domain;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoTabelaFrete;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TabelaFrete.
 */
@Entity
@Table(name = "tabela_frete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TabelaFrete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTabelaFrete tipo;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "nome", length = 150, nullable = false, unique = true)
    private String nome;

    @Size(min = 2, max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "lead_time")
    private Integer leadTime;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "frete_minimo")
    private Double freteMinimo;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_tonelada")
    private Double valorTonelada;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_metro_cubico")
    private Double valorMetroCubico;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_unidade")
    private Double valorUnidade;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_km")
    private Double valorKm;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_adicional")
    private Double valorAdicional;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_coleta")
    private Double valorColeta;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_entrega")
    private Double valorEntrega;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_total")
    private Double valorTotal;

    @DecimalMin(value = "1")
    @DecimalMax(value = "10")
    @Column(name = "valor_km_adicional")
    private Double valorKmAdicional;

    @NotNull
    @Column(name = "data_cadastro", nullable = false)
    private ZonedDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private ZonedDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "cidades", "contaBancarias", "tabelaFretes", "solitacaoColetas", "notificacaos", "faturas" },
        allowSetters = true
    )
    private Embarcador embarcador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "enderecos", "cidades", "contaBancarias", "tabelaFretes", "tomadaPrecos", "contratacaos", "notificacaos", "faturas" },
        allowSetters = true
    )
    private Transportadora transportadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tabelaFretes" }, allowSetters = true)
    private TipoCarga tipoCarga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tabelaFretes" }, allowSetters = true)
    private TipoFrete tipoFrete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tabelaFretes", "fatutas" }, allowSetters = true)
    private FormaCobranca formaCobranca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tabelaFreteOrigems", "tabelaFreteDestinos" }, allowSetters = true)
    private Regiao regiaoOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tabelaFreteOrigems", "tabelaFreteDestinos" }, allowSetters = true)
    private Regiao regiaoDestino;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TabelaFrete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTabelaFrete getTipo() {
        return this.tipo;
    }

    public TabelaFrete tipo(TipoTabelaFrete tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoTabelaFrete tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return this.nome;
    }

    public TabelaFrete nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TabelaFrete descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getLeadTime() {
        return this.leadTime;
    }

    public TabelaFrete leadTime(Integer leadTime) {
        this.setLeadTime(leadTime);
        return this;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Double getFreteMinimo() {
        return this.freteMinimo;
    }

    public TabelaFrete freteMinimo(Double freteMinimo) {
        this.setFreteMinimo(freteMinimo);
        return this;
    }

    public void setFreteMinimo(Double freteMinimo) {
        this.freteMinimo = freteMinimo;
    }

    public Double getValorTonelada() {
        return this.valorTonelada;
    }

    public TabelaFrete valorTonelada(Double valorTonelada) {
        this.setValorTonelada(valorTonelada);
        return this;
    }

    public void setValorTonelada(Double valorTonelada) {
        this.valorTonelada = valorTonelada;
    }

    public Double getValorMetroCubico() {
        return this.valorMetroCubico;
    }

    public TabelaFrete valorMetroCubico(Double valorMetroCubico) {
        this.setValorMetroCubico(valorMetroCubico);
        return this;
    }

    public void setValorMetroCubico(Double valorMetroCubico) {
        this.valorMetroCubico = valorMetroCubico;
    }

    public Double getValorUnidade() {
        return this.valorUnidade;
    }

    public TabelaFrete valorUnidade(Double valorUnidade) {
        this.setValorUnidade(valorUnidade);
        return this;
    }

    public void setValorUnidade(Double valorUnidade) {
        this.valorUnidade = valorUnidade;
    }

    public Double getValorKm() {
        return this.valorKm;
    }

    public TabelaFrete valorKm(Double valorKm) {
        this.setValorKm(valorKm);
        return this;
    }

    public void setValorKm(Double valorKm) {
        this.valorKm = valorKm;
    }

    public Double getValorAdicional() {
        return this.valorAdicional;
    }

    public TabelaFrete valorAdicional(Double valorAdicional) {
        this.setValorAdicional(valorAdicional);
        return this;
    }

    public void setValorAdicional(Double valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    public Double getValorColeta() {
        return this.valorColeta;
    }

    public TabelaFrete valorColeta(Double valorColeta) {
        this.setValorColeta(valorColeta);
        return this;
    }

    public void setValorColeta(Double valorColeta) {
        this.valorColeta = valorColeta;
    }

    public Double getValorEntrega() {
        return this.valorEntrega;
    }

    public TabelaFrete valorEntrega(Double valorEntrega) {
        this.setValorEntrega(valorEntrega);
        return this;
    }

    public void setValorEntrega(Double valorEntrega) {
        this.valorEntrega = valorEntrega;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public TabelaFrete valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorKmAdicional() {
        return this.valorKmAdicional;
    }

    public TabelaFrete valorKmAdicional(Double valorKmAdicional) {
        this.setValorKmAdicional(valorKmAdicional);
        return this;
    }

    public void setValorKmAdicional(Double valorKmAdicional) {
        this.valorKmAdicional = valorKmAdicional;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public TabelaFrete dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public TabelaFrete dataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.setDataAtualizacao(dataAtualizacao);
        return this;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Embarcador getEmbarcador() {
        return this.embarcador;
    }

    public void setEmbarcador(Embarcador embarcador) {
        this.embarcador = embarcador;
    }

    public TabelaFrete embarcador(Embarcador embarcador) {
        this.setEmbarcador(embarcador);
        return this;
    }

    public Transportadora getTransportadora() {
        return this.transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public TabelaFrete transportadora(Transportadora transportadora) {
        this.setTransportadora(transportadora);
        return this;
    }

    public TipoCarga getTipoCarga() {
        return this.tipoCarga;
    }

    public void setTipoCarga(TipoCarga tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public TabelaFrete tipoCarga(TipoCarga tipoCarga) {
        this.setTipoCarga(tipoCarga);
        return this;
    }

    public TipoFrete getTipoFrete() {
        return this.tipoFrete;
    }

    public void setTipoFrete(TipoFrete tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public TabelaFrete tipoFrete(TipoFrete tipoFrete) {
        this.setTipoFrete(tipoFrete);
        return this;
    }

    public FormaCobranca getFormaCobranca() {
        return this.formaCobranca;
    }

    public void setFormaCobranca(FormaCobranca formaCobranca) {
        this.formaCobranca = formaCobranca;
    }

    public TabelaFrete formaCobranca(FormaCobranca formaCobranca) {
        this.setFormaCobranca(formaCobranca);
        return this;
    }

    public Regiao getRegiaoOrigem() {
        return this.regiaoOrigem;
    }

    public void setRegiaoOrigem(Regiao regiao) {
        this.regiaoOrigem = regiao;
    }

    public TabelaFrete regiaoOrigem(Regiao regiao) {
        this.setRegiaoOrigem(regiao);
        return this;
    }

    public Regiao getRegiaoDestino() {
        return this.regiaoDestino;
    }

    public void setRegiaoDestino(Regiao regiao) {
        this.regiaoDestino = regiao;
    }

    public TabelaFrete regiaoDestino(Regiao regiao) {
        this.setRegiaoDestino(regiao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabelaFrete)) {
            return false;
        }
        return getId() != null && getId().equals(((TabelaFrete) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TabelaFrete{" +
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
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            "}";
    }
}
