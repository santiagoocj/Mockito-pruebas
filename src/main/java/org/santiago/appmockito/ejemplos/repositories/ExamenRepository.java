package org.santiago.appmockito.ejemplos.repositories;

import org.santiago.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {

    List<Examen> findAll();

    Examen guardar(Examen examen);
}
