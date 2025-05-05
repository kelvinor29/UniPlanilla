package com.kelvin.uni_planilla.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kelvin.uni_planilla.dto.BeneficioEmpleadoDTO;
import com.kelvin.uni_planilla.dto.EmpleadoLaboralDTO;
import com.kelvin.uni_planilla.models.Beneficio;
import com.kelvin.uni_planilla.models.enums.EstadoBasicoEnum;
import com.kelvin.uni_planilla.models.enums.TipoSalarioEnum;
import com.kelvin.uni_planilla.repositories.BeneficioRepository;
import com.kelvin.uni_planilla.services.IBeneficioService;

@Service
public class BeneficioServiceImpl implements IBeneficioService {

    @Autowired
    private BeneficioRepository beneficioRep;

    @Override
    @Transactional(readOnly = true)
    public List<Beneficio> listarBeneficiosActivos() {
        return beneficioRep.findByEstadoBen(EstadoBasicoEnum.ACT);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Beneficio> obtenerBeneficioPorId(int idBeneficio){
        return beneficioRep.findById(idBeneficio);
    }

    @Override
    public void calcularBeneficiosEmpleadosPorMes(List<EmpleadoLaboralDTO> infoEmpleadoPorMes) {

        List<Beneficio> beneficiosActivos = listarBeneficiosActivos();

        infoEmpleadoPorMes.stream().filter(e -> e.getTipoSalario() == TipoSalarioEnum.Global)
                .forEach(e -> e.setSalarioBruto(e.getSalarioBase()));

        // Calcular y actualizar datos del empleado del mes (Solo empleados con salario
        // compuesto)
        for (EmpleadoLaboralDTO empleado : infoEmpleadoPorMes.stream()
                .filter(e -> e.getTipoSalario() == TipoSalarioEnum.Compuesto).toList()) {

            BigDecimal totalBeneficios = BigDecimal.ZERO;

            // Calcular montos de los beneficios para el empleado
            for (Beneficio beneficio : beneficiosActivos) {
                BigDecimal monto = calcularMontoBeneficio(beneficio, empleado);
                totalBeneficios = totalBeneficios.add(monto);

                // Guardar asunto y monto del beneficio en el DTO
                empleado.getMontosBeneficios().add(new BeneficioEmpleadoDTO(beneficio.getIdBeneficio(), beneficio.getAsuntoBen(), monto));

            }
            // Actualizar el salario bruto
            empleado.setSalarioBruto(empleado.getSalarioBase().add(totalBeneficios));
        }

    }

    private BigDecimal calcularMontoBeneficio(Beneficio beneficio, EmpleadoLaboralDTO empleado) {

        // Validar si el beneficio aplica en la categoria del empleado
        if (!aplicaACategoria(beneficio.getCategoriaAplicaBen(), empleado.getCategoria()))
            return BigDecimal.ZERO;

        BigDecimal monto = BigDecimal.ZERO;

        // Validar el tipo de beneficio y realizar su multiplicacion correspondiente
        if (beneficio.isNecesitaAniosBen()){
            if(empleado.getAniosServicio() > 0) 
            monto = beneficio.getValorBen().multiply(BigDecimal.valueOf(empleado.getAniosServicio()));
        } else if (beneficio.isNecesitaPuntosProfBen())
            monto = beneficio.getValorBen().multiply(BigDecimal.valueOf(empleado.getPuntosProfesionales()));
        else
            monto = beneficio.getValorBen();

        // Validar si es un porcentaje y multiplicarlo por el salario base y si no se
        // retorna el monto
        return beneficio.isEsPorcentajeBen() ? monto.multiply(empleado.getSalarioBase()) : monto;
    }

    public boolean aplicaACategoria(int categoriaBen, int categoriaEmpleado) {
        // Validar si aplica a la misma categoria del empleado o es para cualquier
        // categoria (0)
        return categoriaBen == categoriaEmpleado || categoriaBen == 0;
    }

}
