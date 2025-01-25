package discount;

public interface DiscountStrategy {
    double applyDiscount(double price, double discountAmount);
}