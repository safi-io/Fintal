package main.java.bankManagementSystem.controller.CustomerDashboard;

import main.java.bankManagementSystem.dao.CustomerDashborad.AccountDAO;
import main.java.bankManagementSystem.model.CustomerAccountBranchModel;

public class AccountController {
    private final AccountDAO accountDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
    }

    public CustomerAccountBranchModel handleGetDashboardData(String accountNumber) {
        return accountDAO.getDashboardData(accountNumber);
    }

    public String handleGetCustomerNameUsingAccountNumber(String accountNumber) {
        return accountDAO.getCustomerNameUsingAccountNumber(accountNumber);
    }

}
