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
                    Integer esNomActivo = empleadoRep.tieneNombramientoActivo(id);
                    // Validar si tiene un nombramiento activo
                    if (Integer.valueOf(1).equals(esNomActivo))
                        return false; // No se puede eliminar porque tiene un nombramiento activo

                    Integer tieneRelaciones = empleadoRep.tieneEmpleadoRelaciones(id);
                    // Validar si tiene relaciones con otras entidades
                    if (Integer.valueOf(1).equals(tieneRelaciones)) {
                        // Borrado lógico
                        emp.setBorradoE(true);
                        emp.setEstadoE(EstadoBasico.INA);
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

}
