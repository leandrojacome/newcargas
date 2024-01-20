package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoVeiculoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoVeiculoDTO.class);
        TipoVeiculoDTO tipoVeiculoDTO1 = new TipoVeiculoDTO();
        tipoVeiculoDTO1.setId(1L);
        TipoVeiculoDTO tipoVeiculoDTO2 = new TipoVeiculoDTO();
        assertThat(tipoVeiculoDTO1).isNotEqualTo(tipoVeiculoDTO2);
        tipoVeiculoDTO2.setId(tipoVeiculoDTO1.getId());
        assertThat(tipoVeiculoDTO1).isEqualTo(tipoVeiculoDTO2);
        tipoVeiculoDTO2.setId(2L);
        assertThat(tipoVeiculoDTO1).isNotEqualTo(tipoVeiculoDTO2);
        tipoVeiculoDTO1.setId(null);
        assertThat(tipoVeiculoDTO1).isNotEqualTo(tipoVeiculoDTO2);
    }
}
