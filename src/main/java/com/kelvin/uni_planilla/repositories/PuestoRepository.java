package com.kelvin.uni_planilla.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.Puesto;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    // @Query(value = """
    //             SELECT	P.ID_PUESTO, P.PUESTO, P.CATEGORIA, P.SALARIO_BASE, P.SALARIO_GLOBAL, P.FECHA_ACTUALIZACION_P, P.BORRADO_P
    //             FROM	PUESTOS P JOIN NOMBRAMIENTOS N ON P.ID_PUESTO = N.ID_PUESTO
    //             WHERE	N.ID_NOMBRAMIENTO = :idNombramiento
    //         """, nativeQuery = true)
    // public Optional<Puesto> obtenerPuestoPorNombramiento(@Param("idNombramiento") int idNombramiento);
}
