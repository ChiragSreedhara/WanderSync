# Code Smells

Participating Team Members: Chirag Sreedhara, Umer Wasti, Tsui Yi Tracy Cheung, Christopher Ling

Code Smell 1: Inappropriate Intimacy Coupling

This can be seen between the item.Order and email.EmailSender classes. The sendConfirmationEmail() method 
directly depends on email.EmailSender, making it difficult to change email-sending behavior without 
modifying item.Order. This can be solved by creating an email.EmailService interface. Then, pass an 
email.EmailService implementation (like email.EmailSender) to item.Order. This decouples item.Order from a specific email 
implementation, which is beneficial for testing and scaling later on in the process.


Code Smell 2: Long Method Bloaters

The calculateTotalPrice method in item.Order.java was overly complex, containing multiple
responsibilities that hindered readability and maintainability. To resolve this, I 
refactored the method and delegated key responsibilities to into separate private 
helper methods: calcTax(), giftCard(), and tenPercentDiscount(). This adheres
to the Single Responsibility Principle and allows each method to handle a specific
part of the logic, which simplifies testing and accommodates future additions.

Code Smell 3: Feature Envy Coupling

calculateTotalPrice() method in item.Order class is interested in the item subtotal. It uses price, 
discountType, discountAmount, and quantity attributes of item.Item class. Since item.Item has most of the 
data, the part to calculate item subtotal will be put into item.Item as getSubtotal().

calculateTotalPrice() in item.Order class is also interested in the taxRate and price of item.TaxableItem
class to calculate tax. item.TaxableItem has most of the data, so the method to calculate tax will be 
put in item.TaxableItem.

Code Smell 4: OO Abusers - Switch Statement

The calculateSubtotal method in the item.Item class used a switch statement based on the DiscountType 
enum to apply discounts. This design violated the Open/Closed Principle, making it difficult to 
add or modify discount types without changing the code in multiple places. The switch statement 
also  led to code duplication and reduced flexibility by embedding discount logic directly 
in the item.Item class.

To resolve this, I removed the DiscountType enum and replaced it with polymorphism. I created a 
discount.DiscountStrategy interface, with specific classes for each discount type: discount.PercentageDiscount, 
discount.AmountDiscount, and discount.NoDiscount. Now, each discount type is represented by its own class, 
encapsulating its behavior. The item.Item class has a discount.DiscountStrategy instance, and calculateSubtotal 
delegates the discount calculation to this strategy. This change makes the code more flexible, as 
new discount types can be added by simply implementing the discount.DiscountStrategy interface, without 
modifying the item.Item class or calculateSubtotal.
