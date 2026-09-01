package edu.uees.tutorias.demo;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.notification.factory.ConsolaCreator;
import edu.uees.tutorias.notification.factory.EmailCreator;
import edu.uees.tutorias.notification.factory.NotificadorCreator;
import edu.uees.tutorias.notification.factory.PushCreator;
import edu.uees.tutorias.notification.factory.SMSCreator;

/**
 * Demostracion de Ae2 | Parte A - Factory Method.
 *
 * Muestra la creacion y uso de las 4 variantes de NotificadorCreator sin
 * que el codigo cliente (este metodo main) conozca ninguna clase concreta
 * de Notificador: solo trabaja con la abstraccion NotificadorCreator.
 *
 * PushCreator es la variante adicional agregada para evidenciar
 * extensibilidad: se sumo sin modificar NotificadorCreator, Notificador
 * ni ninguno de los otros tres ConcreteCreator.
 */
public class DemoFactoryMethod {

    public static void main(String[] args) {
        Docente docente = new Docente("D1", "Jaime Sayago", "jsayago@uees.edu.ec");
        Estudiante estudiante = new Estudiante("E1", "Juan Fernando", "juan.zhingri@gmail.com", "Computacion");

        NotificadorCreator[] creators = {
                new EmailCreator(),
                new ConsolaCreator(),
                new SMSCreator(),
                new PushCreator() // variante adicional (extensibilidad)
        };

        for (NotificadorCreator creator : creators) {
            System.out.println("--- " + creator.getClass().getSimpleName() + " ---");
            creator.notificar(estudiante, "Recordatorio de tutoria",
                    "Tu tutoria con " + docente.getNombre() + " es manana.");
        }
    }
}
