package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorConsola;

/** ConcreteCreator: fabrica un NotificadorConsola. */
public class ConsolaCreator extends NotificadorCreator {

    @Override
    protected Notificador crear() {
        return new NotificadorConsola();
    }
}
