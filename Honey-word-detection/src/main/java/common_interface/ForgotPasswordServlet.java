package common_interface;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import database.JDBC;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        try (Connection connection = JDBC.getConnection()) {
            String userId = JDBC.getUserIdByEmail(connection, email);

            if (userId != null) {
                String token = generateToken();
                storeToken(connection, userId, token);
                sendResetEmail(email, token); // Pass the email parameter to sendResetEmail method
                response.sendRedirect("reset-link-sent.html");
            } else {
                response.sendRedirect("email-not-found.html");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            response.sendRedirect("db-error.html");
        }
    }



    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void storeToken(Connection connection, String userId, String token) throws SQLException {
        if (userId != null) {
            String query = "INSERT INTO password_reset_tokens (user_id, token, expiration) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 1 HOUR))";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, userId);
                stmt.setString(2, token);
                stmt.executeUpdate();
            }
        } else {
            LOGGER.warning("User ID is null. Cannot store token.");
            // Handle the situation where userId is null (e.g., redirect to an error page)
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Set the recipient to the email parameter
            message.setSubject("Password Reset");
            message.setText("Dear User,\n\n"
                + "To reset your password, please click the link below:\n\n"
                + "http://localhost:8082/NEW/ResetPasswordServlet?token=" + token + "\n\n"
                + "This link will expire in one hour.\n\n"
                + "Regards,\nYour App");

            Transport.send(message);

            System.out.println("Password reset email sent to: " + email);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending password reset email: " + e.getMessage(), e);
            // Handle email sending error
        } }}
