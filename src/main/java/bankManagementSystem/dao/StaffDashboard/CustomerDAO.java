package main.java.bankManagementSystem.dao.StaffDashboard;

import main.java.bankManagementSystem.model.CustomerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public CustomerDAO() {
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

    public List<CustomerModel> getAllCustomers() {
        List<CustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM CUSTOMER";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                CustomerModel customer = new CustomerModel(rs.getString("customer_CNIC"), rs.getString("customer_Name"), rs.getString("customer_Mail"), rs.getString("customer_Phone"), rs.getDate("customer_DOB").toLocalDate());


                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch customers: " + e.getMessage());
        }

        return customers;
    }

    public CustomerModel handleGetCustomerByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM customer c " + "JOIN account a ON c.customer_cnic = a.account_customer_cnic " + "WHERE a.account_number = ?";
        CustomerModel requiredCustomer = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                requiredCustomer = new CustomerModel(rs.getString("customer_CNIC"), rs.getString("customer_Name"), rs.getString("customer_Mail"), rs.getString("customer_Phone"), rs.getDate("customer_DOB").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch customer: " + e.getMessage());
        }

        return requiredCustomer;
    }

    public CustomerModel getCustomerByCNIC(String CNIC) {
        String query = "SELECT * FROM CUSTOMER WHERE CUSTOMER_CNIC = ?";
        CustomerModel requiredCustomer = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, CNIC);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                requiredCustomer = new CustomerModel(rs.getString("customer_CNIC"), rs.getString("customer_Name"), rs.getString("customer_Mail"), rs.getString("customer_Phone"), rs.getDate("customer_DOB").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch customer: " + e.getMessage());
        }

        return requiredCustomer;
    }

    public boolean updateCustomerByAccountNumber(String accountNumber, String name, String email, String phone, String password) {
        StringBuilder query = new StringBuilder("UPDATE customer SET ");
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        // Conditionally add fields
        if (name != null && !name.isEmpty()) {
            fields.add("customer_Name = ?");
            values.add(name);
        }
        if (email != null && !email.isEmpty()) {
            fields.add("customer_Mail = ?");
            values.add(email);
        }
        if (phone != null && !phone.isEmpty()) {
            fields.add("customer_Phone = ?");
            values.add(phone);
        }
        if (password != null && !password.isEmpty()) {
            fields.add("customer_Password = ?");
            values.add(password);
        }

        if (fields.isEmpty()) return false; // Nothing to update

        query.append(String.join(", ", fields));
        query.append(" WHERE customer_CNIC = (SELECT account_customer_cnic FROM account WHERE account_number = ?)");

        values.add(accountNumber); // add at the end

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAccountNumberByMail(String customerMail) {
        final String sql = "SELECT a.ACCOUNT_NUMBER " + "FROM CUSTOMER  c " + "JOIN ACCOUNT   a ON c.CUSTOMER_CNIC = a.ACCOUNT_CUSTOMER_CNIC " + "WHERE c.CUSTOMER_MAIL = ? ";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerMail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ACCOUNT_NUMBER");
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching account number", e);
        }
    }

    public String getAccountTypeByAccountNumber(String accountNumber) {

        String sql = """
                                SELECT ACCOUNT_TYPE FROM ACCOUNT WHERE ACCOUNT_NUMBER = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ACCOUNT_TYPE");
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching account type", e);
        }
    }

    public void updateLastLogin(String accountNumber) {
        String query = """
                    UPDATE ACCOUNT SET ACCOUNT_LAST_LOGIN = ? WHERE account_number = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            stmt.setString(2, accountNumber);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update last login date", e);
        }


    }

    public boolean getIsAccountOpened(String accountNumber) {
        String query = "SELECT ACCOUNT_IS_OPENED FROM ACCOUNT WHERE ACCOUNT_NUMBER = ?;";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("ACCOUNT_IS_OPENED");
            } else {
                // Account not found, return false or throw exception based on your logic
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get account status", e);
        }
    }

    public boolean getIsAccountDeleted(String accountNumber) {
        String query = "SELECT ACCOUNT_IS_DELETED FROM ACCOUNT WHERE ACCOUNT_NUMBER = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("ACCOUNT_IS_DELETED");
            } else {
                // Account not found, return false or throw exception based on your logic
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get account status", e);
        }
    }

}
