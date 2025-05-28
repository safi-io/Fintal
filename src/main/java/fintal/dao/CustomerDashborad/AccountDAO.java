package main.java.fintal.dao.CustomerDashborad;

import main.java.fintal.model.CustomerAccountBranchModel;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class AccountDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public AccountDAO() {
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

    public CustomerAccountBranchModel getDashboardData(String accountNumber) {

        String query = "SELECT A.ACCOUNT_NUMBER, A.ACCOUNT_TYPE, A.ACCOUNT_CURRENT_BALANCE, A.ACCOUNT_OPENING_DATE, A.ACCOUNT_LAST_LOGIN, B.BRANCH_NAME, C.CUSTOMER_NAME " + "FROM ACCOUNT AS A " + "INNER JOIN BRANCH AS B ON B.BRANCH_ID = A.ACCOUNT_BRANCH_ID " + "INNER JOIN CUSTOMER AS C ON C.CUSTOMER_CNIC = A.ACCOUNT_CUSTOMER_CNIC " + "WHERE A.ACCOUNT_IS_OPENED AND A.ACCOUNT_NUMBER = ?;";


        CustomerAccountBranchModel requiredDashboardData = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate lastLoginDate = null;
                Date lastLoginSqlDate = rs.getDate("account_last_login");
                if (lastLoginSqlDate != null) {
                    lastLoginDate = lastLoginSqlDate.toLocalDate();
                }

                requiredDashboardData = new CustomerAccountBranchModel(
                        rs.getString("account_number"),
                        rs.getString("account_type"),
                        rs.getBigDecimal("account_current_balance"),
                        rs.getDate("account_opening_date").toLocalDate(),
                        lastLoginDate,
                        rs.getString("branch_name"),
                        rs.getString("customer_name")
                );

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Dashboard Data: " + e.getMessage());
        }

        return requiredDashboardData;
    }

    public String getCustomerNameUsingAccountNumber(String accountNumber) {
        String name = null;
        String sql = """
                    SELECT c.customer_name as name
                    FROM customer c
                    JOIN account a ON c.customer_cnic = a.account_customer_cnic
                    WHERE a.account_number = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Optionally log or rethrow
        }

        return name;
    }

    public boolean addStripeAmount(String accountNumber) {
        String query = """
                UPDATE account 
                SET account_current_balance = account_current_balance + 10000 
                WHERE account_number = ?
                """;

        int rowsAffected = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            rowsAffected = stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();  // Optionally log or rethrow
        }

        return rowsAffected > 0;
    }


}
