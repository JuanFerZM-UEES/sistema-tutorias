# Sistema de Gestión de Tutorías

Proyecto desarrollado para la **Actividad 5 (Ae1) — Diseño orientado a objetos de un sistema**

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
| `ServicioReservas` | Orquesta las reglas de negocio (solicitar, confirmar, cancelar, reprogramar). |
| `RepositorioReservas` (interfaz) | Abstracción de persistencia de reservas. |
| `RepositorioReservasEnMemoria` | Implementación concreta de persistencia (en memoria) para compilar y probar el proyecto. |
| `Notificador` (interfaz) | Abstracción para notificar eventos a un usuario. |
| `NotificadorEmail` / `NotificadorConsola` | Implementaciones concretas de notificación. |

## Decisiones de diseño relevantes

- **Encapsulación de reglas en el propio objeto**: `HorarioDisponible` es el único que puede cambiar su bandera `disponible`, y `Reserva` es la única que valida si una transición de estado (confirmar, cancelar, reprogramar, completar) es válida. `ServicioReservas` no manipula esos estados directamente; les pide a los objetos que lo hagan.
- **Separación de responsabilidades**: la lógica de negocio (`ServicioReservas`), la persistencia (`RepositorioReservas`) y la notificación (`Notificador`) están en paquetes y clases distintos, sin mezclarse.
- **Inyección de dependencias por constructor**: `ServicioReservas` recibe `RepositorioReservas` y `Notificador` como parámetros de su constructor, en vez de instanciarlos internamente.
- **Herencia solo donde hay una relación real "es-un"**: `Estudiante` y `Docente` heredan de `Usuario` porque comparten identidad y datos de contacto sin necesitar redefinir ese comportamiento. No se usó herencia en ningún otro punto del diseño únicamente para reutilizar código.

## Principios SOLID aplicados

- **SRP (Single Responsibility Principle)**: `ServicioReservas` solo coordina reglas de reserva. Si cambia la tecnología de persistencia, el proveedor de notificaciones o las reglas de validación de un horario, cada cambio impacta una clase distinta (`RepositorioReservas`, `Notificador` o `HorarioDisponible`/`Reserva`), no todas a la vez.
- **DIP (Dependency Inversion Principle)**: `ServicioReservas` depende de las interfaces `RepositorioReservas` y `Notificador`, nunca de `RepositorioReservasEnMemoria` o `NotificadorEmail` directamente. Esto se evidencia en las pruebas unitarias, donde se sustituye `Notificador` por un doble de prueba sin tocar `ServicioReservas`.
- **OCP (Open/Closed Principle)**: agregar una nueva forma de notificar (por ejemplo `NotificadorSMS`) o de persistir (por ejemplo una implementación JDBC) solo requiere crear una clase nueva que implemente la interfaz correspondiente; `ServicioReservas` no se modifica. `NotificadorConsola` es evidencia concreta de esto: se añadió sin tocar una sola línea de `ServicioReservas`.

## Diagrama UML

Ver [`docs/modelo-clases.png`](docs/modelo-clases.png) (fuente editable en [`docs/modelo-clases.mmd`](docs/modelo-clases.mmd), sintaxis Mermaid).

## Requisitos para ejecutar el proyecto

- JDK 17 o superior
- Apache Maven 3.8+

## Compilación y ejecución

```bash
mvn clean compile      # compila el proyecto
mvn clean test         # ejecuta las pruebas unitarias (JUnit 5)
mvn exec:java           # ejecuta la demostración (clase App)
```

## Declaración de uso de inteligencia artificial

Utilicé Claude para optimizar la escritura y verificar los errores de programación en java.
