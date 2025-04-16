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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLEADOS")
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private int idEmpleado;

    @Column(name = "NOMBRE_E", nullable = false, length = 25)
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres.")
    @NotBlank(message = "El nombre es requerido.")
    private String nombre;

    @Column(name = "APELLIDO1_E", nullable = false, length = 25)
    @Size(max = 25, message = "El primer apellido no puede tener más de 25 caracteres.")
    @NotBlank(message = "El primer apellido es requerido.")
    private String apellido1;

    @Column(name = "APELLIDO2_E")
    @Size(max = 25, message = "El segundo apellido no puede tener más de 25 caracteres.")
    private String apellido2;

    @Column(name = "CEDULA_E", nullable = false, length = 9, unique = true)
    @Size(max = 9, message = "La cédula no puede tener más de 9 caracteres.")
    @NotBlank(message = "La cédula es requerida.")
    private String cedula;

    @Column(name = "CORREO_ELECTRONICO_E", nullable = false, length = 50, unique = true)
    @Email(message = "Formato de correo electrónico incorrecto.")
    @Size(max = 50, message = "El correo electrónico no puede tener más de 50 caracteres.")
    @NotBlank(message = "El correo electrónico es requerido.")
    private String correoElectronico;

    @Column(name = "TELEFONO_E", nullable = false, length = 8, unique = true)
    @Size(max = 8, message = "El teléfono no puede tener más de 8 caracteres.")
    @NotBlank(message = "El teléfono es requerido.")
    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono solo permite 8 digitos.")
    private String telefono;

    @Column(name = "FECHA_NACIMIENTO_E", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "PUNTOS_PROFESIONALES_E", nullable = false)
    private byte puntosProfesionales = 0;

    @Column(name = "ESTADO_E", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private EstadoBasico estadoE = EstadoBasico.ACT;

    @Column(name = "BORRADO_E", nullable = false)
    private boolean borradoE = false;

    @OneToMany(mappedBy = "empleado") // @JsonIgnore??
    private Set<Nombramiento> nombramientos = new HashSet<>();

    @OneToMany(mappedBy = "empleado")
    private Set<EmpleadoTitulo> empleadoTitulos = new HashSet<>();

    @OneToMany(mappedBy = "empleado")
    private Set<Permiso> permisos = new HashSet<>();

    @OneToMany(mappedBy = "empleado")
    private Set<Incapacidad> incapacidades = new HashSet<>();

    @OneToMany(mappedBy = "empleado")
    private Set<Pension> pensiones = new HashSet<>();

    @OneToMany(mappedBy = "empleado")
    private Set<DetallePlanilla> detallesPlanillas = new HashSet<>();

}