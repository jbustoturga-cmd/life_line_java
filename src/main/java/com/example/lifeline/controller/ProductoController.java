package com.example.lifeline.controller;

import com.example.lifeline.model.Producto;
import com.example.lifeline.service.ProductoService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService servicio;

    public ProductoController(ProductoService servicio) {
        this.servicio = servicio;
    }

    // LISTAR.
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("productos", servicio.listar());
        return "listar";
    }

    // NUEVO FORMULARIO.
    @GetMapping("/nuevo")
    public String nuevoFormulario(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "formulario";
    }

    // GUARDAR PRODUCTO NUEVO + IMAGEN.
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Producto producto,
            @RequestParam("imagenFile") MultipartFile imagenFile
    ) {
        servicio.insertar(producto, imagenFile);
        return "redirect:/productos";
    }

    // EDITAR.
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("producto", servicio.obtenerPorId(id));
        return "formulario";
    }

    // ACTUALIZAR PRODUCTO + IMAGEN.
    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @ModelAttribute Producto producto,
            @RequestParam("imagenFile") MultipartFile imagenFile
    ) {
        servicio.actualizar(id, producto, imagenFile);
        return "redirect:/productos";
    }

    // ELIMINAR.
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return "redirect:/productos";
    }
}
//