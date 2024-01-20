package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormaCobrancaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormaCobrancaDTO.class);
        FormaCobrancaDTO formaCobrancaDTO1 = new FormaCobrancaDTO();
        formaCobrancaDTO1.setId(1L);
        FormaCobrancaDTO formaCobrancaDTO2 = new FormaCobrancaDTO();
        assertThat(formaCobrancaDTO1).isNotEqualTo(formaCobrancaDTO2);
        formaCobrancaDTO2.setId(formaCobrancaDTO1.getId());
        assertThat(formaCobrancaDTO1).isEqualTo(formaCobrancaDTO2);
        formaCobrancaDTO2.setId(2L);
        assertThat(formaCobrancaDTO1).isNotEqualTo(formaCobrancaDTO2);
        formaCobrancaDTO1.setId(null);
        assertThat(formaCobrancaDTO1).isNotEqualTo(formaCobrancaDTO2);
    }
}
