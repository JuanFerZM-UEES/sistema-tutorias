package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorConsola;
import edu.uees.tutorias.notification.NotificadorEmail;
import edu.uees.tutorias.notification.NotificadorPush;
import edu.uees.tutorias.notification.NotificadorSMS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Pruebas de Factory Method: cada ConcreteCreator debe fabricar la
 * implementacion de Notificador que le corresponde (crear() es protected,
 * pero accesible aqui porque el test vive en el mismo paquete), y
 * notificar(...) debe poder invocarse a traves de la abstraccion
 * NotificadorCreator sin lanzar excepciones.
 */
class NotificadorCreatorTest {

    private final Estudiante estudiante =
            new Estudiante("E1", "Ana Torres", "ana.torres@uees.edu.ec", "Computacion");

    @Test
    void emailCreatorFabricaNotificadorEmail() {
        NotificadorCreator creator = new EmailCreator();
        Notificador producto = creator.crear();

        assertInstanceOf(NotificadorEmail.class, producto);
        assertDoesNotThrow(() -> creator.notificar(estudiante, "Asunto", "Mensaje"));
    }

    @Test
    void consolaCreatorFabricaNotificadorConsola() {
        NotificadorCreator creator = new ConsolaCreator();
        assertInstanceOf(NotificadorConsola.class, creator.crear());
    }

    @Test
    void smsCreatorFabricaNotificadorSMS() {
        NotificadorCreator creator = new SMSCreator();
        assertInstanceOf(NotificadorSMS.class, creator.crear());
    }

    @Test
    void pushCreatorFabricaNotificadorPush() {
        // Variante adicional agregada para evidenciar extensibilidad.
        NotificadorCreator creator = new PushCreator();
        assertInstanceOf(NotificadorPush.class, creator.crear());
    }
}
