package com.kelvin.uni_planilla.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DeduccionEmpleadoDTO {
    private int idDeduccion;
    private String asuntoDec;
    private BigDecimal montoDec;

    public DeduccionEmpleadoDTO(int idDeduccion, String asuntoDec, BigDecimal montoDec) {
        this.idDeduccion = idDeduccion;
        this.asuntoDec = asuntoDec;
        this.montoDec = montoDec;
    }
}