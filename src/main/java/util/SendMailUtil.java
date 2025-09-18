package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Email utility class for sending various types of emails.
 * Handles email configuration and provides methods for common email operations
 * in the BookieCake application.
 * 
 * @author BookieCake Team
 * @version 1.0
 */
public class SendMailUtil {
    
    // SMTP Configuration Constants
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "phucbinbin2005@gmail.com"; // TODO: Move to environment variable
    private static final String SMTP_PASSWORD = "onch ekrn djsj wamu"; // TODO: Move to environment variable
    private static final String FROM_NAME = "BookieCake";
    
    /**
     * Creates and configures the mail session with SMTP settings.
     * 
     * @return configured mail Session object
     */
    private static Session createMailSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });
    }
    
    /**
     * Creates a basic email message with common settings.
     * 
     * @param session the mail session
     * @param to the recipient email address
     * @param subject the email subject
     * @return configured MimeMessage object
     * @throws MessagingException if an error occurs while creating the message
     * @throws UnsupportedEncodingException if the encoding is not supported
     */
    private static MimeMessage createBaseMessage(Session session, String to, String subject) 
            throws MessagingException, UnsupportedEncodingException {
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USER, FROM_NAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject, "UTF-8");
        
        return message;
    }
    
    /**
     * Sends an account verification email to the user.
     * 
     * @param recipientEmail the recipient's email address
     * @param verificationLink the verification link to include in the email
     * @throws MessagingException if an error occurs while sending the email
     * @throws UnsupportedEncodingException if the encoding is not supported
     * @throws IllegalArgumentException if parameters are null or empty
     */
    public static void sendVerificationMail(String recipientEmail, String verificationLink)
            throws MessagingException, UnsupportedEncodingException {
        
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }
        
        if (verificationLink == null || verificationLink.trim().isEmpty()) {
            throw new IllegalArgumentException("Verification link cannot be null or empty");
        }
        
        Session session = createMailSession();
        MimeMessage message = createBaseMessage(session, recipientEmail, "Xác thực tài khoản BookieCake");
        
        String htmlContent = String.format(
            "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
            "<h2 style='color: #2c3e50;'>Chào mừng đến với BookieCake!</h2>" +
            "<p>Cảm ơn bạn đã đăng ký tài khoản. Vui lòng nhấp vào liên kết bên dưới để xác thực tài khoản:</p>" +
            "<div style='text-align: center; margin: 30px 0;'>" +
            "<a href='%s' style='background-color: #3498db; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block;'>Xác thực tài khoản</a>" +
            "</div>" +
            "<p style='color: #7f8c8d; font-size: 14px;'>Nếu bạn không thể nhấp vào nút, hãy copy và paste liên kết sau vào trình duyệt:</p>" +
            "<p style='word-break: break-all; color: #7f8c8d; font-size: 12px;'>%s</p>" +
            "<hr style='border: none; border-top: 1px solid #ecf0f1; margin: 30px 0;'>" +
            "<p style='color: #95a5a6; font-size: 12px;'>Đây là email tự động, vui lòng không trả lời email này.</p>" +
            "</div>",
            verificationLink, verificationLink
        );
        
        message.setContent(htmlContent, "text/html; charset=UTF-8");
        Transport.send(message);
    }
    
    /**
     * Sends a password reset email to the user.
     * 
     * @param recipientEmail the recipient's email address
     * @param resetLink the password reset link to include in the email
     * @throws MessagingException if an error occurs while sending the email
     * @throws UnsupportedEncodingException if the encoding is not supported
     * @throws IllegalArgumentException if parameters are null or empty
     */
    public static void sendPasswordResetMail(String recipientEmail, String resetLink)
            throws MessagingException, UnsupportedEncodingException {
        
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }
        
        if (resetLink == null || resetLink.trim().isEmpty()) {
            throw new IllegalArgumentException("Reset link cannot be null or empty");
        }
        
        Session session = createMailSession();
        MimeMessage message = createBaseMessage(session, recipientEmail, "Đặt lại mật khẩu BookieCake");
        
        String htmlContent = String.format(
            "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
            "<h2 style='color: #e74c3c;'>Yêu cầu đặt lại mật khẩu</h2>" +
            "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>" +
            "<p>Nhấp vào liên kết bên dưới để đặt lại mật khẩu:</p>" +
            "<div style='text-align: center; margin: 30px 0;'>" +
            "<a href='%s' style='background-color: #e74c3c; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block;'>Đặt lại mật khẩu</a>" +
            "</div>" +
            "<p style='color: #e67e22; font-weight: bold;'>⚠️ Liên kết này sẽ hết hạn sau 30 phút.</p>" +
            "<p style='color: #7f8c8d; font-size: 14px;'>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>" +
            "<p style='color: #7f8c8d; font-size: 14px;'>Nếu bạn không thể nhấp vào nút, hãy copy và paste liên kết sau vào trình duyệt:</p>" +
            "<p style='word-break: break-all; color: #7f8c8d; font-size: 12px;'>%s</p>" +
            "<hr style='border: none; border-top: 1px solid #ecf0f1; margin: 30px 0;'>" +
            "<p style='color: #95a5a6; font-size: 12px;'>Đây là email tự động, vui lòng không trả lời email này.</p>" +
            "</div>",
            resetLink, resetLink
        );
        
        message.setContent(htmlContent, "text/html; charset=UTF-8");
        Transport.send(message);
    }
    
    /**
     * Sends a generic notification email.
     * 
     * @param recipientEmail the recipient's email address
     * @param subject the email subject
     * @param content the email content (HTML format)
     * @throws MessagingException if an error occurs while sending the email
     * @throws UnsupportedEncodingException if the encoding is not supported
     * @throws IllegalArgumentException if parameters are null or empty
     */
    public static void sendNotificationMail(String recipientEmail, String subject, String content)
            throws MessagingException, UnsupportedEncodingException {
        
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }
        
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        
        Session session = createMailSession();
        MimeMessage message = createBaseMessage(session, recipientEmail, subject);
        message.setContent(content, "text/html; charset=UTF-8");
        
        Transport.send(message);
    }
}
