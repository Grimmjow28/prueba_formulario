package com.prueba_formualario.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RespuestaMapperTest {

    private RespuestaMapper respuestaMapper;

    @BeforeEach
    public void setUp() {
        respuestaMapper = new RespuestaMapperImpl();
    }
}
