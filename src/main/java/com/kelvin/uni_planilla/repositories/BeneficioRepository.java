package com.kelvin.uni_planilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.Beneficio;
import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Integer> {

    public List<Beneficio> findByEstadoBen(EstadoBasicoEnum estado);

}
