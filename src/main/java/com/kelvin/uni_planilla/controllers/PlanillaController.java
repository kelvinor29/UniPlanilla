package com.kelvin.uni_planilla.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;
import com.kelvin.uni_planilla.services.IPlanillaService;
import com.kelvin.uni_planilla.services.impl.PlanillaServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;

import com.kelvin.uni_planilla.dto.PlanillaDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/planillas")
public class PlanillaController {
    private static final Logger logger = LoggerFactory.getLogger(PlanillaController.class);

    @Autowired
    private IPlanillaService planillaService;

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

        for (TipoPlanillaDTO tipoPlanillaDTO : tiposPlanillaDTO) {
            logger.info("Planilla: {}", tipoPlanillaDTO);
        }

        return ResponseEntity.ok(tiposPlanillaDTO);
    }

    @PostMapping("/calcular")
    public String calcularPlanilla(@Valid @ModelAttribute("planilla") PlanillaDTO planillaDTO, BindingResult resultado,
            Model model) {

        if (resultado.hasErrors()) {
            return cargarPlanillaVacia(model);
        }

        // Calculo de la planilla
        planillaService.calcularPlanilla(planillaDTO.getMesCalculado(), planillaDTO.getAnioPl(),
                planillaDTO.getFechaCalculo(),
                planillaDTO.getTipoPlanilla());

        return "redirect:/planillas/";

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
