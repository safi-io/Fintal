package main.java.bankManagementSystem.dao.CustomerDashborad;

import main.java.bankManagementSystem.model.BillModel;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

public class BillDAO {

    private String URL;
    private String USER;
    private String PASSWORD;

    public BillDAO() {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BillModel getBill(String beneficiaryAccountNumber, String referenceNumber) {
        BillModel bill = null;
        String query = """
                SELECT * FROM BILLS
                WHERE BENEFICIARY_ACCOUNT_NUMBER = ? AND REFERENCE_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, beneficiaryAccountNumber);
            stmt.setString(2, referenceNumber);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bill = new BillModel(rs.getString("reference_number"), rs.getString("consumer_name"), rs.getBigDecimal("amount"), rs.getBoolean("is_paid"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bill;
    }

    public boolean payBill(String customerAccount, String beneficiaryAccount, String billReferenceNumber, BigDecimal billAmount) throws Exception {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);  // Start transaction

            // 1. Debit customer
            try (PreparedStatement debitStmt = conn.prepareStatement(
                    "UPDATE ACCOUNT SET ACCOUNT_CURRENT_BALANCE = ACCOUNT_CURRENT_BALANCE - ? " +
                            "WHERE ACCOUNT_NUMBER = ? AND ACCOUNT_CURRENT_BALANCE >= ?")) {
                debitStmt.setBigDecimal(1, billAmount);
                debitStmt.setString(2, customerAccount);
                debitStmt.setBigDecimal(3, billAmount);

                if (debitStmt.executeUpdate() != 1) {
                    throw new SQLException("Insufficient balance or customer account not found.");
                }
            }

            // 2. Credit beneficiary
            try (PreparedStatement creditStmt = conn.prepareStatement(
                    "UPDATE ACCOUNT SET ACCOUNT_CURRENT_BALANCE = ACCOUNT_CURRENT_BALANCE + ? " +
                            "WHERE ACCOUNT_NUMBER = ?")) {
                creditStmt.setBigDecimal(1, billAmount);
                creditStmt.setString(2, beneficiaryAccount);

                if (creditStmt.executeUpdate() != 1) {
                    throw new SQLException("Beneficiary account not found.");
                }
            }

            // 3. Insert DEBIT transaction
            try (PreparedStatement debitTxnStmt = conn.prepareStatement(
                    "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, TRANSACTION_AMOUNT, " +
                            "TRANSACTION_DESCRIPTION, COUNTERPARTY_ACCOUNT) VALUES (?, 'DEBIT', ?, ?, ?)")) {
                debitTxnStmt.setString(1, customerAccount);
                debitTxnStmt.setBigDecimal(2, billAmount);
                debitTxnStmt.setString(3, "Bill Payment - Ref " + billReferenceNumber);
                debitTxnStmt.setString(4, beneficiaryAccount);
                debitTxnStmt.executeUpdate();
            }

            // 4. Insert CREDIT transaction
            try (PreparedStatement creditTxnStmt = conn.prepareStatement(
                    "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, TRANSACTION_AMOUNT, " +
                            "TRANSACTION_DESCRIPTION, COUNTERPARTY_ACCOUNT) VALUES (?, 'CREDIT', ?, ?, ?)")) {
                creditTxnStmt.setString(1, beneficiaryAccount);
                creditTxnStmt.setBigDecimal(2, billAmount);
                creditTxnStmt.setString(3, "Bill Payment Received - Ref " + billReferenceNumber);
                creditTxnStmt.setString(4, customerAccount);
                creditTxnStmt.executeUpdate();
            }

            // 5. Mark bill as paid
            try (PreparedStatement updateBillStmt = conn.prepareStatement(
                    "UPDATE BILLS SET IS_PAID = true WHERE BENEFICIARY_ACCOUNT_NUMBER = ? AND REFERENCE_NUMBER = ?")) {
                updateBillStmt.setString(1, beneficiaryAccount);
                updateBillStmt.setString(2, billReferenceNumber);

                if (updateBillStmt.executeUpdate() != 1) {
                    throw new SQLException("Bill not found or already marked as paid.");
                }
            }

            // All good — commit
            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

}
