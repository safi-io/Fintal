package main.java.bankManagementSystem.controller.StaffDashboard;

import main.java.bankManagementSystem.dao.StaffDashboard.AccountDAO;
import main.java.bankManagementSystem.model.CustomerAccountModel;

import java.util.List;

public class AccountController {
    private final AccountDAO accountDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
    }

    public List<CustomerAccountModel> handleGetAccountApplications() {
        return accountDAO.getAccountApplications();
    }

    public boolean handleApproveAccount(String accountNumber) {
        return accountDAO.approveAccount(accountNumber);
    }

    public List<CustomerAccountModel> handleGetAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean handleAccountDelete(String accountNumber) {
        return accountDAO.accountDelete(accountNumber);
    }
}
