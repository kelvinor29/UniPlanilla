package com.kelvin.uni_planilla.services;

import java.util.List;

import com.kelvin.uni_planilla.models.Normativa;

public interface INormativaService {

    public List<Normativa> listarNormativasActivas();
}