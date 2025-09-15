package util;

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

public class SendMailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_USER = "phucbinbin2005@gmail.com"; // đổi
    private static final String SMTP_PASS = "onch ekrn djsj wamu"; // đổi

    public static void sendVerificationMail(String to, String verifyLink)
            throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(SMTP_USER, "BookStore"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject("Xác thực tài khoản");
        msg.setContent(
                "<h3>Nhấp vào liên kết để xác thực:</h3>" + "<a href=\"" + verifyLink + "\">Xác thực tài khoản</a>",
                "text/html; charset=UTF-8");

        Transport.send(msg);
    }
}
