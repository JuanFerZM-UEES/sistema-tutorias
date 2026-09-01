package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorPush;

/**
 * ConcreteCreator agregado como variante adicional (extensibilidad):
 * fabrica un NotificadorPush. Ni NotificadorCreator ni los demas
 * ConcreteCreator existentes se modificaron para incorporar esta clase.
 */
public class PushCreator extends NotificadorCreator {

    @Override
    protected Notificador crear() {
        return new NotificadorPush();
    }
}
