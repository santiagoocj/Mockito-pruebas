package org.santiago.appmockito.ejemplos.repositories;

import org.santiago.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl  implements ExamenRepository{

    //NOTA: Al implementar mockito ya no es necesario crear ExamenRepositoryImpl
    @Override
    public List<Examen> findAll() {
        return Arrays.asList(new Examen(1L,"Matemáticas"),
                new Examen(2L, "Español"),
                new Examen(3L, "Ingles"),
                new Examen(4L, "Informática"));
    }

    @Override
    public Examen guardar(Examen examen) {
        return null;
    }
}
