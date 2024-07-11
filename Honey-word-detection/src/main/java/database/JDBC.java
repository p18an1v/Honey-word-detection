package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletResponse;

public class JDBC {

    private static final Logger LOGGER = Logger.getLogger(JDBC.class.getName());
    private static Connection con;

    public static void connectDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "root");
            LOGGER.info("Connected to Database...");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
            throw new SQLException("Database connection error: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            connectDatabase();
        }
        return con;
    }

    public static void registerUser(String fullname, String username, String email, String password, String phone, String securityQuestion, String securityAnswer, HttpServletResponse response) {
        String updateString = "INSERT INTO Honeyword_user_data (username, fullname, email, password, phone, securityQuestion, securityAnswer) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateString)) {

            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, phone);
            stmt.setString(6, securityQuestion);
            stmt.setString(7, securityAnswer);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("User registered successfully");
                response.sendRedirect("registration-success.html");
            } else {
                LOGGER.warning("User registration failed: No rows affected");
                response.sendRedirect("registration-failure.html");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error registering user: " + e.getMessage(), e);
            try {
                response.sendRedirect("db-error.html");
            } catch (IOException ioException) {
                LOGGER.log(Level.SEVERE, "IO Error redirecting to DB error page: " + ioException.getMessage(), ioException);
                // Handle the IOException here (e.g., log the error, provide a fallback response)
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Error redirecting after registration: " + e.getMessage(), e);
            // Handle the IOException here (e.g., log the error, provide a fallback response)
        }
    }

    public static String getUserIdByEmail(Connection connection, String email) {
        String userId = null;
        String query = "SELECT id FROM Honeyword_user_data WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getString("id");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user ID: " + e.getMessage(), e);
            System.out.println("Error retrieving user ID: " + e.getMessage());
        }

        return userId;
    }


    public static String getUserIdByToken(Connection connection, String token) throws SQLException {
        String query = "SELECT user_id FROM password_reset_tokens WHERE token = ? AND expiration > NOW()";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("user_id");
            }
        }
        return null;
    }

    public static void updatePassword(Connection connection, String userId, String hashedPassword) throws SQLException {
        String query = "UPDATE Honeyword_user_data SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }
}
