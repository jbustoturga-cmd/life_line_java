package com.example.lifeline.model;


import java.math.BigDecimal;
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

@Entity
@Table(name = "Productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "precio_final")
    private BigDecimal precioFinal;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String categoria;

    private Integer cantidad;

    private Boolean estado;

    // ✔ Nombre coherente con el service.
    @Column(name = "imagen")
    private String imagen;
}
//