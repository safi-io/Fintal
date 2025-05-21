package main.java.bankManagementSystem.dao.BeneficiaryDashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class DashboardDAO {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public DashboardDAO() {
        String url = "", user = "", pass = "";
        try {
            Path path = Paths.get("src/main/resources/config.properties");
            Properties props = new Properties();
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                props.load(reader);
            }
            url = props.getProperty("db.url");
            user = props.getProperty("db.username");
            pass = props.getProperty("db.password");

        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
        this.URL = url;
        this.USER = user;
        this.PASSWORD = pass;
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
