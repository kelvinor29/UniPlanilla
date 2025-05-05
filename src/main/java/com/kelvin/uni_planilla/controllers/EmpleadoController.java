package com.kelvin.uni_planilla.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("mensaje", null);
        return "empleados/index";
    }

    @PostMapping("/eliminar")
    public String eliminarEmpleado(@RequestParam("idEmpleado") int idEmpleado, RedirectAttributes redirect,
            Model model) {
        String mensaje = (idEmpleado <= 0)
                ? "No se pudo eliminar el empleado: ID inválido."
                : (empleadoService.eliminarEmpleado(idEmpleado)
                        ? "Empleado eliminado correctamente."
                        : "El empleado no puede ser eliminado porque se encuentra en un nombramiento activo.");

        redirect.addFlashAttribute("mensaje", mensaje);
        return "redirect:/empleados/";
    }

    @GetMapping("/agregar")
    public String agregarEmpleado(Empleado empleado, Model model) {
        model.addAttribute("modoEdicion", false);
        return "empleados/agregar";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado, RedirectAttributes redirect,
            BindingResult resultado, Model model) {

        // Validar datos unicos
        if (empleadoService.esCedulaRepetida(empleado.getCedula(), empleado.getIdEmpleado()))
            resultado.rejectValue("cedula", "error.empleado", "Ya existe un empleado con esta cédula.");

        if (empleadoService.esTelefonoRepetido(empleado.getTelefono(), empleado.getIdEmpleado()))
            resultado.rejectValue("telefono", "error.empleado", "Ya existe un empleado con este teléfono.");

        if (empleadoService.esCorreoRepetido(empleado.getCorreoElectronico(), empleado.getIdEmpleado()))
            resultado.rejectValue("correoElectronico", "error.empleado", "Ya existe un empleado con este correo.");

        if (resultado.hasErrors()) {
            model.addAttribute("modoEdicion", empleado.getIdEmpleado() != 0);
            return "empleados/agregar";
        }

        empleadoService.guardarEmpleado(empleado);
        redirect.addFlashAttribute("mensaje", "Empleado guardado correctamente!");
        return "redirect:/empleados/";
    }

    @GetMapping("/editar/{idEmpleado}")
    public String editarEmpleado(@PathVariable("idEmpleado") int id, Model model) {

        return empleadoService.obtenerEmpleado(id)
                .map(emp -> {
                    model.addAttribute("empleado", emp);
                    model.addAttribute("modoEdicion", true);
                    return "empleados/agregar";
                }).orElse("redirect:/empleados/?error=EmpleadoNoEncontrado"); //TODO: Generar el html de error
    }

    private String cargarListaEmpleados(Model model, String mensaje) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        model.addAttribute("mensaje", mensaje);
        return "empleados/index";
    }

}
