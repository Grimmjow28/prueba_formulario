package com.prueba_formualario.myapp.web.rest;

import com.prueba_formualario.myapp.repository.MarcaPcRepository;
import com.prueba_formualario.myapp.service.MarcaPcService;
import com.prueba_formualario.myapp.service.dto.MarcaPcDTO;
import com.prueba_formualario.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.prueba_formualario.myapp.domain.MarcaPc}.
 */
@RestController
@RequestMapping("/api")
public class MarcaPcResource {

    private final Logger log = LoggerFactory.getLogger(MarcaPcResource.class);

    private static final String ENTITY_NAME = "marcaPc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarcaPcService marcaPcService;

    private final MarcaPcRepository marcaPcRepository;

    public MarcaPcResource(MarcaPcService marcaPcService, MarcaPcRepository marcaPcRepository) {
        this.marcaPcService = marcaPcService;
        this.marcaPcRepository = marcaPcRepository;
    }

    /**
     * {@code POST  /marca-pcs} : Create a new marcaPc.
     *
     * @param marcaPcDTO the marcaPcDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marcaPcDTO, or with status {@code 400 (Bad Request)} if the marcaPc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marca-pcs")
    public ResponseEntity<MarcaPcDTO> createMarcaPc(@RequestBody MarcaPcDTO marcaPcDTO) throws URISyntaxException {
        log.debug("REST request to save MarcaPc : {}", marcaPcDTO);
        if (marcaPcDTO.getId() != null) {
            throw new BadRequestAlertException("A new marcaPc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarcaPcDTO result = marcaPcService.save(marcaPcDTO);
        return ResponseEntity
            .created(new URI("/api/marca-pcs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marca-pcs/:id} : Updates an existing marcaPc.
     *
     * @param id the id of the marcaPcDTO to save.
     * @param marcaPcDTO the marcaPcDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marcaPcDTO,
     * or with status {@code 400 (Bad Request)} if the marcaPcDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marcaPcDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marca-pcs/{id}")
    public ResponseEntity<MarcaPcDTO> updateMarcaPc(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MarcaPcDTO marcaPcDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MarcaPc : {}, {}", id, marcaPcDTO);
        if (marcaPcDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marcaPcDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marcaPcRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarcaPcDTO result = marcaPcService.save(marcaPcDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marcaPcDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /marca-pcs/:id} : Partial updates given fields of an existing marcaPc, field will ignore if it is null
     *
     * @param id the id of the marcaPcDTO to save.
     * @param marcaPcDTO the marcaPcDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marcaPcDTO,
     * or with status {@code 400 (Bad Request)} if the marcaPcDTO is not valid,
     * or with status {@code 404 (Not Found)} if the marcaPcDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the marcaPcDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marca-pcs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarcaPcDTO> partialUpdateMarcaPc(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MarcaPcDTO marcaPcDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarcaPc partially : {}, {}", id, marcaPcDTO);
        if (marcaPcDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marcaPcDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marcaPcRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarcaPcDTO> result = marcaPcService.partialUpdate(marcaPcDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marcaPcDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /marca-pcs} : get all the marcaPcs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marcaPcs in body.
     */
    @GetMapping("/marca-pcs")
    public ResponseEntity<List<MarcaPcDTO>> getAllMarcaPcs(Pageable pageable) {
        log.debug("REST request to get a page of MarcaPcs");
        Page<MarcaPcDTO> page = marcaPcService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marca-pcs/:id} : get the "id" marcaPc.
     *
     * @param id the id of the marcaPcDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marcaPcDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marca-pcs/{id}")
    public ResponseEntity<MarcaPcDTO> getMarcaPc(@PathVariable Long id) {
        log.debug("REST request to get MarcaPc : {}", id);
        Optional<MarcaPcDTO> marcaPcDTO = marcaPcService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marcaPcDTO);
    }

    /**
     * {@code DELETE  /marca-pcs/:id} : delete the "id" marcaPc.
     *
     * @param id the id of the marcaPcDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marca-pcs/{id}")
    public ResponseEntity<Void> deleteMarcaPc(@PathVariable Long id) {
        log.debug("REST request to delete MarcaPc : {}", id);
        marcaPcService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
