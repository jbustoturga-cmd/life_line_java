package  com.example.lifeline.controller;

import  com.example.lifeline.model.CartItem;
import  com.example.lifeline.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del carrito de compras.
 * Define los endpoints para interactuar con la lógica del carrito.
 * (CartService), devolviendo datos en formato JSON/HTTP Responses.
 */
@RestController
@RequestMapping("/api/carrito") // Prefijo para todos los endpoints de este controlador.
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Endpoint para obtener todos los items del carrito de un usuario.
     * Asume que el ID del usuario se obtiene de la sesión/token de autenticación
     * (simulado con @PathVariable).
     *
     * GET /api/carrito/{userId}.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * DTO (Data Transfer Object) para la petición de añadir un ítem..
     * Se define como clase estática anidada para simplificar.
     */
    public static class AddItemRequest {
        public Long productId;
        public int quantity;
        public String productOptions; // Opcional, para manejar variantes
    }

    /**
     * Endpoint para añadir un producto al carrito..
     * POST /api/carrito/{userId}/add
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addItem(
            @PathVariable Long userId,
            @RequestBody AddItemRequest request) {
        try {
            // Validación básica de entrada.
            if (request.productId == null || request.quantity <= 0) {
                return ResponseEntity.badRequest().body("ID de producto y cantidad válidos son requeridos.");
            }

            CartItem updatedItem = cartService.addItemToCart(
                    userId,
                    request.productId,
                    request.quantity
            // En la implementación real, también se pasaría productOptions
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedItem);
        } catch (IllegalArgumentException e) {
            // Captura de errores esperados (e.g., cantidad negativa)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Manejar errores de negocio (e.g., producto no encontrado, sin stock).
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al añadir ítem: " + e.getMessage());
        }
    }

    /**
     * Endpoint para actualizar la cantidad de un item existente en el carrito.
     * PUT /api/carrito/item/{itemId}/quantity/{quantity}.
     */
    @PutMapping("/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<?> updateItemQuantity(
            @PathVariable Long itemId,
            @PathVariable int quantity) {
        try {
            if (quantity < 0) {
                return ResponseEntity.badRequest().body("La cantidad debe ser cero o mayor.");
            }

            CartItem updatedItem = cartService.updateItemQuantity(itemId, quantity);

            if (updatedItem == null) {
                // Si updateItemQuantity retorna null, significa que la cantidad era 0 y se.
                // eliminó
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Item eliminado del carrito.");
            }
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item de carrito no encontrado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para eliminar un item del carrito.
     * DELETE /api/carrito/item/{itemId}.
     */
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    /**
     * Endpoint para vaciar completamente el carrito de un usuario (e.g., al
     * checkout).
     * DELETE /api/carrito/{userId}/clear.
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
//