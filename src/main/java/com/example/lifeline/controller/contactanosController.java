package com.example.lifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class contactanosController {
      @GetMapping("/Contactanos")
    public String mostrarContactanos() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/contactanos.html).
        return "contactanos";
    }
    
      @GetMapping("/Contact")
    public String mostrarContact() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/contactanos.html).
        return "contact";
    }
}
//