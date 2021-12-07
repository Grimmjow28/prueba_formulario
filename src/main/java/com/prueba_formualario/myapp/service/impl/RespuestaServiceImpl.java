package com.prueba_formualario.myapp.service.impl;

import com.prueba_formualario.myapp.domain.Respuesta;
import com.prueba_formualario.myapp.repository.RespuestaRepository;
import com.prueba_formualario.myapp.service.RespuestaService;
import com.prueba_formualario.myapp.service.dto.RespuestaDTO;
import com.prueba_formualario.myapp.service.mapper.RespuestaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Respuesta}.
 */
@Service
@Transactional
public class RespuestaServiceImpl implements RespuestaService {

    private final Logger log = LoggerFactory.getLogger(RespuestaServiceImpl.class);

    private final RespuestaRepository respuestaRepository;

    private final RespuestaMapper respuestaMapper;

    public RespuestaServiceImpl(RespuestaRepository respuestaRepository, RespuestaMapper respuestaMapper) {
        this.respuestaRepository = respuestaRepository;
        this.respuestaMapper = respuestaMapper;
    }

    @Override
    public RespuestaDTO save(RespuestaDTO respuestaDTO) {
        log.debug("Request to save Respuesta : {}", respuestaDTO);
        Respuesta respuesta = respuestaMapper.toEntity(respuestaDTO);
        respuesta = respuestaRepository.save(respuesta);
        return respuestaMapper.toDto(respuesta);
    }

    @Override
    public Optional<RespuestaDTO> partialUpdate(RespuestaDTO respuestaDTO) {
        log.debug("Request to partially update Respuesta : {}", respuestaDTO);

        return respuestaRepository
            .findById(respuestaDTO.getId())
            .map(existingRespuesta -> {
                respuestaMapper.partialUpdate(existingRespuesta, respuestaDTO);

                return existingRespuesta;
            })
            .map(respuestaRepository::save)
            .map(respuestaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaDTO> findAll() {
        log.debug("Request to get all Respuestas");
        return respuestaRepository.findAll().stream().map(respuestaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RespuestaDTO> findOne(Long id) {
        log.debug("Request to get Respuesta : {}", id);
        return respuestaRepository.findById(id).map(respuestaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Respuesta : {}", id);
        respuestaRepository.deleteById(id);
    }
}
