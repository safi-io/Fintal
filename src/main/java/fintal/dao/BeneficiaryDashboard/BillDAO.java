package main.java.fintal.dao.BeneficiaryDashboard;

import main.java.fintal.model.BillModel;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BillDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public BillDAO() {
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

    public boolean createBranch(BillModel bill) {

        String query = "INSERT INTO BILLS (BENEFICIARY_ACCOUNT_NUMBER, REFERENCE_NUMBER, CONSUMER_NAME, AMOUNT) VALUES (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, bill.getBeneficiaryAccountNumber());
            stmt.setString(2, bill.getReferenceNumber());
            stmt.setString(3, bill.getConsumerName());
            stmt.setBigDecimal(4, bill.getAmount());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BillModel> getAllBills(String beneficiaryAccountNumber) {
        List<BillModel> bills = new ArrayList<>();
        String sql = """
                SELECT * FROM BILLS WHERE BENEFICIARY_ACCOUNT_NUMBER = ?;
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, beneficiaryAccountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BillModel bill = new BillModel(rs.getString("reference_number"), rs.getString("consumer_name"), rs.getBigDecimal("amount"), rs.getBoolean("is_paid"));
                bills.add(bill);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bills;
    }

}