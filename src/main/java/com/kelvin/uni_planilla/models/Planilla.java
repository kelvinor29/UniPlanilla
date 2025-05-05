package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "PLANILLAS")
public class Planilla implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLANILLA")
    private int idPlanilla;

    @Column(name = "ANIO_PL", nullable = false)
    private short anioPl;

    @Column(name = "MES_CALCULADO", nullable = false)
    private byte mesCalculado;

    @Column(name = "FECHA_CALCULO", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCalculo = LocalDate.now();

    @Column(name = "TIPO_PLANILLA", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de planilla es requerido")
    private TipoPlanillaEnum tipoPlanilla = TipoPlanillaEnum.ORDINARIO;

    @Column(name = "FECHA_PRIMER_PAGO", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPrimerPago;

    @Column(name = "FECHA_SEGUNDO_PAGO", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaSegundoPago;

    // TODO: Crear anotacion perzonalidad o logica en el servicio para validar que
    // entre los dos porcentajes cumplan el 100%
    @Column(name = "PORCENTAJE_PRIMER_PAGO", nullable = false)
    private double porcentajePrimerPago;

    @Column(name = "PORCENTAJE_SEGUNDO_PAGO", nullable = false)
    private double porcentajeSegundoPago;

    @OneToMany(mappedBy = "planilla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePlanilla> detallesPlanilla = new ArrayList<>();
}
