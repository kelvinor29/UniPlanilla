package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Deduccion;

public interface IDeduccionService {
    public List<Deduccion> listarDeduccionesActivos();

    public void calcularDeduccionesEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes);
}
