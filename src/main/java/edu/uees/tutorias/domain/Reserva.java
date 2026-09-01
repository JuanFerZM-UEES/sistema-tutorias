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
 *
 * Nota de diseno (Ae2 | Builder): ademas de los datos obligatorios
 * (estudiante, horario), una Reserva admite datos opcionales de
 * configuracion (modalidad, motivo, observaciones, recordatorio). Pasarlos
 * todos por un unico constructor obliga a listar hasta 8 parametros
 * posicionales dificiles de leer en el punto de llamada (ver el
 * constructor "completo" mas abajo): ese es exactamente el problema que
 * resuelve ReservaBuilder con una API fluida y valores por defecto.
 */
public class Reserva {

    private final String id;
    private final Estudiante estudiante;
    private HorarioDisponible horario;
    private EstadoReserva estado;
    private final LocalDateTime fechaCreacion;

    // Datos opcionales de configuracion (Ae2 | Builder)
    private final ModalidadTutoria modalidad;
    private final String motivo;
    private final String observaciones;
    private final boolean recordatorio;

    /**
     * Constructor original (Ae1). Se conserva sin cambios en su firma para
     * no romper a ServicioReservas ni a las pruebas existentes: es la
     * evidencia de que esta clase "permanece estable" para sus usos
     * previos. Internamente delega al constructor completo, rellenando
     * los campos opcionales con los mismos valores por defecto que usa
     * ReservaBuilder.
     */
    public Reserva(String id, Estudiante estudiante, HorarioDisponible horario, LocalDateTime fechaCreacion) {
        this(id, estudiante, horario, fechaCreacion,
                ModalidadTutoria.PRESENCIAL, "", "", true);
    }

    /**
     * Constructor "completo" (Ae2 | Builder), con todos los campos
     * obligatorios y opcionales. No es publico a proposito: la unica vía
     * pensada para construir una Reserva con datos opcionales es
     * ReservaBuilder (mismo paquete), que valida los campos obligatorios
     * antes de invocar este constructor.
     */
    Reserva(String id, Estudiante estudiante, HorarioDisponible horario, LocalDateTime fechaCreacion,
            ModalidadTutoria modalidad, String motivo, String observaciones, boolean recordatorio) {
        this.id = Objects.requireNonNull(id);
        this.estudiante = Objects.requireNonNull(estudiante);
        this.horario = Objects.requireNonNull(horario);
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion);
        this.estado = EstadoReserva.PENDIENTE;
        this.modalidad = Objects.requireNonNull(modalidad);
        this.motivo = motivo == null ? "" : motivo;
        this.observaciones = observaciones == null ? "" : observaciones;
        this.recordatorio = recordatorio;
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

    public ModalidadTutoria getModalidad() {
        return modalidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public boolean isRecordatorio() {
        return recordatorio;
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

    @Override
    public String toString() {
        return "Reserva{" +
                "id='" + id + '\'' +
                ", estudiante=" + estudiante.getNombre() +
                ", docente=" + horario.getDocente().getNombre() +
                ", horario=" + horario.getInicio() +
                ", estado=" + estado +
                ", modalidad=" + modalidad +
                ", motivo='" + motivo + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", recordatorio=" + recordatorio +
                '}';
    }
}
