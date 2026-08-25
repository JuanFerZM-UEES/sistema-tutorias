package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Docente que publica y administra sus propios horarios de tutoria.
 *
 * Responsabilidad: mantener la coleccion de horarios que el propio docente
 * ofrece y garantizar que solo el pueda crearlos. La informacion (la lista
 * de horarios) permanece encapsulada: se expone una vista de solo lectura
 * para que otras clases no puedan alterarla directamente, sino unicamente
 * a traves de los metodos que protegen la regla de negocio.
 */
public class Docente extends Usuario {

    private final List<HorarioDisponible> horarios = new ArrayList<>();

    public Docente(String id, String nombre, String email) {
        super(id, nombre, email);
    }

    /**
     * Publica un nuevo bloque de horario disponible para tutorias.
     * Regla protegida: la hora de fin debe ser posterior a la de inicio,
     * validacion que ya realiza el constructor de HorarioDisponible.
     */
    public HorarioDisponible publicarHorario(String horarioId, LocalDateTime inicio, LocalDateTime fin) {
        HorarioDisponible horario = new HorarioDisponible(horarioId, this, inicio, fin);
        horarios.add(horario);
        return horario;
    }

    public List<HorarioDisponible> getHorarios() {
        return Collections.unmodifiableList(horarios);
    }

    public List<HorarioDisponible> getHorariosDisponibles() {
        List<HorarioDisponible> disponibles = new ArrayList<>();
        for (HorarioDisponible h : horarios) {
            if (h.estaDisponible()) {
                disponibles.add(h);
            }
        }
        return disponibles;
    }
}
