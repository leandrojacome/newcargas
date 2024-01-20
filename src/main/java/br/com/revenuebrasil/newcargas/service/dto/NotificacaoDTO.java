package br.com.revenuebrasil.newcargas.service.dto;

import br.com.revenuebrasil.newcargas.domain.enumeration.TipoNotificacao;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.revenuebrasil.newcargas.domain.Notificacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoNotificacao tipo;

    @Size(min = 2, max = 150)
    private String email;

    @Size(min = 10, max = 11)
    private String telefone;

    @NotNull
    @Size(min = 2, max = 150)
    private String assunto;

    @NotNull
    @Size(min = 2, max = 500)
    private String mensagem;

    @NotNull
    private ZonedDateTime dataHoraEnvio;

    private ZonedDateTime dataHoraLeitura;

    @NotNull
    private ZonedDateTime dataCadastro;

    private ZonedDateTime dataAtualizacao;

    private Boolean lido;

    private ZonedDateTime dataLeitura;

    private Boolean removido;

    private ZonedDateTime dataRemocao;

    @Size(min = 2, max = 150)
    private String usuarioRemocao;

    private EmbarcadorDTO embarcador;

    private TransportadoraDTO transportadora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public ZonedDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(ZonedDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public ZonedDateTime getDataHoraLeitura() {
        return dataHoraLeitura;
    }

    public void setDataHoraLeitura(ZonedDateTime dataHoraLeitura) {
        this.dataHoraLeitura = dataHoraLeitura;
    }

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public ZonedDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(ZonedDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getLido() {
        return lido;
    }

    public void setLido(Boolean lido) {
        this.lido = lido;
    }

    public ZonedDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(ZonedDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
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
        if (!(o instanceof NotificacaoDTO)) {
            return false;
        }

        NotificacaoDTO notificacaoDTO = (NotificacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacaoDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", assunto='" + getAssunto() + "'" +
            ", mensagem='" + getMensagem() + "'" +
            ", dataHoraEnvio='" + getDataHoraEnvio() + "'" +
            ", dataHoraLeitura='" + getDataHoraLeitura() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataAtualizacao='" + getDataAtualizacao() + "'" +
            ", lido='" + getLido() + "'" +
            ", dataLeitura='" + getDataLeitura() + "'" +
            ", removido='" + getRemovido() + "'" +
            ", dataRemocao='" + getDataRemocao() + "'" +
            ", usuarioRemocao='" + getUsuarioRemocao() + "'" +
            ", embarcador=" + getEmbarcador() +
            ", transportadora=" + getTransportadora() +
            "}";
    }
}
