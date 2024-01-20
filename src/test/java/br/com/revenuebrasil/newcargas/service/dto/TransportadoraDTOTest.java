package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportadoraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportadoraDTO.class);
        TransportadoraDTO transportadoraDTO1 = new TransportadoraDTO();
        transportadoraDTO1.setId(1L);
        TransportadoraDTO transportadoraDTO2 = new TransportadoraDTO();
        assertThat(transportadoraDTO1).isNotEqualTo(transportadoraDTO2);
        transportadoraDTO2.setId(transportadoraDTO1.getId());
        assertThat(transportadoraDTO1).isEqualTo(transportadoraDTO2);
        transportadoraDTO2.setId(2L);
        assertThat(transportadoraDTO1).isNotEqualTo(transportadoraDTO2);
        transportadoraDTO1.setId(null);
        assertThat(transportadoraDTO1).isNotEqualTo(transportadoraDTO2);
    }
}
