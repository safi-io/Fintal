package main.java.bankManagementSystem.dao.AdminDashboard;

import main.java.bankManagementSystem.model.BranchModel;

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

public class BranchDAO {

    private String URL;
    private String USER;
    private String PASSWORD;

    public BranchDAO() {
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

    public boolean createBranch(BranchModel branch) {
        String query = "INSERT INTO BRANCH (BRANCH_NAME, BRANCH_ADDRESS, BRANCH_PHONE) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, branch.getBranchName());
            stmt.setString(2, branch.getBranchAddress());
            stmt.setString(3, branch.getBranchPhone());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteBranch(String branchID) {
        String query = "DELETE FROM BRANCH WHERE BRANCH_ID = (?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, branchID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateBranch(BranchModel updateBranch) {
        String query = "UPDATE BRANCH SET BRANCH_NAME = ?, BRANCH_ADDRESS = ?, BRANCH_PHONE = ? WHERE BRANCH_ID = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, updateBranch.getBranchName());
            stmt.setString(2, updateBranch.getBranchAddress());
            stmt.setString(3, updateBranch.getBranchPhone());
            stmt.setInt(4, updateBranch.getBranchId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public List<String> getBranchIdNameList() {
        List<String> branchList = new ArrayList<>();
        String query = "SELECT CONCAT(BRANCH_ID, ' _ ', BRANCH_NAME) AS RESULT FROM BRANCH";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                branchList.add(rs.getString("RESULT"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return branchList;
    }

    public List<Integer> getAllBranchIds() {
        List<Integer> branchIDList = new ArrayList<>();
        String query = "SELECT BRANCH_ID FROM BRANCH";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                branchIDList.add(Integer.valueOf(rs.getString("BRANCH_ID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return branchIDList;
    }

    public BranchModel getBranchById(int branchId) {
        BranchModel branch = null;
        String query = "SELECT * FROM BRANCH WHERE BRANCH_ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    branch = new BranchModel(rs.getInt("BRANCH_ID"),   // Corrected: now String
                            rs.getString("BRANCH_NAME"), rs.getString("BRANCH_ADDRESS"), rs.getString("BRANCH_PHONE"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch branch by ID: " + e.getMessage());
        }

        return branch;
    }

}