package edu.uees.tutorias.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de ReservaBuilder: valores por defecto de los campos opcionales,
 * configuracion completa via fluent API, y validacion de campos
 * obligatorios antes de construir.
 */
class ReservaBuilderTest {

    private Docente docente;
    private Estudiante estudiante;
    private HorarioDisponible horario;

    @BeforeEach
    void configurar() {
        docente = new Docente("D1", "Jaime Sayago", "jsayago@uees.edu.ec");
        estudiante = new Estudiante("E1", "Ana Torres", "ana.torres@uees.edu.ec", "Computacion");
        horario = docente.publicarHorario("H1", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1));
    }

    @Test
    void construyeUnaReservaMinimaConValoresPorDefecto() {
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .horario(horario)
                .build();

        assertNotNull(reserva.getId());
        assertEquals(ModalidadTutoria.PRESENCIAL, reserva.getModalidad());
        assertEquals("", reserva.getMotivo());
        assertEquals("", reserva.getObservaciones());
        assertTrue(reserva.isRecordatorio());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
    }

    @Test
    void construyeUnaReservaCompletaConTodosLosCamposOpcionales() {
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .horario(horario)
                .id("R-100")
                .modalidad(ModalidadTutoria.VIRTUAL)
                .motivo("Revision de proyecto final")
                .observaciones("Traer diagrama UML actualizado")
                .recordatorio(false)
                .build();

        assertEquals("R-100", reserva.getId());
        assertEquals(ModalidadTutoria.VIRTUAL, reserva.getModalidad());
        assertEquals("Revision de proyecto final", reserva.getMotivo());
        assertEquals("Traer diagrama UML actualizado", reserva.getObservaciones());
        assertFalse(reserva.isRecordatorio());
    }

    @Test
    void noSePuedeConstruirSinEstudiante() {
        assertThrows(IllegalStateException.class,
                () -> new ReservaBuilder().horario(horario).build());
    }

    @Test
    void noSePuedeConstruirSinHorario() {
        assertThrows(IllegalStateException.class,
                () -> new ReservaBuilder().estudiante(estudiante).build());
    }
}
