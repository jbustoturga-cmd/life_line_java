package  com.example.lifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class envioscontroller {
    @GetMapping("/envios")
    public String mostrarEnvios() {
        // Retorna el nombre del archivo HTML (ubicado en /templates/envios.html)
        return "envios";
    }

}
