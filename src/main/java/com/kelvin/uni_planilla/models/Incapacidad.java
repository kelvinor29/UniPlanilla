package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.kelvin.uni_planilla.models.enums.EstadoBasico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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

    @Column(name = "FECHA_INICIO_IN", nullable = false)
    @FutureOrPresent(message = "La fecha de inicio debe ser de hoy en adelante.")
    private LocalDate fechaInicioInc;

    @Column(name = "FECHA_FIN_IN", nullable = false)
    @FutureOrPresent(message = "La fecha de fin debe ser de hoy en adelante.")
    private LocalDate fechaFinInc;

    @Column(name = "MOTIVO_IN", nullable = false, length = 50)
    @Size(max = 50, message = "El motivo no puede tener más de 50 caracteres.")
    @NotBlank(message = "El motivo es requerido.")
    private String motivoInc;

    @Column(name = "ESTADO_IN", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasico estadoInc = EstadoBasico.ACT;

    @ManyToMany(mappedBy = "incapacidades")
    private Set<Normativa> normativas = new HashSet<>();
}
