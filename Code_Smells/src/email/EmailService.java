package email;

public interface EmailService {
    void sendConfirmationEmail(String email, String subject, String message);
}