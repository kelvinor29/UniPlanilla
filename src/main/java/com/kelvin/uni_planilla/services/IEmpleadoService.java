package com.kelvin.uni_planilla.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.Nombramiento;

public interface IEmpleadoService {
    public List<Empleado> listarEmpleados();

    public List<Empleado> listarEmpleados(String filtro);

    public Optional<Empleado> obtenerEmpleado(int id);

    public Empleado guardarEmpleado(Empleado empleado);

    public boolean eliminarEmpleado(int id);

    public boolean esCedulaRepetida(String cedula, int idEmpleado);

    public boolean esCorreoRepetido(String correo, int idEmpleado);

    public boolean esTelefonoRepetido(String telefono, int idEmpleado);

    // ==============================================================================
    public List<Empleado> listarEmpleadosParaPlanilla(int mes, int anio);

    public Nombramiento obtenerNombramientoPorMes(int mes, int anio, int idEmpleado);
    
    public BigDecimal calcularSalarioDiario(BigDecimal salarioBaseMes);

    public BigDecimal calcularMontoADescontar(BigDecimal salarioBase, int totalDiasIncapacidad);

}
