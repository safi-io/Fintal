package main.java.bankManagementSystem.controller.CustomerDashboard;

import main.java.bankManagementSystem.dao.CustomerDashborad.AccountDAO;
import main.java.bankManagementSystem.model.AccountBranchModel;

public class AccountController {
    private final AccountDAO accountDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
    }

    public AccountBranchModel handleGetDashboardData(String accountNumber) {
        return accountDAO.getDashboardData(accountNumber);
    }
}
