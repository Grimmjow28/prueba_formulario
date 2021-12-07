package com.prueba_formualario.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prueba_formualario.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RespuestaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RespuestaDTO.class);
        RespuestaDTO respuestaDTO1 = new RespuestaDTO();
        respuestaDTO1.setId(1L);
        RespuestaDTO respuestaDTO2 = new RespuestaDTO();
        assertThat(respuestaDTO1).isNotEqualTo(respuestaDTO2);
        respuestaDTO2.setId(respuestaDTO1.getId());
        assertThat(respuestaDTO1).isEqualTo(respuestaDTO2);
        respuestaDTO2.setId(2L);
        assertThat(respuestaDTO1).isNotEqualTo(respuestaDTO2);
        respuestaDTO1.setId(null);
        assertThat(respuestaDTO1).isNotEqualTo(respuestaDTO2);
    }
}
