package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name = "IMPUESTOS_RENTAS")
public class ImpuestoRenta implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_IMP_RENTA")
    private int idRenta;

    @Column(name = "PORCENTAJE_IMPUESTO", nullable = false)
    @NotNull(message = "El porcentaje es requerido.")
    @PositiveOrZero(message = "El porcentaje debe ser positivo.")
    @DecimalMax(value = "100", message = "El porcentaje debe ser entre 0 y 100.")
    private double porcentajeImpuesto;

    @Column(name = "ANIO_IR", nullable = false)
    private short anioImpuesto;

    @Column(name = "MONTO_MINIMO", nullable = false, precision = 9, scale = 2)
    private BigDecimal montoMinimo;

    @Column(name = "MONTO_MAXIMO", nullable = false, precision = 9, scale = 2)
    private BigDecimal montoMaximo;

    @Column(name = "ESTADO_IMP", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasico estadoImp = EstadoBasico.ACT;

    @ManyToMany(mappedBy = "impuestosRenta")
    private Set<DetallePlanilla> detallesPlanillas = new HashSet<>();
}
