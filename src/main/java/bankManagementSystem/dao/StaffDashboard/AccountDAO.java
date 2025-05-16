package main.java.bankManagementSystem.dao.StaffDashboard;

import main.java.bankManagementSystem.model.AccountModel;
import main.java.bankManagementSystem.model.CustomerAccountBranchModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public AccountDAO() {
        try {
            Path path = Paths.get("src/main/resources/config.properties");
            Properties props;
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                props = new Properties();
                props.load(reader);
            }
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");

        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    public List<CustomerAccountBranchModel> getAccountApplications() {
        String query = "SELECT C.CUSTOMER_CNIC, C.CUSTOMER_NAME, C.CUSTOMER_MAIL, C.CUSTOMER_PHONE, " + "A.ACCOUNT_NUMBER, A.ACCOUNT_TYPE, A.ACCOUNT_OPENING_DATE, A.ACCOUNT_BRANCH_ID, B.BRANCH_NAME " + "FROM ACCOUNT AS A " + "INNER JOIN CUSTOMER AS C ON A.ACCOUNT_CUSTOMER_CNIC = C.CUSTOMER_CNIC " + "INNER JOIN BRANCH AS B ON B.BRANCH_ID = A.ACCOUNT_BRANCH_ID WHERE !A.ACCOUNT_IS_OPENED;";


        List<CustomerAccountBranchModel> accountsApplicationsList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                CustomerAccountBranchModel accountApplication = new CustomerAccountBranchModel(rs.getString("CUSTOMER_CNIC"), rs.getString("CUSTOMER_NAME"), rs.getString("CUSTOMER_MAIL"), rs.getString("CUSTOMER_PHONE"), rs.getString("ACCOUNT_NUMBER"), rs.getString("ACCOUNT_TYPE"), rs.getDate("ACCOUNT_OPENING_DATE").toLocalDate(), rs.getString("ACCOUNT_BRANCH_ID"), rs.getString("BRANCH_NAME"));

                accountsApplicationsList.add(accountApplication);
            }

            return accountsApplicationsList;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Account Opening Application: " + e.getMessage());
        }


    }

    public boolean approveAccount(String accountNumber) {
        String query = "UPDATE ACCOUNT SET ACCOUNT_IS_OPENED = 1 WHERE ACCOUNT_NUMBER = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AccountModel getAccountByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM ACCOUNT WHERE ACCOUNT_NUMBER = ?";

        AccountModel requiredAccount = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                requiredAccount = new AccountModel(rs.getString("account_number"), rs.getString("account_type"), rs.getBigDecimal("account_current_balance"), rs.getBoolean("account_is_opened"), rs.getBoolean("account_is_deleted"), rs.getDate("account_opening_date").toLocalDate(), rs.getString("account_Customer_CNIC"), rs.getString("account_Branch_ID"));
            }
            return requiredAccount;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Account: " + e.getMessage());
        }
    }

    public List<CustomerAccountBranchModel> getAllAccounts() {
        String query = "SELECT C.CUSTOMER_CNIC, C.CUSTOMER_NAME, C.CUSTOMER_MAIL, C.CUSTOMER_PHONE, " + "A.ACCOUNT_NUMBER,A.ACCOUNT_CURRENT_BALANCE, A.ACCOUNT_TYPE, A.ACCOUNT_OPENING_DATE, A.ACCOUNT_BRANCH_ID, B.BRANCH_NAME " + "FROM ACCOUNT AS A " + "INNER JOIN CUSTOMER AS C ON A.ACCOUNT_CUSTOMER_CNIC = C.CUSTOMER_CNIC " + "INNER JOIN BRANCH AS B ON B.BRANCH_ID = A.ACCOUNT_BRANCH_ID WHERE A.ACCOUNT_IS_OPENED AND !A.ACCOUNT_IS_DELETED";

        List<CustomerAccountBranchModel> accountsList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                CustomerAccountBranchModel accountApplication = new CustomerAccountBranchModel(rs.getString("CUSTOMER_CNIC"), rs.getString("CUSTOMER_NAME"), rs.getString("CUSTOMER_MAIL"), rs.getString("CUSTOMER_PHONE"), rs.getString("ACCOUNT_NUMBER"), rs.getBigDecimal("ACCOUNT_CURRENT_BALANCE"), rs.getString("ACCOUNT_TYPE"), rs.getDate("ACCOUNT_OPENING_DATE").toLocalDate(), rs.getString("ACCOUNT_BRANCH_ID"), rs.getString("BRANCH_NAME"));

                accountsList.add(accountApplication);
            }

            return accountsList;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Account Opening Application: " + e.getMessage());
        }
    }

    public boolean accountDelete(String accountNumber) {
        String query = "UPDATE ACCOUNT SET ACCOUNT_IS_DELETED = 1 WHERE ACCOUNT_NUMBER = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
