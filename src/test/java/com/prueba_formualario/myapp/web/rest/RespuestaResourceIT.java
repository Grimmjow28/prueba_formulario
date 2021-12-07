package com.prueba_formualario.myapp.web.rest;

import static com.prueba_formualario.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prueba_formualario.myapp.IntegrationTest;
import com.prueba_formualario.myapp.domain.Respuesta;
import com.prueba_formualario.myapp.repository.RespuestaRepository;
import com.prueba_formualario.myapp.service.dto.RespuestaDTO;
import com.prueba_formualario.myapp.service.mapper.RespuestaMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RespuestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RespuestaResourceIT {

    private static final Integer DEFAULT_NUMERO_IDENTIFICACION = 1;
    private static final Integer UPDATED_NUMERO_IDENTIFICACION = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIOS = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIOS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_HORA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_HORA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/respuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private RespuestaMapper respuestaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRespuestaMockMvc;

    private Respuesta respuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Respuesta createEntity(EntityManager em) {
        Respuesta respuesta = new Respuesta()
            .numeroIdentificacion(DEFAULT_NUMERO_IDENTIFICACION)
            .email(DEFAULT_EMAIL)
            .comentarios(DEFAULT_COMENTARIOS)
            .fechaHora(DEFAULT_FECHA_HORA);
        return respuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Respuesta createUpdatedEntity(EntityManager em) {
        Respuesta respuesta = new Respuesta()
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION)
            .email(UPDATED_EMAIL)
            .comentarios(UPDATED_COMENTARIOS)
            .fechaHora(UPDATED_FECHA_HORA);
        return respuesta;
    }

    @BeforeEach
    public void initTest() {
        respuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createRespuesta() throws Exception {
        int databaseSizeBeforeCreate = respuestaRepository.findAll().size();
        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);
        restRespuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isCreated());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeCreate + 1);
        Respuesta testRespuesta = respuestaList.get(respuestaList.size() - 1);
        assertThat(testRespuesta.getNumeroIdentificacion()).isEqualTo(DEFAULT_NUMERO_IDENTIFICACION);
        assertThat(testRespuesta.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRespuesta.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testRespuesta.getFechaHora()).isEqualTo(DEFAULT_FECHA_HORA);
    }

    @Test
    @Transactional
    void createRespuestaWithExistingId() throws Exception {
        // Create the Respuesta with an existing ID
        respuesta.setId(1L);
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        int databaseSizeBeforeCreate = respuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRespuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRespuestas() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        // Get all the respuestaList
        restRespuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroIdentificacion").value(hasItem(DEFAULT_NUMERO_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(sameInstant(DEFAULT_FECHA_HORA))));
    }

    @Test
    @Transactional
    void getRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        // Get the respuesta
        restRespuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, respuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(respuesta.getId().intValue()))
            .andExpect(jsonPath("$.numeroIdentificacion").value(DEFAULT_NUMERO_IDENTIFICACION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS))
            .andExpect(jsonPath("$.fechaHora").value(sameInstant(DEFAULT_FECHA_HORA)));
    }

    @Test
    @Transactional
    void getNonExistingRespuesta() throws Exception {
        // Get the respuesta
        restRespuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();

        // Update the respuesta
        Respuesta updatedRespuesta = respuestaRepository.findById(respuesta.getId()).get();
        // Disconnect from session so that the updates on updatedRespuesta are not directly saved in db
        em.detach(updatedRespuesta);
        updatedRespuesta
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION)
            .email(UPDATED_EMAIL)
            .comentarios(UPDATED_COMENTARIOS)
            .fechaHora(UPDATED_FECHA_HORA);
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(updatedRespuesta);

        restRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, respuestaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respuestaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
        Respuesta testRespuesta = respuestaList.get(respuestaList.size() - 1);
        assertThat(testRespuesta.getNumeroIdentificacion()).isEqualTo(UPDATED_NUMERO_IDENTIFICACION);
        assertThat(testRespuesta.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRespuesta.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testRespuesta.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void putNonExistingRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();
        respuesta.setId(count.incrementAndGet());

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, respuestaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();
        respuesta.setId(count.incrementAndGet());

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();
        respuesta.setId(count.incrementAndGet());

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespuestaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRespuestaWithPatch() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();

        // Update the respuesta using partial update
        Respuesta partialUpdatedRespuesta = new Respuesta();
        partialUpdatedRespuesta.setId(respuesta.getId());

        partialUpdatedRespuesta.numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION).comentarios(UPDATED_COMENTARIOS);

        restRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRespuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRespuesta))
            )
            .andExpect(status().isOk());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
        Respuesta testRespuesta = respuestaList.get(respuestaList.size() - 1);
        assertThat(testRespuesta.getNumeroIdentificacion()).isEqualTo(UPDATED_NUMERO_IDENTIFICACION);
        assertThat(testRespuesta.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRespuesta.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testRespuesta.getFechaHora()).isEqualTo(DEFAULT_FECHA_HORA);
    }

    @Test
    @Transactional
    void fullUpdateRespuestaWithPatch() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();

        // Update the respuesta using partial update
        Respuesta partialUpdatedRespuesta = new Respuesta();
        partialUpdatedRespuesta.setId(respuesta.getId());

        partialUpdatedRespuesta
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION)
            .email(UPDATED_EMAIL)
            .comentarios(UPDATED_COMENTARIOS)
            .fechaHora(UPDATED_FECHA_HORA);

        restRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRespuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRespuesta))
            )
            .andExpect(status().isOk());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
        Respuesta testRespuesta = respuestaList.get(respuestaList.size() - 1);
        assertThat(testRespuesta.getNumeroIdentificacion()).isEqualTo(UPDATED_NUMERO_IDENTIFICACION);
        assertThat(testRespuesta.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRespuesta.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testRespuesta.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void patchNonExistingRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();
        respuesta.setId(count.incrementAndGet());

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, respuestaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(respuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();
        respuesta.setId(count.incrementAndGet());

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(respuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();
        respuesta.setId(count.incrementAndGet());

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(respuestaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        int databaseSizeBeforeDelete = respuestaRepository.findAll().size();

        // Delete the respuesta
        restRespuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, respuesta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
