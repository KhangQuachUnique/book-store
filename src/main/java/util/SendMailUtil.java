package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * BookieCake Email Service Utility
 * Refactored to use external HTML templates for cleaner code
 */
public class SendMailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_USER = "bookiecake.vn@gmail.com"; // Change this
    private static final String SMTP_PASS = "tykr suyr mruc lkpi"; // Change this

    // Production mode - set to false to disable debug logging
    private static final boolean DEBUG_MODE = false;

    /**
     * Test method to validate email configuration
     */
    public static void testEmailConfig() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");
        if (DEBUG_MODE) {
            props.put("mail.debug", "true");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        // Test connection
        Transport transport = session.getTransport("smtp");
        transport.connect(SMTP_HOST, SMTP_USER, SMTP_PASS);
        transport.close();
        System.out.println("✅ Email configuration test successful!");
    }

    /**
     * Common method to create email session
     */
    private static Session createEmailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        if (DEBUG_MODE) {
            props.put("mail.debug", "true");
        }

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });
    }

    /**
     * Send verification email after user registration
     */
    public static void sendVerificationMail(String to, String verifyLink)
            throws MessagingException, UnsupportedEncodingException, IOException {

        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send verification email to: " + to);
        }

        Session session = createEmailSession();

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SMTP_USER, "BookieCake - Book Store"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("🔐 Xác thực tài khoản BookieCake");
            msg.setContent(EmailTemplateUtil.getVerificationEmailTemplate(verifyLink), "text/html; charset=UTF-8");

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Sending verification email...");
            }
            Transport.send(msg);

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
    public static void sendPasswordResetMail(String to, String resetLink)
            throws MessagingException, UnsupportedEncodingException, IOException {

        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send password reset email to: " + to);
        }

        Session session = createEmailSession();

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SMTP_USER, "BookieCake - Book Store"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("🔑 Đặt lại mật khẩu BookieCake");
            msg.setContent(EmailTemplateUtil.getPasswordResetEmailTemplate(resetLink), "text/html; charset=UTF-8");

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Sending password reset email...");
            }
            Transport.send(msg);

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
    public static void sendWelcomeMail(String to, String userName)
            throws MessagingException, UnsupportedEncodingException, IOException {

        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send welcome email to: " + to);
        }

        Session session = createEmailSession();

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SMTP_USER, "BookieCake - Book Store"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("🎉 Chào mừng đến với BookieCake!");
            msg.setContent(EmailTemplateUtil.getWelcomeEmailTemplate(userName), "text/html; charset=UTF-8");

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Sending welcome email...");
            }
            Transport.send(msg);

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
            throws MessagingException, UnsupportedEncodingException, IOException {

        if (DEBUG_MODE) {
            System.out.println("DEBUG: Starting to send order confirmation email to: " + to);
        }

        Session session = createEmailSession();

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SMTP_USER, "BookieCake - Book Store"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("✅ Xác nhận đơn hàng #" + orderNumber + " - BookieCake");
            msg.setContent(EmailTemplateUtil.getOrderConfirmationEmailTemplate(userName, orderNumber, totalAmount),
                    "text/html; charset=UTF-8");

            if (DEBUG_MODE) {
                System.out.println("DEBUG: Sending order confirmation email...");
            }
            Transport.send(msg);

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
