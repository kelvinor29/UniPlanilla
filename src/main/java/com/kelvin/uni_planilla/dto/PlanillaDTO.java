package com.kelvin.uni_planilla.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;

import lombok.Data;

@Data
public class PlanillaDTO implements Serializable{

    private short anioPl;
    private byte mesCalculado;
    private TipoPlanillaEnum tipoPlanilla;
    private LocalDate fechaCalculo;

}
