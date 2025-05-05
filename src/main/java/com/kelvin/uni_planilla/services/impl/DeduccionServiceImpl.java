package com.kelvin.uni_planilla.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;
import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Deduccion;
import com.kelvin.uni_planilla.models.ImpuestoRenta;
import com.kelvin.uni_planilla.models.Pension;
import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;
import com.kelvin.uni_planilla.repositories.DeduccionRepository;
import com.kelvin.uni_planilla.services.IDeduccionService;
import com.kelvin.uni_planilla.services.IImpuestoRentaService;
import com.kelvin.uni_planilla.services.IPensionService;

@Service
public class DeduccionServiceImpl implements IDeduccionService {

    @Autowired
    private DeduccionRepository deduccionRep;

    @Autowired
    private IPensionService pensionService;

    @Autowired
    private IImpuestoRentaService rentaService;

    @Override
    @Transactional(readOnly = true)
    public List<Deduccion> listarDeduccionesActivos() {
        return deduccionRep.findByEstadoDec(EstadoBasicoEnum.ACT);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deduccion> obtenerDeduccionPorId(int idDeduccion) {
        return deduccionRep.findById(idDeduccion);
    }

    @Override
    public void calcularDeduccionesEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes) {

        List<Deduccion> deduccionesActivas = listarDeduccionesActivos();
        List<ImpuestoRenta> listaRentasActivas = rentaService.listarRentasActivasPorAnio(LocalDate.now().getYear());

        // Calcular y actualizar datos del empleado por mes
        for (EmpleadoLaboralDTO empleado : infoEmpleadoPorMes) {

            BigDecimal totalDeducciones = BigDecimal.ZERO;
            BigDecimal salarioBruto = empleado.getSalarioBruto();

            List<Pension> listaPensiones = pensionService.listarPensionesActivasEmpleado(empleado.getIdEmpleado());

            // Calcular los montos de las pensiones y restar al salario bruto
            for (Pension pension : listaPensiones) {
                BigDecimal monto = pension.getMontoPen();

                // Validar si el empleado tiene suficientes fondos
                if (totalDeducciones.add(monto).compareTo(salarioBruto) <= 0) {
                    totalDeducciones = totalDeducciones.add(monto);
                    empleado.getPensiones().add(pension);
                }
            }

            // Calcular montos de los deducciones para el empleado
            for (Deduccion deduccion : deduccionesActivas) {
                BigDecimal monto = calcularMontoDeduccion(deduccion, empleado);

                // Validar si el empleado tiene suficientes fondos
                if (totalDeducciones.add(monto).compareTo(salarioBruto) <= 0) {

                    // Guardar asunto y monto del deduccion en el DTO
                    totalDeducciones = totalDeducciones.add(monto);
                    empleado.getMontosDeducciones().add(new DeduccionEmpleadoDTO(deduccion.getIdDeduccion(), deduccion.getAsuntoDec(), monto));

                }

            }

            // Calcular monto de los impuestos de renta
            BigDecimal montoImpuestosRenta = calcularMontoRentas(listaRentasActivas, empleado.getSalarioBruto(), 0);

            totalDeducciones = totalDeducciones.add(montoImpuestosRenta);
            empleado.setSalarioNeto(empleado.getSalarioBruto().subtract(totalDeducciones).max(BigDecimal.ZERO));

            empleado.setTotalImpuestosRentas(montoImpuestosRenta);
            empleado.setImpuestoRentas(listaRentasActivas);
        }

    }

    private BigDecimal calcularMontoDeduccion(Deduccion deduccion, EmpleadoLaboralDTO empleado) {

        BigDecimal monto = BigDecimal.ZERO;

        // Validar a que categoria se le aplica la deduccion
        boolean aplicaCategoria = deduccion.getCategoriaAplicaDec() == empleado.getCategoria()
                || deduccion.getCategoriaAplicaDec() == 0;

        if (!aplicaCategoria)
            return BigDecimal.ZERO;

        // Validar si es necesario la cantidad de años laborados
        if (deduccion.isNecesitaAniosDec() && empleado.getAniosServicio() > 0) {

            // Multiplicar el valor entre los años que se ha trabajado
            monto = deduccion.getValorFijoDec().multiply(BigDecimal.valueOf(empleado.getAniosServicio()));

            // Validar tipo de valor
            if (deduccion.isEsPorcentajeDec())
                monto = monto.multiply(empleado.getSalarioBruto());
            else
                monto = monto.add(empleado.getSalarioBruto());

        } else if (deduccion.isNecesitaPuntosProfDec()) { // Validar si necesita los puntos profesionales

            // Multiplicar valor entre los puntos profesionales
            monto = deduccion.getValorFijoDec().multiply(BigDecimal.valueOf(empleado.getPuntosProfesionales()));

            if (deduccion.isEsPorcentajeDec()) {
                // Multiplicar porcentaje entre el salario
                monto = monto.multiply(empleado.getSalarioBruto());
            }

        } else {

            if (deduccion.isEsPorcentajeDec()) {
                // Multiplicar porcentaje entre el salario
                monto = deduccion.getValorFijoDec().multiply(empleado.getSalarioBruto());
            } else {
                // Sumar monto al salario base
                monto = deduccion.getValorFijoDec();
            }

        }

        return monto;
    }

    // Calcular el monto de la renta de manera recursiva
    private BigDecimal calcularMontoRentas(List<ImpuestoRenta> impuestos, BigDecimal salarioBruto, int numImpuesto) {

        // Validar si ya no hay impuestos por calcular
        if (numImpuesto >= impuestos.size())
            return BigDecimal.ZERO;

        ImpuestoRenta impuesto = impuestos.get(numImpuesto);
        BigDecimal montoExceso;

        // Validar si el salario bruto es menor o igual al minimo del impuesto de la
        // renta
        if (salarioBruto.compareTo(impuesto.getMontoMinimo()) <= 0)
            return BigDecimal.ZERO;

        // Validar si el tope a validar es el máximo
        if (impuesto.getMontoMaximo().compareTo(BigDecimal.ZERO) == 0)
            montoExceso = salarioBruto.subtract(impuesto.getMontoMinimo().max(BigDecimal.ZERO));
        else
            montoExceso = salarioBruto.min(impuesto.getMontoMaximo())
                    .subtract(impuesto.getMontoMinimo().max(BigDecimal.ZERO));

        if (montoExceso.compareTo(BigDecimal.ZERO) <= 0)
            montoExceso = BigDecimal.ZERO;

        // Calcular el impuesto para este tope
        BigDecimal porcentaje = BigDecimal.valueOf(impuesto.getPorcentajeImpuesto()).divide(BigDecimal.valueOf(100));
        BigDecimal montoImpuesto = montoExceso.multiply(porcentaje);

        return montoImpuesto.add(calcularMontoRentas(impuestos, salarioBruto, numImpuesto + 1));
    }
}
