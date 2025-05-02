package com.kelvin.uni_planilla.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.dto.PermisoEmpleadoDTO;
import com.kelvin.uni_planilla.models.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

        // Consultar que empleados los permisos que tuvo en el mes pasado
        @Query(value = """
                        SELECT	P.ID_EMPLEADO AS idEmpleado,
                                SUM(DATEDIFF(DAY,       CASE WHEN P.FECHA_INICIO_PE >= :inicioMes THEN P.FECHA_INICIO_PE ELSE :inicioMes END,
                                                        CASE WHEN P.FECHA_FINAL_PE <= :finMes THEN P.FECHA_FINAL_PE ELSE :finMes END ) + 1 )
                                AS totalDiasPermiso,
                                P.CON_GOCE AS conGoce
                        FROM	PERMISOS P
                        WHERE	P.FECHA_INICIO_PE <= :finMes AND P.FECHA_FINAL_PE >= :inicioMes
                                AND P.ID_EMPLEADO IN (:idsEmpleados)
                        GROUP BY P.ID_EMPLEADO, p.CON_GOCE
                        """, nativeQuery = true)
        public List<PermisoEmpleadoDTO> listarPermisosEmpleadosPorMes(@Param("idsEmpleados") List<Integer> idsEmpleados,
                        @Param("inicioMes") LocalDate inicioMes,
                        @Param("finMes") LocalDate finMes);
}
