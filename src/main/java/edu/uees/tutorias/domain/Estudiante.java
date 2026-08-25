package edu.uees.tutorias.domain;

/**
 * Estudiante que puede solicitar tutorias.
 *
 * Se modela como una especializacion de Usuario (herencia valida: un
 * Estudiante ES-UN Usuario, comparte identidad y datos de contacto, y no
 * se necesita sustituir el comportamiento heredado, por lo que se respeta
 * el principio de sustitucion de Liskov).
 */
public class Estudiante extends Usuario {

    private final String carrera;

    public Estudiante(String id, String nombre, String email, String carrera) {
        super(id, nombre, email);
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }
}
