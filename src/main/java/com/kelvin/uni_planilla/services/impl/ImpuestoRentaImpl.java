package com.kelvin.uni_planilla.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelvin.uni_planilla.models.ImpuestoRenta;
import com.kelvin.uni_planilla.repositories.ImpuestoRentaRepository;
import com.kelvin.uni_planilla.services.IImpuestoRentaService;

@Service
public class ImpuestoRentaImpl implements IImpuestoRentaService{

    @Autowired
    private ImpuestoRentaRepository rentaRep;

    @Override
    @Transactional(readOnly = true)
    public List<ImpuestoRenta> listarRentasActivasPorAnio(int anio) {
        return rentaRep.listarRentasActivasPorAnio(anio);
    }

}
