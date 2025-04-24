package com.kelvin.uni_planilla.services;

import java.util.List;
import java.util.Optional;

import com.kelvin.uni_planilla.models.Empleado;

public interface IEmpleadoService {
    public List<Empleado> listarEmpleados();
    public List<Empleado> listarEmpleados(String filtro);
    public Optional<Empleado> obtenerEmpleado(int id);

    public Empleado guardarEmpleado(Empleado empleado);
    public boolean eliminarEmpleado(int id);

    public boolean esCedulaRepetida(String cedula, int idEmpleado);
    public boolean esCorreoRepetido(String correo, int idEmpleado);
    public boolean esTelefonoRepetido(String telefono, int idEmpleado);
}
