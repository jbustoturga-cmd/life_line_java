package  com.example.lifeline.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidad que representa un ítem individual dentro del carrito de compras..
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el usuario (quién es el dueño del carrito).
    private Long userId;

    // Relación ManyToOne con el producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto producto;

    // Cantidad de este producto que el usuario desea
    private int quantity;

    // El precio unitario al momento de añadir el ítem.
    private BigDecimal unitPrice;

    // Opciones del producto como "color" o "talla"
    private String productOptions;

    // Constructor vacío (requerido por JPA)
    public CartItem() {
    }

    // Constructor completo.
    public CartItem(Long userId, Producto producto, int quantity, BigDecimal unitPrice, String productOptions) {
        this.userId = userId;
        this.producto = producto;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productOptions = productOptions;
    }

    // --- Getters y Setters ---.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        // Actualizar el precio unitario al precio actual del producto
        if (producto != null && producto.getPrecioFinal() != null) {
            this.unitPrice = producto.getPrecioFinal();
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(String productOptions) {
        this.productOptions = productOptions;
    }

    // Método de ayuda para calcular el subtotal de este ítem.
    public BigDecimal getSubTotal() {
        if (unitPrice == null || quantity <= 0) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Método para obtener el precio como double (para el JSON del frontend).
    @JsonProperty("price")
    public double getPriceAsDouble() {
        return unitPrice != null ? unitPrice.doubleValue() : 0.0;
    }

    // Compatibilidad: obtener productId desde el producto
    @JsonProperty("productId")
    public Long getProductId() {
        return producto != null ? producto.getId() : null;
    }

    // Mantener compatibilidad con el nombre "product" para el JSON del frontend.
    @JsonProperty("product")
    public Producto getProduct() {
        return producto;
    }
}
//