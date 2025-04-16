package com.kelvin.uni_planilla.repositories;

import java.util.List;

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

    public List<Empleado> findByCedula(String cedula);

    // Validar existencia de
    public boolean existsByCedula(String cedula);

    public boolean existsByCorreoElectronico(String correo);

    public boolean existsByTelefono(String cedula);

    @Query(value = "SELECT 1 FROM NOMBRAMIENTOS N WHERE N.ID_EMPLEADO = :idEmpleado AND (FECHA_FIN_N IS NULL OR FECHA_FIN_N >= GETDATE())", nativeQuery = true)
    public boolean tieneNombramientoActivo(@Param("idEmpleado") int idEmpleado);

    @Query(value = """
            SELECT	CASE
                WHEN	EXISTS(	SELECT 1 FROM EMPLEADOS_TITULOS WHERE ID_EMPLEADO = :idEmpleado)
                        OR EXISTS(	SELECT 1 FROM PERMISOS WHERE ID_EMPLEADO = :idEmpleado)
                        OR EXISTS(	SELECT 1 FROM INCAPACIDADES WHERE ID_EMPLEADO = :idEmpleado)
                        OR EXISTS(	SELECT 1 FROM PENSIONES WHERE ID_EMPLEADO = :idEmpleado)
                        OR EXISTS(	SELECT 1 FROM DETALLES_PLANILLAS WHERE ID_EMPLEADO = :idEmpleado)
                THEN 1
                ELSE 0
            END
            """, nativeQuery = true)
    public boolean tieneEmpleadoRelaciones(@Param("idEmpleado") int idEmpleado);

}
