package com.prueba_formualario.myapp.service.mapper;

import com.prueba_formualario.myapp.domain.MarcaPc;
import com.prueba_formualario.myapp.service.dto.MarcaPcDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MarcaPc} and its DTO {@link MarcaPcDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MarcaPcMapper extends EntityMapper<MarcaPcDTO, MarcaPc> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MarcaPcDTO toDtoId(MarcaPc marcaPc);
}
