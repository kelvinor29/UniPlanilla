package com.kelvin.uni_planilla.models.enums;

public enum TipoGradoAcademico {
    NO_APLICA("No Aplica"),
    TECNICO("Técnico"),
    BACHILLERATO("Bachillerato"),
    LICENCIATURA("Licenciatura"),
    MAESTRIA("Maestría"),
    DIPLOMADO("Diplomado");

    private final String tipoGrado;

    TipoGradoAcademico(String tipoGrado){
        this.tipoGrado = tipoGrado;
    }

    @Override
    public String toString() {
        return tipoGrado;
    }
}
