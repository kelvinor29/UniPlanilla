package com.kelvin.uni_planilla.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.kelvin.uni_planilla.utils.PlanillaUtil;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kelvin.uni_planilla.dto.TipoPlanillaDTO;
import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;
import com.kelvin.uni_planilla.services.IPlanillaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/planillas")
public class PlanillaController {

    @Autowired
    private IPlanillaService planillaService;

    @GetMapping("/")
    public String calculo(Model model) {
        int mesActual = LocalDate.now().getMonthValue();

        model.addAttribute("mesesCalculo", PlanillaUtil.obtenerMesesDisponibles(mesActual));
        model.addAttribute("nombreMeses", PlanillaUtil.obtenerTodosMeses());
        model.addAttribute("anios", PlanillaUtil.obtenerAniosDisponibles(mesActual));
        model.addAttribute("fechaActual", LocalDate.now());

        return "planillas/index";
    }

    @GetMapping("/tiposPlanilla")
    @ResponseBody
    public List<TipoPlanillaDTO> obtenerTiposPlanillaPorMes(@RequestParam int mes, @RequestParam int anio, Model model) {
        List<TipoPlanillaEnum> tiposPlanilla = planillaService.obtenerTiposPlanillaPorMes(mes);

        return tiposPlanilla.stream()
                .map(tp -> new TipoPlanillaDTO(tp.name(), tp.getTipoPlanilla()))
                .collect(Collectors.toList());
    }

    @PostMapping("/calcular")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    

}
