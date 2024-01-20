package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegiaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegiaoDTO.class);
        RegiaoDTO regiaoDTO1 = new RegiaoDTO();
        regiaoDTO1.setId(1L);
        RegiaoDTO regiaoDTO2 = new RegiaoDTO();
        assertThat(regiaoDTO1).isNotEqualTo(regiaoDTO2);
        regiaoDTO2.setId(regiaoDTO1.getId());
        assertThat(regiaoDTO1).isEqualTo(regiaoDTO2);
        regiaoDTO2.setId(2L);
        assertThat(regiaoDTO1).isNotEqualTo(regiaoDTO2);
        regiaoDTO1.setId(null);
        assertThat(regiaoDTO1).isNotEqualTo(regiaoDTO2);
    }
}
