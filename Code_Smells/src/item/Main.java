package item;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import discount.AmountDiscount;
import discount.NoDiscount;
import discount.PercentageDiscount;
import email.EmailSender;
import email.EmailService;

public class Main {
        private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) {
        Item item1 = new Item("Book", 20, 1, 5, new AmountDiscount());
        Item item2 = new Item("Laptop", 1000, 1, 0.1, new PercentageDiscount());
        Item item3 = new Item("Gift Card", 10, 1, 0, new NoDiscount());

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Order order = new Order(items, "John Doe", "johndoe@example.com");

        LOGGER.log(Level.INFO,"Total Price: {0}", order.calculateTotalPrice()); // Using built-in formatting

        EmailService emailSender = new EmailSender();
        order.sendConfirmationEmail(emailSender);

        order.printOrder();
    }
}

