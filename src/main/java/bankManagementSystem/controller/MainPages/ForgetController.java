package main.java.bankManagementSystem.controller.MainPages;

import main.java.bankManagementSystem.dao.MainPages.ForgetDAO;
import org.mindrot.jbcrypt.BCrypt;

public class ForgetController {

    private final ForgetDAO forgetDAO;

    public ForgetController() {
        this.forgetDAO = new ForgetDAO();
    }

    public String generateOTP() {
        int min = 100000;
        int max = 999999;
        int otp = (int) (Math.random() * (max - min + 1)) + min;
        return String.valueOf(otp);
    }

    public boolean handleUpdatePassword(String selectedRole, String email, String newPass) {
        String hashedPassword = BCrypt.hashpw(newPass, BCrypt.gensalt());

        return forgetDAO.updatePassword(selectedRole, email, hashedPassword);

    }

    public boolean handleEmailExists(String role, String email) {
        return forgetDAO.emailExists(role, email);
    }
}
