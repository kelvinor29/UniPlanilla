package com.kelvin.uni_planilla.services;

import java.time.LocalDate;
import java.util.List;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;

public interface IIncapacidadService {

    public void calcularIncapacidadYSubsidioEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes, LocalDate inicioMes, LocalDate finMes);
}
