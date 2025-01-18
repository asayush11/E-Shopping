package src;

public class CODPayment implements Payment {
    public boolean processPayment(double amount) {
        System.out.println("Cash on delivery payment of amount " + amount + " processed.");
        return true;
    }
}
