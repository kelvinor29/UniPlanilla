package com.kelvin.uni_planilla.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

        // Listar empleados no borrados
        public List<Empleado> findByBorradoEFalse();

        // Listar por nombre o cedula que no esten borrados
        public List<Empleado> findByNombreContainsOrCedulaContainsAndBorradoEFalse(String nombre, String cedula);

        public Optional<Empleado> findByCedula(String cedula);

        public Optional<Empleado> findByCorreoElectronico(String correoElectronico);

        public Optional<Empleado> findByTelefono(String telefono);

        // @Query(value = "SELECT 1 FROM NOMBRAMIENTOS N WHERE N.ID_EMPLEADO =
        // :idEmpleado AND (FECHA_FIN_N IS NULL OR FECHA_FIN_N >= GETDATE())",
        // nativeQuery = true)
        // public Boolean tieneNombramientoActivo(@Param("idEmpleado") int idEmpleado);

        /*
         * Se utiliza CASE para retornar un 1 o 0 sin importar el resultado e evitar
         * nulos
         */
        @Query(value = """
                        SELECT  CASE WHEN COUNT(N.ID_NOMBRAMIENTO) > 0 THEN 1 ELSE 0 END
                        FROM    NOMBRAMIENTOS N
                        WHERE   N.ID_EMPLEADO = :idEmpleado AND (FECHA_FIN_N IS NULL OR FECHA_FIN_N >= GETDATE())
                        """, nativeQuery = true)
        public Integer tieneNombramientoActivo(@Param("idEmpleado") int idEmpleado);

        @Query(value = """
                        SELECT	CASE
                                WHEN	EXISTS(	    SELECT 1 FROM EMPLEADOS_TITULOS WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM PERMISOS WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM INCAPACIDADES WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM PENSIONES WHERE ID_EMPLEADO = :idEmpleado)
                                        OR EXISTS(	SELECT 1 FROM DETALLES_PLANILLAS WHERE ID_EMPLEADO = :idEmpleado)
                                THEN 1
                                ELSE 0
                        END
                        """, nativeQuery = true)
        public Integer tieneEmpleadoRelaciones(@Param("idEmpleado") int idEmpleado);

}
