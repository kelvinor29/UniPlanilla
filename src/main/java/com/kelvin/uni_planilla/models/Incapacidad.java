package com.kelvin.uni_planilla.models;

import jakarta.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "INCAPACIDADES")
public class Incapacidad implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INCAPACIDAD")
    private int idIncapacidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    @NotNull(message = "Debe seleccionar un empleado.")
    private Empleado empleado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_NORMATIVA", nullable = false)
    @NotNull(message = "Debe seleccionar una normativa.")
    private Normativa normativa;

    @Column(name = "FECHA_INICIO_IN", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicioInc;

    @Column(name = "FECHA_FIN_IN", nullable = false)
    @FutureOrPresent(message = "La fecha de fin debe ser de hoy en adelante.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFinInc;

    @Column(name = "MOTIVO_IN", nullable = false, length = 50)
    @Size(max = 50, message = "El motivo no puede tener más de 50 caracteres.")
    @NotBlank(message = "El motivo es requerido.")
    private String motivoInc;

    @Column(name = "ESTADO_IN", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasicoEnum estadoInc = EstadoBasicoEnum.ACT;

    @Transient
    public long getDiasIncapacidad() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaInicioInc, fechaFinInc) + 1;
    }

}
