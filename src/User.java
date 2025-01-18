package src;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String password;
    private final List<Order> orders;
    private final ShoppingCart shoppingCart;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.orders = new ArrayList<>();
        this.shoppingCart = new ShoppingCart();
    }

    public void addOrder(Order order) {
        orders.add(order);
        shoppingCart.clear();
        System.out.println("Order added: " + order.getId());
    }

    public void cancelOrder(String orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
        System.out.println("Order cancelled: " + orderId);
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
        if (order != null) {
            order.setStatus(status);
            System.out.println("Order status updated: " + orderId + " Status: " + status);
        }
    }

    public void addToshoppingCart(Product product, int quantity) {
        shoppingCart.addItem(product, quantity);
        System.out.println("Product added to cart: " + product.getName());
    }

    public void removeFromshoppingCart(String productId) {
        shoppingCart.removeItem(productId);
        System.out.println("Product removed from cart: " + productId);
    }

    public void updateItemQuantity(String productId, int quantity) {
        shoppingCart.updateItemQuantity(productId, quantity);
        System.out.println("Cart updated: " + productId + " Quantity: " + quantity);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
