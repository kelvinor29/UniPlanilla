package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.math.BigDecimal;

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
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "PENSIONES")
public class Pension implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PENSION")
    private int idPension;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    @NotNull(message = "Debe seleccionar un empleado.")
    private Empleado empleado;

    @Column(name = "MONTO_P", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El monto de la pensión es requerido.")
    @Digits(integer = 8, fraction = 2, message = "El monto de la pensión debe tener un máximo de 8 dígitos enteros y 2 decimales.")
    @Positive(message = "El monto de la pensión debe ser un número positivo.")
    private BigDecimal montoPen;

    @Column(name = "ESTADO_P", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasicoEnum estadoPen = EstadoBasicoEnum.ACT;
}
