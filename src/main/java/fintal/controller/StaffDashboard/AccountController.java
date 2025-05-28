package main.java.fintal.controller.StaffDashboard;

import main.java.fintal.dao.StaffDashboard.AccountDAO;
import main.java.fintal.model.CustomerAccountBranchModel;

import java.util.List;

public class AccountController {
    private final AccountDAO accountDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
    }

    public List<CustomerAccountBranchModel> handleGetAccountApplications() {
        return accountDAO.getAccountApplications();
    }

    public boolean handleApproveAccount(String accountNumber) {
        return accountDAO.approveAccount(accountNumber);
    }

    public List<CustomerAccountBranchModel> handleGetAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean handleAccountDelete(String accountNumber) {
        return accountDAO.accountDelete(accountNumber);
    }
}
