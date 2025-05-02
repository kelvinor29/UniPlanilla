package com.kelvin.uni_planilla.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kelvin.uni_planilla.models.Permiso;
import com.kelvin.uni_planilla.models.enums.TipoSalarioEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class EmpleadoLaboralDTO implements Serializable {

    private int anioCalculo = LocalDate.now().getYear();

    private int idEmpleado;
    @Enumerated(EnumType.STRING)
    private TipoSalarioEnum tipoSalario;
    private byte categoria;
    private int aniosServicio;
    private int puntosProfesionales;

    private BigDecimal salarioBase = BigDecimal.ZERO;
    private BigDecimal salarioBruto = BigDecimal.ZERO;
    private BigDecimal salarioNeto = BigDecimal.ZERO;

    private BigDecimal totalBeneficios = BigDecimal.ZERO;
    private BigDecimal totalDeducciones = BigDecimal.ZERO;
    private List<BeneficioEmpleadoDTO> montosBeneficios;
    private List<DeduccionEmpleadoDTO> montosDeducciones;
    private List<Permiso> permisos;
    private List<IncapacidadEmpleadoDTO> incapacidades;

    private int diasIncapacidad;
    private BigDecimal subsidioTotal = BigDecimal.ZERO;
    private int diasPermisoConGoce;
    private int diasPermisoSinGoce;

    public EmpleadoLaboralDTO(int idEmpleado, String tipoSalario, byte categoria, BigDecimal salarioBase,
            int aniosServicio, int puntosProfesionales) {
        this.idEmpleado = idEmpleado;
        this.tipoSalario = TipoSalarioEnum.valueOf(tipoSalario);
        this.salarioBase = salarioBase;
        this.categoria = categoria;
        this.aniosServicio = aniosServicio;
        this.puntosProfesionales = puntosProfesionales;

        this.montosBeneficios = new ArrayList<>();
        this.montosDeducciones = new ArrayList<>();
    }

}
