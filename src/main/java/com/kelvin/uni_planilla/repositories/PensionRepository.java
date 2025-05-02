package com.kelvin.uni_planilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;
import com.kelvin.uni_planilla.models.Pension;

@Repository
public interface PensionRepository extends JpaRepository<Pension, Integer>{

    @Query(value = """
            SELECT 'Pensión #' + CAST(P.ID_PENSION AS VARCHAR) AS asuntoDec, P.MONTO_P AS montoDec
            FROM	PENSIONES P
            WHERE	P.ID_EMPLEADO = :idEmpleado AND P.ESTADO_P = 'ACT'
            """, nativeQuery = true)
    public List<DeduccionEmpleadoDTO> listarPensionesActivasPorEmpleado(@Param("idEmpleado") int idEmpleado);
    
}
