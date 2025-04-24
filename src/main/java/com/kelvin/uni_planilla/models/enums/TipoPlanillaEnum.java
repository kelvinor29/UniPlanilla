package com.kelvin.uni_planilla.models.enums;

public enum TipoPlanillaEnum {
    ORDINARIO("Planilla Ordinaria"),
    EXTRAORDINARIO("Planilla Extraordinaria"),
    AGUINALDO("Aguinaldo"),
    SALARIO_ESCOLAR("Salario Escolar");

    private final String tipoPlanilla;

    TipoPlanillaEnum(String tipoPlanilla){
        this.tipoPlanilla = tipoPlanilla;
    }

    public String getTipoPlanilla() {
        return tipoPlanilla;
    }    

}
