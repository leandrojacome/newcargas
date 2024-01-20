package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContratacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContratacaoDTO.class);
        ContratacaoDTO contratacaoDTO1 = new ContratacaoDTO();
        contratacaoDTO1.setId(1L);
        ContratacaoDTO contratacaoDTO2 = new ContratacaoDTO();
        assertThat(contratacaoDTO1).isNotEqualTo(contratacaoDTO2);
        contratacaoDTO2.setId(contratacaoDTO1.getId());
        assertThat(contratacaoDTO1).isEqualTo(contratacaoDTO2);
        contratacaoDTO2.setId(2L);
        assertThat(contratacaoDTO1).isNotEqualTo(contratacaoDTO2);
        contratacaoDTO1.setId(null);
        assertThat(contratacaoDTO1).isNotEqualTo(contratacaoDTO2);
    }
}
