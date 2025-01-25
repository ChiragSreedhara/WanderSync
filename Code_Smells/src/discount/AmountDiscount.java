package discount;

public class AmountDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price, double discountAmount) {
        return price - discountAmount;
    }
}