package com.kelvin.uni_planilla.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kelvin.uni_planilla.models.DetallePlanilla;

@Repository
public interface DetallesPlanillaRepository extends JpaRepository<DetallePlanilla, Integer>{

}
