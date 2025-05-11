package main.java.bankManagementSystem.dao.StaffDashboard;

import main.java.bankManagementSystem.model.CustomerModel;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public CustomerDAO() {
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

    public List<CustomerModel> getAllCustomers() {
        List<CustomerModel> customers = new ArrayList<>();
        String query = "SELECT * FROM CUSTOMER";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                CustomerModel customer = new CustomerModel(rs.getString("customer_CNIC"), rs.getString("customer_Name"), rs.getString("customer_Mail"), rs.getString("customer_Phone"), rs.getDate("customer_DOB").toLocalDate());


                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch customers: " + e.getMessage());
        }

        return customers;
    }

    public CustomerModel getCustomerByCNIC(String CNIC) {
        String query = "SELECT * FROM CUSTOMER WHERE CUSTOMER_CNIC = ?";
        CustomerModel requiredCustomer = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, CNIC);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                requiredCustomer = new CustomerModel(
                        rs.getString("customer_CNIC"),
                        rs.getString("customer_Name"),
                        rs.getString("customer_Mail"),
                        rs.getString("customer_Phone"),
                        rs.getDate("customer_DOB").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch customer: " + e.getMessage());
        }

        return requiredCustomer;
    }

}
