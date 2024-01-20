package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusColetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusColetaDTO.class);
        StatusColetaDTO statusColetaDTO1 = new StatusColetaDTO();
        statusColetaDTO1.setId(1L);
        StatusColetaDTO statusColetaDTO2 = new StatusColetaDTO();
        assertThat(statusColetaDTO1).isNotEqualTo(statusColetaDTO2);
        statusColetaDTO2.setId(statusColetaDTO1.getId());
        assertThat(statusColetaDTO1).isEqualTo(statusColetaDTO2);
        statusColetaDTO2.setId(2L);
        assertThat(statusColetaDTO1).isNotEqualTo(statusColetaDTO2);
        statusColetaDTO1.setId(null);
        assertThat(statusColetaDTO1).isNotEqualTo(statusColetaDTO2);
    }
}
