package com.kelvin.uni_planilla.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kelvin.uni_planilla.models.Empleado;
import com.kelvin.uni_planilla.services.IEmpleadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService empleadoService;

    @GetMapping("/")
    public String listarEmpleados(Model model) {
        return cargarListaEmpleados(model, "");
    }

    @PostMapping("/buscar")
    public String buscarEmpleado(String filtro, Model model) {
        List<Empleado> empleados = empleadoService.listarEmpleados(filtro);
        model.addAttribute("empleados", empleados);
        model.addAttribute("mensaje", "");
        return "empleados/index";
    }

    @PostMapping("/eliminar")
    public String eliminarEmpleado(@RequestParam("idEmpleado") int idEmpleado, Model model) {
        String mensaje = (idEmpleado <= 0)
                ? "No se pudo eliminar el empleado: ID inválido."
                : (empleadoService.eliminarEmpleado(idEmpleado)
                        ? "Empleado eliminado correctamente."
                        : "El empleado no puede ser eliminado porque se encuentra en un nombramiento activo.");

        return cargarListaEmpleados(model, mensaje);
    }

    @GetMapping("/agregar")
    public String agregarEmpleado(Empleado empleado) {
        return "empleados/agregar";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid Empleado empleado, Errors errors) {
        if (errors.hasErrors())
            return "empleado/agregar";

        empleadoService.guardarEmpleado(empleado);
        return "redirect:/empleados/";
    }

    @GetMapping("/editar/{idEmpleado}")
    public String editarEmpleado(Empleado empleado, Model model) {

        return empleadoService.obtenerEmpleado(empleado.getIdEmpleado())
                .map(emp -> {
                    model.addAttribute("empleado", empleado);
                    return "empleado/agregar";
                }).orElse("redirect:/empleados/?error=EmpleadoNoEncontrado");
    }

    // ==============================================================================================================

    private String cargarListaEmpleados(Model model, String mensaje) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        model.addAttribute("mensaje", mensaje);
        return "empleados/index";
    }

}
