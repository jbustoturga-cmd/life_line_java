package com.example.lifeline.service;

import com.example.lifeline.model.Cliente;
import com.example.lifeline.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los clientes.
 * Proporciona métodos para listar todos los clientes y aplicar filtros
 * por nombre y rango de fechas de registro..
 */
@Service
public class ClienteService {
    private final ClienteRepository repo;

    /* Constructor del servicio de clientes.  */
    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    /* Obtiene todos los clientes registrados en la base de datos.   */
    public List<Cliente> listarTodos() {
        return repo.findAll();
    }

    /**
     * Filtra clientes según criterios opcionales: nombre y rango de fechas.
     *   <li>Si se pasa un nombre, se buscan coincidencias parciales (case-insensitive).</li>
     *   <li>Si se pasa un rango de fechas (desde y hasta), se filtran los clientes cuyo
     *       {@code fechaRegistro} esté dentro de ese rango.</li>.
     *   <li>Si solo se pasa una fecha inicial, se incluyen clientes registrados desde esa fecha en adelante.</li>
     *   <li>Si solo se pasa una fecha final, se incluyen clientes registrados hasta esa fecha.</li>
     *   <li>Si no se pasa ningún criterio, se devuelven todos los clientes.</li>
     *
     * @param nombre nombre del cliente a filtrar (opcional)
     * @param desde fecha inicial del filtro (opcional)
     * @param hasta fecha final del filtro (opcional).
     * @return lista de clientes que cumplen con los criterios de búsqueda
     */
    public List<Cliente> filtrarClientes(String nombre, LocalDate desde, LocalDate hasta) {
        List<Cliente> todos = repo.findAll();

        return todos.stream()
                .filter(c -> nombre == null || c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(c -> {
                    if (desde != null && hasta != null) {
                        return !c.getFechaRegistro().toLocalDate().isBefore(desde) &&
                                !c.getFechaRegistro().toLocalDate().isAfter(hasta);
                    } else if (desde != null) {
                        return !c.getFechaRegistro().toLocalDate().isBefore(desde);
                    } else if (hasta != null) {
                        return !c.getFechaRegistro().toLocalDate().isAfter(hasta);
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

}
//