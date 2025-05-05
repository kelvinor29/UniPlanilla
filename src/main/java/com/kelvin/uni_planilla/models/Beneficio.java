package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import lombok.Data;

@Data
@Entity
@Table(name = "BENEFICIOS")
public class Beneficio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BENEFICIO")
    private int idBeneficio;

    @Column(name = "FECHA_ACTUALIZACION_B", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaActualizacionBen;

    @Column(name = "ASUNTO_B", nullable = false)
    private String asuntoBen;

    @Column(name = "VALOR_B", precision = 10, scale = 2)
    private BigDecimal valorBen;

    @Column(name = "ES_PORCENTAJE_B")
    private boolean esPorcentajeBen;

    @Column(name = "CATEGORIA_APLICA_B")
    private short categoriaAplicaBen = 0;

    @Column(name = "NECESITA_PUNTOS_PROFESIONALES_B")
    private boolean necesitaPuntosProfBen = false;

    @Column(name = "NECESITA_ANIOS_B")
    private boolean necesitaAniosBen = false;

    @Column(name = "ESTADO_B", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoBasicoEnum estadoBen = EstadoBasicoEnum.ACT;

    @OneToMany(mappedBy = "beneficio")
    private List<DetalleBeneficio> detallesBeneficios = new ArrayList<>();
}
