package main.java.bankManagementSystem.dao.CustomerDashborad;

import main.java.bankManagementSystem.model.TransactionCustomerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TransactionsDAO {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public TransactionsDAO() {
        String url = null, user = null, password = null;
        try {
            Path path = Paths.get("src/main/resources/config.properties");
            Properties props = new Properties();
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                props.load(reader);
                url = props.getProperty("db.url");
                user = props.getProperty("db.username");
                password = props.getProperty("db.password");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
        this.URL = url;
        this.USER = user;
        this.PASSWORD = password;
    }

    public Long transferMoney(String senderAcc, String receiverAcc, BigDecimal amount, String description) throws Exception {

        Connection conn = null;
        Long debitTxnId = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);                        // ➊ start TX

            /* 1 ─ Debit sender */
            try (PreparedStatement debitStmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_CURRENT_BALANCE = ACCOUNT_CURRENT_BALANCE - ? " + "WHERE ACCOUNT_NUMBER = ? AND ACCOUNT_CURRENT_BALANCE >= ?")) {

                debitStmt.setBigDecimal(1, amount);
                debitStmt.setString(2, senderAcc);
                debitStmt.setBigDecimal(3, amount);

                if (debitStmt.executeUpdate() != 1) throw new SQLException("Insufficient balance in Sender Account!");
            }

            /* 2 ─ Credit receiver */
            try (PreparedStatement creditStmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_CURRENT_BALANCE = ACCOUNT_CURRENT_BALANCE + ? " + "WHERE ACCOUNT_NUMBER = ?")) {

                creditStmt.setBigDecimal(1, amount);
                creditStmt.setString(2, receiverAcc);

                if (creditStmt.executeUpdate() != 1) throw new SQLException("Receiver account not found!");
            }

            /* 3 ─ Insert DEBIT row & grab generated key */
            String debitSQL = "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, TRANSACTION_AMOUNT, " + "TRANSACTION_DESCRIPTION, COUNTERPARTY_ACCOUNT) VALUES (?, 'DEBIT', ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(debitSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, senderAcc);
                ps.setBigDecimal(2, amount);
                ps.setString(3, description);
                ps.setString(4, receiverAcc);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) debitTxnId = rs.getLong(1);
                }
            }

            /* 4 ─ Insert CREDIT row */
            String creditSQL = "INSERT INTO TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, TRANSACTION_AMOUNT, " + "TRANSACTION_DESCRIPTION, COUNTERPARTY_ACCOUNT) VALUES (?, 'CREDIT', ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(creditSQL)) {
                ps.setString(1, receiverAcc);
                ps.setBigDecimal(2, amount);
                ps.setString(3, description);
                ps.setString(4, senderAcc);
                ps.executeUpdate();
            }

            conn.commit();                                    // ➋ success
            System.out.println("Transfer successful.");
            return debitTxnId;                                // ➌ return proof ID

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
                System.err.println("Transaction rolled back: " + e.getMessage());
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

    public List<TransactionCustomerModel> getAllTransactionsByAccountNumber(String accountNumber) {
        System.out.println(accountNumber);
        String query = "SELECT\n" + "  T.TRANSACTION_TYPE,\n" + "  T.TRANSACTION_AMOUNT,\n" + "  T.TRANSACTION_DESCRIPTION,\n" + "  CP.ACCOUNT_NUMBER AS counterparty_account,\n" + "  CCP.CUSTOMER_NAME AS COUNTER_PARTY_NAME,\n" + "  T.TIMESTAMP\n" + "  \n" + "FROM TRANSACTIONS AS T\n" + "INNER JOIN ACCOUNT AS MP ON T.ACCOUNT_NUMBER = MP.ACCOUNT_NUMBER\n" + "INNER JOIN ACCOUNT AS CP ON T.COUNTERPARTY_ACCOUNT = CP.ACCOUNT_NUMBER\n" + "\n" + "JOIN CUSTOMER AS CMP ON MP.ACCOUNT_CUSTOMER_CNIC = CMP.CUSTOMER_CNIC\n" + "JOIN CUSTOMER AS CCP ON CP.ACCOUNT_CUSTOMER_CNIC = CCP.CUSTOMER_CNIC\n" + "WHERE T.ACCOUNT_NUMBER = ? ORDER BY TIMESTAMP DESC;";

        List<TransactionCustomerModel> transactionCustomerModelList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                TransactionCustomerModel transaction = new TransactionCustomerModel(rs.getString("transaction_type"), rs.getBigDecimal("transaction_amount"), rs.getString("transaction_description"), rs.getString("counterparty_account"), rs.getString("counter_party_name"), rs.getTimestamp("timestamp"));

                transactionCustomerModelList.add(transaction);
            }
            System.out.println(transactionCustomerModelList);
            return transactionCustomerModelList;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Transactions: " + e.getMessage());
        }

    }

}
