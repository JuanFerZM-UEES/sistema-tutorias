package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorEmail;

/** ConcreteCreator: fabrica un NotificadorEmail. */
public class EmailCreator extends NotificadorCreator {

    @Override
    protected Notificador crear() {
        return new NotificadorEmail();
    }
}
