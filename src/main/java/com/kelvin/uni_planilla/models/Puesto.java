package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "PUESTOS")
public class Puesto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PUESTO")
    private int idPuesto;

    @Column(name="PUESTO",nullable = false, length = 50, unique = true)
    @Size(max = 50, message = "El puesto no puede tener más de 50 caracteres.")
    @NotBlank(message = "El puesto es requerido.")
    private String nombrePuesto;

    @Column(nullable = false)
    @NotNull(message = "La categoría es requerida.")
    @Positive(message = "La categoría debe ser un número positivo.")
    private byte categoria = 1;

    @Column(name = "SALARIO_BASE", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El salario base es requerido.")
    @Digits(integer = 8, fraction = 2, message = "El salario base debe tener un máximo de 8 dígitos enteros y 2 decimales.")
    @Positive(message = "El salario base debe ser un número positivo.")
    private BigDecimal salarioBase;

    @Column(name = "SALARIO_GLOBAL", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El salario global es requerido.")
    @Digits(integer = 8, fraction = 2, message = "El salario global debe tener un máximo de 8 dígitos enteros y 2 decimales.")
    @Positive(message = "El salario global debe ser un número positivo.")
    private BigDecimal salarioGlobal;

    @Column(name = "FECHA_ACTUALIZACION_P", nullable = false)
    @NotNull(message = "La fecha de creación/actualización es requerida.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaActualizacion = LocalDate.now();

    @Column(name = "BORRADO_P", nullable = false)
    private boolean borradoP = false;

    @OneToMany(mappedBy = "puesto")
    private Set<Nombramiento> nombramientos = new HashSet<>();

}
