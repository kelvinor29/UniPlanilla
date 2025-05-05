package com.kelvin.uni_planilla.models;

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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "PERMISOS")
public class Permiso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERMISO")
    private int idPermiso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    @NotNull(message = "Debe seleccionar un empleado.")
    private Empleado empleado;

    @Column(name = "FECHA_INICIO_PE", nullable = false)
    @FutureOrPresent(message = "La fecha de inicio debe ser de hoy en adelante.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicioPer;

    @Column(name = "FECHA_FINAL_PE", nullable = false)
    @FutureOrPresent(message = "La fecha de fin debe ser de hoy en adelante.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFinalPer;

    @Column(name = "CON_GOCE", nullable = false)
    private boolean conGoce = false;

    @Column(name = "MOTIVO_PE", nullable = false, length = 50)
    @Size(max = 50, message = "El motivo no puede tener más de 50 caracteres.")
    @NotBlank(message = "El motivo es requerido.")
    private String motivoPer;

    @Column(name = "ESTADO_PE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasicoEnum estadoPer = EstadoBasicoEnum.ACT;

    @Transient
    public long getDiasPermiso() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaInicioPer, fechaFinalPer) + 1;
    }
}
