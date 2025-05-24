package main.java.bankManagementSystem.controller.MainPages;

import main.java.bankManagementSystem.dao.MainPages.LoginDAO;
import main.java.bankManagementSystem.dao.StaffDashboard.AccountDAO;

public class LoginController {
    private LoginDAO loginDAO;

    public LoginController() {
        this.loginDAO = new LoginDAO();
    }

    public boolean isCredentialsValid(String mail, String password, String role) throws Exception {
        return loginDAO.isCredentialsValid(mail, password, role);
    }
}
