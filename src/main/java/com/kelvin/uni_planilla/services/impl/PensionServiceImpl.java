package com.kelvin.uni_planilla.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.models.Pension;
import com.kelvin.uni_planilla.repositories.PensionRepository;
import com.kelvin.uni_planilla.services.IPensionService;

@Service
public class PensionServiceImpl implements IPensionService{

    @Autowired
    private PensionRepository pensionRep;

    @Override
    public List<Pension> listarPensionesActivasEmpleado(int idEmpleado) {
        return pensionRep.listarPensionesActivasPorEmpleado(idEmpleado);
    }

}
