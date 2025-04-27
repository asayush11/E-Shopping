package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private final Map<String, Item> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        String productId = product.getId();
        items.put(productId, new Item(product, quantity));
    }

    public void removeItem(String productId) {
        items.remove(productId);
        System.out.println("Item removed from cart: " + productId);
    }

    public void updateItemQuantity(String productId, int quantity) {
        Item item = items.get(productId);
        if(item != null) {
            item.updateQuantity(quantity);
            System.out.println("Item quantity updated: " + productId + " Quantity: " + item.getQuantity());
            if(item.getQuantity() <= 0) {
                removeItem(productId);
            }
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }
    public Item getItemById(String productId) {
        return items.get(productId);
    }

    public void clear() {
        items.clear();
    }
}
