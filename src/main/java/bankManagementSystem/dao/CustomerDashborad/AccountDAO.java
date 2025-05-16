package main.java.bankManagementSystem.dao.CustomerDashborad;

import main.java.bankManagementSystem.model.AccountBranchModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
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

    public AccountBranchModel getDashboardData(String accountNumber) {

        String query = "SELECT A.ACCOUNT_NUMBER, A.ACCOUNT_TYPE, A.ACCOUNT_CURRENT_BALANCE, A.ACCOUNT_OPENING_DATE, A.ACCOUNT_LAST_LOGIN, B.BRANCH_NAME FROM ACCOUNT AS A INNER JOIN  BRANCH AS B ON B.BRANCH_ID = A.ACCOUNT_BRANCH_ID WHERE A.ACCOUNT_IS_OPENED && A.ACCOUNT_NUMBER = ?;";


        AccountBranchModel requiredDashboardData = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                requiredDashboardData = new AccountBranchModel(rs.getString("account_number"), rs.getString("account_type"), rs.getBigDecimal("account_current_balance"), rs.getDate("account_opening_date").toLocalDate(), rs.getDate("account_last_login").toLocalDate(), rs.getString("branch_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Dashboard Data: " + e.getMessage());
        }

        return requiredDashboardData;
    }

}
