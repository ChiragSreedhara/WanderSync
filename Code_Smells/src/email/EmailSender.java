package email;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSender implements EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailSender.class.getName());

    @Override
    public void sendConfirmationEmail(String email, String subject, String message) {
        LOGGER.log(Level.INFO, "Email to: {0}", email); // Using built-in formatting
        LOGGER.log(Level.INFO, "Subject: {0}", subject); // Using built-in formatting
        LOGGER.log(Level.INFO, "Message: {0}", message); // Using built-in formatting
    }
}