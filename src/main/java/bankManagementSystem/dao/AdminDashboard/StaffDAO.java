package main.java.bankManagementSystem.dao.AdminDashboard;

import main.java.bankManagementSystem.model.StaffModel;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StaffDAO {
    private String URL;
    private String USER;
    private String PASSWORD;

    public StaffDAO() {
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

    // By Model
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

    // By ID
    public boolean updateStaffById(String staffId, String staffName, String staffMail, String staffPhone, String staffPassword) {
        StringBuilder query = new StringBuilder("UPDATE staff SET ");
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        // Conditionally add fields
        if (staffName != null && !staffName.isEmpty()) {
            fields.add("staff_Name = ?");
            values.add(staffName);
        }
        if (staffMail != null && !staffMail.isEmpty()) {
            fields.add("staff_Mail = ?");
            values.add(staffMail);
        }
        if (staffPhone != null && !staffPhone.isEmpty()) {
            fields.add("staff_Phone = ?");
            values.add(staffPhone);
        }
        if (staffPassword != null && !staffPassword.isEmpty()) {
            fields.add("staff_Password = ?");
            values.add(staffPassword);
        }

        // Nothing to update
        if (fields.isEmpty()) return false;

        // Build final query
        query.append(String.join(", ", fields));
        query.append(" WHERE staff_Id = ?");

        values.add(staffId); // Add staffId as last parameter

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<StaffModel> getStaffDataOtherThanId(int staffId) {
        List<StaffModel> staffList = new ArrayList<>();
        String query = "SELECT * FROM STAFF WHERE staff_id <> ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StaffModel sm = new StaffModel(rs.getInt("staff_id"), rs.getString("staff_name"), rs.getString("staff_mail"), rs.getString("staff_phone"), rs.getInt("staff_branch_id"));
                staffList.add(sm);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staffList;
    }

    public List<String> getStaffIdNameList() {
        List<String> staffIdList = new ArrayList<>();
        String query = "SELECT CONCAT(STAFF_ID, ' _ ', STAFF_NAME) AS RESULT FROM STAFF";

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

    public StaffModel getStaffById(int staffId) {
        StaffModel staff = null;
        String query = "SELECT * FROM STAFF WHERE STAFF_ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, staffId);
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

    public int getIdByMail(String staffMail) {
        String query = "SELECT STAFF_ID FROM STAFF WHERE STAFF_MAIL = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, staffMail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("STAFF_ID");
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching id", e);
        }
    }


}
