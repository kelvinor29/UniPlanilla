package com.kelvin.uni_planilla.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Planilla;

@Repository
public interface PlanillaRepository extends JpaRepository<Planilla, Integer> {

        // Validar si el tipo de planilla ya fue generada en el mes
        @Query(value = """
                        SELECT  CAST(CASE WHEN COUNT(P.ID_PLANILLA)> 0 THEN 1 ELSE 0 END AS BIT)
                        FROM    PLANILLAS P
                        WHERE   P.MES_CALCULADO = :mes AND P.ANIO_PL = :anio AND TIPO_PLANILLA = :planilla
                        """, nativeQuery = true)
        public Boolean existePlanillaEnMes(@Param("mes") int mes, @Param("anio") int anio,
                        @Param("planilla") String tipoPlanilla);

        // Consulta para obtener todos los datos necesarios para los cálculos de cada
        // empleado
        @Query(value = """
                        WITH AniosServicio AS (
                        SELECT ID_EMPLEADO,
                                SUM(DATEDIFF(DAY, FECHA_INICIO_N, CASE WHEN FECHA_FIN_N > GETDATE() THEN GETDATE() ELSE ISNULL(FECHA_FIN_N, GETDATE()) END)) / 365 AS aniosServicio
                        FROM   NOMBRAMIENTOS
                        GROUP BY ID_EMPLEADO
                        )

                        SELECT	E.ID_EMPLEADO AS idEmpleado, N.TIPO_SALARIO AS tipoSalario, P.CATEGORIA AS categoria,
                                CASE    WHEN N.TIPO_SALARIO = 'Global' THEN P.SALARIO_GLOBAL
                                        WHEN N.TIPO_SALARIO = 'Compuesto' THEN P.SALARIO_BASE
                                END AS salarioBase,
                                ISNULL(ASO.aniosServicio, 0) AS aniosServicio,
                                E.PUNTOS_PROFESIONALES_E AS puntosProfesionales
                        FROM   EMPLEADOS E JOIN NOMBRAMIENTOS N ON E.ID_EMPLEADO = N.ID_EMPLEADO
                                        JOIN PUESTOS P ON N.ID_PUESTO = P.ID_PUESTO
                                        LEFT JOIN AniosServicio ASO ON ASO.ID_EMPLEADO = E.ID_EMPLEADO
                        WHERE  E.BORRADO_E = 0 AND E.ID_EMPLEADO IN (:idsEmpleados)
                        AND N.FECHA_INICIO_N <= :finMes AND (N.FECHA_FIN_N IS NULL OR N.FECHA_FIN_N >= :inicioMes)
                        """, nativeQuery = true)
        public List<EmpleadoLaboralDTO> obtenerDatosEmpleadosPlanilla(
                        @Param("idsEmpleados") List<Integer> idsEmpleados, @Param("inicioMes") LocalDate inicioMes,
                        @Param("finMes") LocalDate finMes);
}
