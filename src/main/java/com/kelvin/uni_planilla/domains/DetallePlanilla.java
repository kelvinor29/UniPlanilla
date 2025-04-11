package com.kelvin.uni_planilla.domains;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DETALLES_PLANILLAS")
public class DetallePlanilla implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_PLANILLA")
    private int idDetallePlanilla;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    private Empleado empleado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_PLANILLA", nullable = false)
    private Planilla planilla;

    @Column(name = "DIAS_TRABAJADOS", nullable = false)
    private byte diasTrabajados = 20;

    @Column(name = "DIAS_PAGADOS", nullable = false)
    private byte diasPagados;

    @Column(name = "SALARIO_BASE", nullable = false)
    private BigDecimal salarioBase;

    @Column(name = "SALARIO_BRUTO", nullable = false)
    private BigDecimal salarioBruto;

    @Column(name = "SALARIO_NETO", nullable = false)
    private BigDecimal salarioNeto;

    @Column(name = "MONTO_RETROACTIVO", nullable = false)
    private BigDecimal montoRetroactivo;

    @OneToMany(mappedBy = "detallePlanilla")
    private Set<DetalleDeduccion> detallesDeducciones = new HashSet<>();

    @OneToMany(mappedBy = "detallePlanilla")
    private Set<DetalleBeneficio> detallesBeneficios = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "DETALLES_RENTAS",
        joinColumns = @JoinColumn(name = "ID_DETALLE_PLANILLA"),
        inverseJoinColumns = @JoinColumn(name ="ID_IMP_RENTA")
    )
    private Set<ImpuestoRenta> impuestosRenta = new HashSet<>();
}
