package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoFreteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoFreteDTO.class);
        TipoFreteDTO tipoFreteDTO1 = new TipoFreteDTO();
        tipoFreteDTO1.setId(1L);
        TipoFreteDTO tipoFreteDTO2 = new TipoFreteDTO();
        assertThat(tipoFreteDTO1).isNotEqualTo(tipoFreteDTO2);
        tipoFreteDTO2.setId(tipoFreteDTO1.getId());
        assertThat(tipoFreteDTO1).isEqualTo(tipoFreteDTO2);
        tipoFreteDTO2.setId(2L);
        assertThat(tipoFreteDTO1).isNotEqualTo(tipoFreteDTO2);
        tipoFreteDTO1.setId(null);
        assertThat(tipoFreteDTO1).isNotEqualTo(tipoFreteDTO2);
    }
}
