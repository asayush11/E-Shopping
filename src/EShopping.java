package src;

import java.util.*;
import java.util.stream.Collectors;

public class EShopping {
    private static EShopping instance;
    private final Map<String, User> users;
    private final Map<String, Product> products;
    private final Map<String, Order> orders;

    private EShopping() {
        users = new HashMap<>();
        products = new HashMap<>();
        orders = new HashMap<>();
    }

    public static synchronized EShopping getInstance() {
        if (instance == null) {
            instance = new EShopping();
        }
        return instance;
    }

    public User registerUser(String name, String email, String password) {
        String userId = generateUserId();
        User user = new User(userId, name, email, password);
        users.put(user.getId(), user);
        System.out.println("User registered: " + user.getId());
        return user;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void removeUser(String userId) {
        users.remove(userId);
        // dont remove if there is an order associated with the user with status other than DELIVERED OR CANCELLED
        for (Order order : orders.values()) {
            if (order.getUser().getId().equals(userId) && order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.CANCELLED) {
                throw new IllegalStateException("Cannot remove user with pending orders.");
            }
        }
        System.out.println("User removed from system: " + userId);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        System.out.println("Product added in system: " + product.getName());
    }

    public void removeProduct(String productId) {
        products.remove(productId);
        System.out.println("Product removed from system: " + productId);
    }

    public void updateProductQuantity(String productId, int quantity) {
        Product product = products.get(productId);
        if (product != null) {
            product.updateQuantity(quantity);
            if(product.getQuantity() < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }
            else if(product.getQuantity() == 0) {
                products.remove(productId);
                System.out.println("Product out of stock: " + productId);
            }
            System.out.println("Product quantity updated: " + productId + " Quantity: " + quantity);
        }
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public List<Product> searchProducts(String keyword) {
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public synchronized Order placeOrder(User user, Payment payment) {
        List<OrderItem> orderItems = new ArrayList<>();
        ShoppingCart cart = user.getShoppingCart();
        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            if (product.isAvailable(quantity)) {
                product.updateQuantity(-quantity);
                orderItems.add(item);
            }
        }

        if (orderItems.isEmpty()) {
            throw new IllegalStateException("No available products in the cart.");
        }

        String orderId = generateOrderId();
        Order order = new Order(orderId, user, orderItems);
        orders.put(orderId, order);
        user.addOrder(order);

        if (payment.processPayment(order.getTotalAmount())) {
            order.setStatus(OrderStatus.PROCESSING);
            System.out.println("Payment successful for order: " + order.getId());
        } else {
            order.setStatus(OrderStatus.CANCELLED);
            // Revert the product quantities
            for (OrderItem item : orderItems) {
                Product product = item.getProduct();
                int quantity = item.getQuantity();
                product.updateQuantity(quantity);
            }
            System.out.println("Payment failed for order: " + order.getId());
        }

        return order;
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            User user = order.getUser();
            user.cancelOrder(orderId);
            // Revert the product quantities
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                int quantity = item.getQuantity();
                product.updateQuantity(quantity);
            }
            System.out.println("Order cancelled: " + orderId);
        }
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    private String generateOrderId() {
        return "OR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateUserId() {
        return "US" + users.size();
    }

    public void markOrderDelivered(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.DELIVERED);
            User user = order.getUser();
            user.updateOrderStatus(orderId, OrderStatus.DELIVERED);
            System.out.println("Order delivered: " + orderId);
        }
    }
}
