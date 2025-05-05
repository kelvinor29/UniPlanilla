package com.kelvin.uni_planilla.services.impl;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.uni_planilla.dto.BeneficioEmpleadoDTO;
import com.kelvin.uni_planilla.dto.DeduccionEmpleadoDTO;
import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Beneficio;
import com.kelvin.uni_planilla.models.Deduccion;
import com.kelvin.uni_planilla.models.DetalleBeneficio;
import com.kelvin.uni_planilla.models.DetalleDeduccion;
import com.kelvin.uni_planilla.models.DetallePlanilla;
import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.models.Planilla;
import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;
import com.kelvin.uni_planilla.repositories.PlanillaRepository;
import com.kelvin.uni_planilla.services.IBeneficioService;
import com.kelvin.uni_planilla.services.IDeduccionService;
import com.kelvin.uni_planilla.services.IEmpleadoService;
import com.kelvin.uni_planilla.services.IIncapacidadService;
import com.kelvin.uni_planilla.services.IPermisoService;
import com.kelvin.uni_planilla.services.IPlanillaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PlanillaServiceImpl implements IPlanillaService {

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
    @Transactional
    public Planilla calcularPlanilla(byte mes, short anio, LocalDate fechaActual, TipoPlanillaEnum tipoPlanilla) {

        Planilla planilla = new Planilla();
        planilla.setAnioPl(anio);
        planilla.setMesCalculado(mes);
        planilla.setFechaCalculo(fechaActual);
        planilla.setTipoPlanilla(tipoPlanilla);

        planilla.setFechaPrimerPago(fechaActual.plusDays(14));
        planilla.setFechaSegundoPago(fechaActual.plusDays(27));
        planilla.setPorcentajePrimerPago(0.40);
        planilla.setPorcentajeSegundoPago(0.60);

        List<DetallePlanilla> detallePlanillas = new ArrayList<>();

        // Cálculo del ordinario
        if (tipoPlanilla == TipoPlanillaEnum.ORDINARIO)
            detallePlanillas = calcularDetallesOrdinaria(mes, anio, planilla);

        planilla.setDetallesPlanilla(new ArrayList<>(detallePlanillas));
        return planillaRep.save(planilla);
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

        return infoEmpleadosMes.stream().map(e -> crearDetallePlanilla(e, planilla)).collect(Collectors.toList());
    }

    private DetallePlanilla crearDetallePlanilla(EmpleadoLaboralDTO empleadoDTO, Planilla planilla) {
        Empleado empleado = empleadoService.obtenerEmpleado(empleadoDTO.getIdEmpleado())
                .orElseThrow(() -> new EntityNotFoundException("Empleano no disponible."));

        byte diasTrabajados = (byte) Math.max(0, 20 - (empleadoDTO.getDiasIncapacidad()
                + empleadoDTO.getDiasPermisoConGoce() + empleadoDTO.getDiasPermisoSinGoce()));
        byte diasPagados = (byte) Math.max(0, 20 - (empleadoDTO.getDiasPermisoSinGoce()));

        DetallePlanilla detalle = new DetallePlanilla();
        detalle.setEmpleado(empleado);
        detalle.setPlanilla(planilla);
        detalle.setDiasTrabajados(diasTrabajados);
        detalle.setDiasPagados(diasPagados);

        // Info Salarios
        detalle.setSalarioBase(empleadoDTO.getSalarioBase());
        detalle.setSalarioBruto(empleadoDTO.getSalarioBruto());
        detalle.setSalarioNeto(empleadoDTO.getSalarioNeto());

        // Listas
        detalle.setDetallesBeneficios(guardarBeneficiosEmpleado(empleadoDTO, detalle));
        detalle.setDetallesDeducciones(guardarDeduccionesEmpleado(empleadoDTO, detalle));

        detalle.setImpuestosRenta(new ArrayList<>(empleadoDTO.getImpuestoRentas()));

        return detalle;
    }

    private List<DetalleBeneficio> guardarBeneficiosEmpleado(EmpleadoLaboralDTO empleado, DetallePlanilla detalle) {
        List<DetalleBeneficio> listaDeducciones = new ArrayList<>();

        // Por cada beneficio se guarda en la tabla intermedia
        for (BeneficioEmpleadoDTO deduccionDTO : empleado.getMontosBeneficios()) {
            Beneficio beneficio = beneficioService.obtenerBeneficioPorId(deduccionDTO.getIdBeneficio())
                    .orElseThrow(() -> new EntityNotFoundException("Beneficio no disponible."));

            DetalleBeneficio detalleDec = new DetalleBeneficio();
            detalleDec.setBeneficio(beneficio);
            detalleDec.setDetallePlanilla(detalle);
            detalleDec.setMontoBeneficio(deduccionDTO.getMontoBen());

            listaDeducciones.add(detalleDec);
        }

        return listaDeducciones;
    }

    private List<DetalleDeduccion> guardarDeduccionesEmpleado(EmpleadoLaboralDTO empleado, DetallePlanilla detalle) {
        List<DetalleDeduccion> listaBeneficios = new ArrayList<>();

        // Por cada deducción se guarda en la tabla intermedia
        for (DeduccionEmpleadoDTO deduccionDTO : empleado.getMontosDeducciones()) {
            Deduccion deduccion = deduccionService.obtenerDeduccionPorId(deduccionDTO.getIdDeduccion())
                    .orElseThrow(() -> new EntityNotFoundException("Deducción no disponible."));

            DetalleDeduccion detalleDec = new DetalleDeduccion();
            detalleDec.setDeduccion(deduccion);
            detalleDec.setDetallePlanilla(detalle);
            detalleDec.setMontoDeduccion(deduccionDTO.getMontoDec());

            listaBeneficios.add(detalleDec);
        }

        return listaBeneficios;
    }


    @Override
    public boolean existePlanillaEnMes(byte mesCalculado, short anioPl, String tipoPlanilla){
        return planillaRep.existePlanillaEnMes(mesCalculado, anioPl, tipoPlanilla);
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

        return infoEmpleadoPorMes;
    }

    @Override
    public List<Planilla> listarPlanillasExistentes() {
        return planillaRep.findAll();
    }

    @Override
    public Planilla obtenerPlanillaPorIdDetalle(int idDetallePlanilla) {
        return planillaRep.obtenerPlanillaPorId(idDetallePlanilla);
    }
}
