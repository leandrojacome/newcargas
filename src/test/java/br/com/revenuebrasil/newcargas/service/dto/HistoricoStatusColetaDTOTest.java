package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricoStatusColetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricoStatusColetaDTO.class);
        HistoricoStatusColetaDTO historicoStatusColetaDTO1 = new HistoricoStatusColetaDTO();
        historicoStatusColetaDTO1.setId(1L);
        HistoricoStatusColetaDTO historicoStatusColetaDTO2 = new HistoricoStatusColetaDTO();
        assertThat(historicoStatusColetaDTO1).isNotEqualTo(historicoStatusColetaDTO2);
        historicoStatusColetaDTO2.setId(historicoStatusColetaDTO1.getId());
        assertThat(historicoStatusColetaDTO1).isEqualTo(historicoStatusColetaDTO2);
        historicoStatusColetaDTO2.setId(2L);
        assertThat(historicoStatusColetaDTO1).isNotEqualTo(historicoStatusColetaDTO2);
        historicoStatusColetaDTO1.setId(null);
        assertThat(historicoStatusColetaDTO1).isNotEqualTo(historicoStatusColetaDTO2);
    }
}
