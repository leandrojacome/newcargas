package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolicitacaoColetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoColetaDTO.class);
        SolicitacaoColetaDTO solicitacaoColetaDTO1 = new SolicitacaoColetaDTO();
        solicitacaoColetaDTO1.setId(1L);
        SolicitacaoColetaDTO solicitacaoColetaDTO2 = new SolicitacaoColetaDTO();
        assertThat(solicitacaoColetaDTO1).isNotEqualTo(solicitacaoColetaDTO2);
        solicitacaoColetaDTO2.setId(solicitacaoColetaDTO1.getId());
        assertThat(solicitacaoColetaDTO1).isEqualTo(solicitacaoColetaDTO2);
        solicitacaoColetaDTO2.setId(2L);
        assertThat(solicitacaoColetaDTO1).isNotEqualTo(solicitacaoColetaDTO2);
        solicitacaoColetaDTO1.setId(null);
        assertThat(solicitacaoColetaDTO1).isNotEqualTo(solicitacaoColetaDTO2);
    }
}
