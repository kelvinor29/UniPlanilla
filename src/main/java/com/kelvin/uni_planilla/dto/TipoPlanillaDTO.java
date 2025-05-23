package com.kelvin.uni_planilla.dto;

public class TipoPlanillaDTO {
    private final String value;
    private final String label;

    public TipoPlanillaDTO(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

}
