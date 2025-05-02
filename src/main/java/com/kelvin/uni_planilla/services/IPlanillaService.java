package com.kelvin.uni_planilla.services;

import java.time.LocalDate;
import java.util.List;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;

public interface IPlanillaService {

    public List<TipoPlanillaEnum> obtenerTiposPlanillaPorMes(byte mesActual, short anioActual);

    public void calcularPlanilla(byte mes, short anio, LocalDate fechaActual, TipoPlanillaEnum tipoPlanilla);

    public List<EmpleadoLaboralDTO> obtenerDatosEmpleadoParaPlanilla(LocalDate inicioMes,
            LocalDate finMes, List<Integer> idsEmpleados);
}
