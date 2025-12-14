package com.example.lifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class muralController {
      
    @GetMapping("/Mural")
    public String mostrarMural() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/mural.html).
        return "mural";
    }
}

//