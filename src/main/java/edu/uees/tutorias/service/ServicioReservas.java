package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Orquesta el ciclo de vida de una Reserva: solicitud, confirmacion,
 * cancelacion y reprogramacion.
 *
 * SRP: esta clase solo coordina reglas de negocio de reservas. No sabe
 * como se guarda una reserva (delega en RepositorioReservas) ni como se
 * notifica a un usuario (delega en Notificador), ni valida por si misma
 * transiciones de estado (delega en Reserva). Si cambia la tecnologia de
 * persistencia, el proveedor de notificaciones o el formato de reportes,
 * esta clase no deberia modificarse.
 *
 * DIP: recibe sus colaboradores (RepositorioReservas, Notificador) por
 * constructor, como abstracciones. No crea instancias concretas
 * internamente, por lo que no queda acoplada a una tecnologia especifica.
 */
public class ServicioReservas {

    private final RepositorioReservas repositorio;
    private final Notificador notificador;

    public ServicioReservas(RepositorioReservas repositorio, Notificador notificador) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public Reserva solicitarReserva(String reservaId, Estudiante estudiante, HorarioDisponible horario) {
        if (!horario.estaDisponible()) {
            throw new HorarioNoDisponibleException(
                    "El horario " + horario.getId() + " ya no esta disponible");
        }

        horario.marcarComoOcupado();
        Reserva reserva = new Reserva(reservaId, estudiante, horario, LocalDateTime.now());
        repositorio.guardar(reserva);

        notificador.notificar(estudiante, "Solicitud de tutoria registrada",
                "Tu solicitud para el " + horario.getInicio() + " quedo pendiente de confirmacion.");
        notificador.notificar(horario.getDocente(), "Nueva solicitud de tutoria",
                estudiante.getNombre() + " solicito el horario del " + horario.getInicio() + ".");

        return reserva;
    }

    public Reserva confirmarReserva(String reservaId) {
        Reserva reserva = obtenerOFallar(reservaId);
        reserva.confirmar();
        repositorio.actualizar(reserva);

        notificador.notificar(reserva.getEstudiante(), "Tutoria confirmada",
                "Tu tutoria del " + reserva.getHorario().getInicio() + " fue confirmada.");
        return reserva;
    }

    public Reserva cancelarReserva(String reservaId, String motivo) {
        Reserva reserva = obtenerOFallar(reservaId);
        reserva.cancelar();
        reserva.getHorario().marcarComoDisponible();
        repositorio.actualizar(reserva);

        notificador.notificar(reserva.getEstudiante(), "Tutoria cancelada",
                "Tu tutoria fue cancelada. Motivo: " + motivo);
        notificador.notificar(reserva.getHorario().getDocente(), "Tutoria cancelada",
                "La tutoria con " + reserva.getEstudiante().getNombre() + " fue cancelada. Motivo: " + motivo);
        return reserva;
    }

    public Reserva reprogramarReserva(String reservaId, HorarioDisponible nuevoHorario) {
        if (!nuevoHorario.estaDisponible()) {
            throw new HorarioNoDisponibleException(
                    "El horario " + nuevoHorario.getId() + " ya no esta disponible");
        }

        Reserva reserva = obtenerOFallar(reservaId);
        HorarioDisponible horarioAnterior = reserva.getHorario();

        nuevoHorario.marcarComoOcupado();
        reserva.reprogramar(nuevoHorario);
        horarioAnterior.marcarComoDisponible();
        repositorio.actualizar(reserva);

        notificador.notificar(reserva.getEstudiante(), "Tutoria reprogramada",
                "Tu tutoria fue reprogramada para el " + nuevoHorario.getInicio() + ".");
        return reserva;
    }

    private Reserva obtenerOFallar(String reservaId) {
        return repositorio.buscarPorId(reservaId)
                .orElseThrow(() -> new ReservaNoEncontradaException(reservaId));
    }
}
