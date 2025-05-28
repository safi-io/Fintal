package main.java.fintal.dao.CustomerDashborad;

import main.java.fintal.model.LoanApplicationModel;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoanDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public LoanDAO() {
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

    public List<LoanApplicationModel> getLoanApplicationByAccountNumber(String accountNumber) {
        String query = "SELECT LOAN_ID, LOAN_AMOUNT, LOAN_TENURE, LOAN_PURPOSE, LOAN_APPLY_DATE, LOAN_IS_APPROVED \n" + "FROM LOAN_APPLICATION WHERE LOAN_ACCOUNT_NUMBER = ? ORDER BY LOAN_APPLY_DATE";

        List<LoanApplicationModel> loanApplicationList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                LoanApplicationModel application = new LoanApplicationModel(rs.getInt("loan_id"), rs.getBigDecimal("loan_amount"), rs.getInt("loan_tenure"), rs.getString("loan_purpose"), rs.getDate("loan_apply_date").toLocalDate(), rs.getBoolean("loan_is_approved"));

                loanApplicationList.add(application);
            }
            return loanApplicationList;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Applications: " + e.getMessage());
        }

    }

    public boolean submitLoanApplication(LoanApplicationModel loanApplication) {
        String query = "INSERT INTO LOAN_APPLICATION (LOAN_AMOUNT, LOAN_ACCOUNT_NUMBER, LOAN_TENURE, LOAN_PURPOSE,LOAN_NOTE) \n" + "VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setBigDecimal(1, loanApplication.getLoanAmount());
            stmt.setString(2, loanApplication.getLoanAccountNumber());
            stmt.setInt(3, loanApplication.getLoanTenure());
            stmt.setString(4, loanApplication.getLoanPurpose());
            stmt.setString(5, loanApplication.getLoanNote());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getOutstandingLoanBalance(String accountNumber) {
        String query = "SELECT SUM(LOAN_AMOUNT) FROM LOAN_APPLICATION WHERE LOAN_ACCOUNT_NUMBER = ? AND LOAN_IS_APPROVED = TRUE";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal(1);
                    return total != null ? total : BigDecimal.ZERO;  // return 0 if no loans found
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return BigDecimal.ZERO;  // fallback in case no rows
    }

}