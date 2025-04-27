package src;

public class Item {
    private final Product product;
    private int quantity;

    public Item(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
    public void updateQuantity(int quantity) {
        this.quantity += quantity;
    }
}
