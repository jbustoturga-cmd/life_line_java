package com.example.lifeline.controller;

import com.example.lifeline.model.Producto;
import com.example.lifeline.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TiendaController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/Tienda")
    public String tienda(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            userId = 1L;
        }
        
        // ✅ Cambiar aquí
        model.addAttribute("usuarioId", userId);
        
        List<Producto> productos = productoService.listar();
        model.addAttribute("productos", productos);
        
        return "tienda";
    }

    @GetMapping("/carrito")
    public String carrito(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            userId = 1L;
        }
        
        // ✅ Cambiar aquí
        model.addAttribute("usuarioId", userId);
        
        return "carrito";
    }

    @GetMapping("/productos/categoria/{categoria}")
    public String productosPorCategoria(@PathVariable String categoria,
                                       Model model,
                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            userId = 1L;
        }
        
        // ✅ Cambiar aquí
        model.addAttribute("usuarioId", userId);
        
        List<Producto> productos = productoService.listarPorCategoria(categoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaActual", categoria);
        
        return "tienda";
    }

    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id,
                                  Model model,
                                  HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            userId = 1L;
        }
        
        // ✅ Cambiar aquí
        model.addAttribute("usuarioId", userId);
        
        Producto producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        
        return "producto-detalle";
    }
}