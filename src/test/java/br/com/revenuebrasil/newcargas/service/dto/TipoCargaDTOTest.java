package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoCargaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoCargaDTO.class);
        TipoCargaDTO tipoCargaDTO1 = new TipoCargaDTO();
        tipoCargaDTO1.setId(1L);
        TipoCargaDTO tipoCargaDTO2 = new TipoCargaDTO();
        assertThat(tipoCargaDTO1).isNotEqualTo(tipoCargaDTO2);
        tipoCargaDTO2.setId(tipoCargaDTO1.getId());
        assertThat(tipoCargaDTO1).isEqualTo(tipoCargaDTO2);
        tipoCargaDTO2.setId(2L);
        assertThat(tipoCargaDTO1).isNotEqualTo(tipoCargaDTO2);
        tipoCargaDTO1.setId(null);
        assertThat(tipoCargaDTO1).isNotEqualTo(tipoCargaDTO2);
    }
}
