package com.kelvin.uni_planilla.services;

import java.time.LocalDate;
import java.util.List;

import com.kelvin.uni_planilla.models.Nombramiento;

public interface INombramientoService {
	public Nombramiento obtenerNombramientoEmpleadoPorPlanilla(List<Nombramiento> nombramientos,
			LocalDate fechaCalculo);
}
