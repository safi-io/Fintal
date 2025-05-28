package main.java.bankManagementSystem.dao.BeneficiaryDashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class DashboardDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public DashboardDAO() {
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

    public BigDecimal getUnPaidAmount(String beneficiaryAccountNumber) {
        BigDecimal count = BigDecimal.ZERO;
        String sql = """
                SELECT SUM(AMOUNT) AS amt
                FROM BILLS
                WHERE IS_PAID = false AND BENEFICIARY_ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, beneficiaryAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getBigDecimal("amt") != null) {
                    count = rs.getBigDecimal("amt");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unpaid amount: " + e.getMessage());
        }

        return count;
    }

    public BigDecimal getPaidAmount(String beneficiaryAccountNumber) {
        BigDecimal count = BigDecimal.ZERO;
        String sql = """
                SELECT SUM(AMOUNT) AS amt
                FROM BILLS
                WHERE IS_PAID = true AND BENEFICIARY_ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, beneficiaryAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getBigDecimal("amt") != null) {
                    count = rs.getBigDecimal("amt");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching paid amount: " + e.getMessage());
        }

        return count;
    }

    public int getUnPaidCount(String beneficiaryAccountNumber) {
        int count = 0;
        String sql = """
                SELECT COUNT(*) AS total
                FROM BILLS
                WHERE IS_PAID = false AND BENEFICIARY_ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, beneficiaryAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unpaid count: " + e.getMessage());
        }

        return count;
    }

    public int getPaidCount(String beneficiaryAccountNumber) {
        int count = 0;
        String sql = """
                SELECT COUNT(*) AS total
                FROM BILLS
                WHERE IS_PAID = true AND BENEFICIARY_ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, beneficiaryAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching paid count: " + e.getMessage());
        }

        return count;
    }

    public LocalDate getLastLogin(String beneficiaryAccountNumber) {
        LocalDate date = null;
        String sql = """
                SELECT ACCOUNT_LAST_LOGIN AS lastLogin
                FROM ACCOUNT
                WHERE ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, beneficiaryAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date d = rs.getDate("lastLogin");
                    if (d != null) {
                        date = d.toLocalDate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching last login date: " + e.getMessage());
        }

        return date;
    }

    public String getBeneficiaryName(String beneficiaryAccountNumber) {
        String name = null;
        String sql = """
                SELECT CUSTOMER_NAME AS name
                FROM ACCOUNT
                JOIN CUSTOMER ON ACCOUNT_CUSTOMER_CNIC = CUSTOMER_CNIC
                WHERE ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, beneficiaryAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching beneficiary name: " + e.getMessage());
        }

        return name;
    }
}
