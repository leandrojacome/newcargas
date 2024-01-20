package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FaturaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaturaDTO.class);
        FaturaDTO faturaDTO1 = new FaturaDTO();
        faturaDTO1.setId(1L);
        FaturaDTO faturaDTO2 = new FaturaDTO();
        assertThat(faturaDTO1).isNotEqualTo(faturaDTO2);
        faturaDTO2.setId(faturaDTO1.getId());
        assertThat(faturaDTO1).isEqualTo(faturaDTO2);
        faturaDTO2.setId(2L);
        assertThat(faturaDTO1).isNotEqualTo(faturaDTO2);
        faturaDTO1.setId(null);
        assertThat(faturaDTO1).isNotEqualTo(faturaDTO2);
    }
}
