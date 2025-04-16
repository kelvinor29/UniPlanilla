package com.kelvin.uni_planilla.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.enums.EstadoBasico;
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
    public Optional<Empleado> obtenerEmpleado(int id) {
        return empleadoRep.findById(id);
    }

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        // Validar datos únicos
        validarDatosUnicos(empleado);
        return empleadoRep.save(empleado);
    }

    @Override
    public boolean eliminarEmpleado(int id) {
        // Optional<Empleado> empleado = obtenerEmpleado(id);

        // if (empleado.isEmpty())
        // return true; // Empleado ya no existe

        // Empleado emp = empleado.get();

        // // Validar si tiene un nombramiento activo
        // if (empleadoRep.tieneNombramientoActivo(id))
        // return false; // No se puede eliminar porque tiene un nombramiento activo

        // // Validar si tiene relaciones con otras entidades
        // if (empleadoRep.tieneEmpleadoRelaciones(id)) {
        // // Borrado lógico
        // emp.setBorradoE(true);
        // emp.setEstadoE(EstadoBasico.INA);
        // empleadoRep.save(emp);
        // } else
        // // Borrado físico
        // empleadoRep.delete(emp);

        // return true;

        return obtenerEmpleado(id)
                .map(emp -> {
                    // Validar si tiene un nombramiento activo
                    if (empleadoRep.tieneNombramientoActivo(id))
                        return false; // No se puede eliminar porque tiene un nombramiento activo

                    // Validar si tiene relaciones con otras entidades
                    if (empleadoRep.tieneEmpleadoRelaciones(id)) {
                        // Borrado lógico
                        emp.setBorradoE(true);
                        emp.setEstadoE(EstadoBasico.INA);
                        empleadoRep.save(emp);
                    } else
                        // Borrado físico
                        empleadoRep.delete(emp);
                    return true;
                }).orElse(true);
    }

    @Override
    public boolean esCedulaRepetida(String cedula) {
        return empleadoRep.existsByCedula(cedula);
    }

    @Override
    public boolean esCorreoRepetido(String correo) {
        return empleadoRep.existsByCorreoElectronico(correo);
    }

    @Override
    public boolean esTelefonoRepetido(String telefono) {
        return empleadoRep.existsByTelefono(telefono);
    }

    // ==============================================================================================================

    private void validarDatosUnicos(Empleado empleado) {
        if (empleadoRep.existsByCedula(empleado.getCedula()))
            throw new IllegalArgumentException("Ya existe un empleado con esta cédula.");
        if (empleadoRep.existsByTelefono(empleado.getTelefono()))
            throw new IllegalArgumentException("Ya existe un empleado con este teléfono.");
        if (empleadoRep.existsByCorreoElectronico(empleado.getCorreoElectronico()))
            throw new IllegalArgumentException("Ya existe un empleado con este correo electrónico.");
    }
}
