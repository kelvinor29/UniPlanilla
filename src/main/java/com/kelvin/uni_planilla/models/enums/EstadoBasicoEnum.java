package com.kelvin.uni_planilla.models.enums;

public enum EstadoBasicoEnum {
    ACT("Activo"), 
    INA("Inactivo");

    private final String DISPLAY_NAME;

    EstadoBasicoEnum(String displayName) {
        this.DISPLAY_NAME = displayName;
    }

    @Override
    public String toString() {
        return DISPLAY_NAME;
    }
    
}
