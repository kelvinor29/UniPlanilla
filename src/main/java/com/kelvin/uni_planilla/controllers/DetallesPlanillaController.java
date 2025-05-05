package com.kelvin.uni_planilla.controllers;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Planilla;
import com.kelvin.uni_planilla.services.IDetallesPlanillaService;
import com.kelvin.uni_planilla.services.IPlanillaService;

@Controller
@RequestMapping("/resumen")
public class DetallesPlanillaController {

    @Autowired
    private IDetallesPlanillaService detallesService;

    @Autowired
    private IPlanillaService planillaService;

    @GetMapping("/{idDetallePlanilla}")
    public String verDetallesEmpleadoPlanilla(@PathVariable("idDetallePlanilla") int idDetallePlanilla, Model model) {

        EmpleadoLaboralDTO empleadoDTO = detallesService.obtenerDetallesPlanilla(idDetallePlanilla);
        
        DateTimeFormatter fechaFormateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        model.addAttribute("fechaCalculoFormateada", empleadoDTO.getFechaCalculo().format(fechaFormateador));
        model.addAttribute("empDetp", empleadoDTO);

        Planilla planilla = planillaService.obtenerPlanillaPorIdDetalle(idDetallePlanilla);
        model.addAttribute("planilla", planilla);

        return "planillas/detalles";
    }

}
