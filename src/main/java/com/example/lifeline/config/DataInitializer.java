package com.example.lifeline.config;

import com.example.lifeline.model.Usuario;
import com.example.lifeline.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository repo, BCryptPasswordEncoder passwordEncoder) {
        return args -> {

            Optional<Usuario> adminOpt = repo.findByUserName("admin");

            if (adminOpt.isEmpty()) {

                Usuario admin = new Usuario();

                // --- CAMPOS ESENCIALES YA EXISTENTES ---
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("123"));
                admin.setRol("ADMIN");

                // --- CAMPOS NUEVOS AGREGADOS ---
                admin.setEmail("admin@lifeline.com");
                admin.setFechaNacimiento(LocalDate.of(2000, 1, 1));
                admin.setTipoDocumento("cc");
                admin.setNumeroDocumento("1000000000");
                // admin.setEstadio(null);  // No necesario para ADMIN

                repo.save(admin);
                System.out.println("Usuario admin creado con éxito");

            } else {
                System.out.println("Usuario admin ya existe");
            }
        };
    }
}
