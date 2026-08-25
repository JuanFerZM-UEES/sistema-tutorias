package edu.uees.tutorias;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorEmail;
import edu.uees.tutorias.persistence.RepositorioReservasEnMemoria;
import edu.uees.tutorias.service.RepositorioReservas;
import edu.uees.tutorias.service.ServicioReservas;

import java.time.LocalDateTime;

/**
 * Punto de entrada de demostracion. Muestra el flujo tipico: un docente
 * publica un horario, un estudiante lo reserva, la tutoria se confirma y,
 * en otro escenario, una reserva se cancela liberando el horario.
 *
 * Nota de diseno: esta clase es la unica que decide QUE implementaciones
 * concretas usar (RepositorioReservasEnMemoria, NotificadorEmail). El
 * resto del sistema (ServicioReservas) solo conoce las interfaces.
 */
public class App {

    public static void main(String[] args) {
        RepositorioReservas repositorio = new RepositorioReservasEnMemoria();
        Notificador notificador = new NotificadorEmail();
        ServicioReservas servicioReservas = new ServicioReservas(repositorio, notificador);

        Docente docente = new Docente("D1", "Jaime Sayago", "jsayago@uees.edu.ec");
        Estudiante estudiante = new Estudiante("E1", "Juan Fernando", "juan.zhingri@gmail.com", "Computacion");

        HorarioDisponible horario = docente.publicarHorario(
                "H1", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));

        Reserva reserva = servicioReservas.solicitarReserva("R1", estudiante, horario);
        System.out.println("Estado tras solicitar: " + reserva.getEstado());

        servicioReservas.confirmarReserva(reserva.getId());
        System.out.println("Estado tras confirmar: " + reserva.getEstado());

        servicioReservas.cancelarReserva(reserva.getId(), "El estudiante ya no puede asistir");
        System.out.println("Estado tras cancelar: " + reserva.getEstado());
        System.out.println("Horario liberado, disponible = " + horario.estaDisponible());
    }
}
