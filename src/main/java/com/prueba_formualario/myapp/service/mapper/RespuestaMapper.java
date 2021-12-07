package com.prueba_formualario.myapp.service.mapper;

import com.prueba_formualario.myapp.domain.Respuesta;
import com.prueba_formualario.myapp.service.dto.RespuestaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Respuesta} and its DTO {@link RespuestaDTO}.
 */
@Mapper(componentModel = "spring", uses = { MarcaPcMapper.class })
public interface RespuestaMapper extends EntityMapper<RespuestaDTO, Respuesta> {
    @Mapping(target = "marcaPc", source = "marcaPc", qualifiedByName = "id")
    RespuestaDTO toDto(Respuesta s);
}
