package com.example.lifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {
    
    
    @GetMapping("/Perfil")
    public String mostrarPerfil() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/perfil.html)
        return "perfil";
    }
}

