package com.kelvin.uni_planilla.domains;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DETALLES_DEDUCCIONES")
public class DetalleDeduccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_DEDUCCION")
    private int idDetalleDeduccion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_DETALLE_PLANILLA", nullable = false)
    private DetallePlanilla detallePlanilla;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_DEDUCCION", nullable = false)
    private Deduccion deduccion;

    @Column(name = "MONTO_DEDUCCION", precision = 10, scale = 2)
    private BigDecimal montoDeduccion = BigDecimal.ZERO;
}
