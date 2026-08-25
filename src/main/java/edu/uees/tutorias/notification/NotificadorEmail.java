package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion concreta de Notificador que simula el envio de un correo
 * electronico. En un entorno real, aqui se integraria un proveedor SMTP
 * (o una API de correo); el resto del sistema no necesita saberlo porque
 * solo conoce la interfaz Notificador.
 */
public class NotificadorEmail implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String asunto, String mensaje) {
        System.out.printf(
                "[EMAIL] Para: %s <%s> | Asunto: %s | Mensaje: %s%n",
                destinatario.getNombre(), destinatario.getEmail(), asunto, mensaje);
    }
}
