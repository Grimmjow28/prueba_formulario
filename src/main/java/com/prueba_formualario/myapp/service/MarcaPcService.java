package com.prueba_formualario.myapp.service;

import com.prueba_formualario.myapp.service.dto.MarcaPcDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.prueba_formualario.myapp.domain.MarcaPc}.
 */
public interface MarcaPcService {
    /**
     * Save a marcaPc.
     *
     * @param marcaPcDTO the entity to save.
     * @return the persisted entity.
     */
    MarcaPcDTO save(MarcaPcDTO marcaPcDTO);

    /**
     * Partially updates a marcaPc.
     *
     * @param marcaPcDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MarcaPcDTO> partialUpdate(MarcaPcDTO marcaPcDTO);

    /**
     * Get all the marcaPcs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MarcaPcDTO> findAll(Pageable pageable);

    /**
     * Get the "id" marcaPc.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MarcaPcDTO> findOne(Long id);

    /**
     * Delete the "id" marcaPc.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
