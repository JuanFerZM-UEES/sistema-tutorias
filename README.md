# Sistema de Gestión de Tutorías
**Repositorio:** https://github.com/JuanFerZM-UEES/sistema-tutorias
Proyecto desarrollado para la **Actividad 5 (Ae1) — Diseño orientado a objetos de un sistema** y extendido en **Ae2 — Implementación comparativa de patrones de diseño (Semana 3)**.

## Descripción del problema

El sistema apoya la gestión de tutorías académicas entre estudiantes y docentes: los docentes publican horarios disponibles, los estudiantes solicitan una tutoría sobre uno de esos horarios, y la reserva resultante pasa por un ciclo de vida (pendiente, confirmada, cancelada, reprogramada o completada) mientras se notifica a ambas partes de los eventos relevantes.

El diseño evita acoplar la lógica de negocio a una tecnología concreta de persistencia o de notificación, de modo que ambas puedan cambiarse sin modificar las reglas del dominio.

## Clases principales y responsabilidades

| Clase / Interfaz | Responsabilidad |
|---|---|
| `Usuario` (abstracta) | Identidad y datos de contacto comunes a estudiantes y docentes. |
| `Estudiante` | Especialización de `Usuario` que puede solicitar tutorías. |
| `Docente` | Especialización de `Usuario` que publica y administra sus horarios. |
| `HorarioDisponible` | Protege su propio estado de disponibilidad (ocupado / libre). |
| `Reserva` | Protege las transiciones de estado válidas del encuentro estudiante–docente. |
| `EstadoReserva` | Enumeración de los estados posibles de una reserva. |
| `ModalidadTutoria` | Enumeración de la modalidad de la tutoría (PRESENCIAL / VIRTUAL). |
| `ServicioReservas` | Orquesta las reglas de negocio (solicitar, confirmar, cancelar, reprogramar). |
| `RepositorioReservas` (interfaz) | Abstracción de persistencia de reservas. |
| `RepositorioReservasEnMemoria` | Implementación concreta de persistencia (en memoria) para compilar y probar el proyecto. |
| `Notificador` (interfaz) | Abstracción para notificar eventos a un usuario. |
| `NotificadorEmail` / `NotificadorConsola` / `NotificadorSMS` / `NotificadorPush` | Implementaciones concretas de notificación (Ae1 + Ae2). |
| `NotificadorCreator` (abstracta) | Creator de Factory Method: decide qué `Notificador` usar sin exponer la clase concreta al cliente. |
| `EmailCreator` / `ConsolaCreator` / `SMSCreator` / `PushCreator` | ConcreteCreators de Factory Method (Ae2). |
| `ReservaBuilder` | Builder de `Reserva`: construcción progresiva con campos obligatorios y opcionales (Ae2). |

## Decisiones de diseño relevantes

- **Encapsulación de reglas en el propio objeto**: `HorarioDisponible` es el único que puede cambiar su bandera `disponible`, y `Reserva` es la única que valida si una transición de estado (confirmar, cancelar, reprogramar, completar) es válida. `ServicioReservas` no manipula esos estados directamente; les pide a los objetos que lo hagan.
- **Separación de responsabilidades**: la lógica de negocio (`ServicioReservas`), la persistencia (`RepositorioReservas`) y la notificación (`Notificador`) están en paquetes y clases distintos, sin mezclarse.
- **Inyección de dependencias por constructor**: `ServicioReservas` recibe `RepositorioReservas` y `Notificador` como parámetros de su constructor, en vez de instanciarlos internamente.
- **Herencia solo donde hay una relación real "es-un"**: `Estudiante` y `Docente` heredan de `Usuario` porque comparten identidad y datos de contacto sin necesitar redefinir ese comportamiento. No se usó herencia en ningún otro punto del diseño únicamente para reutilizar código.

## Principios SOLID aplicados

- **SRP (Single Responsibility Principle)**: `ServicioReservas` solo coordina reglas de reserva. Si cambia la tecnología de persistencia, el proveedor de notificaciones o las reglas de validación de un horario, cada cambio impacta una clase distinta (`RepositorioReservas`, `Notificador` o `HorarioDisponible`/`Reserva`), no todas a la vez.
- **DIP (Dependency Inversion Principle)**: `ServicioReservas` depende de las interfaces `RepositorioReservas` y `Notificador`, nunca de `RepositorioReservasEnMemoria` o `NotificadorEmail` directamente. Esto se evidencia en las pruebas unitarias, donde se sustituye `Notificador` por un doble de prueba sin tocar `ServicioReservas`.
- **OCP (Open/Closed Principle)**: agregar una nueva forma de notificar (por ejemplo `NotificadorSMS`) o de persistir (por ejemplo una implementación JDBC) solo requiere crear una clase nueva que implemente la interfaz correspondiente; `ServicioReservas` no se modifica. `NotificadorConsola` es evidencia concreta de esto: se añadió sin tocar una sola línea de `ServicioReservas`.

## Diagrama UML (Ae1 — modelo de clases general)

Ver [`docs/modelo-clases.png`](docs/modelo-clases.png) (fuente editable en [`docs/modelo-clases.mmd`](docs/modelo-clases.mmd), sintaxis Mermaid).

---

## Ae2 | Implementación comparativa de patrones de diseño (Semana 3)

Caso base: el sistema requiere **diferentes mecanismos de notificación** (antes solo existían dos, agregados "a mano") y una **Reserva** cuya configuración empezó a incluir varios datos opcionales (modalidad, motivo, observaciones, recordatorio) además de los obligatorios (estudiante, horario).

### Parte A — Factory Method (`notification/factory`)

**Problema inicial.** Antes de este cambio, decidir qué `Notificador` concreto usar hubiera exigido un condicional (`if`/`switch` sobre un "tipo" de canal) en el código cliente, que crece cada vez que se agrega un canal nuevo:

```java
if (tipo.equals("EMAIL")) return new NotificadorEmail();
if (tipo.equals("SMS"))   return new NotificadorSMS();
if (tipo.equals("PUSH"))  return new NotificadorPush();
// ... un "if" más por cada canal nuevo
```

**Diseño aplicado.**

| Rol GoF | Clase(s) |
|---|---|
| Product | `Notificador` (interfaz, ya existía desde Ae1) |
| ConcreteProduct | `NotificadorEmail`, `NotificadorConsola` (Ae1) + `NotificadorSMS`, `NotificadorPush` (Ae2) |
| Creator | `NotificadorCreator` (abstracta; método fábrica `crear()` + método plantilla `notificar(...)`) |
| ConcreteCreator | `EmailCreator`, `ConsolaCreator`, `SMSCreator`, `PushCreator` |

`NotificadorCreator` no decide qué `Notificador` concreto instanciar: delega esa decisión al método `crear()`, que cada subclase implementa. El método `notificar(destinatario, asunto, mensaje)` es el método plantilla que usa el producto sin conocer su tipo concreto.

**Extensibilidad.** `NotificadorSMS`/`SMSCreator` es la tercera variante y `NotificadorPush`/`PushCreator` la cuarta, agregada explícitamente para evidenciar extensibilidad: **ninguna clase existente** (`Notificador`, `NotificadorCreator`, ni los demás ConcreteCreator) se modificó para incorporarlas.

**Qué cambia y qué permanece estable.** Cambia: se agregan clases nuevas (productos y creators). Permanece estable: `Notificador`, `ServicioReservas` (que sigue dependiendo solo de la interfaz `Notificador`, exactamente igual que en Ae1) y todos los ConcreteCreator/ConcreteProduct ya existentes.

Diagrama: [`docs/factory-method.png`](docs/factory-method.png) (fuente en [`docs/factory-method.mmd`](docs/factory-method.mmd)).

Demostración ejecutable: `edu.uees.tutorias.demo.DemoFactoryMethod` (usa las 4 variantes a través de la abstracción `NotificadorCreator`).

### Parte B — Builder (`domain/ReservaBuilder`)

**Problema inicial.** Pasar los datos opcionales de una `Reserva` (modalidad, motivo, observaciones, recordatorio) junto con los obligatorios por un único constructor obliga a un constructor de 8 parámetros posicionales, difícil de leer y fácil de invocar con los argumentos en el orden equivocado:

```java
// Dificil de leer en el punto de llamada: ¿que representa cada parametro?
new Reserva(id, estudiante, horario, fechaCreacion,
            ModalidadTutoria.VIRTUAL, "Revision de proyecto",
            "Traer diagrama UML", true);
```

**Campos obligatorios vs. opcionales.**

| Tipo | Campos |
|---|---|
| Obligatorios | `estudiante`, `horario` |
| Opcionales (con valor por defecto) | `id` (UUID autogenerado), `fechaCreacion` (ahora), `modalidad` (PRESENCIAL), `motivo` (""), `observaciones` (""), `recordatorio` (true) |

**Diseño aplicado.** `ReservaBuilder` expone una Fluent API (`estudiante(...)`, `horario(...)`, `modalidad(...)`, `motivo(...)`, `observaciones(...)`, `recordatorio(...)`) y `build()`, que valida los campos obligatorios (lanza `IllegalStateException` si falta `estudiante` u `horario`) antes de invocar el constructor completo de `Reserva`. El constructor original de `Reserva` (4 parámetros, de Ae1) se conserva sin cambios en su firma —delega internamente al constructor completo con los mismos valores por defecto— por lo que `ServicioReservas` y las pruebas de Ae1 no se modificaron.

Se construyeron dos configuraciones distintas para demostrar el patrón: una **mínima** (solo campos obligatorios, todo lo demás usa valores por defecto) y una **completa** (todos los campos opcionales explícitos, modalidad virtual). Ver `edu.uees.tutorias.demo.DemoBuilder`.

Diagrama: [`docs/builder.png`](docs/builder.png) (fuente en [`docs/builder.mmd`](docs/builder.mmd)).

### Parte C — Comparación técnica

| Criterio | Factory Method | Builder |
|---|---|---|
| Problema que resuelve | Decidir qué implementación concreta de un producto crear, sin dispersar ese conocimiento en el código cliente. | Construir un objeto con varios campos opcionales sin recurrir a un constructor con muchos parámetros posicionales. |
| Variabilidad principal | Varía **qué clase concreta** se instancia (el tipo de objeto creado). | Varía **cómo se ensambla** un mismo tipo de objeto (qué combinación de datos opcionales se configura). |
| Participantes | Product, ConcreteProduct, Creator, ConcreteCreator. | Product (`Reserva`) y Builder (`ReservaBuilder`); sin Director explícito, el propio cliente encadena los métodos. |
| Ventaja principal | Nuevas variantes se agregan creando una clase nueva, sin tocar código existente (OCP). | Construcción legible en el punto de llamada, valores por defecto centralizados, validación antes de construir. |
| Costo / consecuencia | Aumenta el número de clases (jerarquía paralela Product/Creator); introduce indirección. | Clase adicional; cierta duplicación de campos entre el Builder y el objeto construido. |
| Cuándo utilizarlo | Cuando el sistema debe soportar varias variantes intercambiables de un mismo tipo de objeto, y se prevé agregar más con el tiempo. | Cuando un objeto tiene varios campos opcionales y su construcción se beneficia de ser explícita y progresiva. |
| Cuándo evitarlo | Cuando solo existe una implementación concreta y no se prevé variabilidad real. | Para objetos simples con pocos campos, todos obligatorios (por ejemplo, un `Punto(x, y)`). |

### Parte D — Conclusiones

Factory Method y Builder resuelven problemas distintos aunque ambos sean patrones creacionales: Factory Method responde a "¿qué clase concreta debo instanciar?" cuando esa decisión puede variar y crecer con el tiempo (canales de notificación), mientras que Builder responde a "¿cómo ensamblo, de forma legible y validada, un objeto con muchos datos opcionales?" cuando el problema no es la variedad de tipos sino la variedad de configuraciones de un mismo tipo (`Reserva`).

En este proyecto, Factory Method se justifica porque el número de canales de notificación es un eje de crecimiento real del sistema (ya pasó de 2 a 4 variantes) y el costo de la indirección adicional se compensa con que `ServicioReservas` sigue sin conocer ninguna clase concreta. Builder se justifica porque `Reserva` empezó a acumular datos opcionales de configuración: el constructor de 8 parámetros hubiera sido difícil de mantener, y el costo de una clase adicional es bajo comparado con la legibilidad y la validación que gana el punto de llamada. Ninguno de los dos patrones se aplicó "porque sí": ambos resuelven una complejidad real y verificable en el propio código (el condicional de creación evitado, y el constructor largo evitado), no una anticipación especulativa de requisitos futuros.

### Parte E — Evidencias de Ae2

- Código Java de Factory Method: [`notification/factory`](src/main/java/edu/uees/tutorias/notification/factory) + `NotificadorSMS.java`/`NotificadorPush.java` en [`notification`](src/main/java/edu/uees/tutorias/notification).
- Código Java de Builder: [`domain/ReservaBuilder.java`](src/main/java/edu/uees/tutorias/domain/ReservaBuilder.java), [`domain/ModalidadTutoria.java`](src/main/java/edu/uees/tutorias/domain/ModalidadTutoria.java), cambios en [`domain/Reserva.java`](src/main/java/edu/uees/tutorias/domain/Reserva.java).
- Demostraciones: [`demo/DemoFactoryMethod.java`](src/main/java/edu/uees/tutorias/demo/DemoFactoryMethod.java), [`demo/DemoBuilder.java`](src/main/java/edu/uees/tutorias/demo/DemoBuilder.java).
- Pruebas unitarias: [`NotificadorCreatorTest`](src/test/java/edu/uees/tutorias/notification/factory/NotificadorCreatorTest.java), [`ReservaBuilderTest`](src/test/java/edu/uees/tutorias/domain/ReservaBuilderTest.java).
- UML: [`docs/factory-method.png`](docs/factory-method.png), [`docs/builder.png`](docs/builder.png).
- Historial de commits progresivo en este mismo repositorio (ver `git log`).

---

## Requisitos para ejecutar el proyecto

- JDK 17 o superior
- Apache Maven 3.8+

## Compilación y ejecución

```bash
mvn clean compile      # compila el proyecto
mvn clean test         # ejecuta las pruebas unitarias (JUnit 5)
mvn exec:java           # ejecuta la demostracion de Ae1 (clase App)

# Demostraciones de Ae2 (patrones de diseno):
mvn exec:java -Dexec.mainClass="edu.uees.tutorias.demo.DemoFactoryMethod"
mvn exec:java -Dexec.mainClass="edu.uees.tutorias.demo.DemoBuilder"
```

## Declaración de uso de inteligencia artificial
Durante el desarrollo de esta actividad (Ae1 y Ae2) utilicé como inteligencia artificial Claude.
En caso de uso, las utilicé para: revisión del código Java, generacion de los diagramas UML (Mermaid), y reorganización de las oraciones de los documentos a entregar.
Verifiqué y adapté las respuestas obtenidas (el proyecto compila y las pruebas unitarias pasan), y puedo explicar y justificar el código y las decisiones de diseño presentadas, incluyendo por que se adapto la firma de `Notificador` del Recurso 4 (que usaba `String destino`) al `Usuario` real del dominio en lugar de copiar el ejemplo de forma mecanica.
