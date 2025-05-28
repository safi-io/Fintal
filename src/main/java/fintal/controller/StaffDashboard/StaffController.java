package main.java.fintal.controller.StaffDashboard;

import main.java.fintal.dao.AdminDashboard.StaffDAO;
import main.java.fintal.model.StaffModel;
import org.mindrot.jbcrypt.BCrypt;

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

        if (!staffPassword.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(staffPassword, BCrypt.gensalt());
            return staffDAO.updateStaffById(staffId, staffName, staffMail, staffPhone, hashedPassword);
        } else {
            return staffDAO.updateStaffById(staffId, staffName, staffMail, staffPhone, staffPassword);
        }

    }

    public List<StaffModel> handleGetStaffDataOtherThanId(int staffId) {
        return staffDAO.getStaffDataOtherThanId(staffId);
    }


}
