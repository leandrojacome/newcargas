package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmbarcadorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmbarcadorDTO.class);
        EmbarcadorDTO embarcadorDTO1 = new EmbarcadorDTO();
        embarcadorDTO1.setId(1L);
        EmbarcadorDTO embarcadorDTO2 = new EmbarcadorDTO();
        assertThat(embarcadorDTO1).isNotEqualTo(embarcadorDTO2);
        embarcadorDTO2.setId(embarcadorDTO1.getId());
        assertThat(embarcadorDTO1).isEqualTo(embarcadorDTO2);
        embarcadorDTO2.setId(2L);
        assertThat(embarcadorDTO1).isNotEqualTo(embarcadorDTO2);
        embarcadorDTO1.setId(null);
        assertThat(embarcadorDTO1).isNotEqualTo(embarcadorDTO2);
    }
}
