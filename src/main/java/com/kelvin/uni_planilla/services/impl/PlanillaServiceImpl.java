package com.kelvin.uni_planilla.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.dto.BeneficioEmpleadoDTO;
import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;
import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Beneficio;
import com.kelvin.uni_planilla.models.DetallePlanilla;
import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.Incapacidad;
import com.kelvin.uni_planilla.models.Planilla;
import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;
import com.kelvin.uni_planilla.models.enums.TipoSalarioEnum;
import com.kelvin.uni_planilla.repositories.PlanillaRepository;
import com.kelvin.uni_planilla.services.IBeneficioService;
import com.kelvin.uni_planilla.services.IDeduccionService;
import com.kelvin.uni_planilla.services.IEmpleadoService;
import com.kelvin.uni_planilla.services.IIncapacidadService;
import com.kelvin.uni_planilla.services.IPermisoService;
import com.kelvin.uni_planilla.services.IPlanillaService;

@Service
public class PlanillaServiceImpl implements IPlanillaService {

    private static final Logger logger = LoggerFactory.getLogger(PlanillaServiceImpl.class);

    @Autowired
    private PlanillaRepository planillaRep;

    @Autowired
    private IEmpleadoService empleadoService;

    @Autowired
    private IIncapacidadService incapacidadService;

    @Autowired
    private IBeneficioService beneficioService;

    @Autowired
    private IDeduccionService deduccionService;

    @Autowired
    private IPermisoService permisoService;

    // Lista de planillas disponibles a seleccionar
    @Override
    public List<TipoPlanillaEnum> obtenerTiposPlanillaPorMes(byte mesActual, short anioActual) {
        List<TipoPlanillaEnum> planillas = new ArrayList<>();

        // Validar si ya se creó alguna de las planillas para excluirlas
        if (!planillaRep.existePlanillaEnMes(mesActual, anioActual, TipoPlanillaEnum.ORDINARIO.name()))
            planillas.add(TipoPlanillaEnum.ORDINARIO);
        else
            planillas.add(TipoPlanillaEnum.EXTRAORDINARIO);

        if (Month.of(mesActual) == Month.DECEMBER
                && !planillaRep.existePlanillaEnMes(mesActual, anioActual, TipoPlanillaEnum.AGUINALDO.name()))
            planillas.add(TipoPlanillaEnum.AGUINALDO);
        else if (Month.of(mesActual) == Month.JANUARY
                && !planillaRep.existePlanillaEnMes(mesActual, anioActual, TipoPlanillaEnum.SALARIO_ESCOLAR.name()))
            planillas.add(TipoPlanillaEnum.SALARIO_ESCOLAR);

        return planillas;
    }

    // Cálculo general de planilla
    @Override
    public void calcularPlanilla(byte mes, short anio, LocalDate fechaActual, TipoPlanillaEnum tipoPlanilla) {

        Planilla planilla = new Planilla();
        planilla.setAnioPl(anio);
        planilla.setMesCalculado(mes);
        planilla.setFechaCalculo(fechaActual);
        planilla.setTipoPlanilla(tipoPlanilla);
        planilla.setFechaPrimerPago(fechaActual.plusDays(14));
        planilla.setFechaSegundoPago(fechaActual.plusDays(27));
        planilla.setPorcentajePrimerPago(0.60);
        planilla.setPorcentajeSegundoPago(0.40);

        List<DetallePlanilla> detallePlanillas = new ArrayList<>();

        // Cálculo del ordinario
        if (tipoPlanilla == TipoPlanillaEnum.ORDINARIO)
            detallePlanillas = calcularDetallesOrdinaria(mes, anio, planilla);

        planilla.setDetallesPlanilla(new HashSet<>(detallePlanillas));
        planillaRep.save(planilla);
    }

    // Cálculo para los detalles de planilla ORDINARIA
    public List<DetallePlanilla> calcularDetallesOrdinaria(byte mes, short anio, Planilla planilla) {

        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        // Listar empleados disponibles para el cálculo de la planilla
        List<Empleado> empleadosValidos = empleadoService.listarEmpleadosParaPlanilla(mes, anio);
        if (empleadosValidos.isEmpty())
            return new ArrayList<>();

        // Mapear para obtener todos los id de los empleados
        List<Integer> idsEmpleados = empleadosValidos.stream().map(Empleado::getIdEmpleado)
                .collect(Collectors.toList());

        // Listado de empleados válidos con su respectivo nombramiento y puesto
        List<EmpleadoLaboralDTO> infoEmpleadosMes = obtenerDatosEmpleadoParaPlanilla(inicioMes, finMes, idsEmpleados);

        List<DetallePlanilla> detalles = new ArrayList<>();

        for (EmpleadoLaboralDTO empleado : infoEmpleadosMes) {
            DetallePlanilla detalleP = new DetallePlanilla();

            Empleado emp = empleadoService.obtenerEmpleado(empleado.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado: " + empleado.getIdEmpleado()));

            detalleP.setEmpleado(emp);
            detalleP.setPlanilla(planilla);
            detalleP.setDiasTrabajados((byte) 20); // TODO: Calcular dependiendo de permisos e incapacidades
            detalleP.setDiasPagados((byte) 20); // TODO: Calcular dependiendo de permisos e incapacidades

            detalleP.setSalarioBase(empleado.getSalarioBase());
            detalleP.setSalarioBruto(empleado.getSalarioBruto());
            detalleP.setSalarioNeto(empleado.getSalarioNeto());

            List<Integer> listaIdsBeneficios = empleado.getMontosBeneficios().stream()
                    .map(BeneficioEmpleadoDTO::getIdBeneficio).collect(Collectors.toList());
            List<Integer> listaIdsDeducciones = empleado.getMontosDeducciones().stream()
                    .map(DeduccionEmpleadoDTO::getIdDeduccion).collect(Collectors.toList());
            detalleP.setDetallesBeneficios(beneficioService.listarBeneficiosPorIds(listaIdsBeneficios));

            detalles.add(detalleP);

        }

        return detalles;
    }

    // Obtener datos importantes por mes del empleado para la planilla
    @Override
    public List<EmpleadoLaboralDTO> obtenerDatosEmpleadoParaPlanilla(LocalDate inicioMes,
            LocalDate finMes, List<Integer> idsEmpleados) {

        // Consulta de datos básicos principales por empleado
        List<EmpleadoLaboralDTO> infoEmpleadoPorMes = planillaRep.obtenerDatosEmpleadosPlanilla(idsEmpleados, inicioMes,
                finMes);

        if (infoEmpleadoPorMes == null || infoEmpleadoPorMes.isEmpty())
            return Collections.emptyList();

        // Calcular los beneficios activos para el empleado
        beneficioService.calcularBeneficiosEmpleadosPorMes(infoEmpleadoPorMes);
        // Calcular Deducciones y actualizar datos
        deduccionService.calcularDeduccionesEmpleadosPorMes(infoEmpleadoPorMes);

        // Calcular los permisos
        permisoService.calcularPermisosEmpleadosPorMes(infoEmpleadoPorMes, inicioMes.minusMonths(1),
                finMes.minusMonths(1));

        // Calcular subsidios de cada empleado y actualizar la lista
        incapacidadService.calcularIncapacidadYSubsidioEmpleadosPorMes(infoEmpleadoPorMes, inicioMes.minusMonths(1),
                finMes.minusMonths(1));

        // for (EmpleadoLaboralDTO empleado : infoEmpleadoPorMes) {
        // empleado.setSalarioNeto(empleado.getSalarioBruto().add(empleado.getSubsidioTotal()));
        // logger.info("====================Final====================\n");
        // logger.info("Empleado: {}", empleado.getIdEmpleado());
        // logger.info("Salario: {}", empleado.getTipoSalario());
        // logger.info("Categoria: {}", empleado.getCategoria());
        // logger.info("Anios Servicio: {}", empleado.getAniosServicio());
        // logger.info("Puntos: {}", empleado.getPuntosProfesionales());
        // logger.info("------------------------------------------");
        // logger.info("SalarioBase: {}", empleado.getSalarioBase());
        // logger.info("SalarioBruto: {}", empleado.getSalarioBruto());
        // logger.info("Subsidio Total: {}", empleado.getSubsidioTotal());
        // logger.info("Salario Neto: {}", empleado.getSalarioNeto());
        // logger.info("----------------Beneficios----------------\n");
        // empleado.getMontosBeneficios()
        // .forEach(beneficio -> logger.info("{}: {}", beneficio.getAsuntoBen(),
        // beneficio.getMontoBen()));
        // logger.info("----------------Deducciones----------------\n");
        // empleado.getMontosDeducciones()
        // .forEach(dec -> logger.info("{}: {}", dec.getAsuntoDec(),
        // dec.getMontoDec()));
        // logger.info("------------------------------------------");
        // logger.info("Dias Incapacidad: {}", empleado.getDiasIncapacidad());
        // logger.info("Dias Permiso Goce: {}", empleado.getDiasPermisoConGoce());
        // logger.info("Dias Permiso Sin Goce: {}", empleado.getDiasPermisoSinGoce());
        // logger.info("------------------------------------------");

        // logger.info("=========================================\n");
        // }

        return infoEmpleadoPorMes;
    }

}
