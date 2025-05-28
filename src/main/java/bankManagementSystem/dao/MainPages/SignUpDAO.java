package main.java.bankManagementSystem.dao.MainPages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class SignUpDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public SignUpDAO() {
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
