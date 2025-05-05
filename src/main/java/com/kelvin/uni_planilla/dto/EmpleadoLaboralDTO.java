package com.kelvin.uni_planilla.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.kelvin.uni_planilla.models.ImpuestoRenta;
import com.kelvin.uni_planilla.models.Incapacidad;
import com.kelvin.uni_planilla.models.Pension;
import com.kelvin.uni_planilla.models.Permiso;
import com.kelvin.uni_planilla.models.enums.TipoSalarioEnum;

import lombok.Data;

@Data
public class EmpleadoLaboralDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // Info básica
    private int idDetallePlanilla;
    private int idEmpleado;
    private int anioCalculo = LocalDate.now().getYear();
    private int mesCalculado;
    private int aniosServicio;
    private int puntosProfesionales;
    private byte categoria;
    private int diasIncapacidad;
    private int diasPermisoConGoce;
    private int diasPermisoSinGoce;

    private String nombreCompleto;
    private String correoElectronico;
    private String puestoE;
    private String cedula;
    private String tipoPlanilla;

    private LocalDate fechaCalculo;
    // @Enumerated(EnumType.STRING)
    private TipoSalarioEnum tipoSalario;

    private BigDecimal salarioBase = BigDecimal.ZERO;
    private BigDecimal salarioBruto = BigDecimal.ZERO;
    private BigDecimal salarioNeto = BigDecimal.ZERO;
    private BigDecimal subsidioTotal = BigDecimal.ZERO;
    private BigDecimal totalBeneficios = BigDecimal.ZERO;
    private BigDecimal totalDeducciones = BigDecimal.ZERO;
    private BigDecimal totalImpuestosRentas = BigDecimal.ZERO;

    private List<BeneficioEmpleadoDTO> montosBeneficios = new ArrayList<>();
    private List<DeduccionEmpleadoDTO> montosDeducciones = new ArrayList<>();
    private List<IncapacidadEmpleadoDTO> incapacidadesDTO = new ArrayList<>();
    private List<Pension> pensiones = new ArrayList<>();
    private List<ImpuestoRenta> impuestoRentas = new ArrayList<>();
    private List<Permiso> permisos = new ArrayList<>();
    private List<Incapacidad> incapacidades = new ArrayList<>();

    // Constructor para consulta de info básica
    public EmpleadoLaboralDTO(int idEmpleado, String tipoSalario, byte categoria, String puestoE,
            BigDecimal salarioBase, int aniosServicio, int puntosProfesionales) {
        this.idEmpleado = idEmpleado;
        this.tipoSalario = TipoSalarioEnum.valueOf(tipoSalario);
        this.categoria = categoria;
        this.puestoE = puestoE;
        this.salarioBase = salarioBase;
        this.aniosServicio = aniosServicio;
        this.puntosProfesionales = puntosProfesionales;
    }

    // Constructor para info básica para las tarjetas
    public EmpleadoLaboralDTO(Integer idEmpleado, String nombreCompleto, String correoElectronico, String puestoE,
            BigDecimal salarioBase, BigDecimal salarioBruto, BigDecimal salarioNeto, String tipoSalario,
            Integer idDetallePlanilla) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.puestoE = puestoE;
        this.salarioBase = salarioBase;
        this.salarioBruto = salarioBruto;
        this.salarioNeto = salarioNeto;
        this.tipoSalario = TipoSalarioEnum.valueOf(tipoSalario);
        this.idDetallePlanilla = idDetallePlanilla;
    }

    // Predeterminado
    public EmpleadoLaboralDTO() {
    }

    public String getFechaCalculoFormateada() {
        return fechaCalculo != null ? fechaCalculo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    // Sumar todos los dias de las incapacidades
    public long getTotalDiasIncapacidad() {
        long total = incapacidades.stream().mapToLong(Incapacidad::getDiasIncapacidad).sum();

        // Calcular los dias maximos de incapacidad en el mes
        int diasDelMes = fechaCalculo.lengthOfMonth();
        return Math.min(total, diasDelMes);
    }

    // Sumar todos los dias de los permisos
    public long getTotalDiasPermisos() {
        long total = permisos.stream().mapToLong(Permiso::getDiasPermiso).sum();

        // Calcular los dias maximos de permisos en el mes
        int diasDelMes = fechaCalculo.lengthOfMonth();
        return Math.min(total, diasDelMes);
    }

}
