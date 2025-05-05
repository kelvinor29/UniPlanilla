package com.kelvin.uni_planilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;
import com.kelvin.uni_planilla.models.Deduccion;
import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;

@Repository
public interface DeduccionRepository extends JpaRepository<Deduccion, Integer> {

    public List<Deduccion> findByEstadoDec(EstadoBasicoEnum estado);

    @Query(value = """
            SELECT	D.ID_DEDUCCION AS idDeduccion, D.ASUNTO_D AS asuntoDec, D.VALOR_D AS montoDec
            FROM	DEDUCCIONES D
            WHERE	D.ESTADO_D = 'ACT'
            """, nativeQuery = true)
    public List<DeduccionEmpleadoDTO> listarDeduccionesActivas();

}
