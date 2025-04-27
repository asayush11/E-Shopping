package src;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String password;
    private final ShoppingCart shoppingCart;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.shoppingCart = new ShoppingCart();
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void clearShoppingCart() {
        shoppingCart.clear();
        System.out.println("Shopping cart cleared.");
    }

    public void addToShoppingCart(Product product, int quantity) {
        shoppingCart.addItem(product, quantity);
        System.out.println("Product added to cart: " + product.getName());
    }

    public void removeFromShoppingCart(String productId) {
        shoppingCart.removeItem(productId);
        System.out.println("Product removed from cart: " + productId);
    }

    public void updateItemQuantity(String productId, int quantity) {
        shoppingCart.updateItemQuantity(productId, quantity);
        System.out.println("Cart updated: " + productId + " Quantity: " + shoppingCart.getItemById(productId).getQuantity());
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
}
