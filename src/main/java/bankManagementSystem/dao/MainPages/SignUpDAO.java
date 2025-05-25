package main.java.bankManagementSystem.dao.MainPages;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class SignUpDAO {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public SignUpDAO() {
        String url = "", user = "", password = "";
        try {
            Path path = Paths.get("src/main/resources/config.properties");
            Properties props = new Properties();
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                props.load(reader);
            }
            url = props.getProperty("db.url");
            user = props.getProperty("db.username");
            password = props.getProperty("db.password");

        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
        this.URL = url;
        this.USER = user;
        this.PASSWORD = password;
    }

    public boolean createCustomerAccount(String cnic,
                                         String name,
                                         String mail,
                                         String phone,
                                         Date   dobSql,
                                         String hashedPassword,
                                         String accountNumber,
                                         String accountType,
                                         String branchId) {

        final String INSERT_CUSTOMER =
                "INSERT INTO CUSTOMER (CUSTOMER_CNIC, CUSTOMER_NAME, CUSTOMER_MAIL, CUSTOMER_PHONE, CUSTOMER_DOB, CUSTOMER_PASSWORD) " +
                        "VALUES (?,?,?,?,?,?)";

        final String INSERT_ACCOUNT =
                "INSERT INTO ACCOUNT (ACCOUNT_NUMBER, ACCOUNT_TYPE, ACCOUNT_CUSTOMER_CNIC, ACCOUNT_BRANCH_ID) " +
                        "VALUES (?,?,?,?)";   // ACCOUNT_OPENING_DATE defaults to CURRENT_DATE

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            conn.setAutoCommit(false);                   // ── begin txn ──

            try (PreparedStatement psCust = conn.prepareStatement(INSERT_CUSTOMER);
                 PreparedStatement psAcct = conn.prepareStatement(INSERT_ACCOUNT)) {

                /* ---------- CUSTOMER ---------- */
                psCust.setString(1, cnic);
                psCust.setString(2, name);
                psCust.setString(3, mail);
                psCust.setString(4, phone);
                psCust.setDate  (5, dobSql);
                psCust.setString(6, hashedPassword);
                psCust.executeUpdate();

                /* ---------- ACCOUNT ----------- */
                psAcct.setString(1, accountNumber);
                psAcct.setString(2, accountType);
                psAcct.setString(3, cnic);
                if (branchId == null || branchId.isBlank()) {
                    psAcct.setNull(4, Types.INTEGER);
                } else {
                    psAcct.setInt(4, Integer.parseInt(branchId));
                }
                psAcct.executeUpdate();

                conn.commit();                          // ── success ──
                return true;
            } catch (SQLException e) {
                conn.rollback();                        // ── failure ──
                e.printStackTrace();                    // log / re‑throw as needed
                return false;
            } finally {
                conn.setAutoCommit(true);               // restore default
            }

        } catch (SQLException ex) {
            ex.printStackTrace();                       // could not connect, etc.
            return false;
        }
    }

}
