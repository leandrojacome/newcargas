package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaBancariaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaBancariaDTO.class);
        ContaBancariaDTO contaBancariaDTO1 = new ContaBancariaDTO();
        contaBancariaDTO1.setId(1L);
        ContaBancariaDTO contaBancariaDTO2 = new ContaBancariaDTO();
        assertThat(contaBancariaDTO1).isNotEqualTo(contaBancariaDTO2);
        contaBancariaDTO2.setId(contaBancariaDTO1.getId());
        assertThat(contaBancariaDTO1).isEqualTo(contaBancariaDTO2);
        contaBancariaDTO2.setId(2L);
        assertThat(contaBancariaDTO1).isNotEqualTo(contaBancariaDTO2);
        contaBancariaDTO1.setId(null);
        assertThat(contaBancariaDTO1).isNotEqualTo(contaBancariaDTO2);
    }
}
