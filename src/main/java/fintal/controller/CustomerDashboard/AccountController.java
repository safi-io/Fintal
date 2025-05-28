package main.java.fintal.controller.CustomerDashboard;

import main.java.fintal.dao.CustomerDashborad.AccountDAO;
import main.java.fintal.model.CustomerAccountBranchModel;

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

    public boolean handleAddStripeAmount(String accountNumber) {
        return accountDAO.addStripeAmount(accountNumber);
    }

}
