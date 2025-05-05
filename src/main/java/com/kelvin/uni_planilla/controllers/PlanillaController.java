package com.kelvin.uni_planilla.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kelvin.uni_planilla.utils.PlanillaUtil;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kelvin.uni_planilla.dto.TipoPlanillaDTO;
import com.kelvin.uni_planilla.models.Planilla;
import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;
import com.kelvin.uni_planilla.services.IDetallesPlanillaService;
import com.kelvin.uni_planilla.services.IPlanillaService;

import org.springframework.web.bind.annotation.PostMapping;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.dto.PlanillaDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/planillas")
public class PlanillaController {

    @Autowired
    private IPlanillaService planillaService;

    @Autowired
    private IDetallesPlanillaService detallesService;

    @GetMapping("/")
    public String calculo(Model model) {
        return cargarPlanillaVacia(model);
    }

    @GetMapping("/tiposPlanilla")
    @ResponseBody
    public ResponseEntity<List<TipoPlanillaDTO>> obtenerTiposPlanillaPorMes(@RequestParam byte mes,
            @RequestParam short anio,
            Model model) {

        List<TipoPlanillaEnum> tiposPlanilla = planillaService.obtenerTiposPlanillaPorMes(mes, anio);
        List<TipoPlanillaDTO> tiposPlanillaDTO = tiposPlanilla.stream()
                .map(tp -> new TipoPlanillaDTO(tp.name(), tp.getTipoPlanilla()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tiposPlanillaDTO);
    }

    @PostMapping("/calcular")
    public String calcularPlanilla(@Valid @ModelAttribute("planilla") Planilla planilla, BindingResult resultado,
            Model model) {

        if (resultado.hasErrors())
            return cargarPlanillaVacia(model);

        if (planillaService.existePlanillaEnMes(planilla.getMesCalculado(), planilla.getAnioPl(),
                planilla.getTipoPlanilla().name()))
            return cargarPlanillaVacia(model);

        // Calculo de la planilla y obtenerla
        Planilla planillaNueva = planillaService.calcularPlanilla(planilla.getMesCalculado(),
                planilla.getAnioPl(),
                planilla.getFechaCalculo(), planilla.getTipoPlanilla());

        List<EmpleadoLaboralDTO> detalles = detallesService.listarInfoBasicaEmpleados(planillaNueva);

        int totalEmpleados = 0;
        BigDecimal totalSalariosBrutos = BigDecimal.ZERO;
        BigDecimal totalSalarioNeto = BigDecimal.ZERO;

        // Calcular cantidad de empleados sin duplicacion
        totalEmpleados = detalles.stream().map(EmpleadoLaboralDTO::getIdEmpleado).collect(Collectors.toSet()).size();

        for (EmpleadoLaboralDTO d : detalles) {
            totalSalariosBrutos = totalSalariosBrutos.add(d.getSalarioBruto());
            totalSalarioNeto = totalSalarioNeto.add(d.getSalarioNeto());
        }

        model.addAttribute("planilla", planilla);
        model.addAttribute("empleados", detalles);
        model.addAttribute("totalEmpleados", totalEmpleados);
        model.addAttribute("totalSalariosBrutos", totalSalariosBrutos);
        model.addAttribute("totalSalarioNeto", totalSalarioNeto);

        model.addAttribute("mesesCalculo", planilla.getMesCalculado());
        model.addAttribute("nombreMeses", PlanillaUtil.obtenerTodosMeses());
        model.addAttribute("anios", planilla.getAnioPl());
        model.addAttribute("fechaActual", planilla.getFechaCalculo());

        return "planillas/index";

    }

    // ====================================================================================================
    public String cargarPlanillaVacia(Model model) {
        int mesActual = LocalDate.now().getMonthValue();

        model.addAttribute("planilla", new PlanillaDTO());
        model.addAttribute("mesesCalculo", PlanillaUtil.obtenerMesesDisponibles(mesActual));
        model.addAttribute("nombreMeses", PlanillaUtil.obtenerTodosMeses());
        model.addAttribute("anios", PlanillaUtil.obtenerAniosDisponibles(mesActual));
        model.addAttribute("fechaActual", LocalDate.now());

        return "planillas/index";
    }

}
