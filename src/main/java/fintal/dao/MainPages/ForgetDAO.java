package main.java.fintal.dao.MainPages;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ForgetDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public ForgetDAO() {
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

    public boolean updatePassword(String role, String mail, String hashedPassword) {
        String tableName;

        // Determine the table based on role
        if ("Customer".equalsIgnoreCase(role)) {
            tableName = "customer";
        } else if ("staff".equalsIgnoreCase(role)) {
            tableName = "staff";
        } else {
            // Role not supported
            System.err.println("Unsupported role: " + role);
            return false;
        }

        // Add spaces around keywords in SQL
        String sql = "UPDATE " + tableName + " SET " + tableName + "_password = ? WHERE " + tableName + "_mail = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, mail);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String role, String email) {
        String tableName;

        // Determine the table based on role
        if ("Customer".equalsIgnoreCase(role)) {
            tableName = "customer";
        } else if ("staff".equalsIgnoreCase(role)) {
            tableName = "staff";
        } else {
            System.err.println("Unsupported role: " + role);
            return false;
        }

        String sql = "SELECT 1 FROM " + tableName + " WHERE " + tableName + "_mail = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();  // true if email found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
