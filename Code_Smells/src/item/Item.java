package item;

import discount.DiscountStrategy;

class Item {
    private String name;
    private double price;
    private int quantity;
    private double discountAmount;
    private DiscountStrategy discountStrategy;

    public Item(String name, double price, int quantity, double discountAmount, DiscountStrategy discountStrategy) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discountAmount = discountAmount;
        this.discountStrategy = discountStrategy;
    }

    public double calculateSubtotal() {
        double discountedPrice = discountStrategy.applyDiscount(price, discountAmount);
        return discountedPrice * quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}
