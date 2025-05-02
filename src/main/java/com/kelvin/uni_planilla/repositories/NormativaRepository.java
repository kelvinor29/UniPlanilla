package com.kelvin.uni_planilla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kelvin.uni_planilla.models.Normativa;
import java.util.List;

@Repository
public interface NormativaRepository extends JpaRepository<Normativa, Integer> {

    public Normativa findByIdNormativa(int idNormativa);

    @Query(value = """
                SELECT  ID_NORMATIVA, PORCENTAJE_NI, RANGO_DIAS_INICIO, RANGO_DIAS_FIN, ESTADO_NI 
                FROM    NORMATIVAS N 
                WHERE   N.ESTADO_NI = 'ACT' 
                ORDER BY N.RANGO_DIAS_INICIO
            """, nativeQuery = true)
    public List<Normativa> listarNormativasActivas();
}
