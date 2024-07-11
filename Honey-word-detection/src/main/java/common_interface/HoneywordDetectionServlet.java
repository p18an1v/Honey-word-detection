package common_interface;
import org.mindrot.jbcrypt.BCrypt;
import database.JDBC;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

@WebServlet("/HoneywordDetectionServlet")
public class HoneywordDetectionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(HoneywordDetectionServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String enteredPassword = request.getParameter("enteredPassword");

        try (Connection connection = JDBC.getConnection()) {
            String userId = getUserIdByUsername(connection, username);

            if (userId != null) {
                Set<String> honeywords = Honeychecker.getHoneywordsByUserId(connection, userId);
                boolean honeywordDetected = false;

                for (String hashedHoneyword : honeywords) {
                    if (BCrypt.checkpw(enteredPassword, hashedHoneyword)) {
                        honeywordDetected = true;
                        break;
                    }
                }

                if (honeywordDetected) {
                    String email = getEmailByUserId(connection, userId);
                    if (email != null) {
                        LOGGER.info("Honeyword detected. Sending reset link email to: " + email);
                        String token = generateToken();
                        storeToken(connection, userId, token);
                        sendResetEmail(email, token);
                    } else {
                        LOGGER.warning("Email for user ID " + userId + " not found.");
                    }
                    response.sendRedirect("honeyword-detected.html");
                } else {
                    response.sendRedirect("login-success.html");
                }
            } else {
                response.sendRedirect("login.html");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            response.sendRedirect("db-error.html");
        }
    }

    private String getUserIdByUsername(Connection connection, String username) throws SQLException {
        String query = "SELECT id FROM Honeyword_user_data WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("id") : null;
        }
    }

    private String getEmailByUserId(Connection connection, String userId) throws SQLException {
        String query = "SELECT email FROM Honeyword_user_data WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("email") : null;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void storeToken(Connection connection, String userId, String token) throws SQLException {
        String query = "INSERT INTO password_reset_tokens (user_id, token, expiration) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 1 HOUR))";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, token);
            stmt.executeUpdate();
        }
    }

    private void sendResetEmail(String email, String token) {
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
        }
    }
}
