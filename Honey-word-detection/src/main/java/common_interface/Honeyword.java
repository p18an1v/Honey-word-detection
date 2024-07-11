package common_interface;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import database.JDBC;

public class Honeyword {

    private static final Logger LOGGER = Logger.getLogger(Honeyword.class.getName());

    private static final char[] characters = {'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
    private static final char[] specialCharacters = {'!', '@', '#', '$', '%', '&'};

    public static void generateHoneywords(String originalPassword, String userId, Connection connection) {
        Set<String> honeyWordSet = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO Honeywords (user_id, honeyword) VALUES (?, ?)")) {
            Random random = new Random();

            while (honeyWordSet.size() < 10) {
                StringBuilder honeyword = new StringBuilder();

                for (int i = 0; i < originalPassword.length(); i++) {
                    char currentChar = originalPassword.charAt(i);
                    char replacementChar;

                    if (Character.isLetter(currentChar)) {
                        replacementChar = characters[random.nextInt(characters.length)];
                    } else if (Character.isDigit(currentChar)) {
                        replacementChar = (char) ('0' + random.nextInt(10));
                    } else {
                        replacementChar = specialCharacters[random.nextInt(specialCharacters.length)];
                    }

                    honeyword.append(replacementChar);
                }

                String honeywordString = honeyword.toString();

                // Hash the honeyword using BCrypt before storing
                String hashedHoneyword = BCrypt.hashpw(honeywordString, BCrypt.gensalt());

                // Check if the honeyword is unique before adding to the set and database
                if (!honeyWordSet.contains(hashedHoneyword)) {
                    honeyWordSet.add(hashedHoneyword);
                    stmt.setString(1, userId);
                    stmt.setString(2, hashedHoneyword);
                    stmt.executeUpdate();
                }
            }

            LOGGER.info("Generated and stored honeywords successfully.");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error generating and storing honeywords: " + e.getMessage(), e);
        }
    }

    public static boolean checkHoneywords(String userId, String enteredPassword) {
        try (Connection connection = JDBC.getConnection()) {
            Set<String> honeywords = getHoneywordsByUserId(connection, userId);

            // Check if the entered password matches any of the honeywords
            return honeywords.contains(enteredPassword);
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
            // Handle or log the SQL exception appropriately
            throw e; // Rethrow the exception to handle it in the caller
        }
        return honeywords;
    }
}
