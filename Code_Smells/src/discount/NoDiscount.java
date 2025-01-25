package discount;

public class NoDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price, double discountAmount) {
        return price;
    }
}