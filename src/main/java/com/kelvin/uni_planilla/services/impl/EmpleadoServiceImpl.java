package com.kelvin.uni_planilla.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.Nombramiento;
import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;
import com.kelvin.uni_planilla.repositories.EmpleadoRepository;
import com.kelvin.uni_planilla.services.IEmpleadoService;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRep;

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados() {
        return empleadoRep.findByBorradoEFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados(String filtro) {
        return empleadoRep.findByNombreContainsOrCedulaContainsAndBorradoEFalse(filtro, filtro);
    }

    @Override
    @Transactional(readOnly = true)
    // Listar los empleados disponibles para la planilla
    public List<Empleado> listarEmpleadosParaPlanilla(int mes, int anio) {
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        // Listar los empleados que están trabajando
        return empleadoRep.listarEmpleadosNombramientoVigente(inicioMes, finMes);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> obtenerEmpleado(int id) {
        return empleadoRep.findById(id);
    }

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        return empleadoRep.save(empleado);
    }

    @Override
    public boolean eliminarEmpleado(int id) {
        return obtenerEmpleado(id)
                .map(emp -> {
                    Integer esNomActivo = empleadoRep.tieneNombramientoActivo(id);
                    // Validar si tiene un nombramiento activo
                    if (Integer.valueOf(1).equals(esNomActivo))
                        return false; // No se puede eliminar porque tiene un nombramiento activo

                    Boolean tieneRelaciones = empleadoRep.tieneEmpleadoRelaciones(id);
                    // Validar si tiene relaciones con otras entidades
                    if (tieneRelaciones) {
                        // Borrado lógico
                        emp.setBorradoE(true);
                        emp.setEstadoE(EstadoBasicoEnum.INA);
                        empleadoRep.save(emp);
                    } else
                        // Borrado físico
                        empleadoRep.delete(emp);
                    return true;
                }).orElse(true); // Empleado no existe
    }

    @Override
    public boolean esCedulaRepetida(String cedula, int idEmpleado) {
        // Validar si se encuentra un empleado diferente con dicha cédula
        return empleadoRep.findByCedula(cedula)
                .filter(empleado -> empleado.getIdEmpleado() != idEmpleado).isPresent();
    }

    @Override
    public boolean esCorreoRepetido(String correo, int idEmpleado) {
        // Validar si se encuentra un empleado diferente con dicho correo electrónico
        return empleadoRep.findByCorreoElectronico(correo)
                .filter(empleado -> empleado.getIdEmpleado() != idEmpleado).isPresent();
    }

    @Override
    public boolean esTelefonoRepetido(String telefono, int idEmpleado) {
        // Validar si se encuentra un empleado diferente con dicha teléfono
        return empleadoRep.findByTelefono(telefono)
                .filter(empleado -> empleado.getIdEmpleado() != idEmpleado).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Nombramiento obtenerNombramientoPorMes(int mes, int anio, int idEmpleado) {
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        return empleadoRep.obtenerNombramientoPorFecha(idEmpleado, inicioMes, finMes)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró nombramiento para el empleado en ese mes " + inicioMes.getMonth().name()));
    }

    @Override
    public BigDecimal calcularSalarioDiario(BigDecimal salarioBaseMes) {
        return salarioBaseMes.divide(BigDecimal.valueOf(20), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularMontoADescontar(BigDecimal salarioBase, int totalDiasAusentes) {
        BigDecimal salarioDiario = calcularSalarioDiario(salarioBase);

        if (totalDiasAusentes > 20)
            totalDiasAusentes = 20;

        return salarioDiario.multiply(BigDecimal.valueOf(totalDiasAusentes));
    }
}
