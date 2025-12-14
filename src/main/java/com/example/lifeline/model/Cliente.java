package com.example.lifeline.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor 
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;
    
    private String telefono;

    // ESTE CAMPO ERA EL QUE FALTABA Y CAUSABA EL ERROR DE getPassword/setPassword
    @Column(nullable = false)
    private String password;

    // El ClienteService también necesita getFechaRegistro()
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    /**
     * Inicializa la fecha de registro automáticamente si no ha sido establecida.
     * Nota: Si el ClienteService ya establece esta fecha, este hook es redundante.
     */
    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
    }

    // Los métodos getPassword(), setPassword(), getFechaRegistro(), etc.
    // son generados por Lombok (@Data) y ahora compilan porque el campo 'password' existe.
}