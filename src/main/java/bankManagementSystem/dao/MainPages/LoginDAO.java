package main.java.bankManagementSystem.dao.MainPages;

import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class LoginDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public LoginDAO() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Unable to find config.properties");
            } else {
                props.load(input);
                URL = props.getProperty("db.url");
                USER = props.getProperty("db.username");
                PASSWORD = props.getProperty("db.password");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    public boolean isCredentialsValid(String mail, String password, String role) {
        // Determine the correct table and column names based on role
        String tableName;
        String mailColumn;
        String passwordColumn;

        switch (role.toLowerCase()) {
            case "customer":
                tableName = "customer";
                mailColumn = "customer_mail";
                passwordColumn = "customer_password";
                break;
            case "staff":
                tableName = "STAFF";
                mailColumn = "STAFF_MAIL";
                passwordColumn = "STAFF_PASSWORD";
                break;
            case "admin":
                tableName = "admin";
                mailColumn = "admin_mail";
                passwordColumn = "admin_password";
                break;
            default:
                return false;
        }

        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", passwordColumn, tableName, mailColumn);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mail);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString(passwordColumn);
                    if (tableName.equals("admin")) {
                        return password.equals(storedHash);
                    }
                    return BCrypt.checkpw(password, storedHash);
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to validate credentials: " + e.getMessage(), e);
        }
    }

}
