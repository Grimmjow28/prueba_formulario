package com.prueba_formualario.myapp.service.impl;

import com.prueba_formualario.myapp.domain.MarcaPc;
import com.prueba_formualario.myapp.repository.MarcaPcRepository;
import com.prueba_formualario.myapp.service.MarcaPcService;
import com.prueba_formualario.myapp.service.dto.MarcaPcDTO;
import com.prueba_formualario.myapp.service.mapper.MarcaPcMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MarcaPc}.
 */
@Service
@Transactional
public class MarcaPcServiceImpl implements MarcaPcService {

    private final Logger log = LoggerFactory.getLogger(MarcaPcServiceImpl.class);

    private final MarcaPcRepository marcaPcRepository;

    private final MarcaPcMapper marcaPcMapper;

    public MarcaPcServiceImpl(MarcaPcRepository marcaPcRepository, MarcaPcMapper marcaPcMapper) {
        this.marcaPcRepository = marcaPcRepository;
        this.marcaPcMapper = marcaPcMapper;
    }

    @Override
    public MarcaPcDTO save(MarcaPcDTO marcaPcDTO) {
        log.debug("Request to save MarcaPc : {}", marcaPcDTO);
        MarcaPc marcaPc = marcaPcMapper.toEntity(marcaPcDTO);
        marcaPc = marcaPcRepository.save(marcaPc);
        return marcaPcMapper.toDto(marcaPc);
    }

    @Override
    public Optional<MarcaPcDTO> partialUpdate(MarcaPcDTO marcaPcDTO) {
        log.debug("Request to partially update MarcaPc : {}", marcaPcDTO);

        return marcaPcRepository
            .findById(marcaPcDTO.getId())
            .map(existingMarcaPc -> {
                marcaPcMapper.partialUpdate(existingMarcaPc, marcaPcDTO);

                return existingMarcaPc;
            })
            .map(marcaPcRepository::save)
            .map(marcaPcMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarcaPcDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MarcaPcs");
        return marcaPcRepository.findAll(pageable).map(marcaPcMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaPcDTO> findOne(Long id) {
        log.debug("Request to get MarcaPc : {}", id);
        return marcaPcRepository.findById(id).map(marcaPcMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MarcaPc : {}", id);
        marcaPcRepository.deleteById(id);
    }
}
