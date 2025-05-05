package com.kelvin.uni_planilla.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kelvin.uni_planilla.models.enums.TipoGradoAcademicoEnum;
import com.kelvin.uni_planilla.models.enums.converters.TipoGradoAcademicoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name = "GRADOS_ACADEMICOS")
public class GradoAcademico implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRADO_ACADEMICO")
    private int idGradoAcademico;

    @Convert(converter= TipoGradoAcademicoConverter.class)
    @Column(name = "GRADO_ACADEMICO", nullable = false, length = 20)
    @NotBlank(message = "El grado académico es requerido.")
    private TipoGradoAcademicoEnum gradoAcademico = TipoGradoAcademicoEnum.NO_APLICA;

    @Column(name = "PUNTOS_PROFESIONALES_GA", nullable = false)
    @NotNull(message = "Los puntos profesionales es requerido.")
    @PositiveOrZero(message = "Los puntos debe de ser mayor o igual a 0.")
    private byte puntosProfesionales = 0;

    @Column(name = "BORRADO_GA", nullable = false)
    private boolean borradoGa = false;

    @OneToMany(mappedBy = "gradoAcademico")
    private List<EmpleadoTitulo> empleadosTitulos = new ArrayList<>();
}
