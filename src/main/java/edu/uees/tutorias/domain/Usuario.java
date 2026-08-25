package edu.uees.tutorias.domain;

import java.util.Objects;

/**
 * Representa a un usuario del sistema de tutorias.
 *
 * Responsabilidad: mantener la identidad y los datos de contacto basicos
 * de una persona registrada en el sistema. No conoce reglas de reserva,
 * de notificacion ni de persistencia: esas responsabilidades pertenecen
 * a otras clases (ver ServicioReservas y Notificador), lo que favorece
 * la cohesion de esta clase.
 */
public abstract class Usuario {

    private final String id;
    private final String nombre;
    private final String email;

    protected Usuario(String id, String nombre, String email) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        this.nombre = Objects.requireNonNull(nombre, "nombre no puede ser nulo");
        this.email = Objects.requireNonNull(email, "email no puede ser nulo");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
