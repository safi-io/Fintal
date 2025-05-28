package main.java.bankManagementSystem.dao.CustomerDashborad;

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

public class BeneficiaryDAO {

    private String URL;
    private String USER;
    private String PASSWORD;

    public BeneficiaryDAO() {
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

    public List<String> getBeneficiaryAccountNumbers() {
        List<String> beneficiaryList = new ArrayList<>();
        String query = "SELECT CONCAT(A.ACCOUNT_NUMBER, '_', B.CUSTOMER_NAME) AS RESULT FROM ACCOUNT AS A\n" +
                "JOIN CUSTOMER AS B\n" +
                "ON A.ACCOUNT_CUSTOMER_CNIC = B.CUSTOMER_CNIC\n" +
                "WHERE A.ACCOUNT_TYPE = 'beneficiary';";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                beneficiaryList.add(rs.getString("RESULT"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return beneficiaryList;

    }

}
