package discount;

public class PercentageDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price, double discountAmount) {
        return price - (discountAmount * price);
    }
}