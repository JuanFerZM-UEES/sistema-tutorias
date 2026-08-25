package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.Usuario;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.persistence.RepositorioReservasEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de ServicioReservas.
 *
 * Gracias a DIP, en las pruebas se sustituye el Notificador real por un
 * doble de prueba (NotificadorDePrueba) sin tocar ServicioReservas: eso es
 * evidencia concreta de bajo acoplamiento, no solo una afirmacion teorica.
 */
class ServicioReservasTest {

    private RepositorioReservas repositorio;
    private NotificadorDePrueba notificador;
    private ServicioReservas servicioReservas;
    private Docente docente;
    private Estudiante estudiante;
    private HorarioDisponible horario;

    @BeforeEach
    void configurar() {
        repositorio = new RepositorioReservasEnMemoria();
        notificador = new NotificadorDePrueba();
        servicioReservas = new ServicioReservas(repositorio, notificador);

        docente = new Docente("D1", "Jaime Sayago", "jsayago@uees.edu.ec");
        estudiante = new Estudiante("E1", "Ana Torres", "ana.torres@uees.edu.ec", "Computacion");
        horario = docente.publicarHorario("H1", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1));
    }

    @Test
    void solicitarReservaMarcaElHorarioComoOcupadoYQuedaPendiente() {
        Reserva reserva = servicioReservas.solicitarReserva("R1", estudiante, horario);

        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertTrue(!horario.estaDisponible());
        assertEquals(2, notificador.notificaciones.size(), "debe notificar a estudiante y a docente");
    }

    @Test
    void noSePuedeReservarUnHorarioYaOcupado() {
        servicioReservas.solicitarReserva("R1", estudiante, horario);

        assertThrows(HorarioNoDisponibleException.class,
                () -> servicioReservas.solicitarReserva("R2", estudiante, horario));
    }

    @Test
    void confirmarReservaCambiaSuEstadoACConfirmada() {
        Reserva reserva = servicioReservas.solicitarReserva("R1", estudiante, horario);

        servicioReservas.confirmarReserva(reserva.getId());

        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    @Test
    void cancelarReservaLiberaElHorario() {
        Reserva reserva = servicioReservas.solicitarReserva("R1", estudiante, horario);

        servicioReservas.cancelarReserva(reserva.getId(), "Imprevisto del estudiante");

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertTrue(horario.estaDisponible(), "el horario debe quedar libre para otra reserva");
    }

    @Test
    void noSePuedeConfirmarUnaReservaYaCancelada() {
        Reserva reserva = servicioReservas.solicitarReserva("R1", estudiante, horario);
        servicioReservas.cancelarReserva(reserva.getId(), "Motivo");

        assertThrows(IllegalStateException.class,
                () -> servicioReservas.confirmarReserva(reserva.getId()));
    }

    @Test
    void reprogramarReservaLiberaElHorarioAnteriorYOcupaElNuevo() {
        Reserva reserva = servicioReservas.solicitarReserva("R1", estudiante, horario);
        HorarioDisponible nuevoHorario = docente.publicarHorario("H2",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1));

        servicioReservas.reprogramarReserva(reserva.getId(), nuevoHorario);

        assertEquals(EstadoReserva.REPROGRAMADA, reserva.getEstado());
        assertEquals(nuevoHorario, reserva.getHorario());
        assertTrue(horario.estaDisponible(), "el horario original debe liberarse");
        assertTrue(!nuevoHorario.estaDisponible());
    }

    @Test
    void confirmarUnaReservaInexistenteLanzaExcepcion() {
        assertThrows(ReservaNoEncontradaException.class,
                () -> servicioReservas.confirmarReserva("NO-EXISTE"));
    }

    /**
     * Doble de prueba para Notificador: en vez de enviar un correo real,
     * guarda los mensajes para poder verificarlos en las aserciones.
     */
    private static class NotificadorDePrueba implements Notificador {
        final List<String> notificaciones = new ArrayList<>();

        @Override
        public void notificar(Usuario destinatario, String asunto, String mensaje) {
            notificaciones.add(asunto + " -> " + destinatario.getNombre());
        }
    }
}
