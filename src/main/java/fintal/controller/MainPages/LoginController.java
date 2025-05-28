package main.java.fintal.controller.MainPages;

import main.java.fintal.dao.MainPages.LoginDAO;

public class LoginController {
    private LoginDAO loginDAO;

    public LoginController() {
        this.loginDAO = new LoginDAO();
    }

    public boolean isCredentialsValid(String mail, String password, String role) throws Exception {
        return loginDAO.isCredentialsValid(mail, password, role);
    }
}
