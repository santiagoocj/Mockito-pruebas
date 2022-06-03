package org.santiago.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.santiago.appmockito.ejemplos.models.Examen;
import org.santiago.appmockito.ejemplos.repositories.ExamenRepository;
import org.santiago.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.santiago.appmockito.ejemplos.repositories.PreguntaRepository;
import org.santiago.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    @InjectMocks
    ExamenServiceImpl service;

    @Mock
    ExamenRepository repository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;

    @BeforeEach
    void setUp(){
        // habilitar uso de anotaciones, otra forma es utilizar la anotacion arriba de la
        //clase @ExtendWith(MockitoExtension.class) -> necesita dependencia mockito-junit-jupiter
        MockitoAnnotations.openMocks(this);

//        repository = Mockito.mock(ExamenRepository.class);
//        preguntaRepository = Mockito.mock(PreguntaRepository.class);
//        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findByNombre() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = service.findByNombre("Español");
        assertTrue(examen.isPresent());
        assertEquals(2L, examen.orElseThrow().getId());
        assertEquals("Español", examen.orElseThrow().getNombre());
    }

    @Test
    void findByNombreEmpty() {
        List<Examen> datos = Collections.emptyList();

        Mockito.when(repository.findAll()).thenReturn(datos);

        Optional<Examen> examen = service.findByNombre("Español");
        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntaExamen(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testPreguntaExamenVerify(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());
    }

    @Test
    void testNoExisteExamenVerify(){
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertNull(examen);
        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());
    }

    @Test
    void testGuardarExamen(){
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        Mockito.when(repository.guardar(Mockito.any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 5L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        // When
        Examen examen = service.guardar(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(5L, examen.getId());
        assertEquals("fisica", examen.getNombre());
        Mockito.verify(repository).guardar(Mockito.any(Examen.class));
        Mockito.verify(preguntaRepository).guardarVarias(Mockito.anyList());
    }

    @Test
    void manejoException(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.isNull())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () ->{
            service.findExamenPorNombreConPreguntas("Matemáticas");
        });
    }

    @Test
    void testArgumentMatchers(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matemáticas");

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(argument ->
                argument != null && argument.equals(1L)));
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.eq(1L));
    }

    @Test
    void testArgumentMatchers2(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matemáticas");

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matemáticas");

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat((argument) -> argument != null && argument>0));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long>{

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Es para un mensaje personalizado que imprime mockito en" +
                    "caso de que falle el test"
                    + "debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matemáticas");

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        // con anotaciones @Captor ArgumentCaptor<Long> captor

        Mockito.verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
        assertEquals(1L, captor.getValue());
    }

    //la implementacion doThrow nos permite hacer algo cuando se invoca un método, es al revés
    @Test
    void testDoThrow(){
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        Mockito.doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(Mockito.anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
     //   Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
       Mockito.doAnswer(invocation -> {
           Long id = invocation.getArgument(0);
           return  id == 1L? Datos.PREGUNTAS: Collections.emptyList();
       }).when(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());

       Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
       assertEquals(1L, examen.getId());
       assertEquals("Matemáticas", examen.getNombre());

       Mockito.verify(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());
    }

    @Test
    void testDoCallRealMethod(){
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
  //      Mockito.when(preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        Mockito.doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(Mockito.anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    void testSpy(){
        ExamenRepository examenRepository = Mockito.spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository1 = Mockito.spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository1);

        // si se hace el llamado al mock con when el sistema da alguna manera también tomara la implementación del repositorio y no los datos simulados
       // Mockito.when(preguntaRepository1.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Mockito.doReturn(Datos.PREGUNTAS).when(preguntaRepository1).findPreguntasPorExamenId(Mockito.anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(1l, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        Mockito.verify(examenRepository).findAll();
        Mockito.verify(preguntaRepository1).findPreguntasPorExamenId(Mockito.anyLong());
    }
}