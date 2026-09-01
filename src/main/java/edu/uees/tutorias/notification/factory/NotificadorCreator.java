package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.domain.Usuario;
import edu.uees.tutorias.notification.Notificador;

/**
 * Creator del patron Factory Method para la creacion de Notificador.
 *
 * Problema que resuelve: antes de este diseno, decidir que
 * implementacion de Notificador usar exigia un condicional (if/switch
 * sobre un "tipo" de canal) en el codigo cliente, que crecia cada vez
 * que se agregaba un canal nuevo (ver Recurso 4, "Problema inicial").
 *
 * Esta clase no decide directamente que Notificador concreto instanciar:
 * delega esa decision al metodo fabrica crear(), que cada subclase
 * concreta implementa. El metodo notificar(...) es el metodo plantilla
 * que usa el producto sin conocer su tipo concreto (depende solo de la
 * abstraccion Notificador, igual que ServicioReservas).
 */
public abstract class NotificadorCreator {

    protected abstract Notificador crear();

    public final void notificar(Usuario destinatario, String asunto, String mensaje) {
        Notificador notificador = crear();
        notificador.notificar(destinatario, asunto, mensaje);
    }
}
