package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoteirizacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoteirizacaoDTO.class);
        RoteirizacaoDTO roteirizacaoDTO1 = new RoteirizacaoDTO();
        roteirizacaoDTO1.setId(1L);
        RoteirizacaoDTO roteirizacaoDTO2 = new RoteirizacaoDTO();
        assertThat(roteirizacaoDTO1).isNotEqualTo(roteirizacaoDTO2);
        roteirizacaoDTO2.setId(roteirizacaoDTO1.getId());
        assertThat(roteirizacaoDTO1).isEqualTo(roteirizacaoDTO2);
        roteirizacaoDTO2.setId(2L);
        assertThat(roteirizacaoDTO1).isNotEqualTo(roteirizacaoDTO2);
        roteirizacaoDTO1.setId(null);
        assertThat(roteirizacaoDTO1).isNotEqualTo(roteirizacaoDTO2);
    }
}
