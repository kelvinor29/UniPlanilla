package com.kelvin.uni_planilla.services;

import java.time.LocalDate;
import java.util.List;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.dto.PermisoEmpleadoDTO;

public interface IPermisoService {

    public List<PermisoEmpleadoDTO> calcularPermisosEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes, LocalDate inicioMes, LocalDate finMes);

}
