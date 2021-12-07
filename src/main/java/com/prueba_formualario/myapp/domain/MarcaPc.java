package com.prueba_formualario.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A MarcaPc.
 */
@Entity
@Table(name = "marca_pc")
public class MarcaPc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_marca")
    private String nombreMarca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarcaPc id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreMarca() {
        return this.nombreMarca;
    }

    public MarcaPc nombreMarca(String nombreMarca) {
        this.setNombreMarca(nombreMarca);
        return this;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarcaPc)) {
            return false;
        }
        return id != null && id.equals(((MarcaPc) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarcaPc{" +
            "id=" + getId() +
            ", nombreMarca='" + getNombreMarca() + "'" +
            "}";
    }
}
