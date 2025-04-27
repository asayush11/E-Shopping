package src;

public class ProductCount {
    private final Product product;
    private int count;
    ProductCount(Product product, int count) {
        this.product = product;
        this.count = count;
    }
    public Product getProduct() {
        return product;
    }
    public int getCount() {
        return count;
    }
    public void updateCount(int count) {
        this.count += count;
    }
}