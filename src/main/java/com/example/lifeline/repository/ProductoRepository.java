package com.example.lifeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

// Línea CORRECTA:
import com.example.lifeline.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}