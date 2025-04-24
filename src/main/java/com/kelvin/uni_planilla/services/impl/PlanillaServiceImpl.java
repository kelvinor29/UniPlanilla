package com.kelvin.uni_planilla.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;
import com.kelvin.uni_planilla.services.IPlanillaService;

@Service
public class PlanillaServiceImpl implements IPlanillaService{

    // @Autowired
    // private PlanillaRepository planillaRep;

    // Lista de planillas disponibles a seleccionar
    public List<TipoPlanillaEnum> obtenerTiposPlanillaPorMes(int mesActual) {
        List<TipoPlanillaEnum> planillas = new ArrayList<>();
        planillas.add(TipoPlanillaEnum.ORDINARIO);
        planillas.add(TipoPlanillaEnum.EXTRAORDINARIO);

        if (mesActual == 12)
            planillas.add(TipoPlanillaEnum.AGUINALDO);
        else if (mesActual == 1)
            planillas.add(TipoPlanillaEnum.SALARIO_ESCOLAR);

        return planillas;
    }
}
