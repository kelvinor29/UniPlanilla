package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;

public interface IPensionService {

    public List<DeduccionEmpleadoDTO> listarPensionesActivasEmpleado(int idEmpleado);
}
