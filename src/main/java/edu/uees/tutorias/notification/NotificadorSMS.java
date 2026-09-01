package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Tercera implementacion concreta de Notificador (ConcreteProduct), usada
 * para simular el envio de un mensaje de texto (SMS).
 *
 * Se agrega sin modificar Notificador, NotificadorEmail ni
 * NotificadorConsola: evidencia de OCP a nivel de producto.
 */
public class NotificadorSMS implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String asunto, String mensaje) {
        System.out.printf(
                "[SMS] Para: %s | %s: %s%n",
                destinatario.getNombre(), asunto, mensaje);
    }
}
