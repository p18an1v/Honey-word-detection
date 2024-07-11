package common_interface;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import database.JDBC;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        try (Connection connection = JDBC.getConnection()) {
            String userId = JDBC.getUserIdByToken(connection, token);

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            if (userId != null) {
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                JDBC.updatePassword(connection, userId, hashedPassword);

                out.println("<html><body>");
                out.println("<script>alert('Password reset successful.'); window.location.href = 'index.html';</script>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<script>alert('Invalid or expired token.'); window.location.href = 'index.html';</script>");
                out.println("</body></html>");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            response.sendRedirect("db-error.html");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        // Render a form for the user to input their new password.
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
        	    "<html>" +
        	        "<head>" +
        	            "<title>Reset Password</title>" +
        	            "<style>" +
        	                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #71b7e6, #9b59b6); margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; }" +
        	                "h1 { color: #fff; text-align: center; margin-bottom: 20px; }" +
        	                "form { background: #fff; padding: 40px 30px; border-radius: 15px; box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2); max-width: 400px; width: 100%; }" +
        	                "label { display: block; margin-bottom: 10px; color: #333; font-weight: bold; }" +
        	                "input[type='password'] { width: 100%; padding: 15px; margin-bottom: 20px; border: 1px solid #ddd; border-radius: 10px; font-size: 16px; box-sizing: border-box; transition: border-color 0.3s; }" +
        	                "input[type='password']:focus { border-color: #9b59b6; }" +
        	                "button { background: linear-gradient(135deg, #6a82fb, #fc5c7d); color: #fff; border: none; padding: 15px; border-radius: 10px; font-size: 18px; cursor: pointer; width: 100%; transition: background 0.3s; }" +
        	                "button:hover { background: linear-gradient(135deg, #fc5c7d, #6a82fb); }" +
        	                ".form-container { max-width: 500px; width: 100%; margin: auto; padding: 20px; text-align: center; }" +
        	            "</style>" +
        	        "</head>" +
        	        "<body>" +
        	            "<div class='form-container'>" +
        	                "<form method='POST' action='/NEW/ResetPasswordServlet'>" +
        	                    "<h1>Reset Password</h1>" +
        	                    "<input type='hidden' name='token' value='" + token + "' />" +
        	                    "<label for='password'>New Password:</label>" +
        	                    "<input type='password' id='password' name='password' required />" +
        	                    "<button type='submit'>Reset Password</button>" +
        	                "</form>" +
        	            "</div>" +
        	        "</body>" +
        	    "</html>"
        	);

    }
}
