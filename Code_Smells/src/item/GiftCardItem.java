package item;

import discount.NoDiscount;

public class GiftCardItem extends Item {
    public GiftCardItem(String name, double price, int quantity) {
        super(name, price, quantity, 0, new NoDiscount());
    }
}