package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Builder de Reserva.
 *
 * Problema que resuelve: el constructor "completo" de Reserva necesita 8
 * parametros posicionales (id, estudiante, horario, fechaCreacion,
 * modalidad, motivo, observaciones, recordatorio). En el punto de llamada
 * eso es dificil de leer y facil de invocar con los argumentos en el
 * orden equivocado (por ejemplo, dos String seguidos: motivo/observaciones)
 * ademas de obligar a repetir valores por defecto en cada lugar donde se
 * cree una Reserva.
 *
 * Campos obligatorios: estudiante, horario (sin ellos no existe una
 * reserva valida; build() los valida antes de construir).
 * Campos opcionales, con valor por defecto si no se especifican:
 *   - id                -> se genera un UUID si no se indica uno.
 *   - fechaCreacion      -> LocalDateTime.now() si no se indica.
 *   - modalidad          -> ModalidadTutoria.PRESENCIAL.
 *   - motivo             -> "" (sin motivo registrado).
 *   - observaciones      -> "" (sin observaciones).
 *   - recordatorio       -> true (se envia recordatorio por defecto).
 */
public class ReservaBuilder {

    private Estudiante estudiante;
    private HorarioDisponible horario;

    private String id;
    private LocalDateTime fechaCreacion;
    private ModalidadTutoria modalidad = ModalidadTutoria.PRESENCIAL;
    private String motivo = "";
    private String observaciones = "";
    private boolean recordatorio = true;

    public ReservaBuilder estudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        return this;
    }

    public ReservaBuilder horario(HorarioDisponible horario) {
        this.horario = horario;
        return this;
    }

    public ReservaBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ReservaBuilder fechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public ReservaBuilder modalidad(ModalidadTutoria modalidad) {
        this.modalidad = Objects.requireNonNull(modalidad);
        return this;
    }

    public ReservaBuilder motivo(String motivo) {
        this.motivo = motivo;
        return this;
    }

    public ReservaBuilder observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public ReservaBuilder recordatorio(boolean recordatorio) {
        this.recordatorio = recordatorio;
        return this;
    }

    /**
     * Valida los campos obligatorios y construye la Reserva. Si falta un
     * dato obligatorio, falla aqui (en el builder) y no dentro de Reserva,
     * que es donde fallaria de forma menos clara.
     */
    public Reserva build() {
        if (estudiante == null) {
            throw new IllegalStateException("La reserva requiere un estudiante (metodo estudiante(...))");
        }
        if (horario == null) {
            throw new IllegalStateException("La reserva requiere un horario (metodo horario(...))");
        }

        String idFinal = (id != null) ? id : UUID.randomUUID().toString();
        LocalDateTime fechaFinal = (fechaCreacion != null) ? fechaCreacion : LocalDateTime.now();

        return new Reserva(idFinal, estudiante, horario, fechaFinal,
                modalidad, motivo, observaciones, recordatorio);
    }
}
