package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Planilla;

public interface IDetallesPlanillaService {

    public List<EmpleadoLaboralDTO> listarInfoBasicaEmpleados(Planilla planilla);

    public EmpleadoLaboralDTO obtenerDetallesPlanilla(int idDetallePlanilla);

}
