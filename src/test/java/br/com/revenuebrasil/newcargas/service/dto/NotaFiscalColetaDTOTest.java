package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotaFiscalColetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotaFiscalColetaDTO.class);
        NotaFiscalColetaDTO notaFiscalColetaDTO1 = new NotaFiscalColetaDTO();
        notaFiscalColetaDTO1.setId(1L);
        NotaFiscalColetaDTO notaFiscalColetaDTO2 = new NotaFiscalColetaDTO();
        assertThat(notaFiscalColetaDTO1).isNotEqualTo(notaFiscalColetaDTO2);
        notaFiscalColetaDTO2.setId(notaFiscalColetaDTO1.getId());
        assertThat(notaFiscalColetaDTO1).isEqualTo(notaFiscalColetaDTO2);
        notaFiscalColetaDTO2.setId(2L);
        assertThat(notaFiscalColetaDTO1).isNotEqualTo(notaFiscalColetaDTO2);
        notaFiscalColetaDTO1.setId(null);
        assertThat(notaFiscalColetaDTO1).isNotEqualTo(notaFiscalColetaDTO2);
    }
}
