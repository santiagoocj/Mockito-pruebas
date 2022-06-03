package org.santiago.appmockito.ejemplos.services;

import org.santiago.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(1L,"Matemáticas"),
            new Examen(2L, "Español"),
            new Examen(3L, "Ingles"),
            new Examen(4L, "Informática"));

    public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-1L,"Matemáticas"),
            new Examen(-2L, "Español"),
            new Examen(-3L, "Ingles"),
            new Examen(-4L, "Informática"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null,"Matemáticas"),
            new Examen(null, "Español"),
            new Examen(null, "Ingles"),
            new Examen(null, "Informática"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmética",
            "integrales", "derivadas", "trigonometría", "geometría");

    public final static Examen EXAMEN = new Examen(null, "fisica");
}
