package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.kelvin.uni_planilla.models.enums.EstadoBasico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "ES_PORCENTAJE_B", precision = 3, scale = 2)
    private BigDecimal porcentajeBen;

    @Column(name = "CATEGORIA_APLICA_B")
    private short categoriaAplicaBen = 0;

    @Column(name = "NECESITA_ANIOS_B")
    private boolean necesitaAniosBen = false;

    @Column(name = "ESTADO_B", nullable = false)
    private EstadoBasico estadoBen = EstadoBasico.ACT;

    @OneToMany(mappedBy = "beneficio")
    private Set<DetalleBeneficio> detallesBeneficios = new HashSet<>();
}
