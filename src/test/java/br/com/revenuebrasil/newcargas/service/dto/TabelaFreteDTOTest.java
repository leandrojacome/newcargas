package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TabelaFreteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TabelaFreteDTO.class);
        TabelaFreteDTO tabelaFreteDTO1 = new TabelaFreteDTO();
        tabelaFreteDTO1.setId(1L);
        TabelaFreteDTO tabelaFreteDTO2 = new TabelaFreteDTO();
        assertThat(tabelaFreteDTO1).isNotEqualTo(tabelaFreteDTO2);
        tabelaFreteDTO2.setId(tabelaFreteDTO1.getId());
        assertThat(tabelaFreteDTO1).isEqualTo(tabelaFreteDTO2);
        tabelaFreteDTO2.setId(2L);
        assertThat(tabelaFreteDTO1).isNotEqualTo(tabelaFreteDTO2);
        tabelaFreteDTO1.setId(null);
        assertThat(tabelaFreteDTO1).isNotEqualTo(tabelaFreteDTO2);
    }
}
