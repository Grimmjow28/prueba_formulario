package com.prueba_formualario.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prueba_formualario.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarcaPcDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarcaPcDTO.class);
        MarcaPcDTO marcaPcDTO1 = new MarcaPcDTO();
        marcaPcDTO1.setId(1L);
        MarcaPcDTO marcaPcDTO2 = new MarcaPcDTO();
        assertThat(marcaPcDTO1).isNotEqualTo(marcaPcDTO2);
        marcaPcDTO2.setId(marcaPcDTO1.getId());
        assertThat(marcaPcDTO1).isEqualTo(marcaPcDTO2);
        marcaPcDTO2.setId(2L);
        assertThat(marcaPcDTO1).isNotEqualTo(marcaPcDTO2);
        marcaPcDTO1.setId(null);
        assertThat(marcaPcDTO1).isNotEqualTo(marcaPcDTO2);
    }
}
