package com.kelvin.uni_planilla.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.kelvin.uni_planilla.models.enums.TipoPlanillaEnum;

// Se crea esta clase para mantener la lógica de fechas de los inputs al realizar el cálculo
public class PlanillaUtil {
    private static final Locale LOCALIZACION_ES = new Locale("es", "ES");

    // Retorna todos los mes
    public static Map<Integer, String> obtenerTodosMeses() {
        Map<Integer, String> meses = new LinkedHashMap<>();

        // Obtener clave y valores del enum
        for (Month mes : Month.values())
            meses.put(mes.getValue(), capitalizar(mes.getDisplayName(TextStyle.FULL, LOCALIZACION_ES)));

        return meses;
    }

    // Listar los meses que el usuario puede seleccionar (2)
    public static List<Integer> obtenerMesesDisponibles(int mesActual) {

        // Validar si estamos en enero
        if (mesActual > 1)
            return List.of(mesActual - 1, mesActual); // Diciembre y enero
        else
            return List.of(12, mesActual);
    }

    // Lista de años permitidos: actual y anteriors si es enero
    public static List<Integer> obtenerAniosDisponibles(int mesActual) {
        int anioActual = LocalDate.now().getYear();

        // Validar si estamos en enero
        if (mesActual == 1)
            return List.of(anioActual - 1, anioActual); // Agregar año anterior y actual

        return List.of(anioActual); // Solo año actual
    }

    // Función comúnmente utilizada para que la primera letra de un texto sea
    // mayúscula
    private static String capitalizar(String txt) {
        if (txt == null || txt.isEmpty())
            return txt;
        return txt.substring(0, 1).toUpperCase(LOCALIZACION_ES) +
                txt.substring(1);
    }

}
