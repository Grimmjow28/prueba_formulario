package com.prueba_formualario.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.prueba_formualario.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarcaPcTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarcaPc.class);
        MarcaPc marcaPc1 = new MarcaPc();
        marcaPc1.setId(1L);
        MarcaPc marcaPc2 = new MarcaPc();
        marcaPc2.setId(marcaPc1.getId());
        assertThat(marcaPc1).isEqualTo(marcaPc2);
        marcaPc2.setId(2L);
        assertThat(marcaPc1).isNotEqualTo(marcaPc2);
        marcaPc1.setId(null);
        assertThat(marcaPc1).isNotEqualTo(marcaPc2);
    }
}
