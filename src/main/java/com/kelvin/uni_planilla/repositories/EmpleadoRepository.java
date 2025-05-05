package com.kelvin.uni_planilla.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.Nombramiento;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

        // Listar empleados no borrados
        public List<Empleado> findByBorradoEFalse();

        // Listar por nombre o cedula que no esten borrados
        public List<Empleado> findByNombreContainsOrCedulaContainsAndBorradoEFalse(String nombre, String cedula);

        public Optional<Empleado> findByCedula(String cedula);

        public Optional<Empleado> findByCorreoElectronico(String correoElectronico);

        public Optional<Empleado> findByTelefono(String telefono);

        @Query(value = """
                        SELECT  CASE WHEN COUNT(N.ID_NOMBRAMIENTO) > 0 THEN 1 ELSE 0 END
                        FROM    NOMBRAMIENTOS N
                        WHERE   N.ID_EMPLEADO = :idEmpleado AND (FECHA_FIN_N IS NULL OR FECHA_FIN_N >= GETDATE())
                        """, nativeQuery = true)
        public Integer tieneNombramientoActivo(@Param("idEmpleado") int idEmpleado);

        @Query(value = """
                        SELECT	N.ID_NOMBRAMIENTO, N.ID_EMPLEADO, N.ID_PUESTO, N.TIPO_SALARIO, N.FECHA_INICIO_N, N.FECHA_FIN_N, N.ESTADO_N
                        FROM	NOMBRAMIENTOS N
                        WHERE	N.ID_EMPLEADO = :idEmpleado AND N.FECHA_INICIO_N <= :finMes
                                AND (N.FECHA_FIN_N IS NULL OR N.FECHA_FIN_N >= :inicioMes)
                                """, nativeQuery = true)
        public Optional<Nombramiento> obtenerNombramientoPorFecha(@Param("idEmpleado") int idEmpleado,
                        @Param("inicioMes") LocalDate inicioMes, @Param("finMes") LocalDate finMes);

        @Query(value = """
                        SELECT	CAST(CASE
                                WHEN	EXISTS(	        SELECT 1 FROM EMPLEADOS_TITULOS WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM PERMISOS WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM INCAPACIDADES WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM PENSIONES WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM DETALLES_PLANILLAS WHERE ID_EMPLEADO = :idEmpleado)
                                THEN 1 ELSE 0 END
                                AS BIT)
                        """, nativeQuery = true)
        public Boolean tieneEmpleadoRelaciones(@Param("idEmpleado") int idEmpleado);

        // ==============================================================================
        @Query(value = """
                                        SELECT  E.ID_EMPLEADO, E.NOMBRE_E, E.APELLIDO1_E, E.APELLIDO2_E, E.CEDULA_E, E.CORREO_ELECTRONICO_E, E.TELEFONO_E, E.FECHA_NACIMIENTO_E, E.PUNTOS_PROFESIONALES_E, E.ESTADO_E, E.BORRADO_E
                                        FROM    EMPLEADOS E
                                                JOIN NOMBRAMIENTOS N ON E.ID_EMPLEADO = N.ID_EMPLEADO
                                                JOIN PUESTOS P ON N.ID_PUESTO = P.ID_PUESTO
                                        WHERE   E.BORRADO_E = 0 AND P.BORRADO_P = 0
                                                AND N.FECHA_INICIO_N <= :finMes AND (N.FECHA_FIN_N IS NULL OR N.FECHA_FIN_N >= :inicioMes)
                        """, nativeQuery = true)
        public List<Empleado> listarEmpleadosNombramientoVigente(@Param("inicioMes") LocalDate inicioMes,
                        @Param("finMes") LocalDate finMes);

        // Estas dos funciones, ya tiene proposito
        // @Query(value = """
        //                 SELECT	CAST(CASE WHEN COUNT(P.ID_PERMISO) > 0 THEN 1 ELSE 0 END AS BIT)
        //                 FROM	PERMISOS P
        //                 WHERE	P.CON_GOCE = 0 AND P.ID_EMPLEADO = :idEmpleado
        //                                 AND (P.FECHA_INICIO_PE <= :inicioMes AND (P.FECHA_FINAL_PE IS NULL OR P.FECHA_FINAL_PE >= :finMes))
        //                 """, nativeQuery = true)
        // public Boolean tienePermisoSinGoce(@Param("idEmpleado") int idEmpleado, @Param("inicioMes") LocalDate inicioMes,
        //                 @Param("finMes") LocalDate finMes);

        // @Query(value = """
        //                 SELECT	CAST(CASE WHEN COUNT(I.ID_INCAPACIDAD) > 0 THEN 1 ELSE 0 END AS BIT)
        //                 FROM	INCAPACIDADES I
        //                                 JOIN NORMATIVAS N ON N.ID_NORMATIVA = I.ID_NORMATIVA
        //                 WHERE	I.ESTADO_IN = :estadoInc AND I.ID_EMPLEADO = :idEmpleado
        //                         AND I.FECHA_INICIO_IN <= :inicioMes AND I.FECHA_FIN_IN >= :finMes
        //                         AND N.PORCENTAJE_NI = 0
        //                 """, nativeQuery = true)
        // public Boolean tieneIncapacidadSinSubsidio(@Param("idEmpleado") int idEmpleado,
        //                 @Param("estadoInc") String estadoInc, @Param("inicioMes") LocalDate inicioMes,
        //                 @Param("finMes") LocalDate finMes);

}
