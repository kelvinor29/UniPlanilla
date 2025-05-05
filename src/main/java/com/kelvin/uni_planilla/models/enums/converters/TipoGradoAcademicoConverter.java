package com.kelvin.uni_planilla.models.enums.converters;

import com.kelvin.uni_planilla.models.enums.TipoGradoAcademicoEnum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoGradoAcademicoConverter implements AttributeConverter<TipoGradoAcademicoEnum, String> {

    @Override
    public String convertToDatabaseColumn(TipoGradoAcademicoEnum attribute) {
        if (attribute == null || attribute == TipoGradoAcademicoEnum.NO_APLICA)
            return null;

        return attribute.toString();
    }

    @Override
    public TipoGradoAcademicoEnum convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return TipoGradoAcademicoEnum.NO_APLICA;

        for (TipoGradoAcademicoEnum tipoGrado : TipoGradoAcademicoEnum.values()) {
            if (tipoGrado.toString().equals(dbData))
                return tipoGrado;
        }
        throw new IllegalArgumentException(dbData);
    }

}
