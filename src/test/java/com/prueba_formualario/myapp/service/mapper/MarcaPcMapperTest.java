package com.prueba_formualario.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarcaPcMapperTest {

    private MarcaPcMapper marcaPcMapper;

    @BeforeEach
    public void setUp() {
        marcaPcMapper = new MarcaPcMapperImpl();
    }
}
