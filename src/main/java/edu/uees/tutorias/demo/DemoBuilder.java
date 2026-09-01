package edu.uees.tutorias.demo;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.ModalidadTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;

import java.time.LocalDateTime;

/**
 * Demostracion de Ae2 | Parte B - Builder.
 *
 * Construye dos configuraciones distintas de Reserva con ReservaBuilder:
 * una minima (solo los campos obligatorios, todo lo demas usa valores
 * por defecto) y una completa (todos los campos opcionales explicitos).
 */
public class DemoBuilder {

    public static void main(String[] args) {
        Docente docente = new Docente("D1", "Jaime Sayago", "jsayago@uees.edu.ec");
        Estudiante estudiante = new Estudiante("E1", "Juan Fernando", "juan.zhingri@gmail.com", "Computacion");

        HorarioDisponible horario1 = docente.publicarHorario(
                "H1", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        HorarioDisponible horario2 = docente.publicarHorario(
                "H2", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1));

        // Configuracion 1: minima, solo campos obligatorios.
        Reserva reservaMinima = new ReservaBuilder()
                .estudiante(estudiante)
                .horario(horario1)
                .build();

        // Configuracion 2: completa, con todos los campos opcionales.
        Reserva reservaCompleta = new ReservaBuilder()
                .estudiante(estudiante)
                .horario(horario2)
                .modalidad(ModalidadTutoria.VIRTUAL)
                .motivo("Revision de proyecto final")
                .observaciones("Traer diagrama UML actualizado")
                .recordatorio(true)
                .build();

        System.out.println("Reserva minima  : " + reservaMinima);
        System.out.println("Reserva completa: " + reservaCompleta);

        // build() valida los campos obligatorios antes de construir.
        try {
            new ReservaBuilder().horario(horario1).build();
        } catch (IllegalStateException e) {
            System.out.println("Validacion esperada: " + e.getMessage());
        }
    }
}
