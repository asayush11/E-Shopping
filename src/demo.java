package src;

import java.util.List;

public class demo {
    public static void main(String[] args) {
        EShopping shoppingService = EShopping.getInstance();

        // Register users
        User user1 = shoppingService.registerUser("John Doe", "john@example.com", "password123");
        User user2 = shoppingService.registerUser("Jane Smith", "jane@example.com", "password456");

        // Add products
        Product product1 = new Product("P001", "Smartphone", "High-end smartphone", 999.99);
        Product product2 = new Product("P002", "Laptop", "Powerful gaming laptop", 1999.99);
        shoppingService.addProduct(product1);
        shoppingService.addProduct(product2);

        // User 1 adds products to cart and places an order
        user1.addToShoppingCart(product1, 2);
        user1.addToShoppingCart(product2, 1);
        Payment payment1 = new CardPayment();
        Order order1 = shoppingService.placeOrder(user1, payment1);
        System.out.println("Order placed: " + order1.getId());
        shoppingService.markOrderDelivered(order1.getId());

        // User 2 searches for products and adds to cart
        List<Product> searchResults = shoppingService.searchProducts("laptop");
        System.out.println("Search Results:");
        searchResults.forEach(product -> System.out.println("Product ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getPrice() + ", Description: " + product.getDescription()));

        user2.addToShoppingCart(searchResults.get(0), 4);
        Payment payment2 = new CODPayment();
        Order order2 = shoppingService.placeOrder(user2, payment2);
        System.out.println("Order placed: " + order2.getId());

        // User 1 views order history
        List<Order> userOrders = shoppingService.getUserOrders(user1.getId());
        System.out.println("User 1 Order History:");
        for (Order order : userOrders) {
            System.out.println("Order ID: " + order.getId());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Status: " + order.getStatus());
        }

        // User 2 views order history
        userOrders.clear();
        userOrders = shoppingService.getUserOrders(user2.getId());
        System.out.println("User 2 Order History:");
        for (Order order : userOrders) {
            System.out.println("Order ID: " + order.getId());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Status: " + order.getStatus());
        }

        // Remove user 2
        try {
            shoppingService.removeUser(user2.getId());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        // Cancel order 2
        shoppingService.cancelOrder(order2.getId());
        try {
            shoppingService.removeUser(user2.getId());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        // Remove product 2
        shoppingService.removeProduct(product2.getId());

    }

}
