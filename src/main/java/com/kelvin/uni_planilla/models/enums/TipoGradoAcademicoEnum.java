package com.kelvin.uni_planilla.models.enums;

public enum TipoGradoAcademicoEnum {
    NO_APLICA("No Aplica"),
    TECNICO("Técnico"),
    DIPLOMADO("Diplomado"),
    BACHILLERATO("Bachillerato"),
    LICENCIATURA("Licenciatura"),
    MAESTRIA("Maestría");

    private final String tipoGrado;

    TipoGradoAcademicoEnum(String tipoGrado) {
        this.tipoGrado = tipoGrado;
    }

    @Override
    public String toString() {
        return tipoGrado;
    }
}
