package com.kelvin.uni_planilla.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.dto.IncapacidadEmpleadoDTO;
import com.kelvin.uni_planilla.repositories.IncapacidadRepository;
import com.kelvin.uni_planilla.services.IEmpleadoService;
import com.kelvin.uni_planilla.services.IIncapacidadService;

@Service
public class IncapacidadServiceImpl implements IIncapacidadService {

	@Autowired
	private IncapacidadRepository incapacidadRep;

	@Autowired
	private IEmpleadoService empleadoService;

	@Override
	public void calcularIncapacidadYSubsidioEmpleadosPorMes(List<EmpleadoLaboralDTO> empleados, LocalDate inicioMes,
			LocalDate finMes) {

		// Listado de ids de los empleados
		List<Integer> idsEmpleados = empleados.stream()
				.map(EmpleadoLaboralDTO::getIdEmpleado).toList();

		// Listado de incapacidades de los empleados
		List<IncapacidadEmpleadoDTO> incapacidades = incapacidadRep.listarIncapacidadesEmpleadosPorMes(idsEmpleados,
				inicioMes, finMes);

		// Map para acceder a las incapacidades de cada empleado
		Map<Integer, List<IncapacidadEmpleadoDTO>> incapacidadesPorEmpleado = incapacidades.stream()
				.collect(Collectors.groupingBy(i -> i.getIdEmpleado()));

		empleados.forEach(empleado -> {
			// Listado de incapacidades del empleado por mes
			List<IncapacidadEmpleadoDTO> incapacidadesEmpleado = incapacidadesPorEmpleado.get(empleado.getIdEmpleado());

			if (incapacidadesEmpleado != null && !incapacidadesEmpleado.isEmpty()) {

				int totalDiasIncapacidad = 0;
				BigDecimal totalSubsidio = BigDecimal.ZERO;
				BigDecimal totalMontoADescontar = BigDecimal.ZERO;

				// Calcular subsidio por cada incapacidad
				for (IncapacidadEmpleadoDTO incapacidad : incapacidadesEmpleado) {

					// Guardar el cálculo del total de días con incapacidad
					totalDiasIncapacidad += calcularDiasIncapacidadPorMes(incapacidad, inicioMes, finMes);

					BigDecimal montoDescontar = empleadoService.calcularMontoADescontar(empleado.getSalarioBase(),
							totalDiasIncapacidad);
					BigDecimal subsidio = calcularSubsidio(montoDescontar, incapacidad.getPorcentajeNormativa());
					totalMontoADescontar = totalMontoADescontar.add(montoDescontar);
					totalSubsidio = totalSubsidio.add(subsidio);

				}
				empleado.setDiasIncapacidad(totalDiasIncapacidad);
				empleado.setSubsidioTotal(totalSubsidio);
				empleado.setIncapacidadesDTO(incapacidadesEmpleado);

				empleado.setSalarioNeto(empleado.getSalarioNeto().subtract(totalMontoADescontar).max(BigDecimal.ZERO));
				empleado.setSalarioNeto(empleado.getSalarioNeto().add(totalSubsidio));

			}

		});

	}

	private int calcularDiasIncapacidadPorMes(IncapacidadEmpleadoDTO incapacidad, LocalDate inicioMes,
			LocalDate finMes) {

		// Validar si la fecha de inicio y fin esta dentro del mes
		LocalDate fechaInicio = incapacidad.getFechaInicioInc().isBefore(inicioMes) ? inicioMes
				: incapacidad.getFechaInicioInc();
		LocalDate fechaFin = incapacidad.getFechaFinInc().isAfter(finMes) ? finMes : incapacidad.getFechaFinInc();

		return (int) (ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1);
	}

	private BigDecimal calcularSubsidio(BigDecimal montoPerdido, BigDecimal porcentaje) {
		return montoPerdido.multiply(porcentaje);
	}

}
