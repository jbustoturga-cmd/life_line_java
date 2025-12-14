package com.example.lifeline.service;

import com.example.lifeline.model.Producto;
import com.example.lifeline.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;
import java.io.IOException;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    // Ruta absoluta del proyecto
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/images/";

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    /**
     * Lista todos los productos
     */
    public List<Producto> listar() {
        return repo.findAll();
    }

    /**
     * Lista productos por categoría
     * Para usar en TiendaController
     */
    public List<Producto> listarPorCategoria(String categoria) {
        // Opción 1: Si tienes el método en el repositorio
        // return repo.findByCategoria(categoria);
        
        // Opción 2: Filtrar en memoria (funciona sin modificar repositorio)
        return repo.findAll().stream()
                .filter(p -> categoria.equalsIgnoreCase(p.getCategoria()))
                .toList();
    }

    /**
     * Lista productos por estado (activo/inactivo)
     */
    public List<Producto> listarPorEstado(Boolean estado) {
        return repo.findAll().stream()
                .filter(p -> estado.equals(p.getEstado()))
                .toList();
    }

    /**
     * Lista solo productos activos (para la tienda pública)
     */
    public List<Producto> listarActivos() {
        return listarPorEstado(true);
    }

    /**
     * Inserta un nuevo producto con soporte para imagen.
     */
    public Producto insertar(Producto p, MultipartFile imagenFile) {
        // Guardar imagen
        String imagenGuardada = manejarSubidaImagen(imagenFile);

        // Asignar nombre/URL al producto
        p.setImagen(imagenGuardada);

        return repo.save(p);
    }

    /**
     * Actualizar producto con o sin nueva imagen.
     */
    public Producto actualizar(Long id, Producto p, MultipartFile imagenFile) {
        Producto existente = obtenerPorId(id);

        // Actualizar campos
        existente.setNombre(p.getNombre());
        existente.setDescripcion(p.getDescripcion());
        existente.setCategoria(p.getCategoria());
        existente.setPrecioFinal(p.getPrecioFinal());
        existente.setCantidad(p.getCantidad());
        existente.setEstado(p.getEstado());

        // Manejo de imagen
        if (imagenFile != null && !imagenFile.isEmpty()) {
            String nuevaImagen = manejarSubidaImagen(imagenFile);
            existente.setImagen(nuevaImagen);
        } else if (p.getImagen() != null) {
            // Mantener la imagen existente si no se sube una nueva.
            existente.setImagen(p.getImagen());
        }

        return repo.save(existente);
    }

    /**
     * Elimina un producto por ID
     */
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    /**
     * Obtiene un producto por ID
     */
    public Producto obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    /**
     * Busca productos por nombre (útil para buscador)
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return repo.findAll().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    /**
     * Verifica si hay stock disponible
     */
    public boolean tieneStock(Long productoId, int cantidadRequerida) {
        Producto producto = obtenerPorId(productoId);
        return producto.getCantidad() >= cantidadRequerida;
    }

    /**
     * Reduce el stock de un producto (para cuando se realiza una compra)
     */
    public void reducirStock(Long productoId, int cantidad) {
        Producto producto = obtenerPorId(productoId);
        
        if (producto.getCantidad() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        
        producto.setCantidad(producto.getCantidad() - cantidad);
        repo.save(producto);
    }

    /**
     * Guardar imagen en el servidor.
     */
    private String manejarSubidaImagen(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Nombre único
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;

            // Usar la ruta absoluta
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // Crea la carpeta
            }

            // Guardar archivo en la ruta absoluta
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // Devolver URL relativa accesible
            return "uploads/images/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Error guardando imagen: " + e.getMessage(), e);
        }
    }
}