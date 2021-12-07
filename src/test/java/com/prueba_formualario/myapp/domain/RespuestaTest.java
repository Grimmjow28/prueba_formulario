package com.prueba_formualario.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.prueba_formualario.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RespuestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Respuesta.class);
        Respuesta respuesta1 = new Respuesta();
        respuesta1.setId(1L);
        Respuesta respuesta2 = new Respuesta();
        respuesta2.setId(respuesta1.getId());
        assertThat(respuesta1).isEqualTo(respuesta2);
        respuesta2.setId(2L);
        assertThat(respuesta1).isNotEqualTo(respuesta2);
        respuesta1.setId(null);
        assertThat(respuesta1).isNotEqualTo(respuesta2);
    }
}
