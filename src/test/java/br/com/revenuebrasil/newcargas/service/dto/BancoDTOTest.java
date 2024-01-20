package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BancoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BancoDTO.class);
        BancoDTO bancoDTO1 = new BancoDTO();
        bancoDTO1.setId(1L);
        BancoDTO bancoDTO2 = new BancoDTO();
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
        bancoDTO2.setId(bancoDTO1.getId());
        assertThat(bancoDTO1).isEqualTo(bancoDTO2);
        bancoDTO2.setId(2L);
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
        bancoDTO1.setId(null);
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
    }
}
