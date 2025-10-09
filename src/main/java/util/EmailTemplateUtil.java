package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for loading and processing email templates
 */
public class EmailTemplateUtil {

    private static final String TEMPLATE_BASE_PATH = "/email-templates/";

    /**
     * Load email template from resources
     */
    private static String loadTemplate(String templateName) throws IOException {
        String templatePath = TEMPLATE_BASE_PATH + templateName;

        try (InputStream inputStream = EmailTemplateUtil.class.getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new IOException("Template not found: " + templatePath);
            }

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        }
    }

    /**
     * Get verification email template with dynamic content
     */
    public static String getVerificationEmailTemplate(String verifyLink) throws IOException {
        String template = loadTemplate("verification-email.html");
        return template.replace("{{VERIFY_LINK}}", verifyLink);
    }

    /**
     * Get password reset email template with dynamic content
     */
    public static String getPasswordResetEmailTemplate(String resetLink) throws IOException {
        String template = loadTemplate("password-reset-email.html");
        return template.replace("{{RESET_LINK}}", resetLink);
    }

    /**
     * Get welcome email template with dynamic content
     */
    public static String getWelcomeEmailTemplate(String userName) throws IOException {
        String template = loadTemplate("welcome-email.html");
        return template.replace("{{USER_NAME}}", userName);
    }

    /**
     * Get order confirmation email template with dynamic content
     */
    public static String getOrderConfirmationEmailTemplate(String userName, String orderNumber, String totalAmount)
            throws IOException {
        String template = loadTemplate("order-confirmation-email.html");

        // Format current date
        String orderDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return template
                .replace("{{USER_NAME}}", userName)
                .replace("{{ORDER_NUMBER}}", orderNumber)
                .replace("{{ORDER_DATE}}", orderDate)
                .replace("{{TOTAL_AMOUNT}}", totalAmount);
    }
}
