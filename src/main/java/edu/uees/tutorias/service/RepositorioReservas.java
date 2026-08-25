package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * Abstraccion de persistencia para Reserva.
 *
 * DIP: ServicioReservas depende de esta interfaz, no de una tecnologia de
 * base de datos concreta. Hoy existe una implementacion en memoria
 * (RepositorioReservasEnMemoria); si manana el sistema migra a MySQL,
 * PostgreSQL o un servicio externo, solo se agrega una nueva
 * implementacion de este contrato y ServicioReservas no se modifica.
 */
public interface RepositorioReservas {
    void guardar(Reserva reserva);

    Optional<Reserva> buscarPorId(String id);

    List<Reserva> listarPorEstudiante(String estudianteId);

    void actualizar(Reserva reserva);
}
