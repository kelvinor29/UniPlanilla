package com.kelvin.uni_planilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.ImpuestoRenta;
import com.kelvin.uni_planilla.models.Incapacidad;

@Repository
public interface ImpuestoRentaRepository extends JpaRepository<Incapacidad, Integer> {

        @Query(value = """
                        SELECT	ID_IMP_RENTA, PORCENTAJE_IMPUESTO, ANIO_IR, MONTO_MINIMO, MONTO_MAXIMO, ESTADO_IMP
                        FROM	IMPUESTOS_RENTAS WHERE ANIO_IR = :anio
                        """, nativeQuery = true)
        public List<ImpuestoRenta> listarRentasActivasPorAnio(@Param("anio") int anio);
}
