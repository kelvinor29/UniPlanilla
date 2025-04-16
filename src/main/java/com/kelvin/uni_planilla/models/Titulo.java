package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "TITULOS")
public class Titulo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TITULO")
    private int idTitulo;

    @Column(nullable = false, length = 50)
    @Size(max = 50, message = "El certificado no puede tener más de 50 caracteres.")
    @NotBlank(message = "El certificado es requerido.")
    private String certificado;

    @Column(nullable = false, length = 50)
    @Size(max = 50, message = "La institución no puede tener más de 50 caracteres.")
    @NotBlank(message = "La institución es requerida.")
    private String institucion;

    @Column(name = "BORRADO_T", nullable = false)
    private boolean borradoT;

    @OneToMany(mappedBy = "titulo")
    private Set<EmpleadoTitulo> empleadosTitulos = new HashSet<>();
}
