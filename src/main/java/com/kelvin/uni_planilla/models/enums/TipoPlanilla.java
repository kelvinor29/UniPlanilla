package com.kelvin.uni_planilla.models.enums;

public enum TipoPlanilla {
    ORDINARIO("Ordinario"),
    EXTRAORDINARIO("Extraordinario"),
    AGUINALDO("Aguinaldo"),
    SALARIO_ESCOLAR("Salario Escolar");

    private final String tipoPlanilla;

    TipoPlanilla(String tipoPlanilla){
        this.tipoPlanilla = tipoPlanilla;
    }

    @Override
    public String toString() {
        return tipoPlanilla;
    }
}
