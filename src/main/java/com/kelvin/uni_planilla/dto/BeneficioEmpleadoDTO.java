package com.kelvin.uni_planilla.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BeneficioEmpleadoDTO {
    private int idBeneficio;
    private String asuntoBen;
    private BigDecimal montoBen;

    public BeneficioEmpleadoDTO(int idBeneficio, String asuntoBen, BigDecimal montoBen) {
        this.idBeneficio = idBeneficio;
        this.asuntoBen = asuntoBen;
        this.montoBen = montoBen;
    }
}