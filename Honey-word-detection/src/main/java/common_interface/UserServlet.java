package common_interface;

import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.JDBC;
import java.util.logging.Level;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Connection connection = JDBC.getConnection();

            if ("Login".equals(action)) {
                handleLogin(request, response, connection);
            } else if ("Register".equals(action)) {
                handleRegistration(request, response, connection);
            } else {
                response.sendRedirect("index.html");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            response.sendRedirect("db-error.html");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, Connection connection)
            throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        LOGGER.info("Starting user authentication for username: " + username);

        boolean isAuthenticated = authenticateUser(connection, username, enteredPassword);

        if (isAuthenticated) {
            LOGGER.info("User authenticated successfully for username: " + username);
            response.sendRedirect("login-success.html");
        } else {
            String userId = getUserIdByUsername(connection, username);
            if (userId != null) {
                // Check against honeywords
                Set<String> honeywords = Honeychecker.getHoneywordsByUserId(connection, userId);
                boolean honeywordDetected = false;

                for (String hashedHoneyword : honeywords) {
                    if (BCrypt.checkpw(enteredPassword, hashedHoneyword)) {
                        honeywordDetected = true;
                        break;
                    }
                }

                if (honeywordDetected) {
                    LOGGER.warning("Honeyword detected for user: " + username);
                    response.sendRedirect("honeyword-detected.html");
                } else {
                    LOGGER.warning("Authentication failed for username: " + username);
                    response.sendRedirect("index.html?error=wrong_password");
                }
            } else {
                LOGGER.warning("Authentication failed for username: " + username);
                response.sendRedirect("index.html?error=wrong_password");
            }
        }
    }

    private boolean authenticateUser(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT password FROM Honeyword_user_data WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return verifyPassword(password, hashedPassword);
            }
        }
        return false;
    }

    private String getUserIdByUsername(Connection connection, String username) throws SQLException {
        String query = "SELECT id FROM Honeyword_user_data WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("id") : null;
        }
    }

    private boolean verifyPassword(String enteredPassword, String hashedPassword) {
        return BCrypt.checkpw(enteredPassword, hashedPassword);
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response, Connection connection)
            throws SQLException, IOException, ServletException {
        String fullname = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String securityQuestion = request.getParameter("securityQuestion");
        String securityAnswer = request.getParameter("securityAnswer");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        boolean registrationSuccess = registerUser(fullname, username, email, hashedPassword, phone, securityQuestion, securityAnswer, connection, response);

        if (registrationSuccess) {
            String userId = getUserIdByUsername(connection, username);
            Honeyword.generateHoneywords(password, userId, connection);

            response.sendRedirect("registration-success.html");
        } else {
            response.sendRedirect("registration-failure.html");
        }
    }

    private boolean registerUser(String fullname, String username, String email, String password, String phone, String securityQuestion, String securityAnswer, Connection connection, HttpServletResponse response)
            throws SQLException {
        String insertQuery = "INSERT INTO Honeyword_user_data (fullname, username, email, password, phone, securityQuestion, securityAnswer) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, fullname);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, phone);
            stmt.setString(6, securityQuestion);
            stmt.setString(7, securityAnswer);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        }
    }
}
