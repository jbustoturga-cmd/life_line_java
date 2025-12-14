package com.example.lifeline.model;
//
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol; // Ej: ROLE_ADMIN, ROLE_USER, ROLE_PACIENTE.

    // Campos extra del formulario.
    @Column(unique = true)
    private String email;

    private LocalDate fechaNacimiento;

    private String tipoDocumento;

    @Column(unique = true)
    private String numeroDocumento;

    private String estadio; // Solo si el usuario es paciente.
}
//