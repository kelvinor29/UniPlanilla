package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.models.ImpuestoRenta;

public interface IImpuestoRentaService {

    public List<ImpuestoRenta> listarRentasActivasPorAnio(int anio);
}
