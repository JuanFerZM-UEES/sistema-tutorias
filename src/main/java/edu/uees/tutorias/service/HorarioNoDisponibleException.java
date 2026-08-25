package edu.uees.tutorias.service;

/**
 * Se lanza cuando se intenta reservar o reprogramar sobre un horario que
 * ya no esta disponible.
 */
public class HorarioNoDisponibleException extends RuntimeException {
    public HorarioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
