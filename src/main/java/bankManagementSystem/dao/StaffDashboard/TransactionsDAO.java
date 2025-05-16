package main.java.bankManagementSystem.dao.StaffDashboard;

import main.java.bankManagementSystem.model.CustomerAccountBranchModel;
import main.java.bankManagementSystem.model.TransactionCustomerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;

public class TransactionsDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public TransactionsDAO() {
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

    public List<TransactionCustomerModel> getAllTransactionsByAccountNumber(String accountNumber) {
        System.out.println(accountNumber);
        String query = "SELECT\n" + "  T.TRANSACTION_TYPE,\n" + "  T.TRANSACTION_AMOUNT,\n" + "  T.TRANSACTION_DESCRIPTION,\n" + "  CP.ACCOUNT_NUMBER AS counterparty_account,\n" + "  CCP.CUSTOMER_NAME AS COUNTER_PARTY_NAME,\n" + "  T.TIMESTAMP\n" + "  \n" + "FROM TRANSACTIONS AS T\n" + "INNER JOIN ACCOUNT AS MP ON T.ACCOUNT_NUMBER = MP.ACCOUNT_NUMBER\n" + "INNER JOIN ACCOUNT AS CP ON T.COUNTERPARTY_ACCOUNT = CP.ACCOUNT_NUMBER\n" + "\n" + "JOIN CUSTOMER AS CMP ON MP.ACCOUNT_CUSTOMER_CNIC = CMP.CUSTOMER_CNIC\n" + "JOIN CUSTOMER AS CCP ON CP.ACCOUNT_CUSTOMER_CNIC = CCP.CUSTOMER_CNIC\n" + "WHERE T.ACCOUNT_NUMBER = ?;";

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
