package com.kelvin.uni_planilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.Pension;

@Repository
public interface PensionRepository extends JpaRepository<Pension, Integer>{

    @Query(value = """
            SELECT ID_PENSION, ID_EMPLEADO, MONTO_P, ESTADO_P FROM PENSIONES WHERE ID_EMPLEADO = :idEmpleado AND ESTADO_P = 'ACT'
            """, nativeQuery = true)
    public List<Pension> listarPensionesActivasPorEmpleado(@Param("idEmpleado") int idEmpleado);
    
}
