package common_interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import database.JDBC;

public class Honeychecker {

    private static final Logger LOGGER = Logger.getLogger(Honeychecker.class.getName());

    public static boolean checkHoneywords(String userId, String enteredHashedHoneyword) {
        try (Connection connection = JDBC.getConnection()) {
            Set<String> honeywords = getHoneywordsByUserId(connection, userId);

            for (String hashedHoneyword : honeywords) {
                LOGGER.info("Comparing entered hashed honeyword: " + enteredHashedHoneyword + " with database honeyword: " + hashedHoneyword);
                if (hashedHoneyword.equals(enteredHashedHoneyword)) {
                    // Honeyword detected
                    LOGGER.warning("Honeyword detected for user ID: " + userId);
                    String email = getEmailByUserId(connection, userId);
                    if (email != null) {
                        String token = generateToken();
                        triggerHoneywordDetection(userId, email, token);
                    } else {
                        LOGGER.warning("Email not found for user ID: " + userId);
                    }
                    return true;
                }
            }

            return false; // No honeyword detected
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking honeywords: " + e.getMessage(), e);
            return false;
        }
    }

    public static Set<String> getHoneywordsByUserId(Connection connection, String userId) throws SQLException {
        Set<String> honeywords = new HashSet<>();
        String query = "SELECT honeyword FROM Honeywords WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                honeywords.add(rs.getString("honeyword"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving honeywords: " + e.getMessage(), e);
        }
        return honeywords;
    }

    private static void triggerHoneywordDetection(String userId, String email, String token) {
        LOGGER.warning("Honeyword detected for user ID: " + userId);
        sendResetEmail(email, token);
    }

    private static void sendResetEmail(String email, String token) {
        final String username = "sejal.sharma@mmit.edu.in"; // Your Gmail address
        final String password = "Sejal@2002"; // Your Gmail password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Gmail SMTP server
        props.put("mail.smtp.port", "587"); // Gmail SMTP port

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            LOGGER.info("Preparing to send email to: " + email);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Security Alert: Possible Honeyword Detected");
            message.setText("Dear User,\n\n"
                    + "A possible honeyword attempt was detected on your account. "
                    + "For your security, please reset your password immediately by clicking the link below:\n\n"
                    + "http://localhost:8082/NEW/ResetPasswordServlet?token=" + token + "\n\n"
                    + "This link will expire in one hour.\n\n"
                    + "Regards,\nYour App");

            Transport.send(message);
            LOGGER.info("Reset link email sent to: " + email);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending reset link email: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private static String getEmailByUserId(Connection connection, String userId) {
        String email = null;
        String query = "SELECT email FROM Users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching email for user ID: " + userId + ", Error: " + e.getMessage(), e);
        }
        return email;
    }
}
