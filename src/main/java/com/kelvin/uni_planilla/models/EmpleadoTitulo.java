package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLEADOS_TITULOS")
public class EmpleadoTitulo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO_TITULO")
    private int idEmpleadoTitulo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    @NotNull(message = "Debe seleccionar un empleado.")
    private Empleado empleado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_GRADO_ACADEMICO", nullable = false)
    @NotNull(message = "Debe seleccionar un grado académico.")
    private GradoAcademico gradoAcademico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TITULO", nullable = false)
    @NotNull(message = "Debe seleccionar un titulo.")
    private Titulo titulo;

    @Column(name = "FECHA_OBTUVO")
    @NotNull(message = "La fecha del título obtenido es requerido.")
    @PastOrPresent(message = "La fecha de obtención debe ser hoy o antes.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaObtuvo;

}
