package main.java.bankManagementSystem.dao.AdminDashboard;

import main.java.bankManagementSystem.model.AdminDashbaord.StaffModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StaffDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public StaffDAO() {
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

    public boolean createStaff(StaffModel staff) {
        String query = "INSERT INTO STAFF (STAFF_NAME, STAFF_MAIL, STAFF_PHONE, STAFF_CNIC, STAFF_DOB, STAFF_PASSWORD, STAFF_BRANCH_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, staff.getStaffName());
            stmt.setString(2, staff.getStaffMail());
            stmt.setString(3, staff.getStaffPhone());
            stmt.setString(4, staff.getStaffCNIC());
            stmt.setDate(5, java.sql.Date.valueOf(staff.getStaffDOB()));
            stmt.setString(6, staff.getStaffPassword());
            stmt.setInt(7, staff.getStaffBranchId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteStaff(String staffID) {
        String query = "DELETE FROM STAFF WHERE STAFF_ID = (?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, staffID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateStaff(StaffModel updateStaff) {
        String query = "UPDATE STAFF SET STAFF_NAME = ?, STAFF_MAIL = ?, STAFF_PHONE = ?, STAFF_CNIC = ?, STAFF_DOB = ?, STAFF_BRANCH_ID = ? WHERE STAFF_ID = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, updateStaff.getStaffName());
            stmt.setString(2, updateStaff.getStaffMail());
            stmt.setString(3, updateStaff.getStaffPhone());
            stmt.setString(4, updateStaff.getStaffCNIC());
            stmt.setDate(5, java.sql.Date.valueOf(updateStaff.getStaffDOB()));
            stmt.setInt(6, updateStaff.getStaffBranchId());
            stmt.setInt(7, updateStaff.getStaffId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public List<String> getStaffIdNameList() {
        List<String> staffIdList = new ArrayList<>();
        String query = "SELECT CONCAT(STAFF_ID, ' - ', STAFF_NAME) AS RESULT FROM STAFF";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffIdList.add(rs.getString("RESULT"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staffIdList;
    }

    public List<Integer> getAllStaffIds() {
        List<Integer> staffIDList = new ArrayList<>();
        String query = "SELECT STAFF_ID FROM STAFF";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffIDList.add(Integer.valueOf(rs.getString("STAFF_ID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staffIDList;
    }

    public StaffModel getStaffById(int branchId) {
        StaffModel staff = null;
        String query = "SELECT * FROM STAFF WHERE STAFF_ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    staff = new StaffModel(rs.getInt("STAFF_ID"), rs.getString("STAFF_NAME"), rs.getString("STAFF_MAIL"), rs.getString("STAFF_PHONE"), rs.getString("STAFF_CNIC"), rs.getDate("STAFF_DOB").toLocalDate(), rs.getInt("STAFF_BRANCH_ID"));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch staff by ID: " + e.getMessage());
        }

        return staff;
    }

    public String getPasswordByMail(String staffMail) {
        String query = "SELECT STAFF_PASSWORD FROM STAFF WHERE STAFF_MAIL = ?";
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, staffMail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("STAFF_PASSWORD");
                } else {
                    return null; // Or throw exception if email doesn't exist
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching password", e);
        }
    }

    public List<String> getStaffMailList() {
        List<String> staffMailList = new ArrayList<>();
        String query = "SELECT STAFF_MAIL AS RESULT FROM STAFF";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffMailList.add(rs.getString("RESULT"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staffMailList;
    }

}
