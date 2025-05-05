package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;

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
import jakarta.validation.constraints.Past;
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
    @Pattern(regexp = "^[0-9]{9}$", message = "El cédula solo permite 9 dígitos.")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La fecha de nacimiento debe ser menor a hoy.")
    private LocalDate fechaNacimiento;

    @Column(name = "PUNTOS_PROFESIONALES_E", nullable = false)
    private byte puntosProfesionales = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_E", nullable = false, length = 3)
    private EstadoBasicoEnum estadoE = EstadoBasicoEnum.ACT;

    @Column(name = "BORRADO_E", nullable = false)
    private boolean borradoE = false;

    @OneToMany(mappedBy = "empleado") 
    private List<Nombramiento> nombramientos = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<EmpleadoTitulo> empleadoTitulos = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<Permiso> permisos = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<Incapacidad> incapacidades = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<Pension> pensiones = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<DetallePlanilla> detallesPlanillas = new ArrayList<>();

}
