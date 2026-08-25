package edu.uees.tutorias.service;

/**
 * Se lanza cuando se busca una reserva por id y no existe en el
 * repositorio.
 */
public class ReservaNoEncontradaException extends RuntimeException {
    public ReservaNoEncontradaException(String id) {
        super("No existe una reserva con id " + id);
    }
}
