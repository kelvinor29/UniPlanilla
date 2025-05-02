package com.kelvin.uni_planilla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermisoEmpleadoDTO {
    private int idEmpleado;
    private int totalDiasPermiso = 0;
    private boolean conGoce;
}
