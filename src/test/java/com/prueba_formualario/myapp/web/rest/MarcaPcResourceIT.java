package com.prueba_formualario.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prueba_formualario.myapp.IntegrationTest;
import com.prueba_formualario.myapp.domain.MarcaPc;
import com.prueba_formualario.myapp.repository.MarcaPcRepository;
import com.prueba_formualario.myapp.service.dto.MarcaPcDTO;
import com.prueba_formualario.myapp.service.mapper.MarcaPcMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MarcaPcResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarcaPcResourceIT {

    private static final String DEFAULT_NOMBRE_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_MARCA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marca-pcs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarcaPcRepository marcaPcRepository;

    @Autowired
    private MarcaPcMapper marcaPcMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarcaPcMockMvc;

    private MarcaPc marcaPc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarcaPc createEntity(EntityManager em) {
        MarcaPc marcaPc = new MarcaPc().nombreMarca(DEFAULT_NOMBRE_MARCA);
        return marcaPc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarcaPc createUpdatedEntity(EntityManager em) {
        MarcaPc marcaPc = new MarcaPc().nombreMarca(UPDATED_NOMBRE_MARCA);
        return marcaPc;
    }

    @BeforeEach
    public void initTest() {
        marcaPc = createEntity(em);
    }

    @Test
    @Transactional
    void createMarcaPc() throws Exception {
        int databaseSizeBeforeCreate = marcaPcRepository.findAll().size();
        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);
        restMarcaPcMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marcaPcDTO)))
            .andExpect(status().isCreated());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeCreate + 1);
        MarcaPc testMarcaPc = marcaPcList.get(marcaPcList.size() - 1);
        assertThat(testMarcaPc.getNombreMarca()).isEqualTo(DEFAULT_NOMBRE_MARCA);
    }

    @Test
    @Transactional
    void createMarcaPcWithExistingId() throws Exception {
        // Create the MarcaPc with an existing ID
        marcaPc.setId(1L);
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        int databaseSizeBeforeCreate = marcaPcRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarcaPcMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marcaPcDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMarcaPcs() throws Exception {
        // Initialize the database
        marcaPcRepository.saveAndFlush(marcaPc);

        // Get all the marcaPcList
        restMarcaPcMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marcaPc.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreMarca").value(hasItem(DEFAULT_NOMBRE_MARCA)));
    }

    @Test
    @Transactional
    void getMarcaPc() throws Exception {
        // Initialize the database
        marcaPcRepository.saveAndFlush(marcaPc);

        // Get the marcaPc
        restMarcaPcMockMvc
            .perform(get(ENTITY_API_URL_ID, marcaPc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marcaPc.getId().intValue()))
            .andExpect(jsonPath("$.nombreMarca").value(DEFAULT_NOMBRE_MARCA));
    }

    @Test
    @Transactional
    void getNonExistingMarcaPc() throws Exception {
        // Get the marcaPc
        restMarcaPcMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMarcaPc() throws Exception {
        // Initialize the database
        marcaPcRepository.saveAndFlush(marcaPc);

        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();

        // Update the marcaPc
        MarcaPc updatedMarcaPc = marcaPcRepository.findById(marcaPc.getId()).get();
        // Disconnect from session so that the updates on updatedMarcaPc are not directly saved in db
        em.detach(updatedMarcaPc);
        updatedMarcaPc.nombreMarca(UPDATED_NOMBRE_MARCA);
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(updatedMarcaPc);

        restMarcaPcMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marcaPcDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marcaPcDTO))
            )
            .andExpect(status().isOk());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
        MarcaPc testMarcaPc = marcaPcList.get(marcaPcList.size() - 1);
        assertThat(testMarcaPc.getNombreMarca()).isEqualTo(UPDATED_NOMBRE_MARCA);
    }

    @Test
    @Transactional
    void putNonExistingMarcaPc() throws Exception {
        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();
        marcaPc.setId(count.incrementAndGet());

        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaPcMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marcaPcDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marcaPcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarcaPc() throws Exception {
        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();
        marcaPc.setId(count.incrementAndGet());

        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaPcMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marcaPcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarcaPc() throws Exception {
        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();
        marcaPc.setId(count.incrementAndGet());

        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaPcMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marcaPcDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarcaPcWithPatch() throws Exception {
        // Initialize the database
        marcaPcRepository.saveAndFlush(marcaPc);

        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();

        // Update the marcaPc using partial update
        MarcaPc partialUpdatedMarcaPc = new MarcaPc();
        partialUpdatedMarcaPc.setId(marcaPc.getId());

        partialUpdatedMarcaPc.nombreMarca(UPDATED_NOMBRE_MARCA);

        restMarcaPcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarcaPc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarcaPc))
            )
            .andExpect(status().isOk());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
        MarcaPc testMarcaPc = marcaPcList.get(marcaPcList.size() - 1);
        assertThat(testMarcaPc.getNombreMarca()).isEqualTo(UPDATED_NOMBRE_MARCA);
    }

    @Test
    @Transactional
    void fullUpdateMarcaPcWithPatch() throws Exception {
        // Initialize the database
        marcaPcRepository.saveAndFlush(marcaPc);

        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();

        // Update the marcaPc using partial update
        MarcaPc partialUpdatedMarcaPc = new MarcaPc();
        partialUpdatedMarcaPc.setId(marcaPc.getId());

        partialUpdatedMarcaPc.nombreMarca(UPDATED_NOMBRE_MARCA);

        restMarcaPcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarcaPc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarcaPc))
            )
            .andExpect(status().isOk());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
        MarcaPc testMarcaPc = marcaPcList.get(marcaPcList.size() - 1);
        assertThat(testMarcaPc.getNombreMarca()).isEqualTo(UPDATED_NOMBRE_MARCA);
    }

    @Test
    @Transactional
    void patchNonExistingMarcaPc() throws Exception {
        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();
        marcaPc.setId(count.incrementAndGet());

        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaPcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marcaPcDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marcaPcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarcaPc() throws Exception {
        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();
        marcaPc.setId(count.incrementAndGet());

        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaPcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marcaPcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarcaPc() throws Exception {
        int databaseSizeBeforeUpdate = marcaPcRepository.findAll().size();
        marcaPc.setId(count.incrementAndGet());

        // Create the MarcaPc
        MarcaPcDTO marcaPcDTO = marcaPcMapper.toDto(marcaPc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaPcMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(marcaPcDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarcaPc in the database
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarcaPc() throws Exception {
        // Initialize the database
        marcaPcRepository.saveAndFlush(marcaPc);

        int databaseSizeBeforeDelete = marcaPcRepository.findAll().size();

        // Delete the marcaPc
        restMarcaPcMockMvc
            .perform(delete(ENTITY_API_URL_ID, marcaPc.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MarcaPc> marcaPcList = marcaPcRepository.findAll();
        assertThat(marcaPcList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
