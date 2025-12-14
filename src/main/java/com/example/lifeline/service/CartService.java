package  com.example.lifeline.service;

import  com.example.lifeline.model.CartItem;
import  com.example.lifeline.model.Producto;
import  com.example.lifeline.repository.CartItemRepository;
import  com.example.lifeline.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la lógica de negocio del carrito de compras.
 */
@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, ProductoRepository productoRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todos los ítems del carrito de un usuario específico.
     */
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    /**
     * Añade un producto al carrito o incrementa su cantidad si ya existe.
     */
    public CartItem addItemToCart(Long userId, Long productId, int quantity) {
        // Validaciones
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }

        // Verificar que el producto existe
        Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        // Verificar si ya existe el producto en el carrito del usuario
        Optional<CartItem> existingItem = cartItemRepository
                .findByUserIdAndProducto_Id(userId, productId);

        if (existingItem.isPresent()) {
            // Si ya existe, incrementar la cantidad
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            // Si no existe, crear un nuevo item
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProducto(producto);
            newItem.setQuantity(quantity);
            
            // Asignar directamente (ya es BigDecimal)
            if (producto.getPrecioFinal() != null) {
                newItem.setUnitPrice(producto.getPrecioFinal());
            }
            
            newItem.setProductOptions(null);
            
            return cartItemRepository.save(newItem);
        }
    }

    /**
     * Actualiza la cantidad de un ítem del carrito.
     * Si la cantidad es 0, elimina el ítem.
     */
    public CartItem updateItemQuantity(Long itemId, int newQuantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item de carrito no encontrado con ID: " + itemId));

        if (newQuantity == 0) {
            // Si la cantidad es 0, eliminar el ítem
            cartItemRepository.delete(item);
            return null; // Indica que se eliminó
        }

        if (newQuantity < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        item.setQuantity(newQuantity);
        return cartItemRepository.save(item);
    }

    /**
     * Elimina un ítem específico del carrito.
     */
    public void removeItemFromCart(Long itemId) {
        if (!cartItemRepository.existsById(itemId)) {
            throw new RuntimeException("Item de carrito no encontrado con ID: " + itemId);
        }
        cartItemRepository.deleteById(itemId);
    }

    /**
     * Vacía completamente el carrito de un usuario.
     */
    public void clearCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(items);
    }

    /**
     * Calcula el total del carrito de un usuario.
     */
    public BigDecimal calculateCartTotal(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return items.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Obtiene la cantidad total de ítems en el carrito.
     */
    public int getCartItemCount(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}