package com.example.lifeline.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/", // Ruta raíz (Landing Page: index.html)
                                                                "/iniciosesion", // Página de Login
                                                                "/registro", // Página de Registro
                                                                "/registro/guardar",
                                                                "/Contact", // Página de Contacto
                                                                "/api/carrito/**", // Rutas API del carrito
                                                                "/css/**", // Archivos CSS estáticos
                                                                "/js/**", // Archivos JavaScript estáticos
                                                                "/imagenes/**", // Archivos de imágenes estáticas
                                                                "/img/**")
                                                .permitAll()

                                                // Si usas H2, mantén esto:
                                                .requestMatchers("/h2-console/**").permitAll()

                                                // Rutas que solo el rol 'ADMIN' puede acceder
                                                .requestMatchers("/usuarios/Perfil").hasRole("ADMIN")

                                                // Cualquier otra solicitud requiere autenticación
                                                .anyRequest().authenticated())

                                // 2. Configuración del Formulario de Inicio de Sesión
                                .formLogin(form -> form
                                                .loginPage("/iniciosesion")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/home", true)
                                                .permitAll())

                                // 3. Configuración de Cierre de Sesión
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())

                                .userDetailsService(userDetailsService)

                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.sendRedirect("/iniciosesion?denied");
                                                }))

                                // 5. Configuración de CSRF
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/carrito/**"));

                return http.build();
        }
}
