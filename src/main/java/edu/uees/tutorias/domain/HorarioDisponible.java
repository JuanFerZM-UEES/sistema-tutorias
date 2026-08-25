package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Bloque de tiempo que un Docente ofrece para una tutoria.
 *
 * Responsabilidad: proteger su propio estado de disponibilidad. Ninguna
 * otra clase puede marcar un horario como ocupado o libre saltandose las
 * reglas de esta clase (la bandera "disponible" es privada), lo que
 * mantiene alta cohesion: todo lo relacionado con "puede reservarse este
 * bloque de tiempo" vive aqui.
 */
public class HorarioDisponible {

    private final String id;
    private final Docente docente;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private boolean disponible;

    public HorarioDisponible(String id, Docente docente, LocalDateTime inicio, LocalDateTime fin) {
        if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        this.id = Objects.requireNonNull(id);
        this.docente = Objects.requireNonNull(docente);
        this.inicio = Objects.requireNonNull(inicio);
        this.fin = Objects.requireNonNull(fin);
        this.disponible = true;
    }

    public String getId() {
        return id;
    }

    public Docente getDocente() {
        return docente;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    public void marcarComoOcupado() {
        if (!disponible) {
            throw new IllegalStateException("El horario " + id + " ya esta ocupado");
        }
        this.disponible = false;
    }

    public void marcarComoDisponible() {
        this.disponible = true;
    }
}
