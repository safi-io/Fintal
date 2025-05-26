package main.java.bankManagementSystem.controller.AdminDashboard;

import main.java.bankManagementSystem.dao.AdminDashboard.StaffDAO;
import main.java.bankManagementSystem.model.StaffModel;

import org.mindrot.jbcrypt.BCrypt;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class StaffController {
    private final StaffDAO staffDAO;

    public StaffController() {
        this.staffDAO = new StaffDAO();
    }

    public boolean handleCreateStaff(String staffName, String staffMail, String staffPhone, String staffCNIC, LocalDate staffDOB, int staffBranchId) {

        String password = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        // Send the upper password as mail, and then hash it and store inside database

        String staffHashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        StaffModel staff = new StaffModel(staffName, staffMail, staffPhone, staffCNIC, staffDOB, staffHashedPassword, staffBranchId);
        // Validations
        if (staff.getStaffName().isEmpty() || staff.getStaffMail().isEmpty() || staff.getStaffPhone().isEmpty() || staff.getStaffCNIC().isEmpty() || staff.getStaffBranchId() == 0) {
            return false;
        }
        if (staff.getStaffDOB().isAfter(LocalDate.now())) {
            throw new RuntimeException("Date is Not Valid!");
        }

        // Passing to DAO
        try {
            return staffDAO.createStaff(staff);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean handleDeleteStaff(String selectedStaffID) {

        try {
            return staffDAO.deleteStaff(selectedStaffID);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean handleUpdateStaff(int staffId, String staffName, String staffMail, String staffPhone, String staffCNIC, LocalDate staffDOB, int staffBranchId) {
        StaffModel staff = new StaffModel(staffId, staffName, staffMail, staffPhone, staffCNIC, staffDOB, staffBranchId);

        if (staff.getStaffId() == 0 || staff.getStaffName().isEmpty() || staff.getStaffMail().isEmpty() || staff.getStaffPhone().isEmpty() || staff.getStaffCNIC().isEmpty() || staff.getStaffBranchId() == 0) {
            return false;
        }

        if (staff.getStaffDOB().isAfter(LocalDate.now())) {
            throw new RuntimeException("Date is Not Valid!");
        }

        try {
            return staffDAO.updateStaff(staff);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public StaffModel handleGetStaffById(int branchId) {
        return staffDAO.getStaffById(branchId);
    }

    public List<String> handleGetStaffIdNameList() {
        try {
            return staffDAO.getStaffIdNameList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public int handleGetIdByMail(String mail) {
        return staffDAO.getIdByMail(mail);
    }
}
