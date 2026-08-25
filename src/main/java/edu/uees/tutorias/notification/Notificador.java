package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Abstraccion para el envio de notificaciones a un usuario.
 *
 * DIP: ServicioReservas depende de esta interfaz, nunca de una
 * implementacion concreta como NotificadorEmail.
 * OCP: nuevas formas de notificar (SMS, push, etc.) se agregan creando una
 * nueva clase que implemente este contrato, sin modificar el servicio.
 */
public interface Notificador {
    void notificar(Usuario destinatario, String asunto, String mensaje);
}
