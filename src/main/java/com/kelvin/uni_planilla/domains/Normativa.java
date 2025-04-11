package com.kelvin.uni_planilla.domains;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.kelvin.uni_planilla.domains.enums.EstadoBasico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name = "NORMATIVAS")
public class Normativa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NORMATIVA")
    private int idNormativa;

    @Column(name = "PORCENTAJE_NI", nullable = false, precision = 3, scale = 2)
    @NotNull(message = "El porcentaje es requerido.")
    @PositiveOrZero(message = "El porcentaje debe ser 0 o un número positivo.")
    @DecimalMin(value = "0", inclusive = true, message = "El porcentaje debe de ser entre 0 y 100.")
    @DecimalMax(value = "1", inclusive = true, message = "El porcentaje debe de ser entre 0 y 100.")
    private double porcentajeNorm;

    @Column(name = "RANGO_DIAS_INICIO", nullable = false)
    @PositiveOrZero(message = "El rango mínimo de días debe ser 0 o un número positivo.")
    private byte rangoDiasInicio;

    @Column(name = "RANGO_DIAS_FIN")
    @PositiveOrZero(message = "El rango máximo de días debe ser 0 o un número positivo.")
    private byte rangoDiasFin;

    @Column(name = "ESTADO_N", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasico estadoNorm = EstadoBasico.ACT;

    @ManyToMany
    @JoinTable(
        name = "NORMATIVAS_INCAPACIDADES",
        joinColumns = @JoinColumn(name = "ID_NORMATIVA"),
        inverseJoinColumns = @JoinColumn(name = "ID_INCAPACIDAD")
    )
    private Set<Incapacidad> incapacidades = new HashSet<>();

}
