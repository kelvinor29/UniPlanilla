package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.models.Pension;

public interface IPensionService {

    public List<Pension> listarPensionesActivasEmpleado(int idEmpleado);
}
