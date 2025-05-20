package main.java.bankManagementSystem.controller.StaffDashboard;

import main.java.bankManagementSystem.dao.AdminDashboard.StaffDAO;
import main.java.bankManagementSystem.dao.StaffDashboard.CustomerDAO;
import main.java.bankManagementSystem.model.StaffModel;

import java.util.List;

public class StaffController {

    private final StaffDAO staffDAO;

    public StaffController() {
        this.staffDAO = new StaffDAO();
    }

    public StaffModel handleGetStaffById(int staffId) {
        return staffDAO.getStaffById(staffId);
    }

    public boolean handleUpdateStaffData(String staffId, String staffName, String staffMail, String staffPhone, String staffPassword) {
        return staffDAO.updateStaffById(staffId, staffName, staffMail, staffPhone, staffPassword);
    }

    public List<StaffModel> handleGetStaffDataOtherThanId(int staffId) {
        return staffDAO.getStaffDataOtherThanId(staffId);
    }


}
