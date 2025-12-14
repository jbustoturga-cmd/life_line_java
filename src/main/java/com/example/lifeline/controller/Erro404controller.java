package  com.example.lifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Erro404controller {
    @GetMapping("/Erro404")
    public String mostrarErro404() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/erro404.html).
        return "erro404";
    }
}
//