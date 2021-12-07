package com.prueba_formualario.myapp.repository;

import com.prueba_formualario.myapp.domain.MarcaPc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MarcaPc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarcaPcRepository extends JpaRepository<MarcaPc, Long> {}
