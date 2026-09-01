package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Cuarta implementacion concreta de Notificador (ConcreteProduct),
 * incorporada como variante adicional para evidenciar la extensibilidad
 * del diseno: simula el envio de una notificacion push a la app movil.
 *
 * Ni Notificador ni ninguna de las demas implementaciones cambiaron para
 * que esta clase pudiera agregarse.
 */
public class NotificadorPush implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String asunto, String mensaje) {
        System.out.printf(
                "[PUSH] %s -> %s: %s%n",
                destinatario.getNombre(), asunto, mensaje);
    }
}
