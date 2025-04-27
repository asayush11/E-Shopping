package src;

import java.util.*;
import java.util.stream.Collectors;


public class EShopping {
    private static EShopping instance;
    private final Map<String, User> users;
    private final Map<String, ProductCount> products;
    private final Map<String, Order> orders;

    private EShopping() {
        users = new HashMap<>();
        products = new HashMap<>();
        orders = new HashMap<>();
    }

    public static synchronized EShopping getInstance() {
        if (instance == null) {
            synchronized (EShopping.class) {
                if (instance == null) {
                    instance = new EShopping();
                }
            }
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
        long processingOrder = orders.values().stream()
                        .filter(order -> order.getUser().getId().equals(userId) && order.getStatus().equals(OrderStatus.PROCESSING))
                        .count();
        if (processingOrder > 0) {
            System.out.println("Cannot remove user with active orders.");
        }
        users.remove(userId);
        System.out.println("User removed from system: " + userId);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), new ProductCount(product, 100));
        System.out.println("Product added in system: " + product.getName());
    }

    public void removeProduct(String productId) {
        products.remove(productId);
        System.out.println("Product removed from system: " + productId);
    }

    public void updateProductQuantity(String productId, int quantity) {
        ProductCount productCount = products.get(productId);
        if (productCount != null) {
            productCount.updateCount(quantity);
            System.out.println("Product quantity updated: " + productId + " Quantity: " + productCount.getCount());
        } else {
            System.out.println("Product not found: " + productId);
        }
    }

    public Product getProduct(String productId) {
        return products.get(productId).getProduct();
    }

    public List<Product> searchProducts(String keyword) {
        return products.values().stream()
                .map(ProductCount::getProduct)
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList()).reversed();
    }

    public synchronized Order placeOrder(User user, Payment payment) {
        List<Item> items = new ArrayList<>();
        ShoppingCart cart = user.getShoppingCart();
        cart.getItems()
                .forEach(item -> {
                    Product product = item.getProduct();
                    int quantity = item.getQuantity();
                    if (products.get(product.getId()).getCount() >= quantity) {
                        products.get(product.getId()).updateCount(-quantity);
                        items.add(item);
                    } else {
                        System.out.println("Insufficient stock for product: " + product.getName());
                    }
                });

        if (items.isEmpty()) {
            System.out.println("No available products in the cart.");
            return null;
        }

        String orderId = generateOrderId();
        Order order = new Order(orderId, user, items);
        orders.put(orderId, order);

        if (payment.processPayment(order.getTotalAmount())) {
            System.out.println("Payment successful for order: " + order.getId());
            order.setStatus(OrderStatus.PROCESSING);
            user.clearShoppingCart();
        } else {
            order.setStatus(OrderStatus.CANCELLED);
            items.forEach(item -> {
                Product product = item.getProduct();
                int quantity = item.getQuantity();
                products.get(product.getId()).updateCount(quantity);
            });
            System.out.println("Payment failed for order: " + order.getId());
        }
        return order;
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            order.getItems().forEach(item -> {
                Product product = item.getProduct();
                int quantity = item.getQuantity();
                products.get(product.getId()).updateCount(quantity);
            });
            System.out.println("Order cancelled: " + orderId);
        }
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getUserOrders(String userId) {
        return orders.values().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    private String generateOrderId() {
        return "OR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateUserId() {
        return "US" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void markOrderDelivered(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.DELIVERED);
            System.out.println("Order delivered: " + orderId);
        }
    }
}
