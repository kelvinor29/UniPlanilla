package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.kelvin.uni_planilla.models.enums.EstadoBasico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DEDUCCIONES")
public class Deduccion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEDUCCION")
    private int idDeduccion;

    @Column(name = "FECHA_ACTUALIZACION_D", nullable = false)
    private LocalDate fechaActualizacionDec;

    @Column(name = "ASUNTO_D",nullable = false)
    private String asuntoDec;

    @Column(name = "VALOR_FIJO_D", precision = 10, scale = 2)
    private BigDecimal valorFijoDec;

    @Column(name = "PORCENTAJE_D", precision = 3, scale = 2)
    private BigDecimal porcentajeDec;

    @Column(name = "CATEGORIA_APLICA_D")
    private short categoriaAplicaDec;

    @Column(name = "ESTADO_D", nullable = false)
    private EstadoBasico estadoDec = EstadoBasico.ACT;

    @OneToMany(mappedBy = "deduccion")
    private Set<DetalleDeduccion> detallesDeducciones = new HashSet<>();
}
