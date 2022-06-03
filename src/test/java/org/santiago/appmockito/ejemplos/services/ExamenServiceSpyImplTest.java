package org.santiago.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.santiago.appmockito.ejemplos.models.Examen;
import org.santiago.appmockito.ejemplos.repositories.ExamenRepository;
import org.santiago.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.santiago.appmockito.ejemplos.repositories.PreguntaRepository;
import org.santiago.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceSpyImplTest {

    @InjectMocks
    ExamenServiceImpl service;

    @Spy
    ExamenRepositoryImpl repository;
    @Spy
    PreguntaRepositoryImpl preguntaRepository;


    @Test
    void testSpy(){
        // si se hace el llamado al mock con when el sistema da alguna manera también tomara la implementación del repositorio y no los datos simulados
       // Mockito.when(preguntaRepository1.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Mockito.doReturn(Datos.PREGUNTAS).when(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(1l, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());
    }

    @Test
    void testOrdenDeInvocaciones(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matemáticas");
        service.findExamenPorNombreConPreguntas("Español");

        InOrder inOrder = Mockito.inOrder(repository, preguntaRepository);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(1L);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(2L);
    }

    @Test
    void testNumeroDeInvocaciones(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matemáticas");

        // Por defecto toma que se ejecuta una vez
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(1L);
        Mockito.verify(preguntaRepository, Mockito.times(1)).findPreguntasPorExamenId(1L);
        Mockito.verify(preguntaRepository, Mockito.atLeast(1)).findPreguntasPorExamenId(1L);
        Mockito.verify(preguntaRepository, Mockito.atLeastOnce()).findPreguntasPorExamenId(1L);
        Mockito.verify(preguntaRepository, Mockito.atMost(5)).findPreguntasPorExamenId(1L);
        Mockito.verify(preguntaRepository, Mockito.atMostOnce()).findPreguntasPorExamenId(1L);
    }

    @Test
    void testNumeroInvocaciones3(){
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        service.findExamenPorNombreConPreguntas("Matemáticas");

        Mockito.verify(preguntaRepository, Mockito.never()).findPreguntasPorExamenId(1L);
        Mockito.verifyNoInteractions(preguntaRepository);

        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(repository, Mockito.atLeastOnce()).findAll();
        Mockito.verify(repository, Mockito.atLeast(1)).findAll();
        Mockito.verify(repository, Mockito.atMost(10)).findAll();
        Mockito.verify(repository, Mockito.atMostOnce()).findAll();
    }
}