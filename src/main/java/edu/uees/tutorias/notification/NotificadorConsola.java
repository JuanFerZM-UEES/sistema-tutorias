package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Segunda implementacion de Notificador, usada por ejemplo en pruebas o en
 * un modo de depuracion local.
 *
 * Su sola existencia evidencia OCP: ServicioReservas no cambio en absoluto
 * para admitir esta nueva forma de notificar; solo fue necesario crear una
 * clase adicional que respeta el mismo contrato.
 */
public class NotificadorConsola implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String asunto, String mensaje) {
        System.out.printf("[CONSOLA] %s -> %s: %s%n", asunto, destinatario.getNombre(), mensaje);
    }
}
