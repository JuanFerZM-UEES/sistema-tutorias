package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registra el encuentro entre un Estudiante y un HorarioDisponible, y su
 * estado a lo largo del tiempo.
 *
 * Responsabilidad: proteger las transiciones de estado validas. La reserva
 * es quien decide si puede pasar de PENDIENTE a CONFIRMADA, o si ya esta
 * en un estado terminal (CANCELADA/COMPLETADA) y por lo tanto no admite
 * mas cambios. Esto evita que ServicioReservas (o cualquier otra clase)
 * fuerce una transicion invalida directamente sobre el atributo "estado".
 */
public class Reserva {

    private final String id;
    private final Estudiante estudiante;
    private HorarioDisponible horario;
    private EstadoReserva estado;
    private final LocalDateTime fechaCreacion;

    public Reserva(String id, Estudiante estudiante, HorarioDisponible horario, LocalDateTime fechaCreacion) {
        this.id = Objects.requireNonNull(id);
        this.estudiante = Objects.requireNonNull(estudiante);
        this.horario = Objects.requireNonNull(horario);
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion);
        this.estado = EstadoReserva.PENDIENTE;
    }

    public String getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public HorarioDisponible getHorario() {
        return horario;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void confirmar() {
        if (estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo una reserva PENDIENTE puede confirmarse (estado actual: " + estado + ")");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException(
                    "Una reserva en estado " + estado + " ya no puede cancelarse");
        }
        this.estado = EstadoReserva.CANCELADA;
    }

    public void reprogramar(HorarioDisponible nuevoHorario) {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException(
                    "Una reserva en estado " + estado + " ya no puede reprogramarse");
        }
        this.horario = Objects.requireNonNull(nuevoHorario);
        this.estado = EstadoReserva.REPROGRAMADA;
    }

    public void completar() {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "Solo una reserva CONFIRMADA puede marcarse como COMPLETADA (estado actual: " + estado + ")");
        }
        this.estado = EstadoReserva.COMPLETADA;
    }
}
