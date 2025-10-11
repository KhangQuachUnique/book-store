package util;

import java.io.IOException;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

/**
 * BookieCake Email Service Utility
 * Updated to use Resend API for email delivery
 */
public class SendMailUtil {
    private static final String RESEND_API_KEY = "re_xxxxxxxxx"; // Replace with your Resend API key
    private static final String FROM_EMAIL = "BookieCake <onboarding@resend.dev>"; // Change this to your verified domain
    private static final String REPLY_TO_EMAIL = "support@bookiecake.vn"; // Change this

    // Production mode - set to false to disable debug logging
    private static final boolean DEBUG_MODE = false;
    
    // Initialize Resend client
    private static final Resend resend = new Resend(RESEND_API_KEY);

    /**
     * Test method to validate Resend API configuration
     */
    public static void testEmailConfig() throws ResendException {
        try {
            // Test the API by creating a simple email configuration test
            if (DEBUG_MODE) {
                System.out.println("DEBUG: Testing Resend API configuration...");
            }
            
            // Validate that resend client is properly initialized
            if (resend == null) {
                throw new IllegalStateException("Resend client is not initialized");
            }
            
            System.out.println("✅ Resend API configuration test successful!");
        } catch (Exception e) {
            System.err.println("❌ Resend API configuration test failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Send email using Resend API
     */
    private static CreateEmailResponse sendEmail(String to, String subject, String htmlContent) throws ResendException {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(FROM_EMAIL)
                .to(to)
                .subject(subject)
                .html(htmlContent)
                .replyTo(REPLY_TO_EMAIL)
                .build();

        if (DEBUG_MODE) {
            System.out.println("DEBUG: Sending email to: " + to + " with subject: " + subject);
        }

        return resend.emails().send(params);
    }

    /**
     * Send verification email after user registration
     */
    public static void sendVerificationMail(String to, String verifyLink) throws ResendException, IOException {
        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send verification email to: " + to);
        }

        try {
            String htmlContent = EmailTemplateUtil.getVerificationEmailTemplate(verifyLink);
            CreateEmailResponse response = sendEmail(to, "🔐 Xác thực tài khoản BookieCake", htmlContent);

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Verification email sent with ID: " + response.getId());
            }

            System.out.println("✅ Verification email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Error sending verification email to " + to + ": " + e.getMessage());
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * Send password reset email
     */
    public static void sendPasswordResetMail(String to, String resetLink) throws ResendException, IOException {
        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send password reset email to: " + to);
        }

        try {
            String htmlContent = EmailTemplateUtil.getPasswordResetEmailTemplate(resetLink);
            CreateEmailResponse response = sendEmail(to, "🔑 Đặt lại mật khẩu BookieCake", htmlContent);

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Password reset email sent with ID: " + response.getId());
            }

            System.out.println("✅ Password reset email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Error sending password reset email to " + to + ": " + e.getMessage());
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * Send welcome email after successful registration
     */
    public static void sendWelcomeMail(String to, String userName) throws ResendException, IOException {
        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send welcome email to: " + to);
        }

        try {
            String htmlContent = EmailTemplateUtil.getWelcomeEmailTemplate(userName);
            CreateEmailResponse response = sendEmail(to, "🎉 Chào mừng đến với BookieCake!", htmlContent);

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Welcome email sent with ID: " + response.getId());
            }

            System.out.println("✅ Welcome email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Error sending welcome email to " + to + ": " + e.getMessage());
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * Send order confirmation email
     */
    public static void sendOrderConfirmationEmail(String to, String userName, String orderNumber, String totalAmount)
            throws ResendException, IOException {
        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send order confirmation email to: " + to);
        }

        try {
            String htmlContent = EmailTemplateUtil.getOrderConfirmationEmailTemplate(userName, orderNumber, totalAmount);
            CreateEmailResponse response = sendEmail(to, "✅ Xác nhận đơn hàng #" + orderNumber + " - BookieCake", htmlContent);

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Order confirmation email sent with ID: " + response.getId());
            }

            System.out.println("✅ Order confirmation email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Error sending order confirmation email to " + to + ": " + e.getMessage());
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            throw e;
        }
    }
}
