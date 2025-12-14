package  com.example.lifeline.repository;

import  com.example.lifeline.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de CartItem en la base de datos.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Encuentra todos los ítems del carrito de un usuario específico.
     */
    List<CartItem> findByUserId(Long userId);

    /**
     * Encuentra un ítem específico del carrito por userId y productId.
     * Útil para verificar si un producto ya está en el carrito antes de añadirlo.
     */
    Optional<CartItem> findByUserIdAndProducto_Id(Long userId, Long productId);

    /**
     * Elimina todos los ítems del carrito de un usuario.
     */
    void deleteByUserId(Long userId);

    /**
     * Cuenta cuántos ítems diferentes tiene un usuario en su carrito.
     */
    long countByUserId(Long userId);
}