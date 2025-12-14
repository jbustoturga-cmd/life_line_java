package com.example.lifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
    
@Controller
public class calendarioController {
      
    @GetMapping("/calendario")
    public String mostrarcalendario() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/calendario.html)
        return "calendario";
    }
      @GetMapping("/alarma")
    public String mostraralarma() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/calendario.html)
        return "alarma";
    }
    
    @GetMapping("/nutricion")
    public String mostrarnutricion() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/nutricion.html)
        return "nutricion";
    }
     @GetMapping("/ejercicio")
    public String mostrarejercicio() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/nutricion.html)
        return "ejercicio";
    }
}
