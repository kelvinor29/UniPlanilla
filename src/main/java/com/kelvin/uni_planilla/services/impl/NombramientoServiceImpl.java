package com.kelvin.uni_planilla.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.models.Nombramiento;
import com.kelvin.uni_planilla.services.INombramientoService;

@Service
public class NombramientoServiceImpl implements INombramientoService{

    public Nombramiento obtenerNombramientoEmpleadoPorPlanilla(List<Nombramiento> nombramientos, LocalDate fechaCalculo) {
        // Averiguar el nombramiento que tuvo o tiene el empleado en dicha fecha de planilla
        return nombramientos.stream()
                // Obtener nombramientos que esten en la fecha
                .filter(n -> !n.getFechaInicioNom().isAfter(fechaCalculo))
                .filter(n -> n.getFechaFinNom() == null || !n.getFechaFinNom().isBefore(fechaCalculo))
                .findFirst().orElse(null); // Obtener el primer nombramiento (mala idea)
                // TODO: Arreglar al obtener nombramiento por planilla
    }
}
