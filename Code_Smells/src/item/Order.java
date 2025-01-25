package item;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import email.EmailService;

public class Order {
    private List<Item> items;
    private String customerName;
    private String customerEmail;
    private static final Logger LOGGER = Logger.getLogger(Order.class.getName());

    public Order(List<Item> items, String customerName, String customerEmail) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public double calculateTotalPrice() {
        double total = 0.0;
    	for (Item item : items) {
        	total += item.calculateSubtotal();
            if (item instanceof TaxableItem) {
                total += ((TaxableItem) item).getTax();
            }
        }
        total = giftCard(total);
        total = tenPercentDiscount(total);
        return total;
    }
    
    // subtracts $10 for gift card
    private double giftCard(double total) {
        if (hasGiftCard()) {
            total -= 10.0;
        }
        return total;
    }
    
    // applies 10% discount for orders over $100
    private double tenPercentDiscount(double total) {
        if (total > 100.0) {
            total *= 0.9;
        }
        return total;
    }

    public void sendConfirmationEmail(EmailService sender) {
        StringBuilder messageBuilder = new StringBuilder(); // Create a StringBuilder instance
        messageBuilder.append("Thank you for your order, ").append(customerName).append("!\n\n")
                .append("Your order details:\n");

        for (Item item : items) {
            messageBuilder.append(item.getName()).append(" - ").append(item.getPrice()).append("\n");
        }

        messageBuilder.append("Total: ").append(calculateTotalPrice());
        String message = messageBuilder.toString(); // Convert StringBuilder to String
        sender.sendConfirmationEmail(customerEmail, "Order Confirmation", message);
    }


    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean hasGiftCard() {
        boolean hasGiftCard = false;
        for (Item item : items) {
            if (item instanceof GiftCardItem) {
                hasGiftCard = true;
                break;
            }
        }
        return hasGiftCard;
    }

   public void printOrder() {
        LOGGER.log(Level.INFO,"Order Details:");

        for (Item item : items) {
            LOGGER.log(Level.INFO,"{0} - {1}", new Object[]{item.getName(), item.getPrice()});
        }
   }

   public void addItemsFromAnotherOrder(Order otherOrder) {
        for (Item item : otherOrder.getItems()) {
            items.add(item);
        }
   }

}

