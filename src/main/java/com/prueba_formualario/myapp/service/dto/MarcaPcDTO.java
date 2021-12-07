package com.prueba_formualario.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.prueba_formualario.myapp.domain.MarcaPc} entity.
 */
public class MarcaPcDTO implements Serializable {

    private Long id;

    private String nombreMarca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarcaPcDTO)) {
            return false;
        }

        MarcaPcDTO marcaPcDTO = (MarcaPcDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marcaPcDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarcaPcDTO{" +
            "id=" + getId() +
            ", nombreMarca='" + getNombreMarca() + "'" +
            "}";
    }
}
