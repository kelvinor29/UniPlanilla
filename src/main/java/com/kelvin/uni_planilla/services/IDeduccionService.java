package com.kelvin.uni_planilla.services;

import java.util.List;
import java.util.Optional;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Deduccion;

public interface IDeduccionService {
    public List<Deduccion> listarDeduccionesActivos();

    public Optional<Deduccion> obtenerDeduccionPorId(int idDeduccion);

    public void calcularDeduccionesEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes);

}
