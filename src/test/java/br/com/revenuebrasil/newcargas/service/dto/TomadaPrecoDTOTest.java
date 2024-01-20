package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TomadaPrecoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TomadaPrecoDTO.class);
        TomadaPrecoDTO tomadaPrecoDTO1 = new TomadaPrecoDTO();
        tomadaPrecoDTO1.setId(1L);
        TomadaPrecoDTO tomadaPrecoDTO2 = new TomadaPrecoDTO();
        assertThat(tomadaPrecoDTO1).isNotEqualTo(tomadaPrecoDTO2);
        tomadaPrecoDTO2.setId(tomadaPrecoDTO1.getId());
        assertThat(tomadaPrecoDTO1).isEqualTo(tomadaPrecoDTO2);
        tomadaPrecoDTO2.setId(2L);
        assertThat(tomadaPrecoDTO1).isNotEqualTo(tomadaPrecoDTO2);
        tomadaPrecoDTO1.setId(null);
        assertThat(tomadaPrecoDTO1).isNotEqualTo(tomadaPrecoDTO2);
    }
}
