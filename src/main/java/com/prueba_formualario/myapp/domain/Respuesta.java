package com.prueba_formualario.myapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A Respuesta.
 */
@Entity
@Table(name = "respuesta")
public class Respuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_identificacion")
    private Integer numeroIdentificacion;

    @Column(name = "email")
    private String email;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "fecha_hora")
    private ZonedDateTime fechaHora;

    @ManyToOne
    private MarcaPc marcaPc;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Respuesta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroIdentificacion() {
        return this.numeroIdentificacion;
    }

    public Respuesta numeroIdentificacion(Integer numeroIdentificacion) {
        this.setNumeroIdentificacion(numeroIdentificacion);
        return this;
    }

    public void setNumeroIdentificacion(Integer numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getEmail() {
        return this.email;
    }

    public Respuesta email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public Respuesta comentarios(String comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public ZonedDateTime getFechaHora() {
        return this.fechaHora;
    }

    public Respuesta fechaHora(ZonedDateTime fechaHora) {
        this.setFechaHora(fechaHora);
        return this;
    }

    public void setFechaHora(ZonedDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public MarcaPc getMarcaPc() {
        return this.marcaPc;
    }

    public void setMarcaPc(MarcaPc marcaPc) {
        this.marcaPc = marcaPc;
    }

    public Respuesta marcaPc(MarcaPc marcaPc) {
        this.setMarcaPc(marcaPc);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Respuesta)) {
            return false;
        }
        return id != null && id.equals(((Respuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Respuesta{" +
            "id=" + getId() +
            ", numeroIdentificacion=" + getNumeroIdentificacion() +
            ", email='" + getEmail() + "'" +
            ", comentarios='" + getComentarios() + "'" +
            ", fechaHora='" + getFechaHora() + "'" +
            "}";
    }
}
