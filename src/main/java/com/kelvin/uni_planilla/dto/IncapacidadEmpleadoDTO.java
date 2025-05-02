package com.kelvin.uni_planilla.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class IncapacidadEmpleadoDTO implements Serializable {
    private int idIncapacidad;
    private int idEmpleado;
    private int idNormativa;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicioInc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFinInc;
    
    private String motivoInc;
    private BigDecimal porcentajeNormativa = BigDecimal.ZERO;

    public IncapacidadEmpleadoDTO(int idIncapacidad, int idEmpleado, int idNormativa, Date fechaInicioInc,
            Date fechaFinInc, String motivoInc, BigDecimal porcentajeNormativa) {
        this.idIncapacidad = idIncapacidad;
        this.idEmpleado = idEmpleado;
        this.idNormativa = idNormativa;
        this.fechaInicioInc = fechaInicioInc.toLocalDate();
        this.fechaFinInc = fechaFinInc.toLocalDate();
        this.motivoInc = motivoInc;
        this.porcentajeNormativa = porcentajeNormativa;
    }
}
