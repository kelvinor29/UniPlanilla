package com.kelvin.uni_planilla.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.dto.PermisoEmpleadoDTO;
import com.kelvin.uni_planilla.repositories.PermisoRepository;
import com.kelvin.uni_planilla.services.IEmpleadoService;
import com.kelvin.uni_planilla.services.IPermisoService;

@Service
public class PermisoServiceImpl implements IPermisoService {
    private static final Logger logger = LoggerFactory.getLogger(PermisoServiceImpl.class);

    @Autowired
    private PermisoRepository pensionRep;

    @Autowired
    private IEmpleadoService empleadoService;

    @Override
    public List<PermisoEmpleadoDTO> calcularPermisosEmpleadosPorMes(List<EmpleadoLaboralDTO> empleados,
            LocalDate inicioMes, LocalDate finMes) {

        // Listado de ids de los empleados
        List<Integer> idsEmpleados = empleados.stream()
                .map(EmpleadoLaboralDTO::getIdEmpleado).toList();

        // Listado de permisos de los empleados
        List<PermisoEmpleadoDTO> permisos = pensionRep
                .listarPermisosEmpleadosPorMes(idsEmpleados, inicioMes, finMes);

        // Map para acceder a las permisos de cada empleado
        Map<Integer, List<PermisoEmpleadoDTO>> permisosPorEmpleado = permisos.stream()
                .collect(Collectors.groupingBy(PermisoEmpleadoDTO::getIdEmpleado));

        // Actualizar datos del DTO por cada empleado
        empleados.forEach(empleado -> {

            // Permisos del empleado por empleadoo
            List<PermisoEmpleadoDTO> permisosEmpleado = permisosPorEmpleado.get(empleado.getIdEmpleado());

            if (permisosEmpleado != null) {

                for (PermisoEmpleadoDTO permiso : permisosEmpleado) {

                    int diasPermiso = permiso.getTotalDiasPermiso();

                    if (permiso.isConGoce())
                        empleado.setDiasPermisoConGoce(diasPermiso);
                    else {
                        empleado.setDiasPermisoSinGoce(diasPermiso);

                        BigDecimal salarioDiario = empleadoService.calcularSalarioDiario(empleado.getSalarioBase());
                        BigDecimal montoDescontado = salarioDiario.multiply(BigDecimal.valueOf(diasPermiso));

                        empleado.setSalarioNeto(
                                empleado.getSalarioNeto().subtract(montoDescontado).max(BigDecimal.ZERO));
                    }

                }
            }

        });

        return permisos;
    }

}
