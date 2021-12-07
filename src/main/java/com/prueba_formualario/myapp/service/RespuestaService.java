package com.prueba_formualario.myapp.service;

import com.prueba_formualario.myapp.service.dto.RespuestaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.prueba_formualario.myapp.domain.Respuesta}.
 */
public interface RespuestaService {
    /**
     * Save a respuesta.
     *
     * @param respuestaDTO the entity to save.
     * @return the persisted entity.
     */
    RespuestaDTO save(RespuestaDTO respuestaDTO);

    /**
     * Partially updates a respuesta.
     *
     * @param respuestaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RespuestaDTO> partialUpdate(RespuestaDTO respuestaDTO);

    /**
     * Get all the respuestas.
     *
     * @return the list of entities.
     */
    List<RespuestaDTO> findAll();

    /**
     * Get the "id" respuesta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RespuestaDTO> findOne(Long id);

    /**
     * Delete the "id" respuesta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
