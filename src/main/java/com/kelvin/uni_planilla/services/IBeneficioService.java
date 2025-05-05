package com.kelvin.uni_planilla.services;

import java.util.List;
import java.util.Optional;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Beneficio;

public interface IBeneficioService {
    public List<Beneficio> listarBeneficiosActivos();

    public void calcularBeneficiosEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes);

    public Optional<Beneficio> obtenerBeneficioPorId(int idBeneficio);
}
