package main.java.fintal.dao.StaffDashboard;

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

    // ONLY SHOW PENDING APPLICATIONS
    public List<LoanApplicationModel> getLoanApplications() {
        String query = """
                SELECT LOAN_ID, LOAN_AMOUNT, LOAN_ACCOUNT_NUMBER, LOAN_TENURE, LOAN_PURPOSE, LOAN_NOTE, LOAN_APPLY_DATE
                FROM LOAN_APPLICATION
                WHERE LOAN_IS_APPROVED = false
                ORDER BY LOAN_APPLY_DATE
                """;


        List<LoanApplicationModel> loanApplicationList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoanApplicationModel application = new LoanApplicationModel(rs.getInt("LOAN_ID"), rs.getBigDecimal("LOAN_AMOUNT"), rs.getInt("LOAN_TENURE"), rs.getString("LOAN_PURPOSE"), rs.getString("LOAN_NOTE"), rs.getTimestamp("LOAN_APPLY_DATE").toLocalDateTime().toLocalDate(), rs.getString("LOAN_ACCOUNT_NUMBER"));
                loanApplicationList.add(application);
            }
            return loanApplicationList;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch loan applications: " + e.getMessage());
        }
    }

    public void approveLoanAndDisburse(int loanId, BigDecimal loanAmount, String accountNumber) {
        String approveQuery = "UPDATE LOAN_APPLICATION SET LOAN_IS_APPROVED = TRUE WHERE LOAN_ID = ?";
        String creditQuery = "UPDATE ACCOUNT SET ACCOUNT_CURRENT_BALANCE = ACCOUNT_CURRENT_BALANCE + ? WHERE ACCOUNT_NUMBER = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement approveStmt = conn.prepareStatement(approveQuery); PreparedStatement creditStmt = conn.prepareStatement(creditQuery)) {

                approveStmt.setInt(1, loanId);
                int rowsUpdated = approveStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    throw new RuntimeException("No loan found with ID: " + loanId);
                }

                creditStmt.setBigDecimal(1, loanAmount);
                creditStmt.setString(2, accountNumber);
                int rowsCredited = creditStmt.executeUpdate();

                if (rowsCredited == 0) {
                    conn.rollback();
                    throw new RuntimeException("No account found with number: " + accountNumber);
                }

                conn.commit();  // commit both updates together
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);  // restore default
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to approve and disburse loan: " + e.getMessage(), e);
        }
    }


}
