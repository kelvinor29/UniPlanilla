package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;

public interface IPlanillaService {
    public List<TipoPlanillaEnum> obtenerTiposPlanillaPorMes(int mesActual);
}
