package edu.uees.tutorias.persistence;

import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.service.RepositorioReservas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementacion concreta de RepositorioReservas basada en un mapa en
 * memoria. Sirve para compilar, ejecutar y probar el proyecto sin depender
 * de una base de datos real.
 *
 * Es una pieza reemplazable: para migrar a una tecnologia de persistencia
 * distinta (por ejemplo JDBC contra MySQL) bastaria con crear otra clase
 * que implemente RepositorioReservas; ServicioReservas seguiria
 * funcionando sin cambios porque solo conoce la interfaz.
 */
public class RepositorioReservasEnMemoria implements RepositorioReservas {

    private final Map<String, Reserva> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Reserva reserva) {
        almacen.put(reserva.getId(), reserva);
    }

    @Override
    public Optional<Reserva> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id));
    }

    @Override
    public List<Reserva> listarPorEstudiante(String estudianteId) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : almacen.values()) {
            if (r.getEstudiante().getId().equals(estudianteId)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Reserva reserva) {
        if (!almacen.containsKey(reserva.getId())) {
            throw new IllegalStateException("No existe una reserva con id " + reserva.getId());
        }
        almacen.put(reserva.getId(), reserva);
    }
}
