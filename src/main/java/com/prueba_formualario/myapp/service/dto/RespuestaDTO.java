package com.prueba_formualario.myapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.prueba_formualario.myapp.domain.Respuesta} entity.
 */
public class RespuestaDTO implements Serializable {

    private Long id;

    private Integer numeroIdentificacion;

    private String email;

    private String comentarios;

    private ZonedDateTime fechaHora;

    private MarcaPcDTO marcaPc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(Integer numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public ZonedDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(ZonedDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public MarcaPcDTO getMarcaPc() {
        return marcaPc;
    }

    public void setMarcaPc(MarcaPcDTO marcaPc) {
        this.marcaPc = marcaPc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RespuestaDTO)) {
            return false;
        }

        RespuestaDTO respuestaDTO = (RespuestaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, respuestaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RespuestaDTO{" +
            "id=" + getId() +
            ", numeroIdentificacion=" + getNumeroIdentificacion() +
            ", email='" + getEmail() + "'" +
            ", comentarios='" + getComentarios() + "'" +
            ", fechaHora='" + getFechaHora() + "'" +
            ", marcaPc=" + getMarcaPc() +
            "}";
    }
}
