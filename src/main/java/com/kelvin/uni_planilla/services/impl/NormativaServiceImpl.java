package com.kelvin.uni_planilla.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelvin.uni_planilla.models.Normativa;
import com.kelvin.uni_planilla.repositories.NormativaRepository;
import com.kelvin.uni_planilla.services.INormativaService;

@Service
public class NormativaServiceImpl implements INormativaService{

    @Autowired
    private NormativaRepository normativaRep;

    @Override
    @Transactional(readOnly = true)
    public List<Normativa> listarNormativasActivas() {
        return normativaRep.listarNormativasActivas();
    }
    
}
