package com.example.lifeline.controller;

import com.example.lifeline.model.Usuario;
import com.example.lifeline.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // --- RUTA RAIZ ELIMINADA PARA EVITAR CONFLICTO CON ACCESOCONTROLLER ---
    // @GetMapping("/")
    // public String redireccionRaiz() {
    //     return "redirect:/login";
    // }
    // La ruta raíz ahora es manejada por AccesoController para mostrar la Landing Page.

    @GetMapping("/login")
    public String login() {
        return "login"; // Asume que tienes una plantilla llamada login.html
    }

    @GetMapping("/home")
    public String home(Model model, Authentication auth) {
        model.addAttribute("rol", auth.getAuthorities().toString());
        return "home"; // Asume que tienes una plantilla llamada home.html
    }

    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", repo.findAll());
        return "usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form"; // Reutiliza el formulario para crear/editar
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {
        // Encripta la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        repo.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOpt = repo.findById(id);

        if (usuarioOpt.isEmpty()) {
            // Manejo de error si el usuario no existe
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + id);
        }

        model.addAttribute("usuario", usuarioOpt.get());
        return "form";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/usuarios";
    }

    // --- Gestión del Perfil del Usuario Autenticado ---

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String username = auth.getName();
        // Asumiendo que existe un método findByUserName en tu repositorio
        Optional<Usuario> usuarioOpt = repo.findByUserName(username); 

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado: " + username);
        }

        model.addAttribute("usuario", usuarioOpt.get());
        // Asumiendo que la edición de perfil usa la misma plantilla 'form' o una específica 'perfilForm'
        return "form";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute Usuario usuario, Authentication auth) {
        String username = auth.getName();
        Optional<Usuario> actualOpt = repo.findByUserName(username);

        if (actualOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado: " + username);
        }

        Usuario actual = actualOpt.get();

        // Actualiza los campos que el usuario puede cambiar desde su perfil
        actual.setEmail(usuario.getEmail());
        actual.setFechaNacimiento(usuario.getFechaNacimiento());
        actual.setTipoDocumento(usuario.getTipoDocumento());
        actual.setNumeroDocumento(usuario.getNumeroDocumento());
        actual.setEstadio(usuario.getEstadio());

        // Solo actualiza el nombre de usuario si se proporciona y es diferente
        if (usuario.getUserName() != null && !usuario.getUserName().isEmpty() && !usuario.getUserName().equals(actual.getUserName())) {
            actual.setUserName(usuario.getUserName());
        }

        // Solo actualiza la contraseña si se proporciona una nueva
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            actual.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        repo.save(actual);
        return "redirect:/home?actualizado";
    }
}