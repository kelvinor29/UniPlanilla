package com.kelvin.uni_planilla.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.dto.BeneficioEmpleadoDTO;
import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;
import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.DetallePlanilla;
import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.Nombramiento;
import com.kelvin.uni_planilla.models.Planilla;
import com.kelvin.uni_planilla.models.Puesto;
import com.kelvin.uni_planilla.models.enums.TipoSalarioEnum;
import com.kelvin.uni_planilla.repositories.DetallesPlanillaRepository;
import com.kelvin.uni_planilla.services.IDetallesPlanillaService;
import com.kelvin.uni_planilla.services.INombramientoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DetallesPlanillaServiceImpl implements IDetallesPlanillaService {

	@Autowired
	private DetallesPlanillaRepository detallesRep;

	@Autowired
	private INombramientoService nombramientoService;

	@Override
	public List<EmpleadoLaboralDTO> listarInfoBasicaEmpleados(Planilla planilla) {
		// Listar la informacion basica de cada empleado
		return planilla.getDetallesPlanilla().stream().map(this::mappearEmpleadoLabDTOBasico)
				.collect(Collectors.toList());
	}

	@Override
	public EmpleadoLaboralDTO obtenerDetallesPlanilla(int idDetallePlanilla) {
		// Consulta el detalle de planilla
		DetallePlanilla detallePlanilla = detallesRep.findById(idDetallePlanilla)
				.orElseThrow(() -> new EntityNotFoundException("Detalle de planilla no disponible."));

		return mappearEmpleadoLabDTOCompleto(detallePlanilla);
	}

	private EmpleadoLaboralDTO mappearEmpleadoLabDTOCompleto(DetallePlanilla detalle) {
		Empleado empleado = detalle.getEmpleado();
		Planilla planilla = detalle.getPlanilla();
		Nombramiento nombramiento = obtenerNombramientoVigente(empleado, planilla.getFechaCalculo());

		EmpleadoLaboralDTO dto = new EmpleadoLaboralDTO();

		// Info básica
		dto.setIdDetallePlanilla(detalle.getIdDetallePlanilla());
		dto.setIdEmpleado(empleado.getIdEmpleado());
		dto.setNombreCompleto(empleado.getNombre() + " " + empleado.getApellido1() + " " + empleado.getApellido2());
		dto.setCorreoElectronico(empleado.getCorreoElectronico());
		dto.setCedula(empleado.getCedula());

		// Info del puesto y salario a partir de su nombramiento
		if (nombramiento != null) {
			dto.setTipoSalario(nombramiento.getTipoSalario());

			Puesto puesto = nombramiento.getPuesto();
			if (puesto != null) {
				dto.setPuestoE(puesto.getNombrePuesto());
				dto.setCategoria(puesto.getCategoria());
			}
		} else {
			dto.setPuestoE("Puesto no Disponible.");
			dto.setCategoria((byte) 0);
		}

		// Info de la planilla
		dto.setAnioCalculo(planilla.getAnioPl());
		dto.setFechaCalculo(planilla.getFechaCalculo());
		dto.setTipoPlanilla(planilla.getTipoPlanilla().name());
		dto.setMesCalculado(planilla.getMesCalculado());

		// Info de salarios
		dto.setSalarioBase(detalle.getSalarioBase());
		dto.setSalarioBruto(detalle.getSalarioBruto());
		dto.setSalarioNeto(detalle.getSalarioNeto());

		// Listas
		dto.setPensiones(empleado.getPensiones());
		dto.setIncapacidades(empleado.getIncapacidades());
		dto.setPermisos(empleado.getPermisos());
		dto.setMontosDeducciones(
				detalle.getDetallesDeducciones().stream()
						.map(d -> new DeduccionEmpleadoDTO(d.getDeduccion().getIdDeduccion(),
								d.getDeduccion().getAsuntoDec(),
								d.getMontoDeduccion()))
						.collect(Collectors.toList()));
		dto.setMontosBeneficios(
				detalle.getDetallesBeneficios().stream()
						.map(d -> new BeneficioEmpleadoDTO(d.getBeneficio().getIdBeneficio(),
								d.getBeneficio().getAsuntoBen(),
								d.getMontoBeneficio()))
						.collect(Collectors.toList()));

		calcularDiasPermiso(dto, empleado);

		return dto;
	}

	private EmpleadoLaboralDTO mappearEmpleadoLabDTOBasico(DetallePlanilla detalle) {
		Empleado empleado = detalle.getEmpleado();
		Nombramiento nombramiento = obtenerNombramientoVigente(empleado, detalle.getPlanilla().getFechaCalculo());

		// Validar su existe el nombramiento y puesto del empleado
		String nombrePuesto = (nombramiento != null && nombramiento.getPuesto() != null)
				? nombramiento.getPuesto().getNombrePuesto()
				: "Puesto No Disponible: ";

		TipoSalarioEnum tipoSalario = nombramiento != null ? nombramiento.getTipoSalario() : TipoSalarioEnum.Compuesto;

		return new EmpleadoLaboralDTO(
				empleado.getIdEmpleado(),
				empleado.getNombre() + " " + empleado.getApellido1() + " " + empleado.getApellido2(),
				empleado.getCorreoElectronico(),
				nombrePuesto,
				detalle.getSalarioBase(),
				detalle.getSalarioBruto(),
				detalle.getSalarioNeto(),
				tipoSalario != null ? tipoSalario.toString() : " - ",
				detalle.getIdDetallePlanilla());
	}

	private Nombramiento obtenerNombramientoVigente(Empleado empleado, LocalDate fechaCalculo) {
		return nombramientoService.obtenerNombramientoEmpleadoPorPlanilla(empleado.getNombramientos(), fechaCalculo);
	}

	private void calcularDiasPermiso(EmpleadoLaboralDTO dto, Empleado empleado) {
		// Obtener los dias de permiso con y sin goce
		long diasConGoce = empleado.getPermisos().stream().filter(p -> p.isConGoce()).count();
		long diasSinGoce = empleado.getPermisos().stream().filter(p -> !p.isConGoce()).count();

		dto.setDiasPermisoConGoce((int) diasConGoce);
		dto.setDiasPermisoSinGoce((int) diasSinGoce);
	}
}
