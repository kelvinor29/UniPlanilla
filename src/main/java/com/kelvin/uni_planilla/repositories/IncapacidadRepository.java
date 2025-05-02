package com.kelvin.uni_planilla.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.dto.IncapacidadEmpleadoDTO;
import com.kelvin.uni_planilla.models.Incapacidad;

@Repository
public interface IncapacidadRepository extends JpaRepository<Incapacidad, Integer> {

	@Query(value = """
			SELECT	I.ID_INCAPACIDAD AS idIncapacidad, I.ID_EMPLEADO AS idEmpleado, N.ID_NORMATIVA AS idNormativa, I.FECHA_INICIO_IN AS fechaInicioInc, I.FECHA_FIN_IN AS fechaFinInc, I.MOTIVO_IN AS motivoInc, N.PORCENTAJE_NI AS porcentajeNormativa
			FROM	INCAPACIDADES I
						JOIN NORMATIVAS N ON I.ID_NORMATIVA = N.ID_NORMATIVA
			WHERE	I.FECHA_INICIO_IN <= :finMes AND I.FECHA_FIN_IN >= :inicioMes
					AND I.ID_EMPLEADO IN (:idsEmpleados)
			""", nativeQuery = true)
	public List<IncapacidadEmpleadoDTO> listarIncapacidadesEmpleadosPorMes(
			@Param("idsEmpleados") List<Integer> idsEmpleados, @Param("inicioMes") LocalDate inicioMes,
			@Param("finMes") LocalDate finMes);

}
