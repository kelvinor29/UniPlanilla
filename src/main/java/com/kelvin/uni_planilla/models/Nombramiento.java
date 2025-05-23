package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;
import com.kelvin.uni_planilla.models.enums.TipoSalarioEnum;

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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "NOMBRAMIENTOS")
public class Nombramiento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOMBRAMIENTO")
    private int idNombramiento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_PUESTO", nullable = false)
    @NotNull(message = "Debe seleccionar un puesto.")
    private Puesto puesto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    @NotNull(message = "Debe seleccionar un empleado.")
    private Empleado empleado;

    @Column(name = "TIPO_SALARIO", nullable = false, length = 10)
    @NotEmpty(message = "Debe seleccionar el tipo de salario.")
    @Enumerated(EnumType.STRING)
    private TipoSalarioEnum tipoSalario;

    @Column(name = "FECHA_INICIO_N", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicioNom = LocalDate.now();

    // TODO: Realizar validacion personalizada para validar que fecha fin sea mayor
    // a fecha de incio
    @Column(name = "FECHA_FIN_N")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFinNom;

    @Column(name = "ESTADO_N", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasicoEnum estadoNom = EstadoBasicoEnum.ACT;

}
