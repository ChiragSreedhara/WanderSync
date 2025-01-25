package item;

import discount.DiscountStrategy;

public class TaxableItem extends Item {
    private double taxRate = 7;
    
    public TaxableItem(String name, double price, int quantity, double discountAmount, DiscountStrategy discountStrategy){
        super(name, price, quantity, discountAmount, discountStrategy);
    }

    public double getTaxRate(){
        return taxRate;
    }
    public void setTaxRate(double rate) {
        if(rate>=0){
            taxRate = rate;
        }
    }
    public double getTax() {
        return taxRate / 100.0 * this.getPrice();
    }
}
