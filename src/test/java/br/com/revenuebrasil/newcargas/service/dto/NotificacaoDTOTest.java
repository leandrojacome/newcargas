package br.com.revenuebrasil.newcargas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.revenuebrasil.newcargas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificacaoDTO.class);
        NotificacaoDTO notificacaoDTO1 = new NotificacaoDTO();
        notificacaoDTO1.setId(1L);
        NotificacaoDTO notificacaoDTO2 = new NotificacaoDTO();
        assertThat(notificacaoDTO1).isNotEqualTo(notificacaoDTO2);
        notificacaoDTO2.setId(notificacaoDTO1.getId());
        assertThat(notificacaoDTO1).isEqualTo(notificacaoDTO2);
        notificacaoDTO2.setId(2L);
        assertThat(notificacaoDTO1).isNotEqualTo(notificacaoDTO2);
        notificacaoDTO1.setId(null);
        assertThat(notificacaoDTO1).isNotEqualTo(notificacaoDTO2);
    }
}
