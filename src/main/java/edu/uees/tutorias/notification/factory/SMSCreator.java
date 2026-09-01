package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorSMS;

/** ConcreteCreator: fabrica un NotificadorSMS. */
public class SMSCreator extends NotificadorCreator {

    @Override
    protected Notificador crear() {
        return new NotificadorSMS();
    }
}
