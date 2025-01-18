package src;

public class CardPayment implements Payment {
    public boolean processPayment(double amount) {
        System.out.println("Card payment of amount " + amount + " processed.");
        return true;
    }
}
